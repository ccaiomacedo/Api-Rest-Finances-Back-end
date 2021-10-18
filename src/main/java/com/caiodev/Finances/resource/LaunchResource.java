package com.caiodev.Finances.resource;

import com.caiodev.Finances.dto.LaunchDTO;
import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.enums.LaunchType;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.service.LaunchService;
import com.caiodev.Finances.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lancamentos")
public class LaunchResource {

    private LaunchService service;
    private UserService usuarioService;

    public LaunchResource(LaunchService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LaunchDTO dto){
        try {
            Launch entidade = converter(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch (BusinessRuleException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable Long id,@RequestBody LaunchDTO dto){
        return service.obterPorId(id).map(entity ->{
        try {
            Launch launch = converter(dto);
            launch.setId(entity.getId());
            service.atualizar(launch);
            return ResponseEntity.ok(launch);
        }catch (BusinessRuleException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de Dados",HttpStatus.BAD_REQUEST));
    }


    private Launch converter(LaunchDTO dto){
        Launch launch = new Launch();
        launch.setId(dto.getId());
        launch.setDescricao(dto.getDescricao());
        launch.setAno(dto.getAno());
        launch.setMes(dto.getMes());
        launch.setValor(dto.getValor());

        User user = usuarioService.obterPorId(dto.getUsuario()).orElseThrow(() -> new BusinessRuleException("Usuário não encontrado para o id informado"));

        launch.setUser(user);
        launch.setTipo(LaunchType.valueOf(dto.getTipo()));
        launch.setStatus(LaunchStatus.valueOf(dto.getStatus()));

        return launch;

    }



}

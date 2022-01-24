package com.caiodev.Finances.resource;

import com.caiodev.Finances.dto.LaunchDTO;
import com.caiodev.Finances.dto.UpdateStatusDTO;
import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.entity.UserR;
import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.enums.LaunchType;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.service.LaunchService;
import com.caiodev.Finances.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LaunchResource {

    private LaunchService service;
    private UserService usuarioService;

    public LaunchResource(LaunchService service, UserService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao, // definindo que o parâmetro é opcional
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario //parâmetro obrigatório
    ) {
        Launch lancamentoFiltro = new Launch();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<UserR> user = usuarioService.obterPorId(idUsuario);
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o id encontrado");
        } else {
            lancamentoFiltro.setUser(user.get());
        }
        List<Launch> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LaunchDTO dto) {
        try {
            Launch entidade = converter(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LaunchDTO dto) {
        return service.obterPorId(id).map(entity -> {
            try {
                Launch launch = converter(dto);
                launch.setId(entity.getId());
                service.atualizar(launch);
                return ResponseEntity.ok(launch);
            } catch (BusinessRuleException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO dto) {
        return service.obterPorId(id).map(entity -> {
            LaunchStatus statusSelecionado = LaunchStatus.valueOf(dto.getStatus());
            if (statusSelecionado == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento");
            }
            try {
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            } catch (BusinessRuleException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id) {
        return service.obterPorId(id).map(entidade -> {
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }


    private Launch converter(LaunchDTO dto) {
        Launch launch = new Launch();
        launch.setId(dto.getId());
        launch.setDescricao(dto.getDescricao());
        launch.setAno(dto.getAno());
        launch.setMes(dto.getMes());
        launch.setValor(dto.getValor());

        UserR userR = usuarioService.obterPorId(dto.getUsuario()).orElseThrow(() -> new BusinessRuleException("Usuário não encontrado para o id informado"));

        launch.setUser(userR);
        if (dto.getTipo() != null) {
            launch.setTipo(LaunchType.valueOf(dto.getTipo()));
        }
        if (dto.getStatus() != null) {
            launch.setStatus(LaunchStatus.valueOf(dto.getStatus()));
        }
        return launch;

    }
}

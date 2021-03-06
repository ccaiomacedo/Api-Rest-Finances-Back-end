package com.caiodev.Finances.resource;

import com.caiodev.Finances.dto.TokenDTO;
import com.caiodev.Finances.dto.UserDTO;
import com.caiodev.Finances.entity.UserR;
import com.caiodev.Finances.exception.AuthenticationErrorException;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.service.JwtService;
import com.caiodev.Finances.service.LaunchService;
import com.caiodev.Finances.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UserResource {

    private UserService service;
    private LaunchService launchService;
    private JwtService jwtService;

    public UserResource(UserService service,LaunchService launchService,JwtService jwtService) {
        this.service = service;
        this.launchService = launchService;
        this.jwtService = jwtService;
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody UserDTO dto){
        try{
            UserR usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            String token = jwtService.gerarToken(usuarioAutenticado);
            TokenDTO tokenDTO = new TokenDTO(usuarioAutenticado.getNome(),token);
            return ResponseEntity.ok(tokenDTO);//esse ok é pra dizer que o retorno http vai ser 200
        }catch(AuthenticationErrorException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UserDTO dto) {//esse request faz o objeto json ser convertido para o java
        UserR user = UserR.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
        try {
            UserR usuarioSalvo = service.salvarUsuario(user);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id){
        Optional<UserR> usuario = service.obterPorId(id);
        if(!usuario.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        BigDecimal saldo = launchService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }

}

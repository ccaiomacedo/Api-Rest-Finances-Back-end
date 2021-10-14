package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenário
        User user = User.builder().nome("usuario").email("usuario@email.com").build();
        repository.save(user);

        //ação/execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isTrue();

    }

    public void deveRetornarFalsoQuandoNãoHouverUsuarioCadastradoComOEmail(){
         //cenário
        repository.deleteAll();

        //ação/execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isFalse();
    }



}

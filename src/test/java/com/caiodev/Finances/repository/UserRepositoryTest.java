package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest//ela deleta os dados quando encerra o teste
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenário
        User user = criarUsuario();
        entityManager.persist(user); //está salvando um usuário

        //ação/execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void deveRetornarFalsoQuandoNãoHouverUsuarioCadastradoComOEmail(){
         //cenário

        //ação/execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados(){
        //cenário
        User user = criarUsuario();

        //ação
        User usuarioSalvo = repository.save(user);

        //Verificação
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }
    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        //cenário
        User user = criarUsuario();
        entityManager.persist(user);

        //ação
        Optional<User> result = repository.findByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result.isPresent()).isTrue();


    }
    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNãoExisteNaBase(){
        //cenário

        //ação
        Optional<User> result = repository.findByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result.isPresent()).isFalse();


    }

    public static  User criarUsuario(){
        return User.builder().nome("usuario").email("usuario@email.com").senha("senha").build();
    }
}

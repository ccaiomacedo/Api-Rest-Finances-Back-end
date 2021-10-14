package com.caiodev.Finances.service;


import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    UserService service;

    @Autowired
    UserRepository repository;

    @Test(expected = Test.None.class)//espera que meu teste não lance nenhuma exceção
    public void deveValidarEmail(){
        //cenário
        repository.deleteAll();

        //ação/execução
        service.validarEmail("email@email.com");

    }

    @Test(expected = BusinessRuleException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenário
        User user = User.builder().nome("usuario").email("email@email.com").build();
        repository.save(user);

        //ação
        service.validarEmail("email@email.com");
    }




}

package com.caiodev.Finances.service;


import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.exception.AuthenticationErrorException;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)//para criar um contexto de injeção de dependências
@ActiveProfiles("test")
public class UserServiceTest {

    UserService service;

    @MockBean //ele cria um mock, ele cria uma instância mockada ao invés das instâncias reais, ele substitui todas as instâncias de repository
    UserRepository repository;

    @Before //é pra executar antes de cada de teste
    public void setUp(){
        //repository = Mockito.mock(UserRepository.class);//estou usando o mock pra simular a chamada das funções do repository
        service = new UserServiceImpl(repository);
    }

    @Test(expected = Test.None.class)//espera que n retorne nenhuma exception
    public void deveAutenticarUmUsuarioComSucesso(){
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        User user = User.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));//quando executar o findByEmail passando o email vai retornar o optional passando um user

        //ação
        User result = service.autenticar(email,senha);

        //verificação
        Assertions.assertThat(result).isNotNull();

    }

    @Test(expected = AuthenticationErrorException.class)
    public void deveLançarErroQuandoNãoEncontrarUsuarioCadastradoComOemailInformado(){
        //cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //ação
        service.autenticar("email@email.com","senha");

    }

    @Test(expected = AuthenticationErrorException.class)
    public void deveLancarErroQuandoSenhaNaoBater(){
        //cenário
        String senha = "senha";
        User user = User.builder().email("@email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        //ação
        service.autenticar("@email@email.com","123");
    }


    @Test(expected = Test.None.class)//espera que meu teste não lance nenhuma exceção
    public void deveValidarEmail(){
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);//estou simulando que quando eu chamar o existByEmail e passar qualquer String como parametro ele deve retornar falso

        //ação/execução
        service.validarEmail("email@email.com");

    }

    @Test(expected = BusinessRuleException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);//Está simulando que quando passar qualquer String no método vai retornar true

        //ação
        service.validarEmail("email@email.com");
    }
}

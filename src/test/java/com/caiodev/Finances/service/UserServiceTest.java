package com.caiodev.Finances.service;


import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.exception.AuthenticationErrorException;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)//para criar um contexto de injeção de dependências
@ActiveProfiles("test")
public class UserServiceTest {

    @SpyBean//estou mockando um service no spy, fazendo com que ele utilize as instâncias reais
    UserServiceImpl service;

    @MockBean //ele cria um mock, ele cria uma instância mockada ao invés das instâncias reais, ele substitui todas as instâncias de repository
    UserRepository repository;

    /*
    @Before //é pra executar antes de cada de teste
    public void setUp(){
        //repository = Mockito.mock(UserRepository.class);//estou usando o mock pra simular a chamada das funções do repository
        service = Mockito.spy(UserServiceImpl.class);
    }*/

    @Test(expected = Test.None.class)
    public void deveSalvarUmUsuario(){
        //cenário
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());//ele n vai fazer nd quando eu chamar um método de validar email
        User user = User.builder().id(1L).nome("nome").email("email@email.com").senha("senha").build();
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);//quando eu chamar o método de salvar usuário passando qualquer usuário ele vai me retornar um usuário

        //ação
        User usuarioSalvo = service.salvarUsuario(new User());

        //verificação
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");


    }

    @Test(expected = BusinessRuleException.class)
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
        //cenário
        String email ="email@email.com";
        User user = User.builder().email(email).build();
        Mockito.doThrow(BusinessRuleException.class).when(service).validarEmail(email);//está lançando a exception quando executa esse método passando o email

        //ação
        service.salvarUsuario(user);

        //verificação
        Mockito.verify(repository,Mockito.never()).save(user);//espera que nunca tenha chamado a ação de salvar esse usuário,pq n pode salvar o usuário


    }

    @Test(expected = Test.None.class)//espera que n retorne nenhuma exception
    public void deveAutenticarUmUsuarioComSucesso(){
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        //quando eu mocko um objeto, preciso informar o retorno, pq se não ele retorna default
        User user = User.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));//quando executar o findByEmail passando o email vai retornar o optional passando um user

        //ação
        User result = service.autenticar(email,senha);

        //verificação
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    public void deveLançarErroQuandoNãoEncontrarUsuarioCadastradoComOemailInformado(){
        //cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        //ação
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com","senha"));
        //verificação
        Assertions.assertThat(exception).isInstanceOf(AuthenticationErrorException.class).hasMessage("Usuário não encontrado para o email informado.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        //cenário
        String senha = "senha";
        User user = User.builder().email("@email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        //ação
        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("@email@email.com","123"));//este throwable está capturando o erro que vai ser lançado nesse método
        //Verificação
        Assertions.assertThat(exception).isInstanceOf(AuthenticationErrorException.class).hasMessage("senha inválida");//está passando a exceção que dever retornar
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

package com.caiodev.Finances.resource;

import com.caiodev.Finances.dto.UserDTO;
import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.exception.AuthenticationErrorException;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.service.LaunchService;
import com.caiodev.Finances.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserResource.class) //essa anotação faz com que suba o contexto Rest,suba apenas para eu testar o controller
@AutoConfigureMockMvc //para ter acesso ao objeto MockMVC , esse objeto ajuda a fazer as requisições
public class UserResourceTest {

    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @MockBean
    LaunchService launchService;

    @Test
    public void deveAutenticarUmUsuario() throws Exception{
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UserDTO dto = UserDTO.builder().email(email).senha(senha).build();
        User usuario = User.builder().id(1L).email(email).senha(senha).build();

        Mockito.when(service.autenticar(email,senha)).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);//pega um objeto de qualquer tipo e transforma em uma string json

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders//cenário front-end
                                                .post(API.concat("/autenticar"))//está mandando uma requisição
                                                .accept(JSON)//aceitando o tipo json
                                                .contentType(JSON)//recebendo o tipo json
                                                .content(json);//mandando o objeto json
        mvc.perform(request)//espero que quando fizer a requisição
                .andExpect(MockMvcResultMatchers.status().isOk())//receba o status ok
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))// e receba o json com esses valores
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }
    @Test
    public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception{
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UserDTO dto = UserDTO.builder().email(email).senha(senha).build();
        Mockito.when(service.autenticar(email,senha)).thenThrow(AuthenticationErrorException.class);

        String json = new ObjectMapper().writeValueAsString(dto);//pega um objeto de qualquer tipo e transforma em uma string json

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders//cenário front-end
                .post(API.concat("/autenticar"))//está mandando uma requisição
                .accept(JSON)//aceitando o tipo json
                .contentType(JSON)//recebendo o tipo json
                .content(json);//mandando o objeto json

        mvc.perform(request)//espero que quando fizer a requisição
                .andExpect(MockMvcResultMatchers.status().isBadRequest());//receba o status Bad Request

    }

    @Test
    public void deveCriarUmNovoUsuario() throws Exception{
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UserDTO dto = UserDTO.builder().email(email).senha(senha).build();
        User usuario = User.builder().id(1L).email(email).senha(senha).build();

        Mockito.when(service.salvarUsuario(Mockito.any(User.class))).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);//pega um objeto de qualquer tipo e transforma em uma string json

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders//cenário front-end
                .post(API)//está mandando a requisição da raiz da minha api
                .accept(JSON)//aceitando o tipo json
                .contentType(JSON)//recebendo o tipo json
                .content(json);//mandando o objeto json
        mvc.perform(request)//espero que quando fizer a requisição
                .andExpect(MockMvcResultMatchers.status().isCreated())//receba o status created
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))// e receba o json com esses valores
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }
    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception{
        //cenário
        String email = "usuario@email.com";
        String senha = "123";

        UserDTO dto = UserDTO.builder().email(email).senha(senha).build();

        Mockito.when(service.salvarUsuario(Mockito.any(User.class))).thenThrow(BusinessRuleException.class);
        String json = new ObjectMapper().writeValueAsString(dto);//pega um objeto de qualquer tipo e transforma em uma string json

        //execução e verificação
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders//cenário front-end
                .post(API)//está mandando a requisição da raiz da minha api
                .accept(JSON)//aceitando o tipo json
                .contentType(JSON)//recebendo o tipo json
                .content(json);//mandando o objeto json

        mvc.perform(request)//espero que quando fizer a requisição
                .andExpect(MockMvcResultMatchers.status().isBadRequest());//receba o status created

    }
}

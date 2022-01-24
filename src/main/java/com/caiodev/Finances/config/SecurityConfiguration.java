package com.caiodev.Finances.config;

import com.caiodev.Finances.serviceImpl.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    //esse metodo estava criando uma autenticação em memoria com esses dados
    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService) //aqui ta passando o tipo de autenticação
                .passwordEncoder(passwordEncoder());

    }

    @Override
    protected  void configure(HttpSecurity http) throws  Exception{
        http.csrf().disable()//está desabilitando a proteção de ataque csrf, o ataque é baseado no armazenamento da autenticação e sessão, só que n vai ser usado sessão
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/usuarios/autenticar").permitAll()
                .antMatchers(HttpMethod.POST,"/api/usuarios").permitAll()
                .anyRequest().authenticated()//aqui ta dizendo que pra mandar qualquer requisição tem que estar autenticado
                .and()//quando eu quiser voltar pro metodo http eu uso o and
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//pra toda requisição vai precisar de autenticação
                .and()
                .httpBasic();
    }
}

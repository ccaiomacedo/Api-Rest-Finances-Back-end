package com.caiodev.Finances.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception{
        String senhaCodificada = passwordEncoder().encode("qwe123");

        auth.inMemoryAuthentication()
                .withUser("usuario")
                .password(senhaCodificada)
                .roles("USER");
    }

    @Override
    protected  void configure(HttpSecurity http) throws  Exception{
        http.csrf().disable()
                .authorizeRequests().anyRequest().authenticated()//aqui ta dizendo que pra mandar qualquer requisição tem que estar autenticado
                .and()
                .httpBasic();
    }
}

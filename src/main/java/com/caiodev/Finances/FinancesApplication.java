package com.caiodev.Finances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc // permitindo que aplicações de fora tenham acesso
public class FinancesApplication implements WebMvcConfigurer {
	
	/*CORS é um mecanismo que permite que recursos restritos em uma página da web sejam recuperados por outro domínio 
	fora do domínio ao qual pertence o recurso que será recuperado*/
	@Override
	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/**").allowedMethods("GET","POST","PUT","DELETE","OPTIONS");
	}

	public static void main(String[] args) {
		SpringApplication.run(FinancesApplication.class, args);
	}

}

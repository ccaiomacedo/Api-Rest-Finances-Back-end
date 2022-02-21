package com.caiodev.Finances.api;

import com.caiodev.Finances.service.JwtService;
import com.caiodev.Finances.serviceImpl.SecurityUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
//aqui ele vai decodificar o jwt


    private JwtService jwtService;
    private SecurityUserDetailsService userDetailsService;

    public JwtTokenFilter(JwtService jwtService, SecurityUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, //vai pegar o token como requisição
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1]; // ele vai separar em array as separações com espaço
            boolean isTokenValid = jwtService.isTokenValido(token);
            if (isTokenValid) {
                String login = jwtService.obterLoginUsuario(token);
                UserDetails usuarioAutenticado = userDetailsService.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                        usuarioAutenticado, null, usuarioAutenticado.getAuthorities());//estou pegando os detalhes do userDetails e transformando em um token de autenticação do spring security

                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//está criando uma autenticação pra jogar dentro do contexto do spring security

                SecurityContextHolder.getContext().setAuthentication(user);//está pegando o contexto do spring security e jogando essa autenticação
            }
        }
        filterChain.doFilter(request, response);//pra dar continuidade a execução da requisição

    }
}

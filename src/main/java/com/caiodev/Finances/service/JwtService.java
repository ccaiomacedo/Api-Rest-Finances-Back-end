package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.UserR;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {

    String gerarToken(UserR user);

    //claims são as informações que tem no token
    Claims obterClaims(String token) throws ExpiredJwtException;//vai usar o claims pra adicionar informações no token ou pra obter as informações do token

    boolean isTokenValido(String token);

    String obterLoginUsuario(String token);
}

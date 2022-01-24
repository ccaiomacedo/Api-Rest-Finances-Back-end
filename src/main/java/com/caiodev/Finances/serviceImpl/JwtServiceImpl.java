package com.caiodev.Finances.serviceImpl;

import com.caiodev.Finances.entity.UserR;
import com.caiodev.Finances.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiracao}")
    private String expiracao;
    @Value("${jwt.chave-assinatura}")
    private String chaveAssinatura;

    @Override
    public String gerarToken(UserR user) {
        long exp = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant(); // esta convertendo dateTime em date
        java.util.Date data = Date.from(instant);

        String horaExpiracaoToken = dataHoraExpiracao.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        String token = Jwts.builder()
                .setExpiration(data)
                .setSubject(user.getEmail())//vai identificar o usuário a partir do email
                .claim("nome", user.getNome())
                .claim("horaExpiracao", horaExpiracaoToken)
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();
        return token;
    }

    //vai usar o claims pra adicionar informações no token ou pra obter as informações do token
    @Override
    public Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parser()
                .setSigningKey(chaveAssinatura)//chave de assinatura pra decodificar o token
                .parseClaimsJws(token)
                .getBody(); //retorna os claims do token
    }

    @Override
    public boolean isTokenValido(String token) {
        try {
            Claims claims = obterClaims(token);
            java.util.Date dataEx = claims.getExpiration();
            LocalDateTime dataExpiracao = dataEx.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();//convertendo date para dateTime
            boolean dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
            return !dataHoraAtualIsAfterDataExpiracao;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    @Override
    public String obterLoginUsuario(String token) {
        Claims claims = obterClaims(token);
        return claims.getSubject();
    }
}

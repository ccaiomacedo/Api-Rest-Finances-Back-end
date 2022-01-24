package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.UserR;

import java.util.Optional;

public interface UserService {

    UserR autenticar(String email, String senha);

    UserR salvarUsuario(UserR usuario);

    void validarEmail(String email);

    Optional<UserR> obterPorId(Long id);//optional, pq pode ser que exista usuário para o id ou pode ser que n
}

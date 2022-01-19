package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.User;

import java.util.Optional;

public interface UserService {

    User autenticar(String email,String senha);

    User salvarUsuario(User usuario);

    void validarEmail(String email);

    Optional<User> obterPorId(Long id);//optional, pq pode ser que exista usuário para o id ou pode ser que n
}

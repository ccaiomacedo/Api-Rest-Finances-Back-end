package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.User;

public interface UserService {

    User autenticar(String email,String senha);

    User salvarUsuario(User usuario);

    void validarEmail(String email);
}

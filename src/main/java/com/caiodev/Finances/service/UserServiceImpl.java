package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.repository.UserRepository;

public class UserServiceImpl implements UserService{

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User autenticar(String email, String senha) {
        return null;
    }

    @Override
    public User salvarUsuario(User usuario) {
        return null;
    }

    @Override
    public void validadarEmail(String email) {

    }
}

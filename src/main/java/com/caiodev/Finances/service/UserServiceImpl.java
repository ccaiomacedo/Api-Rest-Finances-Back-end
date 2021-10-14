package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service //diz que é uma classe de serviço para eu poder utilizar a instância em outras classes, e pode injetar as dependências
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
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe){
            throw new BusinessRuleException("Já existe um usuário cadastrado com este email.");
        }
    }
}

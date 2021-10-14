package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.User;
import com.caiodev.Finances.exception.AuthenticationErrorException;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service //diz que é uma classe de serviço para eu poder utilizar a instância em outras classes, e pode injetar as dependências
public class UserServiceImpl implements UserService{

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User autenticar(String email, String senha) {
        Optional<User> user=repository.findByEmail(email);
        if(!user.isPresent()){
            throw new AuthenticationErrorException("Usuário não encontrado para o email informado.");
        }
        if(!user.get().getSenha().equals(senha)){
            throw new AuthenticationErrorException("senha inválida");
        }
        return user.get();
    }

    @Override
    @Transactional
    public User salvarUsuario(User usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe){
            throw new BusinessRuleException("Já existe um usuário cadastrado com este email.");
        }
    }
}

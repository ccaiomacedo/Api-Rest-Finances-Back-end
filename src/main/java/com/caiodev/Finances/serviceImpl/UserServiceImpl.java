package com.caiodev.Finances.serviceImpl;

import com.caiodev.Finances.entity.UserR;
import com.caiodev.Finances.exception.AuthenticationErrorException;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.UserRepository;
import com.caiodev.Finances.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service//diz que é uma classe de serviço para eu poder utilizar a instância em outras classes, e pode injetar as dependências
public class UserServiceImpl implements UserService {

    //teste
    private UserRepository repository;
    private PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;

    }

    @Override
    public UserR autenticar(String email, String senha) {
        Optional<UserR> user = repository.findByEmail(email);
        if (!user.isPresent()) {
            throw new AuthenticationErrorException("Usuário não encontrado para o email informado.");
        }
        boolean senhasBatem = encoder.matches(senha, user.get().getSenha());
        if (!senhasBatem) {
            throw new AuthenticationErrorException("senha inválida");
        }
        return user.get();
    }

    @Override
    @Transactional
    public UserR salvarUsuario(UserR usuario) {
        validarEmail(usuario.getEmail());
        CriptografarSenha(usuario);
        return repository.save(usuario);
    }

    private void CriptografarSenha(UserR usuario) {
        String senha = usuario.getSenha();
        String senhaCripto = encoder.encode(senha);
        usuario.setSenha(senhaCripto);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new BusinessRuleException("Já existe um usuário cadastrado com este email.");
        }
    }

    @Override
    public Optional<UserR> obterPorId(Long id) {
        return repository.findById(id);
    }
}

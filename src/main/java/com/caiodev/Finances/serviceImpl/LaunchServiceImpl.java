package com.caiodev.Finances.serviceImpl;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.repository.LaunchRepository;
import com.caiodev.Finances.service.LaunchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class LaunchServiceImpl implements LaunchService {

    private LaunchRepository repository;

    public LaunchServiceImpl(LaunchRepository repository){
        this.repository = repository; //esse metodo está substituindo o autowired
    }

    @Override
    @Transactional //ele abre uma transação, ai ele executa o conteúdo do metodo e ao final do metodo ele faz um commit e se ocorrer qualquer erro, ele faz o rollback
    public Launch salvar(Launch lancamento) {
        return null;
    }

    @Override
    public Launch atualizar(Launch lancamento) {
        return null;
    }

    @Override
    public void deletar(Launch lancamento) {

    }

    @Override
    public List<Launch> buscar(Launch lancamentoFiltro) {
        return null;
    }

    @Override
    public void atualizarStatus(Launch lancamento, LaunchStatus status) {

    }
}

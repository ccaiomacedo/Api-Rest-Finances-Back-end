package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.enums.LaunchStatus;

import java.util.List;

public interface LaunchService {

    Launch salvar(Launch lancamento);

    Launch atualizar(Launch lancamento);

    void deletar(Launch lancamento);

    List<Launch> buscar(Launch lancamentoFiltro);

    void atualizarStatus(Launch lancamento, LaunchStatus status);

}

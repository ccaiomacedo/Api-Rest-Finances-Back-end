package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.enums.LaunchStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LaunchService {

    Launch salvar(Launch lancamento);

    Launch atualizar(Launch lancamento);

    void deletar(Launch lancamento);

    List<Launch> buscar(Launch lancamentoFiltro);

    void atualizarStatus(Launch lancamento, LaunchStatus status);

    void validar(Launch lancamento);

    Optional<Launch> obterPorId(Long id);

    BigDecimal obterSaldoPorUsuario(Long id);
}

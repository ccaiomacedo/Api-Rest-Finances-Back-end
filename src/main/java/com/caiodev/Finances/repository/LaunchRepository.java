package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.enums.LaunchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LaunchRepository extends JpaRepository<Launch,Long> {

    @Query(value ="select sum(l.valor) from Launch l join l.user u " +
            "where u.id=:idUsuario and l.tipo =:tipo group by u")//vai somar todos os lançamentos do tipo que eu passar agrupado pelo usuário
    BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario,@Param("tipo") LaunchType tipo);
}

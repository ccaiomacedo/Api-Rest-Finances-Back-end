package com.caiodev.Finances.repository;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.enums.LaunchType;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest //anotação pra teste de integração
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//para que não sobrescreva minhas configurações no ambiente de teste
@ActiveProfiles("test")
public class LaunchRepositoryTest {

    @Autowired
    LaunchRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento(){
        Launch lancamento = criarLancamento();
        lancamento = repository.save(lancamento);

        Assertions.assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento(){
        Launch launch = criarEPersistirUmLancamento();

        launch = entityManager.find(Launch.class,launch.getId());//o find recebe a classe que vai buscar na base de dados e o id, ou seja está buscando o lançamento persistido

        repository.delete(launch);
        Launch lancamentoExistente = entityManager.find(Launch.class,launch.getId());
        Assertions.assertThat(lancamentoExistente).isNull();
    }
    @Test
    public void deveAtualizarUmLancamento(){
        Launch lancamento = criarEPersistirUmLancamento();

        lancamento.setAno(2018);
        lancamento.setDescricao("Teste Atualizar");
        lancamento.setStatus(LaunchStatus.CANCELADO);

        repository.save(lancamento);

        Launch lancamentoAtualizado = entityManager.find(Launch.class,lancamento.getId());// está buscando o lançamento persistido

        Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
        Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
        Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(LaunchStatus.CANCELADO);

    }
    @Test
    public void deveBuscarUmLancamentoPorId(){
        Launch lancamento = criarEPersistirUmLancamento();

        Optional<Launch> lancamentoEncontrado = repository.findById(lancamento.getId());
        Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }

    private Launch criarLancamento(){
        return Launch.builder().ano(2019).mes(1).descricao("lancamento qualquer").valor(BigDecimal.valueOf(10)).tipo(LaunchType.RECEITA).status(LaunchStatus.PENDENTE).dataCadastro(LocalDate.now()).build();

    }
    private Launch criarEPersistirUmLancamento(){
        Launch launch = criarLancamento();
        entityManager.persist(launch);//estou salvando no db
        return launch;
    }


}

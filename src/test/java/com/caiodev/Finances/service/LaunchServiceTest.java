package com.caiodev.Finances.service;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.entity.UserR;
import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.LaunchRepository;
import com.caiodev.Finances.repository.LaunchRepositoryTest;
import com.caiodev.Finances.serviceImpl.LaunchServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LaunchServiceTest {

    @SpyBean //estou mockando um service no spy, fazendo com que ele utilize as instâncias reais
    LaunchServiceImpl service;

    @MockBean //ele cria um mock, ele cria uma instância mockada ao invés das instâncias reais, ele substitui todas as instâncias de repository
    LaunchRepository repository;

    @Test
    public void deveSalvarUmLancamento() {
        //cenário

        //primeiro salvo um lançamento
        Launch lancamentoASalvar = LaunchRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);//dessa forma não vai chamar erro quando o service for validar la na implementação

        //depois atualizo ele
        Launch lancamentoSalvo = LaunchRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(LaunchStatus.PENDENTE);

        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execução
        Launch lancamento = service.salvar(lancamentoASalvar);

        //verificação
        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(LaunchStatus.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        //cenário
        Launch lancamentoASalvar = LaunchRepositoryTest.criarLancamento();
        Mockito.doThrow(BusinessRuleException.class).when(service).validar(lancamentoASalvar);
        //execução e verificação
        Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), BusinessRuleException.class);//verifica o tipo de erro que vai lançar e ja verifica se é do tipo BusinessRuleException
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);//verificando que não chamou a função de salvar
    }

    @Test
    public void deveAtualizarUmLancamento() {
        //cenário
        Launch lancamentoSalvo = LaunchRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(LaunchStatus.PENDENTE);

        Mockito.doNothing().when(service).validar(lancamentoSalvo);//n é pra fazer nd quando chamar o método validar

        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execução
        service.atualizar(lancamentoSalvo);

        //verificação
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);//verificando que o repository chamou uma vez a função salvar

    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenário
        Launch lancamento = LaunchRepositoryTest.criarLancamento();

        //execução e verificação
        Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);//verifica o tipo de erro que vai lançar,verifica se é do tipo NullPointerException e captura
        Mockito.verify(repository, Mockito.never()).save(lancamento);
    }

    @Test
    public void deveDeletarUmLancamento() {
        //cenário
        Launch lancamento = LaunchRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        //execução
        service.deletar(lancamento);

        //verificação
        Mockito.verify(repository).delete(lancamento);
    }
    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenário
        Launch lancamento = LaunchRepositoryTest.criarLancamento();

        //execução
        Assertions.catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);//verifica o tipo de erro que vai lançar,verifica se é do tipo NullPointerException e captura

        //verificação
        Mockito.verify(repository,Mockito.never()).delete(lancamento);

    }
    @Test
    public void deveFiltrarLancamentos(){
        //cenário
        Launch lancamento = LaunchRepositoryTest.criarLancamento();
        lancamento.setId(1L);

        List<Launch> lista = Arrays.asList(lancamento);//o asList transforma os objetos em uma lista
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execução
        List<Launch> resultado = service.buscar(lancamento);

        //verificação
        Assertions.assertThat(resultado).isNotEmpty()//lista n está vazia
                .hasSize(1)//possui um elemento
                .contains(lancamento);//contém lancamento
    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento(){
        //cenário
        Launch lancamento = LaunchRepositoryTest.criarLancamento();
        lancamento.setId(1L);
        lancamento.setStatus(LaunchStatus.PENDENTE);

        LaunchStatus novoStatus = LaunchStatus.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //execução
        service.atualizarStatus(lancamento,novoStatus);

        //verificação
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(service).atualizar(lancamento);
    }

    @Test
    public void deveObterLancamentoPorId(){
        //cenário
        Long id = 1L;

        Launch lancamento = LaunchRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        //execução
        Optional<Launch> resultado = service.obterPorId(id);

        //verificação
        Assertions.assertThat(resultado.isPresent()).isTrue();
    }
    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExiste(){
        //cenário
        Long id = 1L;

        Launch lancamento = LaunchRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Launch> resultado = service.obterPorId(id);

        //verificação
        Assertions.assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void deveLançarErrosAoValidarUmLancamento(){
        //cenário
        Launch launch = new Launch();

        Throwable erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe uma descrição válida");

        launch.setDescricao("");

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe uma descrição válida");

        launch.setDescricao("salário");

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um mês válido");

        launch.setMes(0);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um mês válido");

        launch.setMes(13);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um mês válido");

        launch.setMes(1);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um ano válido");

        launch.setAno(202);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um ano válido");

        launch.setAno(2020);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um usuário");

        launch.setUser(new UserR());

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um usuário");

        launch.getUser().setId(1L);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um valor válido");

        launch.setValor(BigDecimal.ZERO);

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um valor válido");

        launch.setValor(BigDecimal.valueOf(1));

        erro = Assertions.catchThrowable(()->service.validar(launch));
        Assertions.assertThat(erro).isInstanceOf(BusinessRuleException.class).hasMessage("Informe um tipo de lançamento");

    }


}

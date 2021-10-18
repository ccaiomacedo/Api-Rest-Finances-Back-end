package com.caiodev.Finances.serviceImpl;

import com.caiodev.Finances.entity.Launch;
import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.enums.LaunchType;
import com.caiodev.Finances.exception.BusinessRuleException;
import com.caiodev.Finances.repository.LaunchRepository;
import com.caiodev.Finances.service.LaunchService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LaunchServiceImpl implements LaunchService {

    private LaunchRepository repository;

    public LaunchServiceImpl(LaunchRepository repository){
        this.repository = repository; //esse metodo está substituindo o autowired
    }

    @Override
    @Transactional //ele abre uma transação, ai ele executa o conteúdo do metodo e ao final do metodo ele faz um commit e se ocorrer qualquer erro ele faz o rollback
    public Launch salvar(Launch lancamento) {
        validar(lancamento);
        lancamento.setStatus(LaunchStatus.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional //ele abre uma transação, ai ele executa o conteúdo do metodo e ao final do metodo ele faz um commit e se ocorrer qualquer erro ele faz o rollback
    public Launch atualizar(Launch lancamento) {
        Objects.requireNonNull(lancamento.getId());//aqui está garantindo que tem que passar um lançamento com id se não vai lançar um nullPointerException, está verificando se esse lancamento que vai atualizar ja possui id
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional //ele abre uma transação, ai ele executa o conteúdo do metodo e ao final do metodo ele faz um commit e se ocorrer qualquer erro ele faz o rollback
    public void deletar(Launch lancamento) {
        Objects.requireNonNull(lancamento.getId());//aqui está garantindo que tem que passar um lançamento com id se não vai lançar um nullPointerException
        repository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Launch> buscar(Launch lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()//pra ignorar se a busca foi feita em maiusculo ou minusculo
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));//serve pra caso o usuário digite apenas um pedaço da descrição ela retorne a descrição
        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Launch lancamento, LaunchStatus status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    public void validar(Launch lancamento) {
        if(lancamento.getDescricao()==null || lancamento.getDescricao().trim().equals("")){//o metodo trim remove os espaços em branco do inicio ou fim de um texto
            throw  new BusinessRuleException("Informe uma descrição válida");
        }
        if(lancamento.getMes()==null || lancamento.getMes()<1||lancamento.getMes()>12){
            throw  new BusinessRuleException("Informe um mês válido");
        }
        if(lancamento.getAno()==null || lancamento.getAno().toString().length()!=4){//
            throw  new BusinessRuleException("Informe um ano válido");
        }
        if(lancamento.getUser()==null || lancamento.getUser().getId()==null){
            throw  new BusinessRuleException("Informe um usuário");
        }
        if(lancamento.getValor()==null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1){//se o BigDecimal comparado a zero for menor que um vai lançar a exception
            throw  new BusinessRuleException("Informe um valor válido");
        }
        if(lancamento.getTipo()==null ){
            throw  new BusinessRuleException("Informe um tipo de lançamento");
        }
    }

    @Override
    public Optional<Launch> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long id) {
        BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, LaunchType.RECEITA);//somatória das receitas
        BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, LaunchType.DESPESA);//somatória das despesas

        if(receitas==null){
            receitas = BigDecimal.ZERO;
        }
        if(despesas==null){
            despesas =BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }
}

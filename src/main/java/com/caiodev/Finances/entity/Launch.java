package com.caiodev.Finances.entity;

import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.enums.LaunchType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="lancamento",schema="financas")
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="mes")
    private Integer mes;

    @Column(name="ano")
    private Integer ano;

    @ManyToOne
    @JoinColumn(name="id_usuario")//join é pra dizer que é uma coluna de relacionamento
    private User user;

    @Column(name="valor")
    private BigDecimal valor;

    @Column(name="data_cadastro")
    private LocalDate dataCadastro;

    @Column(name="tipo")
    @Enumerated(value = EnumType.STRING) //essa anotação é pra dizer que é um tipo enumerado
    private LaunchType tipo;

    @Column(name="status")
    @Enumerated(value = EnumType.STRING) //essa anotação é pra dizer que é um tipo enumerado
    private LaunchStatus Status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LaunchType getTipo() {
        return tipo;
    }

    public void setTipo(LaunchType tipo) {
        this.tipo = tipo;
    }

    public LaunchStatus getStatus() {
        return Status;
    }

    public void setStatus(LaunchStatus status) {
        Status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return id.equals(launch.id) && mes.equals(launch.mes) && ano.equals(launch.ano) && user.equals(launch.user) && valor.equals(launch.valor) && dataCadastro.equals(launch.dataCadastro) && tipo == launch.tipo && Status == launch.Status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mes, ano, user, valor, dataCadastro, tipo, Status);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Launch{");
        sb.append("id=").append(id);
        sb.append(", mes=").append(mes);
        sb.append(", ano=").append(ano);
        sb.append(", user=").append(user);
        sb.append(", valor=").append(valor);
        sb.append(", dataCadastro=").append(dataCadastro);
        sb.append(", tipo=").append(tipo);
        sb.append(", Status=").append(Status);
        sb.append('}');
        return sb.toString();
    }
}

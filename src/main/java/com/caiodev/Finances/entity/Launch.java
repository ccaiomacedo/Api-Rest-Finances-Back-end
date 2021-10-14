package com.caiodev.Finances.entity;

import com.caiodev.Finances.enums.LaunchStatus;
import com.caiodev.Finances.enums.LaunchType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="lancamento",schema="financas")
@Data //anotação lombok que substitui as outras
@Builder
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
    @Enumerated(value = EnumType.STRING) //essa anotação é pra dizer que é um tipo enumerado, e está salvando com string
    private LaunchType tipo;

    @Column(name="status")
    @Enumerated(value = EnumType.STRING) //essa anotação é pra dizer que é um tipo enumerado, e está salvando com string
    private LaunchStatus Status;

}

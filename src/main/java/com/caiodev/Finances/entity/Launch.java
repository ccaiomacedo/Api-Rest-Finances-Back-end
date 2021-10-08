package com.caiodev.Finances.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

}

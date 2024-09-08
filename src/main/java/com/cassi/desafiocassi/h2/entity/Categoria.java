package com.cassi.desafiocassi.h2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;
    @Column(name = "nome_categoria")
    private String nomeCategoria;
    @Column(name = "descricao_categoria")
    private String descricaoCategoria;
    @Column(name = "taxa")
    private BigDecimal taxa;
    @Column(name = "desconto")
    private BigDecimal desconto;
}
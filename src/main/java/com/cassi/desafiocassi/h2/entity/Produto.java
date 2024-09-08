package com.cassi.desafiocassi.h2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;
    @Column(name = "nome_produto")
    private String nomeProduto;
    @Column(name = "descricao_produto")
    private String descricaoProduto;
    @Column(name = "preco_base")
    private BigDecimal precoBase;
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
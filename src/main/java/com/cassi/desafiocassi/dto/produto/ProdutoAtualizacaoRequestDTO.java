package com.cassi.desafiocassi.dto.produto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoAtualizacaoRequestDTO {
    private String nomeProduto;
    private String descricaoProduto;
    @Positive(message = "o preço base deve ser maior que 0!")
    private BigDecimal precoBase;
    private String nomeCategoria;
}
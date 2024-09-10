package com.cassi.desafiocassi.dto.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProdutoCadastroRequestDTO {
    @NotBlank(message = "nome do produto é obrigatório!")
    private String nomeProduto;
    @NotBlank(message = "preço base é obrigatório!")
    private String descricaoProduto;
    @NotNull(message = "preço base é obrigatório!")
    @Positive(message = "o preço base deve ser maior que 0!")
    private BigDecimal precoBase;
    @NotBlank(message = "categoria é obrigatória!")
    private String nomeCategoria;
}
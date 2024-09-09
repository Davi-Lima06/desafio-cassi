package com.cassi.desafiocassi.dto.produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoPrecoFinalResponseDTO {

    private Long idProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private String precoFinal;
    private String nomeCategoria;
    private String dataCadastro;

}
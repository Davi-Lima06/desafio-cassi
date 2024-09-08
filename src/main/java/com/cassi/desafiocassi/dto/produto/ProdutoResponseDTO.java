package com.cassi.desafiocassi.dto.produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoResponseDTO {

    private Long idProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private String precoBase;
    private String nomeCategoria;
    private String dataCadastro;

}
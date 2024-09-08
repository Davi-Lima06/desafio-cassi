package com.cassi.desafiocassi.dto.produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoResponsePaginadoDTO {

    private long numeroDePaginas;
    private long totalRegistros;
    private List<ProdutoResponseDTO> produtos;

}
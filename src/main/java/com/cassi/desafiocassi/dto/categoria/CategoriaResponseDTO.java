package com.cassi.desafiocassi.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponseDTO {
    private String nomeCategoria;
    private String descricaoCategoria;
    private String taxa;
    private String desconto;

}
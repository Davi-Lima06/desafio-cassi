package com.cassi.desafiocassi.configuration.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroGenerico {
    private String mensagem;
}
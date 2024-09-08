package com.cassi.desafiocassi.configuration.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormularioErroGenerico extends ErroGenerico {

    private String campo;

    public FormularioErroGenerico(FieldError fieldError) {
        super(fieldError.getDefaultMessage());
        this.campo = fieldError.getField();;
    }
}
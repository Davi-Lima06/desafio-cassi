package com.cassi.desafiocassi.configuration.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(erros.stream().map(FormularioErroGenerico::new).toList());
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity tratarErro400Generico(GenericException ex) {
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroGenerico(ex.getMessage()));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity tratarErro404(ObjectNotFoundException ex) {
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroGenerico(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity tratarErro500(DataIntegrityException ex) {
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroGenerico(ex.getMessage()));
    }
}
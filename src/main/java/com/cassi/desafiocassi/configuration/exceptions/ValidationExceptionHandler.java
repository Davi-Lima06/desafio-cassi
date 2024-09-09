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
    /**
     * intercepta todas as exceptions do tipo MethodArgumentNotValidException e converte para
     * FormularioErroGenerico com status 400
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity do tipo FormularioErroGenerico
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(erros.stream().map(FormularioErroGenerico::new).toList());
    }

    /**
     * intercepta todas as exceptions do tipo GenericException e converte para
     * ErroGenerico com status 400
     * @param ex ErroGenerico
     * @return ResponseEntity do tipo ErroGenerico
     */
    @ExceptionHandler(GenericException.class)
    public ResponseEntity tratarErro400Generico(GenericException ex) {
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroGenerico(ex.getMessage()));
    }

    /**
     * intercepta todas as exceptions do tipo ObjectNotFoundException e converte para
     * ErroGenerico com status 404
     * @param ex ErroGenerico
     * @return ResponseEntity do tipo ErroGenerico
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity tratarErro404(ObjectNotFoundException ex) {
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroGenerico(ex.getMessage()));
    }

    /**
     * intercepta todas as exceptions do tipo DataIntegrityException e converte para
     * ErroGenerico com status 500
     * @param ex ErroGenerico
     * @return ResponseEntity do tipo ErroGenerico
     */
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity tratarErro500(DataIntegrityException ex) {
        log.error("Erro: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroGenerico(ex.getMessage()));
    }
}
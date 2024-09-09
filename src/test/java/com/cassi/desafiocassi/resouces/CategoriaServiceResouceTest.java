package com.cassi.desafiocassi.resouces;

import com.cassi.desafiocassi.dto.categoria.CategoriaRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaResponseDTO;
import com.cassi.desafiocassi.h2.entity.Categoria;
import com.cassi.desafiocassi.h2.entity.Produto;
import com.cassi.desafiocassi.h2.repository.CategoriaRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoriaServiceResouceTest {

    @MockBean
    CategoriaRepository categoriaRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarCategoriaSucesso() {

        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(gerarCategoria()));
        when(categoriaRepository.save(any())).thenReturn(new Produto());

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarCategoriaRequestDto())
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Categoria cadastrada com sucesso!"));
    }

    private Categoria gerarCategoria() {
        return Categoria.builder()
                .nomeCategoria("categoria")
                .descricaoCategoria("descricao")
                .taxa(new BigDecimal(10))
                .desconto(new BigDecimal(0))
                .idCategoria(1L)
                .build();
    }

    private CategoriaRequestDTO gerarCategoriaRequestDto() {
        return CategoriaRequestDTO.builder()
                .desconto(new BigDecimal(10))
                .taxa(new BigDecimal(0))
                .descricaoCategoria("descrição categoria Request")
                .nomeCategoria("nome categoria Request")
                .build();
    }

    private CategoriaResponseDTO gerarCategoriaResponseDto() {
        return CategoriaResponseDTO.builder()
                .desconto("10%")
                .taxa("0%")
                .descricaoCategoria("descrição categoria Response")
                .nomeCategoria("nome categoria Response")
                .build();
    }
}
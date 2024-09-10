package com.cassi.desafiocassi.testcontroller;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.dto.categoria.CategoriaAtualizacaoRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaCadastroRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaResponseDTO;
import com.cassi.desafiocassi.h2.entity.Categoria;
import com.cassi.desafiocassi.h2.repository.CategoriaRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(null));
        when(categoriaRepository.save(any())).thenReturn(new Categoria());

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarCategoriaRequestDto())
                .when()
                .post("/categorias")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("Categoria cadastrada com sucesso!"));
    }

    @Test
    void cadastrarCategoriaDuplicada() {

        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(gerarCategoria()));

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarCategoriaRequestDto())
                .when()
                .post("/categorias")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "Uma categoria com nome: nome categoria Request, já foi cadastrada!");
    }

    @Test
    void cadastrarCategoriaErroException() {

        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(null));
        when(categoriaRepository.save(any())).thenThrow(new DataIntegrityException(""));

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarCategoriaRequestDto())
                .when()
                .post("/categorias")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "Erro ao inserir categoria, entre em contato com o administrador!");
    }

    @Test
    void listarCategoriasSucesso() {

        Categoria categoria = gerarCategoria();
        categoria.setNomeCategoria("FUNCIONOU");

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria, categoria, categoria));

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/categorias")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        List<CategoriaResponseDTO> produtoResponse = response.jsonPath().getList("", CategoriaResponseDTO.class);

        Assertions.assertEquals(produtoResponse.size(), 3);
        Assertions.assertEquals(produtoResponse.get(0).getNomeCategoria(), "FUNCIONOU");
    }

    @Test
    void listarCategoriasNenhumaCategoriaCadastrada() {

        when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/categorias")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhuma categoria cadastrada!");
    }

    @Test
    void listarCategoriaPorNomeCategoriaSucesso() {
        Categoria categoria = gerarCategoria();
        categoria.setNomeCategoria("NOVO NOME");
        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(categoria));

        Response response = given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        CategoriaResponseDTO categoriaResponseDTO = response.jsonPath().getObject("", CategoriaResponseDTO.class);

        Assertions.assertEquals(categoriaResponseDTO.getNomeCategoria(), "NOVO NOME");
    }

    @Test
    void listarCategoriaPorNomeCategoriaNaoEncontrado() {
        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(null));

        Response response = given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhuma categoria cadastrada com esse nome!");
    }

    @Test
    void atualizarCategoriaPorNomeCategoriaSucesso() {
        Categoria categoria = gerarCategoria();
        CategoriaAtualizacaoRequestDTO categoriaRequest = gerarCategoriaAtualizacaoRequestDto();
        categoriaRequest.setNomeCategoria("Outro nome");
        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(categoria));
        when(categoriaRepository.save(any())).thenReturn(new Categoria());

        given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(categoriaRequest)
                .when()
                .put("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Categoria atualizada com sucesso!"));
    }

    @Test
    void atualizarCategoriaPorNomeCategoriaNaoEncontrado() {
        CategoriaAtualizacaoRequestDTO categoriaRequest = gerarCategoriaAtualizacaoRequestDto();
        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(null));

        Response response = given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(categoriaRequest)
                .when()
                .put("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhuma categoria cadastrada com esse nome!");
    }

    @Test
    void atualizarCategoriaPorNomeCategoriaErroException() {
        CategoriaAtualizacaoRequestDTO categoriaRequest = gerarCategoriaAtualizacaoRequestDto();
        when(categoriaRepository.save(any())).thenThrow(new DataIntegrityException(""));
        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(gerarCategoria()));

        Response response = given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(categoriaRequest)
                .when()
                .put("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "Erro ao atualizar categoria, entre em contato com o administrador!");
    }

    @Test
    void excluirCategoriaPorNomeCategoriaSucesso() {

        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(gerarCategoria()));

        given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Categoria excluída com sucesso!"));
    }

    @Test
    void excluirCategoriaPorNomeCategoriaNaoEncontrado() {

        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(null));

        Response response = given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhuma categoria cadastrada com esse nome!");
    }

    @Test
    void excluirCategoriaPorNomeCategoriaErroException() {

        when(categoriaRepository.findByNomeCategoria("nome")).thenReturn(Optional.ofNullable(gerarCategoria()));
        doThrow(new DataIntegrityException(""))
                .when(categoriaRepository)
                .delete(any());

        Response response = given()
                .pathParam("nomeCategoria", "nome")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/categorias/{nomeCategoria}")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract().response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "Erro ao excluir categoria, entre em contato com o administrador!");
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

    private CategoriaCadastroRequestDTO gerarCategoriaRequestDto() {
        return CategoriaCadastroRequestDTO.builder()
                .desconto(new BigDecimal(10))
                .taxa(new BigDecimal(0))
                .descricaoCategoria("descrição categoria Request")
                .nomeCategoria("nome categoria Request")
                .build();
    }

    private CategoriaAtualizacaoRequestDTO gerarCategoriaAtualizacaoRequestDto() {
        return CategoriaAtualizacaoRequestDTO.builder()
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
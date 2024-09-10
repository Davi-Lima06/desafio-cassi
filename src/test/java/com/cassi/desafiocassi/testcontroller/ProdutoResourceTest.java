package com.cassi.desafiocassi.testcontroller;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.dto.produto.*;
import com.cassi.desafiocassi.h2.entity.Categoria;
import com.cassi.desafiocassi.h2.entity.Produto;
import com.cassi.desafiocassi.h2.repository.CategoriaRepository;
import com.cassi.desafiocassi.h2.repository.ProdutoRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class ProdutoResourceTest {
    @MockBean
    ProdutoRepository produtoRepository;
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
    void cadastrarProdutoSucesso() {

        when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(gerarProduto());
        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(gerarCategoria()));

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarProdutoRequest())
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Produto cadastrado com sucesso!"));
    }

    @Test
    void cadastrarProdutoException() {
        when(produtoRepository.save(Mockito.any(Produto.class))).thenThrow(new DataIntegrityException(""));
        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(gerarCategoria()));

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarProdutoRequest())
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract().response();

        String mensagemErro = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagemErro,"Erro ao cadastrar produto, entre em contato com o administrador!");
    }

    @Test
    void cadastrarProdutoFaltandoCampos() {

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ProdutoCadastroRequestDTO())
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response();

        List<String> mensagemErro = response.jsonPath().getList("mensagem");

        Assertions.assertTrue(mensagemErro.contains("nome do produto é obrigatório!"));
    }

    @Test
    void listarProdutosSucesso() {
        String nomeProduto = "Produto";
        int numeroPagina = 0;
        int tamanhoPagina = 5;
        String sortBy = "precoBase";
        String direcao = "asc";

        Produto produto = gerarProduto();
        produto.setNomeProduto("nome produto teste");
        Page<Produto> paginaProdutos = new PageImpl<>(Arrays.asList(produto,produto,produto));

        when(produtoRepository.listarProdutos(any(), any(), any(), any()))
                .thenReturn(paginaProdutos);

        Response response = given()
                .queryParam("nomeProduto", nomeProduto)
                .queryParam("numeroPagina", numeroPagina)
                .queryParam("tamanhoPagina", tamanhoPagina)
                .queryParam("sortBy", sortBy)
                .queryParam("direction", direcao)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        ProdutoResponsePaginadoDTO produtoResponse = response.jsonPath().getObject("", ProdutoResponsePaginadoDTO.class);

        Assertions.assertEquals(produtoResponse.getProdutos().size(), 3);
        Assertions.assertEquals(produtoResponse.getProdutos().get(0).getNomeProduto(), "nome produto teste");
    }

    @Test
    void listarProdutosNenhumProdutoCadastrado() {


        Page<Produto> paginaProdutos = new PageImpl<>(Collections.emptyList());

        when(produtoRepository.listarProdutos(any(), any(), any(), any()))
                .thenReturn(paginaProdutos);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhum produto cadastrado!");
    }

    @Test
    public void listarProdutoPorIdSucesso() {
        Long idProduto = 1L;
        Produto produto = gerarProduto();
        produto.setNomeProduto("Produto de Exemplo");

        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(produto));

        given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("idProduto", equalTo(idProduto.intValue()))
                .body("nomeProduto", equalTo("Produto de Exemplo"));
    }

    @Test
    public void listarProdutoPorIdProdutoNaoEncontrado() {
        Long idProduto = 1L;

        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhum produto cadastrado com esse id!");
    }

    @Test
    void atualizarProdutoPorIdSucesso() {
        Long idProduto = 1L;

        when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(gerarProduto());
        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(gerarProduto()));
        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(gerarCategoria()));

        given()
                .pathParam("idProduto", idProduto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarProdutoAtualizacaoRequest())
                .when()
                .put("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Produto atualizado com sucesso!"));
    }

    @Test
    void atualizarProdutoPorIdProdutoNaoEncontrado() {
        Long idProduto = 1L;

        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarProdutoAtualizacaoRequest())
                .when()
                .put("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhum produto cadastrado com esse id!");
    }

    @Test
    void atualizarProdutoPorIdErroAtualizacaoException() {
        Long idProduto = 1L;

        when(produtoRepository.save(Mockito.any(Produto.class))).thenThrow(new DataIntegrityException("ERRO"));
        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(gerarProduto()));
        when(categoriaRepository.findByNomeCategoria(any())).thenReturn(Optional.ofNullable(gerarCategoria()));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(gerarProdutoAtualizacaoRequest())
                .when()
                .put("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "Erro ao atualizar o produto, entre em contato com o administrador!");
    }

    @Test
    public void excluirProdutoPorIdSucesso() {
        Long idProduto = 1L;
        Produto produto = gerarProduto();

        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(produto));

        given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Produto excluído com sucesso!"));
    }

    @Test
    public void excluirProdutoPorIdProdutoNaoEncontrado() {
        Long idProduto = 1L;

        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Response response =  given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhum produto cadastrado com esse id!");
    }

    @Test
    public void excluirProdutoPorIdErroExclusaoException() {
        Long idProduto = 1L;

        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(gerarProduto()));
        doThrow(new DataIntegrityException(""))
                .when(produtoRepository)
                .delete(any());

        Response response =  given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/produtos/{idProduto}")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "Erro ao excluir  o produto, entre em contato com o administrador!");
    }

    @Test
    public void calcularPrecoFinalComDesconto() {
        Long idProduto = 1L;
        Produto produto = gerarProduto();
        Categoria categoria = gerarCategoria();
        categoria.setDesconto(new BigDecimal(10));
        produto.setPrecoBase(new BigDecimal(100));
        produto.setCategoria(categoria);
        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(produto));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos/{idProduto}/preco-final")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        ProdutoPrecoFinalResponseDTO produtoResponse = response.jsonPath().getObject("", ProdutoPrecoFinalResponseDTO.class);

        Assertions.assertEquals(produtoResponse.getPrecoFinal(), "R$ 90,00");
    }

    @Test
    public void calcularPrecoFinalComTaxa() {
        Long idProduto = 1L;
        Produto produto = gerarProduto();
        Categoria categoria = gerarCategoria();
        categoria.setTaxa(new BigDecimal(10));
        produto.setPrecoBase(new BigDecimal(100));
        produto.setCategoria(categoria);
        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(produto));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos/{idProduto}/preco-final")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        ProdutoPrecoFinalResponseDTO produtoResponse = response.jsonPath().getObject("", ProdutoPrecoFinalResponseDTO.class);

        Assertions.assertEquals(produtoResponse.getPrecoFinal(), "R$ 110,00");
    }

    @Test
    public void calcularPrecoFinalSemDescontoETaxa() {
        Long idProduto = 1L;
        Produto produto = gerarProduto();
        Categoria categoria = gerarCategoria();
        categoria.setTaxa(new BigDecimal(0));
        categoria.setDesconto(new BigDecimal(0));
        produto.setPrecoBase(new BigDecimal(100));
        produto.setCategoria(categoria);
        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(produto));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos/{idProduto}/preco-final")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        ProdutoPrecoFinalResponseDTO produtoResponse = response.jsonPath().getObject("", ProdutoPrecoFinalResponseDTO.class);

        Assertions.assertEquals(produtoResponse.getPrecoFinal(), "R$ 100,00");
    }

    @Test
    public void calcularPrecoFinalProdutoNaoEncontrado() {
        Long idProduto = 1L;
        when(produtoRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Response response = given()
                .pathParam("idProduto", idProduto)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/produtos/{idProduto}/preco-final")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .response();

        String mensagem = response.jsonPath().getString("mensagem");

        Assertions.assertEquals(mensagem, "nenhum produto cadastrado com esse id!");
    }

    private Produto gerarProduto() {
        return Produto.builder()
                .nomeProduto("nome do produto")
                .descricaoProduto("descrição")
                .dataCadastro(LocalDate.now())
                .categoria(gerarCategoria())
                .precoBase(BigDecimal.valueOf(152.0))
                .idProduto(1L)
                .build();
    }

    private ProdutoCadastroRequestDTO gerarProdutoRequest() {
        return ProdutoCadastroRequestDTO.builder()
                .nomeProduto("nome do produto Request")
                .descricaoProduto("descrição Resquest")
                .nomeCategoria("categoria Request")
                .precoBase(BigDecimal.valueOf(152.0))
                .precoBase(new BigDecimal(20))
                .build();
    }

    private ProdutoAtualizacaoRequestDTO gerarProdutoAtualizacaoRequest() {
        return ProdutoAtualizacaoRequestDTO.builder()
                .nomeProduto("nome do produto Request")
                .descricaoProduto("descrição Resquest")
                .nomeCategoria("categoria Request")
                .precoBase(BigDecimal.valueOf(152.0))
                .precoBase(new BigDecimal(20))
                .build();
    }

    private ProdutoResponseDTO gerarProdutoResponse() {
        return ProdutoResponseDTO.builder()
                .nomeProduto("nome do produto Response")
                .idProduto(1L)
                .descricaoProduto("descrição produto Response")
                .dataCadastro(LocalDate.now().toString())
                .precoBase("preço base Response")
                .build();
    }

    private ProdutoPrecoFinalResponseDTO gerarProdutoPrecoFinalResponse() {
        return ProdutoPrecoFinalResponseDTO.builder()
                .nomeProduto("nome do produto Response")
                .idProduto(1L)
                .descricaoProduto("descrição produto Response")
                .dataCadastro(LocalDate.now().toString())
                .precoFinal("preço final Response")
                .build();
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
}
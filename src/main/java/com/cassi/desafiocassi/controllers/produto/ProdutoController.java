package com.cassi.desafiocassi.controllers.produto;

import com.cassi.desafiocassi.dto.produto.*;
import com.cassi.desafiocassi.h2.repository.ProdutoRepository;
import com.cassi.desafiocassi.services.produto.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    ProdutoRepository produtoRepository;

    @PostMapping
    @Operation(summary = "Cadastro de produtos.",
            description = "endpoint responsável pelo cadastro de produtos no banco de dados.")
    public ResponseEntity<String> cadastrarProduto(@Valid @RequestBody ProdutoCadastroRequestDTO produtoDTO) {

        return ResponseEntity.ok().body(produtoService.cadastrarProduto(produtoDTO));
    }

    @GetMapping("/{idProduto}")
    @Operation(summary = "Listar um produto.",
            description = "endpoint responsável por listar um produto por id do produto.")
    public ResponseEntity<ProdutoResponseDTO> listarProdutoPorIdDoProduto(
            @PathVariable("idProduto") Long idProduto) {
        return ResponseEntity.ok().body(produtoService.listarProdutoPorId(idProduto));
    }

    @PutMapping("/{idProduto}")
    @Operation(summary = "Atualizar um produto.",
            description = "endpoint responsável por atualizar um produto por id do produto.")
    public ResponseEntity<String> atualizarProdutoPorIdProduto(
            @Valid @RequestBody ProdutoAtualizacaoRequestDTO produtoAtualizacaoRequestDTO,
            @PathVariable("idProduto") Long idProduto) {
        return  ResponseEntity.ok().body(produtoService.atualizarProdutoPorId(idProduto, produtoAtualizacaoRequestDTO));
    }

    @DeleteMapping("/{idProduto}")
    @Operation(summary = "Excluir um produto.",
            description = "endpoint responsável por excluir um endpoint do banco de dados por id.")
    public ResponseEntity<String> excluirProdutoPorId(
            @PathVariable("idProduto") Long idProduto) {
        return  ResponseEntity.ok().body(produtoService.excluirProdutoPorId(idProduto));
    }

    @GetMapping("/{idProduto}/preco-final")
    @Operation(summary = "Listar um produto.",
            description = "endpoint responsável por listar um produto por id do produto.")
    public ResponseEntity<ProdutoPrecoFinalResponseDTO> calcularPrecoFinal(
            @PathVariable("idProduto") Long idProduto) {
        return ResponseEntity.ok().body(produtoService.calcularPrecoFinal(idProduto));
    }

    @GetMapping
    @Operation(summary = "Listagem de produtos.",
            description = "endpoint responsável pela listagem de todos os produtos cadastrados no banco de dados.")
    public ResponseEntity<ProdutoResponsePaginadoDTO> listarProdutos(
            @RequestParam(required = false) String nomeProduto,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String nomeCategoria,
            @RequestParam(defaultValue = "0") int numeroPagina,
            @RequestParam(defaultValue = "10") int tamanhoPagina,
            @RequestParam(defaultValue = "precoBase") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        ProdutoResponsePaginadoDTO produtoResponseDTOS = produtoService
                .listarProdutos(nomeProduto, descricao, nomeCategoria, numeroPagina, tamanhoPagina, sortBy, direction);

        return ResponseEntity.ok().body(produtoResponseDTOS);
    }
}
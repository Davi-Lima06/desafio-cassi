package com.cassi.desafiocassi.services;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.configuration.exceptions.ObjectNotFoundException;
import com.cassi.desafiocassi.dto.produto.ProdutoRequestDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoResponseDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoResponsePaginadoDTO;
import com.cassi.desafiocassi.h2.entity.Produto;
import com.cassi.desafiocassi.h2.repository.ProdutoRepository;
import com.cassi.desafiocassi.mappers.produto.ProdutoMapper;
import com.cassi.desafiocassi.services.produto.ProdutoService;

import static com.cassi.desafiocassi.enums.produto.ProdutoMensagemEnum.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarProdutoSucesso() {
        // Arrange
        ProdutoRequestDTO produtoDTO = new ProdutoRequestDTO();
        Produto produto = new Produto();
        when(produtoMapper.converterRequestDtoParaEntidade(produtoDTO)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);

        String resultado = produtoService.cadastrarProduto(produtoDTO);

        assertEquals(CADASTRO_PRODUTO.getMensagem(), resultado);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void cadastrarProdutoException() {
        ProdutoRequestDTO produtoDTO = new ProdutoRequestDTO();
        Produto produto = new Produto();
        when(produtoMapper.converterRequestDtoParaEntidade(produtoDTO)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenThrow(new RuntimeException("Erro ao salvar"));

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> {
            produtoService.cadastrarProduto(produtoDTO);
        });

        assertEquals(ERRO_CADASTRAR_PRODUTO.getMensagem(), exception.getMessage());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void listarProdutosSucesso() {
        String nomeProduto = "Produto";
        String descricao = "Descrição";
        String nomeCategoria = "Categoria";
        int numeroPagina = 0;
        int tamanhoPagina = 5;
        String sortBy = "precoBase";
        String direcao = "asc";

        Produto produto = new Produto();
        Page<Produto> paginaProdutos = new PageImpl<>(List.of(produto));

        when(produtoRepository.listarProdutos(nomeProduto, descricao, nomeCategoria, PageRequest.of(numeroPagina,
                tamanhoPagina, Sort.by(Sort.Direction.ASC, sortBy))))
                .thenReturn(paginaProdutos);

        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO();
        when(produtoMapper.converterEntidadeParaResponseDto(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponsePaginadoDTO resultado = produtoService.listarProdutos(nomeProduto, descricao, nomeCategoria,
                numeroPagina, tamanhoPagina, sortBy, direcao);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalRegistros());
        assertEquals(1, resultado.getNumeroDePaginas());
        assertEquals(1, resultado.getProdutos().size());
    }

    @Test
    void listarProdutosNenhumProdutoCadastrado() {
        String nomeProduto = "Produto";
        String descricao = "Descrição";
        String nomeCategoria = "Categoria";
        int numeroPagina = 0;
        int tamanhoPagina = 5;
        String sortBy = "precoBase";
        String direcao = "asc";

        Page<Produto> paginaProdutos = Page.empty();
        when(produtoRepository.listarProdutos(nomeProduto, descricao, nomeCategoria, PageRequest.of(numeroPagina,
                tamanhoPagina, Sort.by(Sort.Direction.ASC, sortBy))))
                .thenReturn(paginaProdutos);

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            produtoService.listarProdutos(nomeProduto, descricao, nomeCategoria, numeroPagina, tamanhoPagina, sortBy, direcao);
        });

        assertEquals(NENHUM_PRODUTO_CADASTRADO.getMensagem(), exception.getMessage());
        verify(produtoRepository, times(1))
                .listarProdutos(nomeProduto, descricao, nomeCategoria, PageRequest.of(numeroPagina, tamanhoPagina,
                        Sort.by(Sort.Direction.ASC, sortBy)));
    }

}
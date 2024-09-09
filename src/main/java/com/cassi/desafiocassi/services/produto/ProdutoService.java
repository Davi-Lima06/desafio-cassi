package com.cassi.desafiocassi.services.produto;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.configuration.exceptions.ObjectNotFoundException;
import com.cassi.desafiocassi.dto.produto.ProdutoPrecoFinalResponseDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoRequestDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoResponseDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoResponsePaginadoDTO;
import com.cassi.desafiocassi.h2.entity.Produto;
import com.cassi.desafiocassi.h2.repository.ProdutoRepository;
import com.cassi.desafiocassi.mappers.produto.ProdutoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.cassi.desafiocassi.enums.produto.ProdutoMensagemEnum.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public String cadastrarProduto(ProdutoRequestDTO produtoDTO) {
        try {
            Produto produto = produtoMapper.converterRequestDtoParaEntidade(produtoDTO);
            produtoRepository.save(produto);
            return CADASTRO_PRODUTO.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_CADASTRAR_PRODUTO.getMensagem());
        }
    }

    public ProdutoResponsePaginadoDTO listarProdutos(String nomeProduto,
                                                     String descricao,
                                                     String nomeCategoria,
                                                     int numeroPagina,
                                                     int tamanhoPagina,
                                                     String sortBy,
                                                     String direcao) {

        Direction sortDirection = direcao.equalsIgnoreCase("desc") ? DESC : ASC;
        Pageable pageable = PageRequest.of(numeroPagina, tamanhoPagina, by(sortDirection, sortBy));

        Page<Produto> paginaProdutos = produtoRepository.listarProdutos(nomeProduto, descricao, nomeCategoria, pageable);

        if (paginaProdutos.isEmpty()) {
            throw new ObjectNotFoundException(NENHUM_PRODUTO_CADASTRADO.getMensagem());
        }
        long totalPaginas = paginaProdutos.getTotalPages();
        long totalProdutos = paginaProdutos.getTotalElements();

        List<ProdutoResponseDTO> listaProdutosDto = paginaProdutos.stream()
                .map(produtoMapper::converterEntidadeParaResponseDto).toList();

        return ProdutoResponsePaginadoDTO.builder()
                .numeroDePaginas(totalPaginas)
                .totalRegistros(totalProdutos)
                .produtos(listaProdutosDto)
                .build();
    }

    public ProdutoResponseDTO listarProdutoPorId(Long idProduto) {
        ProdutoResponseDTO produtoResponseDTO = produtoMapper
                .converterEntidadeParaResponseDto(buscarProdutoPorId(idProduto));
        return produtoResponseDTO;
    }

    public String atualizarProdutoPorId(Long idProduto, ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = buscarProdutoPorId(idProduto);

        try {
            Produto produtoAtualizado = produtoMapper.atualizarProduto(produto, produtoRequestDTO);
            produtoRepository.save(produtoAtualizado);
            return ATUALIZACAO_PRODUTO.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_ATUALIZACAO_PRODUTO.getMensagem());
        }
    }

    public String excluirProdutoPorId(Long idProduto) {
        Produto produto = buscarProdutoPorId(idProduto);

        try {
            produtoRepository.delete(produto);
            return EXCLUSAO_PRODUTO.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_EXCLUSAO_PRODUTO.getMensagem());
        }
    }

    public ProdutoPrecoFinalResponseDTO calcularPrecoFinal(Long idProduto) {
            Produto produto = buscarProdutoPorId(idProduto);
            BigDecimal precoBase = produto.getPrecoBase();
            BigDecimal ajustePreco = BigDecimal.ZERO;


            if (produto.getCategoria().getDesconto().compareTo(BigDecimal.ZERO) != 0) {
                ajustePreco = produto.getCategoria().getDesconto().negate();
            } else if (produto.getCategoria().getTaxa().compareTo(BigDecimal.ZERO) != 0) {
                ajustePreco = produto.getCategoria().getTaxa();
            }

            BigDecimal fator = BigDecimal.ONE.add(ajustePreco.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

            BigDecimal precoFinal = precoBase.multiply(fator).setScale(2, RoundingMode.HALF_UP);
        return produtoMapper.converterEntidadeParaPrecoFinalResponseDto(produto, precoFinal);
    }

    private Produto buscarProdutoPorId(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if (produto == null) {
            throw new ObjectNotFoundException(PRODUTO_NAO_ENCONTRADO.getMensagem());
        }
        return produto;
    }
}
package com.cassi.desafiocassi.mappers.produto;

import com.cassi.desafiocassi.configuration.exceptions.ObjectNotFoundException;
import com.cassi.desafiocassi.dto.produto.ProdutoPrecoFinalResponseDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoRequestDTO;
import com.cassi.desafiocassi.dto.produto.ProdutoResponseDTO;
import com.cassi.desafiocassi.h2.entity.Categoria;
import com.cassi.desafiocassi.h2.entity.Produto;
import com.cassi.desafiocassi.h2.repository.CategoriaRepository;
import com.cassi.desafiocassi.util.MetodosUteis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cassi.desafiocassi.enums.categoria.CategoriaMensagensEnum.CATEGORIA_NAO_ENCONTRADA;

@Component
public class ProdutoMapper {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Produto converterRequestDtoParaEntidade(ProdutoRequestDTO produtoDTO) {

        return Produto.builder()
                .nomeProduto(produtoDTO.getNomeProduto())
                .descricaoProduto(produtoDTO.getDescricaoProduto())
                .precoBase(produtoDTO.getPrecoBase())
                .categoria(buscarCategoria(produtoDTO.getNomeCategoria()))
                .dataCadastro(LocalDate.now())
                .build();
    }

    public ProdutoResponseDTO converterEntidadeParaResponseDto(Produto produto) {

        return ProdutoResponseDTO.builder()
                .idProduto(produto.getIdProduto())
                .nomeProduto(produto.getNomeProduto())
                .descricaoProduto(produto.getDescricaoProduto())
                .precoBase(MetodosUteis.formatarPreco(produto.getPrecoBase()))
                .nomeCategoria(produto.getCategoria().getNomeCategoria())
                .dataCadastro(MetodosUteis.formatarData(produto.getDataCadastro()))
                .build();
    }

    public ProdutoPrecoFinalResponseDTO converterEntidadeParaPrecoFinalResponseDto(Produto produto, BigDecimal valorFinal) {

        return ProdutoPrecoFinalResponseDTO.builder()
                .idProduto(produto.getIdProduto())
                .nomeProduto(produto.getNomeProduto())
                .descricaoProduto(produto.getDescricaoProduto())
                .precoFinal(MetodosUteis.formatarPreco(valorFinal))
                .nomeCategoria(produto.getCategoria().getNomeCategoria())
                .dataCadastro(MetodosUteis.formatarData(produto.getDataCadastro()))
                .build();
    }

    public List<ProdutoResponseDTO> converterListaEntidadeParaResponseDto(List<Produto> listaProdutosEntidade) {
        List<ProdutoResponseDTO> listaRetorno = new ArrayList<>();
        for (Produto produto : listaProdutosEntidade) {
            listaRetorno.add(converterEntidadeParaResponseDto(produto));
        }

        return listaRetorno;
    }

    public Produto atualizarProduto(Produto produto, ProdutoRequestDTO produtoRequestDTO) {
        if (produtoRequestDTO.getDescricaoProduto() != null) {
            produto.setDescricaoProduto(produtoRequestDTO.getDescricaoProduto());
        }
        if (produtoRequestDTO.getNomeProduto() != null) {
            produto.setNomeProduto(produtoRequestDTO.getNomeProduto());
        }
        if (produtoRequestDTO.getPrecoBase() != null) {
            produto.setPrecoBase(produtoRequestDTO.getPrecoBase());
        }
        if (produtoRequestDTO.getNomeCategoria() != null) {
            Categoria categoria = buscarCategoria(produtoRequestDTO.getNomeCategoria());
            produto.setCategoria(categoria);
        }

        return produto;
    }

    private Categoria buscarCategoria(String nomeCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeCategoria(nomeCategoria);
        return categoria.orElseThrow(() -> new ObjectNotFoundException(CATEGORIA_NAO_ENCONTRADA.getMensagem()));
    }
}
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

    /**
     * mapper que converte um ProdutoRequestDTO para uma entidade.
     * @param produtoDTO dto
     * @return entidade produto
     */
    public Produto converterRequestDtoParaEntidade(ProdutoRequestDTO produtoDTO) {

        return Produto.builder()
                .nomeProduto(produtoDTO.getNomeProduto())
                .descricaoProduto(produtoDTO.getDescricaoProduto())
                .precoBase(produtoDTO.getPrecoBase())
                .categoria(buscarCategoria(produtoDTO.getNomeCategoria()))
                .dataCadastro(LocalDate.now())
                .build();
    }

    /**
     * mapper que converte entidade produto para o
     * @param produto entidade produto
     * @return dto
     */
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

    /**
     * mapper que converte entidade produto pra dto e atualiza o valor final
     * @param produto entidade produto
     * @param valorFinal valor final calculado
     * @return ProdutoPrecoFinalResponseDTO
     */
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

    /**
     * verifica se os campos do produtoRequestDTO são nulos, se não forem
     * atualiza o valor na entidade produto
     * @param produto entidade produto a ser atualizada
     * @param produtoRequestDTO dto de request
     * @return entidade produto
     */
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

    /**
     * busca uma categoria no banco de dados por nome da categoria
     * @param nomeCategoria nome da categoria
     * @return categoria
     */
    private Categoria buscarCategoria(String nomeCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeCategoria(nomeCategoria);
        return categoria.orElseThrow(() -> new ObjectNotFoundException(CATEGORIA_NAO_ENCONTRADA.getMensagem()));
    }
}
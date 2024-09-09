package com.cassi.desafiocassi.mappers.categoria;

import com.cassi.desafiocassi.dto.categoria.CategoriaRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaResponseDTO;
import com.cassi.desafiocassi.h2.entity.Categoria;
import com.cassi.desafiocassi.util.MetodosUteis;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriaMapper {

    /**
     * Converte um objeto de transferência de dados (DTO) de categoria para uma entidade de categoria.
     * @param categoriaRequestDTO objeto CategoriaRequestDTO
     * @return retorna uma categoria
     */
    public Categoria converterDtoParaEntidade(CategoriaRequestDTO categoriaRequestDTO) {
        return Categoria.builder()
                .descricaoCategoria(categoriaRequestDTO.getDescricaoCategoria())
                .nomeCategoria(categoriaRequestDTO.getNomeCategoria())
                .taxa(categoriaRequestDTO.getTaxa())
                .desconto(categoriaRequestDTO.getDesconto())
                .build();
    }

    /**
     * Converte uma entidade de categoria para um objeto de transferência de dados (DTO)
     * @param categoria categoria
     * @return CategoriaResponseDTO
     */
    public CategoriaResponseDTO converterEntidadeParaResponseDto(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .descricaoCategoria(categoria.getDescricaoCategoria())
                .nomeCategoria(categoria.getNomeCategoria())
                .taxa(MetodosUteis.AddPorcentagem(categoria.getTaxa()))
                .desconto(MetodosUteis.AddPorcentagem(categoria.getDesconto()))
                .build();
    }

    /**
     * Converte uma lista entidade de categoria para uma lista de transferência de dados (DTO)
     * @param categorias lista de categorias
     * @return lista de CategoriaResponseDTO
     */
    public List<CategoriaResponseDTO> converterListaCategoriasEntidadeParaListaCategoriasDto(List<Categoria> categorias) {
        List<CategoriaResponseDTO> listaCategoriaCadastroDto = new ArrayList<>();
        for (Categoria categoria: categorias) {
            listaCategoriaCadastroDto.add(converterEntidadeParaResponseDto(categoria));
        }

        return listaCategoriaCadastroDto;
    }

    /**
     * verifica se os campos categoriaRequestDTO são nulos, caso não sejam nulos, seta o valor no
     * objeto categoria.
     * @param categoria categoria a ser atualizada
     * @param categoriaRequestDTO categoriaRequest
     * @return Categoria
     */
    public Categoria atualizarCategoriaMapper(Categoria categoria, CategoriaRequestDTO categoriaRequestDTO) {
        if (categoriaRequestDTO.getNomeCategoria() != null) {
            categoria.setNomeCategoria(categoriaRequestDTO.getNomeCategoria());
        }
        if (categoriaRequestDTO.getDescricaoCategoria() != null) {
            categoria.setDescricaoCategoria(categoriaRequestDTO.getDescricaoCategoria());
        }
        if (categoriaRequestDTO.getTaxa().compareTo(BigDecimal.ZERO) != 0) {
            categoria.setTaxa(categoriaRequestDTO.getTaxa());
        }
        if (categoriaRequestDTO.getDesconto().compareTo(BigDecimal.ZERO) != 0) {
            categoria.setDesconto(categoriaRequestDTO.getDesconto());
        }

        return categoria;
    }
}
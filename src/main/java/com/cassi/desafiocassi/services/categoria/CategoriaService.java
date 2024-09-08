package com.cassi.desafiocassi.services.categoria;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.configuration.exceptions.GenericException;
import com.cassi.desafiocassi.configuration.exceptions.ObjectNotFoundException;
import com.cassi.desafiocassi.dto.categoria.CategoriaRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaResponseDTO;
import com.cassi.desafiocassi.h2.entity.Categoria;
import com.cassi.desafiocassi.h2.repository.CategoriaRepository;
import com.cassi.desafiocassi.mappers.categoria.CategoriaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.cassi.desafiocassi.enums.categoria.CategoriaMensagensEnum.*;

@Service
@Slf4j
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    public String cadastrarCategoria(CategoriaRequestDTO categoriaRequestDto) {
        validarDuplicidadeCategoria(categoriaRequestDto.getNomeCategoria());
        validarDescontoOuTaxa(categoriaRequestDto);

        try {
            Categoria categoria = categoriaMapper.converterDtoParaEntidade(categoriaRequestDto);
            categoriaRepository.save(categoria);
            return categoria.getNomeCategoria();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_CADASTRAR_CATEGORIA.getMensagem());
        }
    }

    public List<CategoriaResponseDTO> listarCategorias() {

        List<Categoria> listaCategorias =  categoriaRepository.findAll();

        if (listaCategorias.isEmpty()) {
            throw new ObjectNotFoundException(NENHUMA_CATEGORIA_CADASTRADA.getMensagem());
        }
        return categoriaMapper
                .converterListaCategoriasEntidadeParaListaCategoriasDto(listaCategorias);
    }

    public CategoriaResponseDTO listarCategoriaPorNomeCategoria(String nomeCategoria) {
        CategoriaResponseDTO categoriaResponseDTO = categoriaMapper
                .converterEntidadeParaResponseDto(buscarCategoriaPorNome(nomeCategoria));
        return categoriaResponseDTO;
    }

    public String atualizarCategoriaPorNomeCategoria(String nomeCategoria, CategoriaRequestDTO categoriaRequestDto) {
        Categoria categoria = buscarCategoriaPorNome(nomeCategoria);

        validarDuplicidadeCategoria(categoriaRequestDto.getNomeCategoria());
        validarDescontoOuTaxa(categoriaRequestDto);
        try {
            Categoria categoriaAtualizada = categoriaMapper.atualizarCategoriaMapper(categoria, categoriaRequestDto);
            categoriaRepository.save(categoriaAtualizada);
            return ATUALIZACAO_CATEGORIA.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_ATUALIZACAO_CATEGORIA.getMensagem());
        }
    }

    public String excluirCategoriaPorNomeCategoria(String nomeCategoria) {
        Categoria categoria = buscarCategoriaPorNome(nomeCategoria);
        try {
            categoriaRepository.delete(categoria);
            return EXCLUSAO_CATEGORIA.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_EXCLUIR_CATEGORIA.getMensagem());
        }
    }

    private void validarDuplicidadeCategoria(String nomeCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeCategoria(nomeCategoria);
        if (categoria.isPresent()) {
            throw new GenericException("Uma categoria com nome: " + nomeCategoria + ", já foi cadastrada!");
        }
    }

    private void validarDescontoOuTaxa(CategoriaRequestDTO categoriaRequestDto) {
        if (categoriaRequestDto.getDesconto().compareTo(BigDecimal.ZERO) != 0 &&
                categoriaRequestDto.getTaxa().compareTo(BigDecimal.ZERO) != 0) {
            throw new GenericException("não é possível cadastrar desconto e taxa para a mesma categoria!");
        }
    }



    private Categoria buscarCategoriaPorNome(String nomeCategoria) {
        Categoria categoria =  categoriaRepository.findByNomeCategoria(nomeCategoria).orElse(null);
        if (categoria == null) {
            throw new ObjectNotFoundException(CATEGORIA_NAO_ENCONTRADA.getMensagem());
        }

        return categoria;
    }
}
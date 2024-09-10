package com.cassi.desafiocassi.services.categoria;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.configuration.exceptions.GenericException;
import com.cassi.desafiocassi.configuration.exceptions.ObjectNotFoundException;
import com.cassi.desafiocassi.dto.categoria.CategoriaAtualizacaoRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaCadastroRequestDTO;
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

    /**
     * cadastra uma categoria no banco de dados e trata as exceções
     * @param categoriaCadastroRequestDto dto de request
     * @return mensagem de sucesso
     */
    public String cadastrarCategoria(CategoriaCadastroRequestDTO categoriaCadastroRequestDto) {
        validarDuplicidadeCategoria(categoriaCadastroRequestDto.getNomeCategoria());
        validarDescontoOuTaxa(categoriaCadastroRequestDto.getDesconto(), categoriaCadastroRequestDto.getTaxa());
        Categoria categoria = categoriaMapper.converterDtoParaEntidade(categoriaCadastroRequestDto);

        try {
            categoriaRepository.save(categoria);
            return categoria.getNomeCategoria();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_CADASTRAR_CATEGORIA.getMensagem());
        }
    }

    /**
     * lista todas as categorias do banco, e faz a validação caso não exista
     * categorias cadastradas
     * @return lista de dto response
     */
    public List<CategoriaResponseDTO> listarCategorias() {

        List<Categoria> listaCategorias =  categoriaRepository.findAll();

        if (listaCategorias.isEmpty()) {
            throw new ObjectNotFoundException(NENHUMA_CATEGORIA_CADASTRADA.getMensagem());
        }
        return categoriaMapper
                .converterListaCategoriasEntidadeParaListaCategoriasDto(listaCategorias);
    }

    /**
     * lista categoria do banco de dados por nome da categoria
     * @param nomeCategoria nome da categoria
     * @return dto response
     */
    public CategoriaResponseDTO listarCategoriaPorNomeCategoria(String nomeCategoria) {
        CategoriaResponseDTO categoriaResponseDTO = categoriaMapper
                .converterEntidadeParaResponseDto(buscarCategoriaPorNome(nomeCategoria));
        return categoriaResponseDTO;
    }

    /**
     * atualiza uma categoria no banco de dados e faz a validação caso seja dados repetidos
     * @param nomeCategoria nome da categoria
     * @param categoriaRequestDto dto response
     * @return mensagem de sucesso
     */
    public String atualizarCategoriaPorNomeCategoria(String nomeCategoria, CategoriaAtualizacaoRequestDTO categoriaRequestDto) {
        Categoria categoria = buscarCategoriaPorNome(nomeCategoria);

        validarDuplicidadeCategoria(categoriaRequestDto.getNomeCategoria());
        validarDescontoOuTaxa(categoriaRequestDto.getDesconto(), categoriaRequestDto.getTaxa());
        Categoria categoriaAtualizada = categoriaMapper.atualizarCategoriaMapper(categoria, categoriaRequestDto);
        try {
            categoriaRepository.save(categoriaAtualizada);
            return ATUALIZACAO_CATEGORIA.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_ATUALIZACAO_CATEGORIA.getMensagem());
        }
    }

    /**
     * exclui uma categoria no banco de dados recebendo o nome da categoria como parametro e trata as exceções
     * @param nomeCategoria bone da categoria
     * @return mensagem de sucesso
     */
    public String excluirCategoriaPorNomeCategoria(String nomeCategoria) {
        Categoria categoria = buscarCategoriaPorNome(nomeCategoria);
        try {
            categoriaRepository.delete(categoria);
            return EXCLUSAO_CATEGORIA.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_EXCLUIR_CATEGORIA.getMensagem());
        }
    }

    /**
     * valida se o nome recebido existe cadastrado no banco de dados
     * @param nomeCategoria nome da categoria
     */
    private void validarDuplicidadeCategoria(String nomeCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findByNomeCategoria(nomeCategoria);
        if (categoria.isPresent()) {
            throw new GenericException("Uma categoria com nome: " + nomeCategoria + ", já foi cadastrada!");
        }
    }

    /**
     * valida se os campos de desconto e taxa foram preenchidos, caso forem o sistema lança um erro
     * @param desconto
     * @param taxa
     */
    private void validarDescontoOuTaxa(BigDecimal desconto, BigDecimal taxa) {
        if (desconto == null || taxa == null) {
            return;
        }
        if (desconto.compareTo(BigDecimal.ZERO) != 0 &&
                taxa.compareTo(BigDecimal.ZERO) != 0) {
            throw new GenericException("não é possível cadastrar desconto e taxa para a mesma categoria!");
        }
    }


    /**
     * busca uma categoria no banco de dados por nome da categoria
     * @param nomeCategoria nome da categoria
     * @return categoria
     */
    private Categoria buscarCategoriaPorNome(String nomeCategoria) {
        Categoria categoria =  categoriaRepository.findByNomeCategoria(nomeCategoria).orElse(null);
        if (categoria == null) {
            throw new ObjectNotFoundException(CATEGORIA_NAO_ENCONTRADA.getMensagem());
        }

        return categoria;
    }
}
package com.cassi.desafiocassi.services.produto;

import com.cassi.desafiocassi.configuration.exceptions.DataIntegrityException;
import com.cassi.desafiocassi.configuration.exceptions.ObjectNotFoundException;
import com.cassi.desafiocassi.dto.produto.*;
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

    /**
     * cadastra um produto no banco de dados e trata as exceções
     * @param produtoDTO dto de request
     * @return mensagem de sucesso
     */
    public Long cadastrarProduto(ProdutoCadastroRequestDTO produtoDTO) {
        Produto produto = produtoMapper.converterRequestDtoParaEntidade(produtoDTO);
        try {
            Produto produtoCadastrado = produtoRepository.save(produto);
            return produtoCadastrado.getIdProduto();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_CADASTRAR_PRODUTO.getMensagem());
        }
    }

    /**
     * recebe os datos para o filtro e paginação e manda para o banco de dados, o banco devolve
     * uma Page de produtos que são listados e convertidos para o dto de response
     * @param nomeProduto filtro nome
     * @param descricao filtro descricao
     * @param nomeCategoria filtro nome
     * @param numeroPagina pagina atual
     * @param tamanhoPagina tamanho da pagina
     * @param sortBy precoBase ou dataCadastro
     * @param direcao asc ou desc
     * @return dto paginado response
     */
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

    /**
     * lista um produto do banco de dados pelo id do produto
     * @param idProduto id do produto
     * @return dto de response
     */
    public ProdutoResponseDTO listarProdutoPorId(Long idProduto) {
        ProdutoResponseDTO produtoResponseDTO = produtoMapper
                .converterEntidadeParaResponseDto(buscarProdutoPorId(idProduto));
        return produtoResponseDTO;
    }

    /**
     * atualiza os dados de um produto no banco de dados
     * @param idProduto id do produto
     * @param produtoAtualizacaoRequestDTO dto request
     * @return mensagem de sucesso
     */
    public String atualizarProdutoPorId(Long idProduto, ProdutoAtualizacaoRequestDTO produtoAtualizacaoRequestDTO) {
        Produto produto = buscarProdutoPorId(idProduto);
        Produto produtoAtualizado = produtoMapper.atualizarProduto(produto, produtoAtualizacaoRequestDTO);
        try {
            produtoRepository.save(produtoAtualizado);
            return ATUALIZACAO_PRODUTO.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_ATUALIZACAO_PRODUTO.getMensagem());
        }
    }

    /**
     * exclui um produto do banco de dados pelo id do produto
     * @param idProduto  id do produto
     * @return mensagem de sucesso
     */
    public String excluirProdutoPorId(Long idProduto) {
        Produto produto = buscarProdutoPorId(idProduto);

        try {
            produtoRepository.delete(produto);
            return EXCLUSAO_PRODUTO.getMensagem();
        } catch (Exception ex) {
            throw new DataIntegrityException(ERRO_EXCLUSAO_PRODUTO.getMensagem());
        }
    }

    /**
     * Calcula o preço final de um produto com base em seu ID, considerando descontos ou taxas da categoria.
     *
     * @param idProduto id do produto
     * @return dto de Response
     */
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

    /**
     * lista produto do banco de dados por id do produto
     * @param idProduto nome da categoria
     * @return entidade produto
     */
    private Produto buscarProdutoPorId(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if (produto == null) {
            throw new ObjectNotFoundException(PRODUTO_NAO_ENCONTRADO.getMensagem());
        }
        return produto;
    }
}
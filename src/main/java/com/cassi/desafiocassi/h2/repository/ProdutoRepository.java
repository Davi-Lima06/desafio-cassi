package com.cassi.desafiocassi.h2.repository;

import com.cassi.desafiocassi.h2.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT p FROM Produto p " +
            "WHERE (:nome IS NULL OR LOWER(p.nomeProduto) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:descricao IS NULL OR LOWER(p.descricaoProduto) LIKE LOWER(CONCAT('%', :descricao, '%'))) " +
            "AND (:nomeCategoria IS NULL OR LOWER(p.categoria.nomeCategoria) LIKE LOWER(CONCAT('%', :nomeCategoria, '%')))")
    Page<Produto> listarProdutos(@Param("nome") String nome,
                                 @Param("descricao") String descricao,
                                 @Param("nomeCategoria") String nomeCategoria,
                                 Pageable pageable);
}
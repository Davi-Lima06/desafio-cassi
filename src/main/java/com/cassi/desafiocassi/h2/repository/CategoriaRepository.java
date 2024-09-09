package com.cassi.desafiocassi.h2.repository;

import com.cassi.desafiocassi.h2.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    /**
     * busca uma categoria por nome do banco de dados.
     * @param nomeCategoria nome da categoria
     * @return retorna um Optional de Categoria
     */
    Optional<Categoria> findByNomeCategoria(String nomeCategoria);
}
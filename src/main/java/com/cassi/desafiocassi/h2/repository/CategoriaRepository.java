package com.cassi.desafiocassi.h2.repository;

import com.cassi.desafiocassi.h2.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNomeCategoria(String nomeCategoria);
}
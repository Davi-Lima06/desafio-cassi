package com.cassi.desafiocassi.controllers.categoria;

import com.cassi.desafiocassi.dto.categoria.CategoriaRequestDTO;
import com.cassi.desafiocassi.dto.categoria.CategoriaResponseDTO;
import com.cassi.desafiocassi.services.categoria.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.cassi.desafiocassi.enums.categoria.CategoriaMensagensEnum.CADASTRO_CATEGORIA;

@RestController
@RequestMapping("categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Cadastro de categorias.",
            description = "endpoint responsável pelo cadastro de categorias no banco de dados.")
    public ResponseEntity<String> cadastrarCategoria(
            @Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO) {

        String nomeCategoria = categoriaService.cadastrarCategoria(categoriaRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{nomeCategoria}").buildAndExpand(nomeCategoria).toUri();

        return  ResponseEntity.created(uri).body(CADASTRO_CATEGORIA.getMensagem());
    }

    @GetMapping
    @Operation(summary = "Listagem de categorias.",
            description = "endpoint responsável pela listagem de todas as categorias cadastradas no banco de dados.")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {

        return ResponseEntity.ok().body(categoriaService.listarCategorias());
    }

    @GetMapping("/{nomeCategoria}")
    @Operation(summary = "Listar uma categoria.",
            description = "endpoint responsável por listar uma categoria por nome da categoria.")
    public ResponseEntity<CategoriaResponseDTO> listarCategoriaPorNomeCategoria(
            @PathVariable("nomeCategoria") String nomeCategoria) {
        return ResponseEntity.ok().body(categoriaService.listarCategoriaPorNomeCategoria(nomeCategoria));
    }

    @PutMapping("/{nomeCategoria}")
    @Operation(summary = "Atualizar uma categoria.",
            description = "endpoint responsável por atualizar uma categoria por nome da categoria.")
    public ResponseEntity<String> atualizarCategoriaPorNomeCategoria(
            @Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO,
            @PathVariable("nomeCategoria") String nomeCategoria) {
        return  ResponseEntity.ok().body(categoriaService.atualizarCategoriaPorNomeCategoria(nomeCategoria, categoriaRequestDTO));
    }

    @DeleteMapping("/{nomeCategoria}")
    @Operation(summary = "Excluir uma categoria.",
            description = "endpoint responsável por excluir uma categoria por nome da categoria.")
    public ResponseEntity<String> excluirCategoriaPorNomeCategoria(
            @PathVariable("nomeCategoria") String nomeCategoria) {
        return  ResponseEntity.ok().body(categoriaService.excluirCategoriaPorNomeCategoria(nomeCategoria));
    }
}
package com.cassi.desafiocassi.enums.categoria;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoriaMensagensEnum {

    ATUALIZACAO_CATEGORIA("Categoria atualizada com sucesso!"),
    CADASTRO_CATEGORIA("Categoria cadastrada com sucesso!"),
    EXCLUSAO_CATEGORIA("Categoria excluída com sucesso!"),
    ERRO_ATUALIZACAO_CATEGORIA("Erro ao atualizar categoria, entre em contato com o administrador!"),
    ERRO_CADASTRAR_CATEGORIA("Erro ao inserir categoria, entre em contato com o administrador!"),
    ERRO_EXCLUIR_CATEGORIA("Erro ao excluir categoria, entre em contato com o administrador!"),
    NENHUMA_CATEGORIA_CADASTRADA("nenhuma categoria cadastrada!"),
    CATEGORIA_NAO_ENCONTRADA("nenhuma categoria cadastrada com esse nome!");

    private String mensagem;
}
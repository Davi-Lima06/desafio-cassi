package com.cassi.desafiocassi.enums.produto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProdutoMensagemEnum {

    CADASTRO_PRODUTO("Produto cadastrado com sucesso!"),
    ERRO_CADASTRAR_PRODUTO("Erro ao cadastrar produto, entre em contato com o administrador!"),
    NENHUM_PRODUTO_CADASTRADO("nenhum produto cadastrado!"),
    ERRO_ATUALIZACAO_PRODUTO("Erro ao atualizar o produto, entre em contato com o administrador!"),
    ATUALIZACAO_PRODUTO("Produto atualizado com sucesso!"),
    EXCLUSAO_PRODUTO("Produto excluído com sucesso!"),
    ERRO_EXCLUSAO_PRODUTO("Erro ao excluir  o produto, entre em contato com o administrador!"),
    PRODUTO_NAO_ENCONTRADO("nenhum produto cadastrado com esse id!");

    private String mensagem;
}
package com.cassi.desafiocassi.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MetodosUteis {

    /**
     * por ser classe de utilidade, não é necessário um construtor privado.
     */
    private MetodosUteis() {
    }

    /**
     * adiciona % no final das strings
     * @param taxaDesconto uma string
     * @return string com % no final
     */
    public static String AddPorcentagem(BigDecimal taxaDesconto) {

        return taxaDesconto + "%";
    }

    /**
     * formata o preço para o padrão BR
     * @param preco preço do produto
     * @return string preço formatada
     */
    public static String formatarPreco(BigDecimal preco) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String precoFormatado = currencyFormat.format(preco);
        return precoFormatado;
    }

    /**
     * formata o data para o padrão BR
     * @param localDateTime data
     * @return data formatada
     */
    public static String formatarData(LocalDate localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDateTime.format(formatter);
    }
}
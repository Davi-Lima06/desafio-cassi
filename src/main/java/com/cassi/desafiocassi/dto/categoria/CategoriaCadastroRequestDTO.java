package com.cassi.desafiocassi.dto.categoria;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaCadastroRequestDTO {
    @NotBlank(message = "nome da categoria é obrigatório!")
    private String nomeCategoria;
    @NotBlank(message = "descrição da categoria é obrigatório!")
    private String descricaoCategoria;
    @Min(value = 0, message = "a taxa não pode ser negativa!")
    @Value(value = "0.0")
    private BigDecimal taxa;
    @Min(value = 0, message = "o desconto não pode ser negativo!")
    @Max(value = 100, message = "o desconto não pode ser maior que 100%")
    @Value(value = "0.0")
    private BigDecimal desconto;

}
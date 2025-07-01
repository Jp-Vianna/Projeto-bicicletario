package com.es2.bicicletario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para a requisição de início de um novo aluguel.
 * O cliente informa para qual ciclista e qual tranca o aluguel será iniciado.
 */
@Data
public class AluguelRequestDTO {

    @NotNull(message = "O ID da tranca é obrigatório.")
    private Integer idTranca;

    @NotNull(message = "O ID do ciclista é obrigatório.")
    private Integer idCiclista;

}
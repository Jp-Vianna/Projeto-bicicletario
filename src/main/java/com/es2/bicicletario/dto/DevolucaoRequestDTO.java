package com.es2.bicicletario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para a requisição de devolução de uma bicicleta.
 * O cliente informa a bicicleta que está sendo devolvida e a tranca onde ela foi deixada.
 */
@Data
public class DevolucaoRequestDTO {

    @NotNull(message = "O ID da tranca onde a bicicleta foi devolvida é obrigatório.")
    private Integer idTranca;

    @NotNull(message = "O ID da bicicleta que está sendo devolvida é obrigatório.")
    private Integer idBicicleta;

}

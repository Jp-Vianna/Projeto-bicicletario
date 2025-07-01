package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AluguelResponseDTO {

    private Integer id;
    private Integer idBicicleta;
    private LocalDateTime horaInicio;
    private Integer trancaInicial;
    private Status status;

    private Integer idCiclista;
    private String nomeCiclista;

    /**
     * Método de fábrica estático para converter uma entidade Aluguel em um AluguelDto.
     * @param aluguel A entidade Aluguel a ser convertida.
     * @return Um objeto AluguelDto preenchido.
     */
    public static AluguelResponseDTO fromEntity(Aluguel aluguel) {
        return new AluguelResponseDTO(
                aluguel.getIdAluguel(),
                aluguel.getIdBicicleta(),
                aluguel.getHoraInicio(),
                aluguel.getTrancaInicial(),
                aluguel.getStatus(),
                aluguel.getCiclista().getId(),
                aluguel.getCiclista().getNomeCiclista() 
        );
    }
}

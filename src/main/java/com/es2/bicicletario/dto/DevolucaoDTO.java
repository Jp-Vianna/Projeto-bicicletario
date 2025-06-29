package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.Devolucao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoDTO {

    private Integer id;
    private Integer trancaFinal;
    private LocalDateTime horaFim;
    private BigDecimal cobranca;

    private Integer idAluguel;

    /**
     * Método de fábrica estático para converter uma entidade Devolucao em um DevolucaoDto.
     * @param devolucao A entidade Devolucao a ser convertida.
     * @return Um objeto DevolucaoDto preenchido.
     */
    public static DevolucaoDTO fromEntity(Devolucao devolucao) {
        return new DevolucaoDTO(
                devolucao.getIdDevolucao(),
                devolucao.getTrancaFinal(),
                devolucao.getHoraFim(),
                devolucao.getCobranca(),
                // Extrai apenas o ID do objeto Aluguel para evitar expor a entidade inteira
                devolucao.getAluguel().getIdAluguel() 
        );
    }
}

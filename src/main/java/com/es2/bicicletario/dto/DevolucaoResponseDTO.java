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
public class DevolucaoResponseDTO {

    private Integer id;
    private Integer trancaFinal;
    private Integer idBicicleta;
    private LocalDateTime horaFim;
    private BigDecimal cobranca;
    private String numCartao;
    private LocalDateTime horaCobranca;
    private Integer idAluguel;

    /**
     * Método de fábrica estático para converter uma entidade Devolucao em um DevolucaoDto.
     * @param devolucao A entidade Devolucao a ser convertida.
     * @return Um objeto DevolucaoDto preenchido.
     */
    public static DevolucaoResponseDTO fromEntity(Devolucao devolucao) {
        return new DevolucaoResponseDTO(
                devolucao.getIdDevolucao(),
                devolucao.getTrancaFinal(),
                devolucao.getIdBicicleta(),
                devolucao.getHoraFim(),
                devolucao.getCobrancaExtra(),
                devolucao.getNumCartao(),
                devolucao.getHoraCobranca(),
                // Extrai apenas o ID do objeto Aluguel para evitar expor a entidade inteira
                devolucao.getAluguel().getIdAluguel() 
        );
    }
}

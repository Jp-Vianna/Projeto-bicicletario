package com.es2.bicicletario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; 

import jakarta.persistence.Embeddable;

/**
 * Value Object para representar um Passaporte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Passaporte {

    private String numeroPassaporte;
    private LocalDate dataDeValidade;
    private String pais;

    /**
     * @return true se as informações básicas do passaporte e o formato do número forem válidos, false caso contrário.
     */
    public static boolean validarPassaporte(Passaporte passaporte) {
        if (passaporte.numeroPassaporte == null || passaporte.numeroPassaporte.trim().isEmpty()) {
            return false;
        }
        if (passaporte.pais == null || passaporte.pais.trim().isEmpty()) {
            return false;
        }
        if (passaporte.dataDeValidade == null) {
            return false;
        }

        return passaporte.dataDeValidade.isAfter(LocalDate.now());
    }
}
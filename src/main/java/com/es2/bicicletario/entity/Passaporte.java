package com.es2.bicicletario.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.regex.Pattern; 

import jakarta.persistence.Embeddable;

/**
 * Value Object para representar um Passaporte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Passaporte {
    private static final Pattern REGEX_EUA = Pattern.compile("^\\d{9}$");
    private static final Pattern REGEX_REINO_UNIDO = Pattern.compile("^\\d{9}$");
    private static final Pattern REGEX_CANADA = Pattern.compile("^[A-Z]{2}\\d{6}$");
    private static final Pattern REGEX_JAPAO = Pattern.compile("^[A-Z]{2}\\d{7}$");
    private static final Pattern REGEX_ALEMANHA = Pattern.compile("^[CFGHJKLMNPRSTVWXYZ0-9]{9}$");


    private String numeroPassaporte;
    private LocalDate dataDeValidade;
    private String pais;

    /**
     * Verifica se o número do passaporte tem um formato válido para o país especificado.
     * Este método é privado e serve como um auxiliar para o método principal de validação.
     * @return true se o formato for válido ou se o país não estiver na lista de validação, false caso contrário.
     */
    private boolean validarFormatoPorPais() {

        String paisNormalizado = this.pais.trim().toUpperCase();

        switch (paisNormalizado) {
            case "ESTADOS UNIDOS":
            case "EUA":
                return REGEX_EUA.matcher(this.numeroPassaporte).matches();

            case "REINO UNIDO":
            case "GRÃ-BRETANHA":
                return REGEX_REINO_UNIDO.matcher(this.numeroPassaporte).matches();

            case "CANADÁ":
                return REGEX_CANADA.matcher(this.numeroPassaporte).matches();

            case "JAPÃO":
                return REGEX_JAPAO.matcher(this.numeroPassaporte).matches();

            case "ALEMANHA":
                return REGEX_ALEMANHA.matcher(this.numeroPassaporte).matches();

            default:
                return false;
        }
    }

    /**
     * @return true se as informações básicas do passaporte e o formato do número forem válidos, false caso contrário.
     */
    public boolean validarPassaporte() {
        if (this.numeroPassaporte == null || this.numeroPassaporte.trim().isEmpty()) {
            return false;
        }
        if (this.pais == null || this.pais.trim().isEmpty()) {
            return false;
        }
        if (this.dataDeValidade == null) {
            return false;
        }

        if (!this.validarFormatoPorPais()) {
            return false;
        }

        return this.dataDeValidade.isAfter(LocalDate.now());
    }
}
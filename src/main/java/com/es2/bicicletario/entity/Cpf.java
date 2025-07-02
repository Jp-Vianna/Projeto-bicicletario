package com.es2.bicicletario.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.InputMismatchException;

import jakarta.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Cpf {

    private String numero;

    /**
     * @return true se o CPF for válido, false caso contrário.
     */
    public static boolean validarCpf(String numero) {
        if (numero == null) {
            return false;
        }

        String cpfLimpo = numero.replaceAll("[^\\d]", "");

        if (cpfLimpo.length() != 11) {
            return false;
        }

        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            return false;
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (cpfLimpo.charAt(i) - 48); // Converte o char para número
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (cpfLimpo.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            return (dig10 == cpfLimpo.charAt(9)) && (dig11 == cpfLimpo.charAt(10));
        } catch (InputMismatchException erro) {
            return false;
        }
    }
}

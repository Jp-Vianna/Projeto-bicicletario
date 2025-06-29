package com.es2.bicicletario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
@EqualsAndHashCode 
public class Email implements Serializable {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    @Column(name = "email") 
    private String email;

    public Email(String endereco) {
        if (!isValido(endereco)) {
            throw new IllegalArgumentException("Formato de e-mail inválido: " + endereco);
        }
        this.email = endereco;
    }

    public static boolean isValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public String toString() {
        return this.email;
    }
}

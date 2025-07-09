package com.es2.bicicletario.ServiceTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.service.EmailService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class EmailServiceTest {

    @Test
    void enviarEmail_DeveSempreRetornarTrue() {
        // Ação
        boolean resultado = EmailService.enviarEmail();

        assertThat(resultado).isTrue();
    }

    @Test
    void deveLancarExcecao_AoTentarInstanciar() {

        assertThatThrownBy(() -> {
            try {
                Constructor<EmailService> constructor = EmailService.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }).isInstanceOf(IllegalStateException.class)
          .hasMessage("Classe de utilidade não deve ser instanciada");
    }
}

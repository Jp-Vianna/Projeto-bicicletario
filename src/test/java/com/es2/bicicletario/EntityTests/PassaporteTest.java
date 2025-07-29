package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.Passaporte;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PassaporteTest {

    @Test
    void validarPassaporte_DeveRetornarTrue_ParaPassaporteValido() {

        Passaporte passaporte = new Passaporte(
                "AB123456",
                LocalDate.now().plusYears(1), // Válido por mais um ano
                "Brasil"
        );

        assertTrue(Passaporte.validarPassaporte(passaporte));
    }

    @Test
    void validarPassaporte_DeveRetornarFalse_ParaPassaporteExpirado() {

        Passaporte passaporte = new Passaporte(
                "CD789012",
                LocalDate.now().minusDays(1), // Expirou ontem
                "Argentina"
        );

        assertFalse(Passaporte.validarPassaporte(passaporte));
    }

    @Test
    void validarPassaporte_DeveRetornarFalse_QuandoNumeroForNuloOuVazio() {

        Passaporte passaporteComNumeroNulo = new Passaporte(null, LocalDate.now().plusYears(2), "Chile");
        Passaporte passaporteComNumeroVazio = new Passaporte("  ", LocalDate.now().plusYears(2), "Chile");

        assertFalse(Passaporte.validarPassaporte(passaporteComNumeroNulo), "A validação deve falhar para número nulo.");
        assertFalse(Passaporte.validarPassaporte(passaporteComNumeroVazio), "A validação deve falhar para número vazio.");
    }

    @Test
    void validarPassaporte_DeveRetornarFalse_QuandoPaisForNuloOuVazio() {

        Passaporte passaporteComPaisNulo = new Passaporte("EF345678", LocalDate.now().plusMonths(6), null);
        Passaporte passaporteComPaisVazio = new Passaporte("EF345678", LocalDate.now().plusMonths(6), "");

        assertFalse(Passaporte.validarPassaporte(passaporteComPaisNulo), "A validação deve falhar para país nulo.");
        assertFalse(Passaporte.validarPassaporte(passaporteComPaisVazio), "A validação deve falhar para país vazio.");
    }

    @Test
    void validarPassaporte_DeveRetornarFalse_QuandoDataDeValidadeForNula() {

        Passaporte passaporte = new Passaporte("GH901234", null, "Uruguai");

        assertFalse(Passaporte.validarPassaporte(passaporte));
    }

    @Test
    void construtorEGetters() {

        String numero = "IJ567890";
        LocalDate validade = LocalDate.of(2030, 1, 15);
        String pais = "Paraguai";

        Passaporte passaporte = new Passaporte(numero, validade, pais);

        assertEquals(numero, passaporte.getNumeroPassaporte());
        assertEquals(validade, passaporte.getDataDeValidade());
        assertEquals(pais, passaporte.getPais());
    }
}

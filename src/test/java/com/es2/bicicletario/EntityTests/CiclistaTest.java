package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CiclistaTest {

    @Test
    void deveInstanciarCiclistaCorretamente() {

        Cpf cpf = new Cpf("12345678909");
        Email email = new Email("ciclista@teste.com");
        YearMonth validadeAnoMes = YearMonth.now().plusYears(1);

        LocalDate ultimoDiaDoMes = validadeAnoMes.atEndOfMonth();

        Date dataValidade = Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant());

        CartaoDeCredito cartao = new CartaoDeCredito(
            "1111222233334444", 
            "Ciclista Teste", 
            dataValidade, 
            "123"
        );

        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNomeCiclista("Ciclista de Teste");
        ciclista.setDataNascimento(LocalDate.of(1995, 5, 10));
        ciclista.setStatus(Status.ATIVO);
        ciclista.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclista.setCpf(cpf);
        ciclista.setEmail(email);
        ciclista.setSenha("senha123");
        ciclista.setCartao(cartao);
        ciclista.setFotoDocumento("/caminho/foto.jpg");
        ciclista.setPassaporte(null); // Para um ciclista brasileiro

        assertNotNull(ciclista);
        assertEquals(1, ciclista.getId());
        assertEquals("Ciclista de Teste", ciclista.getNomeCiclista());
        assertEquals(LocalDate.of(1995, 5, 10), ciclista.getDataNascimento());
        assertEquals(Status.ATIVO, ciclista.getStatus());
        assertEquals(Nacionalidade.BRASILEIRO, ciclista.getNacionalidade());
        assertEquals("12345678909", ciclista.getCpf().getNumero());
        assertEquals("ciclista@teste.com", ciclista.getEmail().getEndereco());
        assertEquals("senha123", ciclista.getSenha());
        assertEquals("1111222233334444", ciclista.getCartao().getNumeroCartao());
        assertNull(ciclista.getPassaporte());
    }

    @Test
    void deveUsarConstrutorComTodosOsArgumentos() {

        Email email = new Email("outro@email.com");
        YearMonth validadeAnoMes = YearMonth.now().plusYears(1);

        LocalDate ultimoDiaDoMes = validadeAnoMes.atEndOfMonth();

        Date dataValidade = Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant());

        CartaoDeCredito cartao = new CartaoDeCredito("5555666677778888", "Outro Titular", dataValidade, "456");
        Passaporte passaporte = new Passaporte("XYZ987", LocalDate.now().plusYears(5), "ESTADOS UNIDOS");

        Ciclista ciclista = new Ciclista(
            2,
            Status.AGUARDANDO_ATIVAMENTO,
            LocalDate.of(2000, 2, 20),
            "Ciclista Estrangeiro",
            null, 
            "outrasenha",
            Nacionalidade.ESTRANGEIRO,
            email,
            "/docs/foto.png",
            cartao,
            passaporte
        );

        assertEquals(2, ciclista.getId());
        assertEquals("Ciclista Estrangeiro", ciclista.getNomeCiclista());
        assertEquals(Nacionalidade.ESTRANGEIRO, ciclista.getNacionalidade());
        assertEquals("XYZ987", ciclista.getPassaporte().getNumeroPassaporte());
        assertNull(ciclista.getCpf());
    }
}

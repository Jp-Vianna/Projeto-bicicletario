package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Devolucao;
import com.es2.bicicletario.entity.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DevolucaoTest {

    private Aluguel aluguel;

    @BeforeEach
    void setUp() {
        // Para testar a Devolucao, primeiro criamos um Aluguel de exemplo,
        // pois toda devolução deve estar associada a um.
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNomeCiclista("Carlos Pereira");

        aluguel = new Aluguel();
        aluguel.setIdAluguel(100);
        aluguel.setCiclista(ciclista);
        aluguel.setIdBicicleta(42);
        aluguel.setStatus(Status.FINALIZADO);
    }

    @Test
    void deveInstanciarDevolucaoCorretamenteComSetters() {
        // Arrange
        LocalDateTime horaFim = LocalDateTime.now();
        BigDecimal cobranca = new BigDecimal("15.50");

        // Act
        Devolucao devolucao = new Devolucao();
        devolucao.setIdDevolucao(20);
        devolucao.setAluguel(aluguel);
        devolucao.setTrancaFinal(305);
        devolucao.setHoraFim(horaFim);
        devolucao.setCobranca(cobranca);

        // Assert
        assertNotNull(devolucao);
        assertEquals(20, devolucao.getIdDevolucao());
        assertEquals(305, devolucao.getTrancaFinal());
        assertEquals(horaFim, devolucao.getHoraFim());
        assertEquals(0, cobranca.compareTo(devolucao.getCobranca())); // Comparar BigDecimals
        assertNotNull(devolucao.getAluguel());
        assertEquals(100, devolucao.getAluguel().getIdAluguel());
    }

    @Test
    void deveInstanciarDevolucaoCorretamenteComConstrutorCompleto() {
        // Arrange
        LocalDateTime horaFim = LocalDateTime.now().minusMinutes(5);
        BigDecimal cobranca = new BigDecimal("25.00");

        // Act
        Devolucao devolucao = new Devolucao(
                21,
                306,
                horaFim,
                cobranca,
                aluguel
        );

        // Assert
        assertNotNull(devolucao);
        assertEquals(21, devolucao.getIdDevolucao());
        assertEquals(306, devolucao.getTrancaFinal());
        assertEquals(horaFim, devolucao.getHoraFim());
        assertEquals(0, cobranca.compareTo(devolucao.getCobranca()));
        assertEquals(aluguel, devolucao.getAluguel());
    }
}

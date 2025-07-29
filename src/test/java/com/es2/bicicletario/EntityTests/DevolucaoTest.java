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

        LocalDateTime horaFim = LocalDateTime.now();
        BigDecimal cobranca = new BigDecimal("15.50");

        Devolucao devolucao = new Devolucao();
        devolucao.setIdDevolucao(20);
        devolucao.setAluguel(aluguel);
        devolucao.setTrancaFinal(305);
        devolucao.setHoraFim(horaFim);
        devolucao.setCobrancaExtra(cobranca);

        assertNotNull(devolucao);
        assertEquals(20, devolucao.getIdDevolucao());
        assertEquals(305, devolucao.getTrancaFinal());
        assertEquals(horaFim, devolucao.getHoraFim());
        assertEquals(0, cobranca.compareTo(devolucao.getCobrancaExtra())); // Comparar BigDecimals
        assertNotNull(devolucao.getAluguel());
        assertEquals(100, devolucao.getAluguel().getIdAluguel());
    }
}

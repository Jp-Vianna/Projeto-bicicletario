package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Status;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AluguelTest {

    private Ciclista ciclista;

    @BeforeEach
    void setUp() {
        ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setNomeCiclista("Joana Santos");
        ciclista.setStatus(Status.ATIVO);
    }

    @Test
    void deveInstanciarAluguelCorretamenteComSetters() {

        LocalDateTime horaInicio = LocalDateTime.now();

        Aluguel aluguel = new Aluguel();
        aluguel.setIdAluguel(100);
        aluguel.setCiclista(ciclista);
        aluguel.setIdBicicleta(55);
        aluguel.setTrancaInicial(201);
        aluguel.setHoraInicio(horaInicio);
        aluguel.setStatus(Status.EM_ANDAMENTO);

        assertNotNull(aluguel);
        assertEquals(100, aluguel.getIdAluguel());
        assertEquals(55, aluguel.getIdBicicleta());
        assertEquals(201, aluguel.getTrancaInicial());
        assertEquals(horaInicio, aluguel.getHoraInicio());
        assertEquals(Status.EM_ANDAMENTO, aluguel.getStatus());
        assertNotNull(aluguel.getCiclista());
        assertEquals(1, aluguel.getCiclista().getId());
    }

    @Test
    void deveInstanciarAluguelCorretamenteComConstrutorCompleto() {

        LocalDateTime horaInicio = LocalDateTime.now().minusHours(1);

        Aluguel aluguel = new Aluguel(
                101,
                56,
                horaInicio,
                ciclista,
                202,
                Status.FINALIZADO
        );

        assertNotNull(aluguel);
        assertEquals(101, aluguel.getIdAluguel());
        assertEquals(56, aluguel.getIdBicicleta());
        assertEquals(horaInicio, aluguel.getHoraInicio());
        assertEquals(ciclista, aluguel.getCiclista());
        assertEquals(202, aluguel.getTrancaInicial());
        assertEquals(Status.FINALIZADO, aluguel.getStatus());
    }
}

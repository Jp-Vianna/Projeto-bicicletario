package com.es2.bicicletario.RepositoryTests;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Status;
import com.es2.bicicletario.repository.AluguelRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AluguelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AluguelRepository aluguelRepository;

    private Ciclista ciclista;

    @BeforeEach
    void setUp() {
        ciclista = new Ciclista();
        ciclista.setNomeCiclista("Ciclista Teste");
        ciclista.setStatus(Status.ATIVO);
        ciclista.setDataNascimento(java.time.LocalDate.of(1990, 1, 1));
        ciclista.setSenha("senha");
        ciclista.setNacionalidade(com.es2.bicicletario.entity.Nacionalidade.BRASILEIRO);
        ciclista.setEmail(new com.es2.bicicletario.entity.Email("teste@teste.com"));
        ciclista.setFotoDocumento("path/foto");
        ciclista.setCartao(new com.es2.bicicletario.entity.CartaoDeCredito());
        entityManager.persist(ciclista);

        Aluguel aluguelEmAndamento = new Aluguel(null, 101, LocalDateTime.now(), ciclista, 201, Status.EM_ANDAMENTO);
        entityManager.persist(aluguelEmAndamento);

        Aluguel aluguelFinalizado = new Aluguel(null, 102, LocalDateTime.now().minusHours(2), ciclista, 202, Status.FINALIZADO);
        entityManager.persist(aluguelFinalizado);
    }

    @Test
    void findByCiclistaId_deveRetornarAlugueisDoCiclista() {
        List<Aluguel> aluguel = aluguelRepository.findAllByCiclistaId(ciclista.getId());
        assertThat(aluguel).isNotEmpty();
    }

    @Test
    void findByCiclistaIdAndStatus_deveRetornarAluguelCorreto() {
        Optional<Aluguel> aluguel = aluguelRepository.findByCiclistaIdAndStatus(ciclista.getId(), Status.EM_ANDAMENTO);
        assertThat(aluguel).isPresent();
        assertThat(aluguel.get().getStatus()).isEqualTo(Status.EM_ANDAMENTO);
    }

    @Test
    void findAllByCiclistaIdAndStatusIn_deveRetornarAlugueisCorretos() {
        List<Status> statuses = List.of(Status.EM_ANDAMENTO, Status.FINALIZADO);
        List<Aluguel> alugueis = aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclista.getId(), statuses);
        assertThat(alugueis).hasSize(2);
    }

    @Test
    void findByIdBicicletaAndStatus_deveRetornarAluguelCorreto() {
        Optional<Aluguel> aluguel = aluguelRepository.findByIdBicicletaAndStatus(101, Status.EM_ANDAMENTO);
        assertThat(aluguel).isPresent();
        assertThat(aluguel.get().getIdBicicleta()).isEqualTo(101);
    }
}

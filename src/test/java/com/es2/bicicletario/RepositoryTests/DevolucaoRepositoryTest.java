package com.es2.bicicletario.RepositoryTests;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Status;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
public class DevolucaoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;


    private Aluguel aluguel;

    @BeforeEach
    void setUp() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNomeCiclista("Ciclista para Devolução");
        ciclista.setStatus(Status.ATIVO);
        ciclista.setDataNascimento(java.time.LocalDate.of(1995, 3, 12));
        ciclista.setSenha("senhaDev");
        ciclista.setNacionalidade(com.es2.bicicletario.entity.Nacionalidade.BRASILEIRO);
        ciclista.setEmail(new com.es2.bicicletario.entity.Email("dev@teste.com"));
        ciclista.setFotoDocumento("path/foto/dev");
        ciclista.setCartao(new com.es2.bicicletario.entity.CartaoDeCredito());
        entityManager.persist(ciclista);

        aluguel = new Aluguel(null, 105, LocalDateTime.now().minusHours(1), ciclista, 205, Status.FINALIZADO);
        entityManager.persist(aluguel);
    }

    // @Test
    // void save_devePersistirDevolucao() {
    //     Devolucao devolucao = new Devolucao();
    //     devolucao.setAluguel(aluguel);
    //     devolucao.setTrancaFinal(301);
    //     devolucao.setHoraFim(LocalDateTime.now());
    //     devolucao.setCobrancaExtra(new BigDecimal("12.50"));

    //     Devolucao savedDevolucao = devolucaoRepository.save(devolucao);

    //     assertThat(savedDevolucao).isNotNull();
    //     assertThat(savedDevolucao.getIdDevolucao()).isNotNull();

    //     Devolucao foundDevolucao = entityManager.find(Devolucao.class, savedDevolucao.getIdDevolucao());
    //     assertThat(foundDevolucao).isNotNull();
    //     assertThat(foundDevolucao.getTrancaFinal()).isEqualTo(301);
    //     assertThat(foundDevolucao.getAluguel().getIdAluguel()).isEqualTo(aluguel.getIdAluguel());
    // }
}

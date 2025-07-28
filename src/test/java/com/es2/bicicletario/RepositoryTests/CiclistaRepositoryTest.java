package com.es2.bicicletario.RepositoryTests;

import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Cpf;
import com.es2.bicicletario.entity.Email;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.entity.Passaporte;
import com.es2.bicicletario.entity.Status;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

@DataJpaTest
public class CiclistaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    private Ciclista ciclistaBrasileiro;
    private Ciclista ciclistaEstrangeiro;

    final String nomeTesteBrasil = "Victor Pereira";
    final String nomeTesteEstrangeiro = "Rob Bourbon";
    final String emailTesteBrasil = "qwe@gmail.com";

    @BeforeEach
    void setUp() {
        ciclistaBrasileiro = new Ciclista();
        ciclistaBrasileiro.setNomeCiclista(nomeTesteBrasil);
        ciclistaBrasileiro.setStatus(Status.ATIVO);
        ciclistaBrasileiro.setDataNascimento(LocalDate.of(1990, 5, 15));
        ciclistaBrasileiro.setCpf(new Cpf("11122233344"));
        ciclistaBrasileiro.setSenha("senha123");
        ciclistaBrasileiro.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaBrasileiro.setEmail(new Email(emailTesteBrasil));
        ciclistaBrasileiro.setFotoDocumento("/path/foto1");
        ciclistaBrasileiro.setCartao(new com.es2.bicicletario.entity.CartaoDeCredito());

        entityManager.persist(ciclistaBrasileiro);

        ciclistaEstrangeiro = new Ciclista();
        ciclistaEstrangeiro.setNomeCiclista(nomeTesteEstrangeiro);
        ciclistaEstrangeiro.setStatus(Status.ATIVO);
        ciclistaEstrangeiro.setDataNascimento(LocalDate.of(1985, 10, 20));
        ciclistaEstrangeiro.setSenha("senha456");
        ciclistaEstrangeiro.setNacionalidade(Nacionalidade.ESTRANGEIRO);
        ciclistaEstrangeiro.setEmail(new Email("estrangeiro@teste.com"));
        ciclistaEstrangeiro.setFotoDocumento("/path/foto2");
        ciclistaEstrangeiro.setPassaporte(new Passaporte("ABC12345", LocalDate.now().plusYears(1), "PAIS TESTE"));
        ciclistaEstrangeiro.setCartao(new com.es2.bicicletario.entity.CartaoDeCredito());

        entityManager.persist(ciclistaEstrangeiro);
    }

    // @Test
    // void findByCpfNumero_deveEncontrarCiclistaPeloCpf() {
    //     Optional<Ciclista> encontrado = ciclistaRepository.findByCpfNumero("11122233344");
    //     assertThat(encontrado).isPresent();
    //     assertThat(encontrado.get().getNomeCiclista()).isEqualTo(nomeTesteBrasil);
    // }

    // @Test
    // void findByEmailEndereco_deveEncontrarCiclistaPeloEmail() {
    //     Optional<Ciclista> encontrado = ciclistaRepository.findByEmailEndereco(emailTesteBrasil);
    //     assertThat(encontrado).isPresent();
    //     assertThat(encontrado.get().getNomeCiclista()).isEqualTo(nomeTesteBrasil);
    // }

    // @Test
    // void findByPassaporteNumeroPassaporte_deveEncontrarCiclistaPeloPassaporte() {
    //     Optional<Ciclista> encontrado = ciclistaRepository.findByPassaporteNumeroPassaporte("ABC12345");
    //     assertThat(encontrado).isPresent();
    //     assertThat(encontrado.get().getNomeCiclista()).isEqualTo(nomeTesteEstrangeiro);
    // }
}

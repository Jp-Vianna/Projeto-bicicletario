package com.es2.bicicletario.RepositoryTests;

import com.es2.bicicletario.entity.Cpf;
import com.es2.bicicletario.entity.Email;
import com.es2.bicicletario.entity.Funcionario;
import com.es2.bicicletario.repository.FuncionarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FuncionarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    final String matriculaTeste = "F001";
    final String nomeTeste = "Mario";

    @Test
    void save_devePersistirFuncionario() {
        Funcionario funcionario = new Funcionario(
                matriculaTeste,
                "senha",
                null,
                new Email("func@teste.com"),
                nomeTeste,
                30,
                "Tester",
                new Cpf("12345678901")
        );

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        assertThat(savedFuncionario).isNotNull();
        assertThat(savedFuncionario.getMatricula()).isEqualTo(matriculaTeste);

        Funcionario foundFuncionario = entityManager.find(Funcionario.class, matriculaTeste);
        assertThat(foundFuncionario.getNome()).isEqualTo(nomeTeste);
    }
}

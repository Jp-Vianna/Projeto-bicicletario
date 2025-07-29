package com.es2.bicicletario.RepositoryTests;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FuncionarioRepositoryTest {

    final String matriculaTeste = "F001";
    final String nomeTeste = "Mario";

    // @Test
    // void save_devePersistirFuncionario() {
    //     Funcionario funcionario = new Funcionario(
    //             matriculaTeste,
    //             "senha",
    //             null,
    //             new Email("func@teste.com"),
    //             nomeTeste,
    //             30,
    //             "Tester",
    //             new Cpf("12345678901")
    //     );

    //     Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

    //     assertThat(savedFuncionario).isNotNull();
    //     assertThat(savedFuncionario.getMatricula()).isEqualTo(matriculaTeste);

    //     Funcionario foundFuncionario = entityManager.find(Funcionario.class, matriculaTeste);
    //     assertThat(foundFuncionario.getNome()).isEqualTo(nomeTeste);
    // }
}

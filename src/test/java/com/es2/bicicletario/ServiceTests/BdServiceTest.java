package com.es2.bicicletario.ServiceTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.es2.bicicletario.service.BdService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class BdServiceIntegrationTest {

    @Autowired
    private BdService bdService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void quandoResetDatabase_eExistemDados_entaoTabelasDevemFicarVazias() {
        
        String sqlInsert = "INSERT INTO funcionarios (matricula, nome, email, idade, funcao, senha, numero) " +
                        "VALUES ('mat-01', 'Jairo', 'teste@email.com', 30, 'Administrativo', 'senha-teste', '11122233396')";
        
        jdbcTemplate.execute(sqlInsert);

        int countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM funcionarios", Integer.class);
        assertEquals(1, countBefore, "Deveria existir 1 registro na tabela antes do reset.");

        bdService.resetDatabase();

        int countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM funcionarios", Integer.class);
        assertEquals(0, countAfter, "A tabela deveria estar vazia após o reset.");
    }
}

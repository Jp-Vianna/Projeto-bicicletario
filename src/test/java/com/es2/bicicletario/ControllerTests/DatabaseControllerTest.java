package com.es2.bicicletario.ControllerTests;

import com.es2.bicicletario.controller.DatabaseController;
import com.es2.bicicletario.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DatabaseController.class)
class DatabaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseService databaseService;

    private static final String INSERT = "INSERT INTO...";
    private static final String FILE = "file";
    private static final String BACKUP_FILE = "backup.sql";
    private static final String CONTEXT = "text/plain";
    private static final String CAMINHO_END = "/api/db/restore";

    @Test
    void restoreDatabase_ComArquivoValido_DeveRetornarStatus200Ok() throws Exception {
        MockMultipartFile sqlFile = new MockMultipartFile(
            FILE,
            BACKUP_FILE,
            CONTEXT,
            INSERT.getBytes()
        );

        doNothing().when(databaseService).restoreDatabase(sqlFile);

        mockMvc.perform(multipart(CAMINHO_END).file(sqlFile))
            .andExpect(status().isOk())
            .andExpect(content().string("Banco de dados restaurado com sucesso!"));
    }

    @Test
    void restoreDatabase_ComArquivoVazio_DeveRetornarStatus400BadRequest() throws Exception {

        MockMultipartFile emptyFile = new MockMultipartFile(
            FILE,
            "empty.sql",
            CONTEXT,
            new byte[0] // Arquivo vazio
        );

        mockMvc.perform(multipart(CAMINHO_END).file(emptyFile))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Por favor, envie um arquivo .sql válido."));
    }

    @Test
    void restoreDatabase_QuandoServiceLancaExcecao_DeveRetornarStatus500InternalServerError() throws Exception {

        MockMultipartFile sqlFile = new MockMultipartFile(
            FILE,
            BACKUP_FILE,
            CONTEXT,
           INSERT.getBytes()
        );

        String mensagemErro = "Falha ao executar script SQL.";

        doThrow(new java.sql.SQLException(mensagemErro))
            .when(databaseService).restoreDatabase(sqlFile);

        mockMvc.perform(multipart(CAMINHO_END).file(sqlFile))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Falha ao restaurar o banco de dados: " + mensagemErro));
    }
}

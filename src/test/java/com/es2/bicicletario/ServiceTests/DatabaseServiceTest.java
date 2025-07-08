package com.es2.bicicletario.ServiceTests;

import com.es2.bicicletario.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseServiceTest {

    @InjectMocks
    private DatabaseService databaseService;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Test
    void restoreDatabase_ComArquivoValido_DeveExecutarComandosSql() throws SQLException, IOException {

        MultipartFile sqlFile = new MockMultipartFile("file", "test.sql", "text/plain", "INSERT...".getBytes());

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        databaseService.restoreDatabase(sqlFile);

        verify(statement, times(1)).execute("DROP ALL OBJECTS DELETE FILES");
        verify(statement, times(1)).execute(startsWith("RUNSCRIPT FROM"));
        verify(connection, times(1)).close(); 
    }

    @Test
    void restoreDatabase_QuandoDataSourceLancaSQLException_DevePropagarExcecao() throws SQLException {

        MultipartFile sqlFile = new MockMultipartFile("file", "test.sql", "text/plain", "INSERT...".getBytes());
        
        when(dataSource.getConnection()).thenThrow(new SQLException("Erro de conexão simulado."));

        assertThatThrownBy(() -> databaseService.restoreDatabase(sqlFile))
            .isInstanceOf(SQLException.class)
            .hasMessage("Erro de conexão simulado.");
    }

    @Test
    void restoreDatabase_QuandoStatementLancaSQLException_DevePropagarExcecao() throws SQLException {

        MultipartFile sqlFile = new MockMultipartFile("file", "test.sql", "text/plain", "INSERT...".getBytes());

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.execute(anyString())).thenThrow(new SQLException("Erro de execução SQL simulado."));

        assertThatThrownBy(() -> databaseService.restoreDatabase(sqlFile))
            .isInstanceOf(SQLException.class)
            .hasMessage("Erro de execução SQL simulado.");
    }
}

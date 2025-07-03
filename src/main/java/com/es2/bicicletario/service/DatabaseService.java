package com.es2.bicicletario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class DatabaseService {

    private final DataSource dataSource;

    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Restaura o banco de dados a partir de um arquivo SQL.
     * Antes de restaurar, apaga todos os objetos existentes para evitar conflitos.
     * @param sqlFile O arquivo .sql enviado via upload.
     */
    public void restoreDatabase(MultipartFile sqlFile) throws SQLException, IOException {

        File tempFile = File.createTempFile("restore-", ".sql");
        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write(sqlFile.getBytes());
        }

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
    
            stmt.execute("DROP ALL OBJECTS DELETE FILES");

            String sql = String.format("RUNSCRIPT FROM '%s'", tempFile.getAbsolutePath());
            stmt.execute(sql);
            
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}

package com.es2.bicicletario.controller;

import com.es2.bicicletario.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Endpoint para restaurar o banco de dados a partir de um arquivo .sql.
     * Exemplo de uso: POST http://localhost:8080/api/db/restore com um form-data contendo o arquivo.
     */
    @PostMapping("/restore")
    public ResponseEntity<String> restoreDatabase(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, envie um arquivo .sql válido.");
        }

        try {
            databaseService.restoreDatabase(file);
            return ResponseEntity.ok("Banco de dados restaurado com sucesso!");
        } catch (IOException | java.sql.SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao restaurar o banco de dados: " + e.getMessage());
        }
    }
}

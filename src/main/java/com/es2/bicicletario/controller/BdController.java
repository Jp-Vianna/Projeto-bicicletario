package com.es2.bicicletario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es2.bicicletario.service.BdService;

@RestController
@RequestMapping("/api/db")
public class BdController {

    private BdService databaseResetService;

    @Autowired
    public BdController(BdService databaseResetService) {
        this.databaseResetService = databaseResetService;
    }

    @PostMapping("/restaurarBanco") 
    public ResponseEntity<String> resetDatabase() {
        try {
            databaseResetService.resetDatabase();
            return ResponseEntity.ok("Banco de dados (H2) resetado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Falha ao resetar o banco de dados: " + e.getMessage());
        }
    }
}

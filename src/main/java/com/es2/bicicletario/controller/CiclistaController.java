package com.es2.bicicletario.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.service.CiclistaService;

import jakarta.validation.Valid;

@RestController
public class CiclistaController {
    
    @Autowired
    CiclistaService ciclistaService;

    @PostMapping
    public ResponseEntity<CiclistaResponseDTO> criarCiclista(@Valid @RequestBody CiclistaRequestDTO dto) {

        Ciclista novoCiclista = ciclistaService.criar(dto);

        CiclistaResponseDTO respostaDto = CiclistaResponseDTO.fromEntity(novoCiclista);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() 
                .path("/{id}") 
                .buildAndExpand(novoCiclista.getId()) 
                .toUri();

        return ResponseEntity.created(location).body(respostaDto);
    }
}

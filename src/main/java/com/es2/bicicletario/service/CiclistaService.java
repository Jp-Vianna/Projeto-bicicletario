package com.es2.bicicletario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Cpf;
import com.es2.bicicletario.entity.Email;
import com.es2.bicicletario.repository.CiclistaRepository;

@Service
public class CiclistaService {

    @Autowired
    CiclistaRepository ciclistaRepository;

    public Ciclista criar(CiclistaRequestDTO dto) {
        // Validações de negócio (ex: senhas coincidem, email já existe, etc.)
        if (!dto.getSenha().equals(dto.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        Email emailObj = new Email(dto.getEmail());
        Cpf cpfObj = new Cpf(dto.getCpf());

        // Criação da entidade com os objetos validados
        Ciclista novoCiclista = new Ciclista();
        novoCiclista.setNomeCiclista(dto.getNomeCiclista());
        novoCiclista.setEmail(emailObj);
        novoCiclista.setCpf(cpfObj);

        return ciclistaRepository.save(novoCiclista);
    } 

}

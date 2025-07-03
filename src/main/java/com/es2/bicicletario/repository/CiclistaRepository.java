package com.es2.bicicletario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es2.bicicletario.entity.Ciclista;

@Repository
public interface CiclistaRepository extends JpaRepository<Ciclista, Integer> {
    Optional<Ciclista> findByCpfNumero(String cpf);
    Optional<Ciclista> findByEmailEndereco(String email);
    Optional<Ciclista> findByPassaporteNumeroPassaporte(String passaporte);
}

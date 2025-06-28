package com.es2.bicicletario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Cpf;

@Repository
public interface CiclistaRepository extends JpaRepository<Ciclista, Long> {
    Optional<Ciclista> findByCpf(Cpf cpf);
}

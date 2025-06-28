package com.es2.bicicletario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Cpf;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    Optional<Aluguel> findByCpf(Cpf cpf);
}

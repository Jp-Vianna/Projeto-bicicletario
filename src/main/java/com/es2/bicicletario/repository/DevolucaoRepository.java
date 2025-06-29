package com.es2.bicicletario.repository;

import com.es2.bicicletario.entity.Devolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Integer> {
    
}
package com.es2.bicicletario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.es2.bicicletario.entity.Aluguel;
import com.es2.bicicletario.entity.Status;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Integer> {
    Optional<Aluguel> findByCiclistaId(Integer idCiclista);
    Optional<Aluguel> findByCiclistaIdAndStatus(Integer idCiclista, Status status);
    List<Aluguel> findAllByCiclistaIdAndStatusIn(Integer idCiclista, List<Status> statuses);
    Optional<Aluguel> findByIdBicicletaAndStatus(Integer idBicicleta, Status status);
}

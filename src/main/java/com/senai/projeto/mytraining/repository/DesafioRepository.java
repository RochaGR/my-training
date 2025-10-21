package com.senai.projeto.mytraining.repository;

import com.senai.projeto.mytraining.model.Desafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesafioRepository extends JpaRepository<Desafio, Long> {
    List<Desafio> findByStatus(Desafio.Status status);
}
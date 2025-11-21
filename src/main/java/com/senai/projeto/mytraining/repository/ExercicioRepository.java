package com.senai.projeto.mytraining.repository;

import com.senai.projeto.mytraining.model.Exercicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Tag(name = "ExercicioRepository", description = "Repositório para acesso de dados de Exercício")
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    @Operation(summary = "Buscar exercícios por treino", description = "Retorna todos os exercícios de um treino específico")
    List<Exercicio> findByTreinoId(Long treinoId);
}
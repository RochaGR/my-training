package com.senai.projeto.mytraining.repository;

import com.senai.projeto.mytraining.model.Treino;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Tag(name = "TreinoRepository", description = "Repositório para acesso de dados de Treino")
public interface TreinoRepository extends JpaRepository<Treino, Long> {

    @Operation(summary = "Buscar treinos por usuário", description = "Retorna todos os treinos de um usuário específico")
    List<Treino> findByUsuarioId(Long usuarioId);
}
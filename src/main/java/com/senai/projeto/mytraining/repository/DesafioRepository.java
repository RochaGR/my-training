package com.senai.projeto.mytraining.repository;

import com.senai.projeto.mytraining.model.Desafio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Tag(name = "DesafioRepository", description = "Repositório para acesso de dados de Desafio")
public interface DesafioRepository extends JpaRepository<Desafio, Long> {

    @Operation(summary = "Buscar desafios por status", description = "Retorna todos os desafios com um status específico (PENDENTE, CONCLUIDO, CANCELADO)")
    List<Desafio> findByStatus(Desafio.Status status);

    @Operation(summary = "Buscar desafios por usuário", description = "Retorna todos os desafios de um usuário específico")
    List<Desafio> findByUsuarioId(Long usuarioId);
}
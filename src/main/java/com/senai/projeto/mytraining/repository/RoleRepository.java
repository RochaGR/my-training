package com.senai.projeto.mytraining.repository;

import com.senai.projeto.mytraining.model.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Tag(name = "RoleRepository", description = "Repositório para acesso de dados de Role")
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Operation(summary = "Buscar role por nome", description = "Retorna uma role com nome específico")
    Optional<Role> findByNome(String nome);
}
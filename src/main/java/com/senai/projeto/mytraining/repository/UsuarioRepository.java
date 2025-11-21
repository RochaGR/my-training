package com.senai.projeto.mytraining.repository;

import com.senai.projeto.mytraining.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Tag(name = "UsuarioRepository", description = "Repositório para acesso de dados de Usuario")
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Operation(summary = "Buscar usuário por email", description = "Retorna um usuario com email específico")
    Optional<Usuario> findByEmail(String email);
}
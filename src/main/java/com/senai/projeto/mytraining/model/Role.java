package com.senai.projeto.mytraining.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role - Função/Permissão de usuário no sistema")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da role", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nome da role", example = "ROLE_ADMIN")
    private String nome;

    @ManyToMany(mappedBy = "roles")
    @Schema(description = "Usuários que possuem esta role")
    private Set<Usuario> usuarios = new HashSet<>();
}
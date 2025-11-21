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
@Schema(description = "Usuário - Pessoa que utiliza a aplicação MyTraining")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email único do usuário", example = "joao@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Senha criptografada do usuário")
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "Roles/Permissões do usuário")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Desafios criados pelo usuário")
    private Set<Desafio> desafios = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Treinos realizados pelo usuário")
    private Set<Treino> treinos = new HashSet<>();
}
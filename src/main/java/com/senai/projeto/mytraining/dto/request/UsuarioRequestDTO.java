package com.senai.projeto.mytraining.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.Set;

public record UsuarioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        @Schema(description = "Nome completo do usuário", example = "João Silva", maxLength = 100)
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @Schema(description = "Email único do usuário", example = "joao@example.com")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "senha123")
        String senha,

        @Schema(description = "IDs das roles a serem atribuídas ao usuário", example = "[2]")
        Set<Long> rolesIds
) {
}

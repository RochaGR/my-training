package com.senai.projeto.mytraining.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record LoginRequestDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @Schema(description = "Email do usuário", example = "usuario@example.com")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Schema(description = "Senha do usuário", example = "senha123")
        String senha
) {
}
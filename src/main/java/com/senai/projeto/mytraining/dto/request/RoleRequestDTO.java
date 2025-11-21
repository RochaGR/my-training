package com.senai.projeto.mytraining.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record RoleRequestDTO(
        @NotBlank(message = "Nome da role é obrigatório")
        @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
        @Schema(description = "Nome da role (ex: ROLE_ADMIN, ROLE_USER)", example = "ROLE_ADMIN", maxLength = 50)
        String nome
) {
}
package com.senai.projeto.mytraining.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoleResponseDTO(
        @Schema(description = "ID único da role", example = "1")
        Long id,

        @Schema(description = "Nome da role", example = "ROLE_ADMIN")
        String nome
) {
}
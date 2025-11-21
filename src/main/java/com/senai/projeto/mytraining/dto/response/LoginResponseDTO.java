package com.senai.projeto.mytraining.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(
        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Dados do usuário autenticado")
        UsuarioResponseDTO usuario
) {
}
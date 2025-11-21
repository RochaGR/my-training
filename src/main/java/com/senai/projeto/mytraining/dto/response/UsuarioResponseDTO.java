package com.senai.projeto.mytraining.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record UsuarioResponseDTO(
        @Schema(description = "ID único do usuário", example = "1")
        Long id,

        @Schema(description = "Nome do usuário", example = "João Silva")
        String nome,

        @Schema(description = "Email do usuário", example = "joao@example.com")
        String email,

        @Schema(description = "Conjunto de roles do usuário")
        Set<RoleResponseDTO> roles,

        @Schema(description = "Conjunto de desafios do usuário")
        Set<DesafioResponseDTO> desafios
) {
}
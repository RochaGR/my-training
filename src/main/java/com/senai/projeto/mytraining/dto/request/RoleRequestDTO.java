package com.senai.projeto.mytraining.dto.request;

import jakarta.validation.constraints.*;

public record RoleRequestDTO(
        @NotBlank(message = "Nome da role é obrigatório")
        @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
        String nome
) {
}
package com.senai.projeto.mytraining.dto.response;

public record LoginResponseDTO(
        String token,
        UsuarioResponseDTO usuario
) {
}


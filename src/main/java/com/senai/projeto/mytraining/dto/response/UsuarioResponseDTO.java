package com.senai.projeto.mytraining.dto.response;

import java.util.Set;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Set<RoleResponseDTO> roles,
        Set<DesafioResponseDTO> desafios
) {
}
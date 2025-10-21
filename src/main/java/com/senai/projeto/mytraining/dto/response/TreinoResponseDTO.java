package com.senai.projeto.mytraining.dto.response;

import com.senai.projeto.mytraining.model.TipoTreino;

import java.time.LocalDateTime;
import java.util.Set;

public record TreinoResponseDTO(
        Long id,
        LocalDateTime dataHora,
        TipoTreino tipo,
        Integer duracaoMin,
        String observacoes,
        Double distanciaKm,
        Long usuarioId,
        String usuarioNome,
        Set<ExercicioResponseDTO> exercicios
) {
}
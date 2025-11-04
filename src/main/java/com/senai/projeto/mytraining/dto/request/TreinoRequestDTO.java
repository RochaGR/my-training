package com.senai.projeto.mytraining.dto.request;

import com.senai.projeto.mytraining.model.TipoTreino;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

public record TreinoRequestDTO(
        @NotNull(message = "Data e hora são obrigatórias")
        LocalDateTime dataHora,

        @NotNull(message = "Tipo de treino é obrigatório")
        TipoTreino tipo,

        @Positive(message = "Duração deve ser um valor positivo")
        Integer duracaoMin,

        @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
        String observacoes,

        @PositiveOrZero(message = "Distância deve ser um valor positivo ou zero")
        Double distanciaKm,

        Long usuarioId, // Opcional quando usando autenticação JWT

        @Valid
        Set<ExercicioRequestDTO> exercicios
) {
}
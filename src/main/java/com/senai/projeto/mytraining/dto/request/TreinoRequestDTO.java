package com.senai.projeto.mytraining.dto.request;

import com.senai.projeto.mytraining.model.TipoTreino;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

public record TreinoRequestDTO(
        @NotNull(message = "Data e hora são obrigatórias")
        @Schema(description = "Data e hora do treino", example = "2024-02-01T07:00:00")
        LocalDateTime dataHora,

        @NotNull(message = "Tipo de treino é obrigatório")
        @Schema(description = "Tipo do treino", example = "CORRIDA")
        TipoTreino tipo,

        @Positive(message = "Duração deve ser um valor positivo")
        @Schema(description = "Duração do treino em minutos", example = "40")
        Integer duracaoMin,

        @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
        @Schema(description = "Observações sobre o treino", example = "Treino intervalado leve", maxLength = 1000)
        String observacoes,

        @PositiveOrZero(message = "Distância deve ser um valor positivo ou zero")
        @Schema(description = "Distância percorrida em km (para CORRIDA e CICLISMO)", example = "7.0")
        Double distanciaKm,

        @Schema(description = "ID do usuário (opcional quando usando JWT)", example = "2")
        Long usuarioId,

        @Valid
        @Schema(description = "Conjunto de exercícios do treino")
        Set<ExercicioRequestDTO> exercicios
) {
}
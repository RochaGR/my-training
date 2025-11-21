package com.senai.projeto.mytraining.dto.response;

import com.senai.projeto.mytraining.model.TipoTreino;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

public record TreinoResponseDTO(
        @Schema(description = "ID único do treino", example = "1")
        Long id,

        @Schema(description = "Data e hora do treino", example = "2024-02-01T07:00:00")
        LocalDateTime dataHora,

        @Schema(description = "Tipo do treino", example = "CORRIDA")
        TipoTreino tipo,

        @Schema(description = "Duração do treino em minutos", example = "40")
        Integer duracaoMin,

        @Schema(description = "Observações sobre o treino", example = "Treino intervalado")
        String observacoes,

        @Schema(description = "Distância percorrida em km", example = "7.0")
        Double distanciaKm,

        @Schema(description = "ID do usuário proprietário do treino", example = "2")
        Long usuarioId,

        @Schema(description = "Nome do usuário proprietário do treino", example = "João Silva")
        String usuarioNome,

        @Schema(description = "Conjunto de exercícios do treino")
        Set<ExercicioResponseDto> exercicios
) {
}
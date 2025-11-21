package com.senai.projeto.mytraining.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExercicioResponseDto(
        @Schema(description = "ID único do exercício", example = "1")
        Long id,

        @Schema(description = "Nome do exercício", example = "Supino Reto")
        String nome,

        @Schema(description = "Número de séries", example = "3")
        Integer series,

        @Schema(description = "Número de repetições", example = "12")
        Integer repeticoes,

        @Schema(description = "Carga em kg", example = "60.0")
        Double cargaKg,

        @Schema(description = "Observações sobre o exercício", example = "Controlar a descida")
        String observacoes,

        @Schema(description = "ID do treino ao qual o exercício pertence", example = "1")
        Long aLong
) {
}

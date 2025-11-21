package com.senai.projeto.mytraining.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ExercicioRequestDTO(
        @NotBlank(message = "Nome do exercício é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        @Schema(description = "Nome do exercício", example = "Supino Reto", maxLength = 100)
        String nome,

        @Min(value = 1, message = "Séries deve ser no mínimo 1")
        @Schema(description = "Número de séries", example = "3")
        Integer series,

        @Min(value = 1, message = "Repetições deve ser no mínimo 1")
        @Schema(description = "Número de repetições", example = "12")
        Integer repeticoes,

        @PositiveOrZero(message = "Carga deve ser um valor positivo ou zero")
        @Schema(description = "Carga em kg (0 se usar peso corporal)", example = "60.0")
        Double cargaKg,

        @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
        @Schema(description = "Observações sobre o exercício", example = "Controlar a descida", maxLength = 500)
        String observacoes,

        @NotNull(message = "ID do treino é obrigatório")
        @Schema(description = "ID do treino ao qual este exercício pertence", example = "1")
        Long treinoId
) {
}
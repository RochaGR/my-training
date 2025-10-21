package com.senai.projeto.mytraining.dto.request;

import jakarta.validation.constraints.*;

public record ExercicioRequestDTO(
        @NotBlank(message = "Nome do exercício é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @Min(value = 1, message = "Séries deve ser no mínimo 1")
        Integer series,

        @Min(value = 1, message = "Repetições deve ser no mínimo 1")
        Integer repeticoes,

        @PositiveOrZero(message = "Carga deve ser um valor positivo ou zero")
        Double cargaKg,

        @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
        String observacoes,

        @NotNull(message = "ID do treino é obrigatório")
        Long treinoId
) {
}
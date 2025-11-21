package com.senai.projeto.mytraining.dto.request;

import com.senai.projeto.mytraining.model.Desafio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record DesafioRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
        @Schema(description = "Título do desafio", example = "Correr 100km em janeiro", maxLength = 100)
        String titulo,

        @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
        @Schema(description = "Descrição detalhada do desafio", example = "Correr 100km ao longo do mês de janeiro", maxLength = 1000)
        String descricao,

        @NotNull(message = "Data de início é obrigatória")
        @Schema(description = "Data de início do desafio", example = "2024-01-01")
        LocalDate dataInicio,

        @NotNull(message = "Data de fim é obrigatória")
        @Schema(description = "Data de término do desafio", example = "2024-01-31")
        LocalDate dataFim,

        @Positive(message = "Objetivo deve ser um valor positivo")
        @Schema(description = "Valor objetivo a ser alcançado", example = "100.0")
        Double objetivoValor,

        @PositiveOrZero(message = "Progresso atual deve ser um valor positivo ou zero")
        @Schema(description = "Progresso atual em relação ao objetivo", example = "45.0")
        Double progressoAtual,

        @NotNull(message = "Unidade é obrigatória")
        @Schema(description = "Unidade de medida do desafio (KM, MINUTOS, CALORIAS, REPETICOES, SERIES)", example = "KM")
        Desafio.Unidade unidade,

        @NotNull(message = "Status é obrigatório")
        @Schema(description = "Status do desafio (PENDENTE, CONCLUIDO, CANCELADO)", example = "PENDENTE")
        Desafio.Status status
) {
}
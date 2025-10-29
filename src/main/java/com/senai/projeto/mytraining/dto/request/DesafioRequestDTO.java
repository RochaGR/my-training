package com.senai.projeto.mytraining.dto.request;

import com.senai.projeto.mytraining.model.Desafio;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DesafioRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
        String titulo,

        @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
        String descricao,

        @NotNull(message = "Data de início é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "Data de fim é obrigatória")
        LocalDate dataFim,

        @Positive(message = "Objetivo deve ser um valor positivo")
        Double objetivoValor,

        @PositiveOrZero(message = "Progresso atual deve ser um valor positivo ou zero")
        Double progressoAtual,

        @NotNull(message = "Unidade é obrigatória")
        Desafio.Unidade unidade,

        @NotNull(message = "Status é obrigatório")
        Desafio.Status status
) {
}
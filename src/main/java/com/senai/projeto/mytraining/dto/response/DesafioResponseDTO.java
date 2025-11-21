package com.senai.projeto.mytraining.dto.response;

import com.senai.projeto.mytraining.model.Desafio;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record DesafioResponseDTO(
        @Schema(description = "ID único do desafio", example = "1")
        Long id,

        @Schema(description = "Título do desafio", example = "Correr 100km em janeiro")
        String titulo,

        @Schema(description = "Descrição do desafio", example = "Correr 100km ao longo do mês")
        String descricao,

        @Schema(description = "Data de início do desafio", example = "2024-01-01")
        LocalDate dataInicio,

        @Schema(description = "Data de término do desafio", example = "2024-01-31")
        LocalDate dataFim,

        @Schema(description = "Valor objetivo a ser alcançado", example = "100.0")
        Double objetivoValor,

        @Schema(description = "Progresso atual", example = "45.0")
        Double progressoAtual,

        @Schema(description = "Unidade de medida", example = "KM")
        Desafio.Unidade unidade,

        @Schema(description = "Status do desafio", example = "PENDENTE")
        Desafio.Status status
) {
}
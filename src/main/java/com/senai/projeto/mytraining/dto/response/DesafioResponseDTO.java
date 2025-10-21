package com.senai.projeto.mytraining.dto.response;

import com.senai.projeto.mytraining.model.Desafio;

import java.time.LocalDate;

public record DesafioResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        Double objetivoValor,
        Desafio.Unidade unidade,
        Desafio.Status status
) {
}
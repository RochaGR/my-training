package com.senai.projeto.mytraining.dto.response;

public record ExercicioResponseDTO (
        Long id,
        String nome,
        Integer series,
        Integer repeticoes,
        Double cargaKg,
        String observacoes,
        Long aLong) {

}

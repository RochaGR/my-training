package com.senai.projeto.mytraining.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de treino disponíveis")
public enum TipoTreino {
    @Schema(description = "Corrida ou atividade de cardio")
    CORRIDA,
    @Schema(description = "Musculação ou exercício com pesos")
    MUSCULACAO,
    @Schema(description = "Ciclismo ou atividade com bicicleta")
    CICLISMO
}
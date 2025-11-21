package com.senai.projeto.mytraining.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Desafio - Meta de fitness que o usuário quer alcançar")
public class Desafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do desafio", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Título do desafio", example = "Correr 100km")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descrição detalhada do desafio", example = "Correr 100km ao longo de janeiro")
    private String descricao;

    @Column(name = "data_inicio")
    @Schema(description = "Data de início do desafio", example = "2024-01-01")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    @Schema(description = "Data de término do desafio", example = "2024-01-31")
    private LocalDate dataFim;

    @Column(name = "objetivo_valor")
    @Schema(description = "Valor objetivo a ser alcançado", example = "100.0")
    private Double objetivoValor;

    @Column(name = "progresso_atual")
    @Schema(description = "Progresso atual em relação ao objetivo", example = "45.0")
    private Double progressoAtual = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Unidade de medida do desafio", example = "KM")
    private Unidade unidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Status do desafio", example = "PENDENTE")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuário proprietário do desafio")
    private Usuario usuario;

    @Schema(description = "Unidades de medida disponíveis para desafios")
    public enum Unidade {
        @Schema(description = "Quilômetros")
        KM,
        @Schema(description = "Minutos")
        MINUTOS,
        @Schema(description = "Calorias")
        CALORIAS,
        @Schema(description = "Repetições")
        REPETICOES,
        @Schema(description = "Séries")
        SERIES
    }

    @Schema(description = "Status possíveis do desafio")
    public enum Status {
        @Schema(description = "Desafio em andamento")
        PENDENTE,
        @Schema(description = "Desafio completado")
        CONCLUIDO,
        @Schema(description = "Desafio cancelado")
        CANCELADO
    }
}

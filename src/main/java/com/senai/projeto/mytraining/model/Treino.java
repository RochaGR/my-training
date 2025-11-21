package com.senai.projeto.mytraining.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Treino - Sessão de exercício realizada pelo usuário")
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do treino", example = "1")
    private Long id;

    @Column(name = "data_hora", nullable = false)
    @Schema(description = "Data e hora do treino", example = "2024-02-01T07:00:00")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo do treino", example = "CORRIDA")
    private TipoTreino tipo;

    @Column(name = "duracao_min")
    @Schema(description = "Duração do treino em minutos", example = "40")
    private Integer duracaoMin;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Observações sobre o treino", example = "Treino intervalado leve")
    private String observacoes;

    @Column(name = "distancia_km")
    @Schema(description = "Distância percorrida em km (para CORRIDA e CICLISMO)", example = "7.0")
    private Double distanciaKm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuário proprietário do treino")
    private Usuario usuario;

    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Exercícios realizados neste treino")
    private Set<Exercicio> exercicios = new HashSet<>();
}
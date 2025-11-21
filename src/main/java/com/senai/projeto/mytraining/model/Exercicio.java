package com.senai.projeto.mytraining.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Exercício - Movimento físico executado durante um treino")
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do exercício", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome do exercício", example = "Supino Reto")
    private String nome;

    @Schema(description = "Número de séries", example = "4")
    private Integer series;

    @Schema(description = "Número de repetições", example = "10")
    private Integer repeticoes;

    @Schema(description = "Carga em kg (0 se peso corporal)", example = "60.0")
    private Double cargaKg;

    @Schema(description = "Observações técnicas do exercício", example = "Barra até o peito")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treino_id", nullable = false)
    @Schema(description = "Treino ao qual este exercício pertence")
    private Treino treino;
}
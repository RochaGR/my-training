package com.senai.projeto.mytraining.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Desafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "objetivo_valor")
    private Double objetivoValor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unidade unidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Unidade {
        KM, MINUTOS, CALORIAS, REPETICOES, SERIES
    }

    public enum Status {
        ATIVO, CONCLUIDO, CANCELADO, PENDENTE
    }
}
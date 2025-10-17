package com.senai.projeto.mytraining.model;

import com.senai.projeto.mytraining.model.Exercicio;
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
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTreino tipo;

    @Column(name = "duracao_min")
    private Integer duracaoMin;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercicio> exercicios = new HashSet<>();
}
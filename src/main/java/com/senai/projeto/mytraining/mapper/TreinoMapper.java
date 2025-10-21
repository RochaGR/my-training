package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.TreinoRequestDTO;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import com.senai.projeto.mytraining.model.Treino;
import com.senai.projeto.mytraining.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TreinoMapper {

    private final ExercicioMapper exercicioMapper;

    public Treino toEntity(TreinoRequestDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }

        Treino treino = new Treino();
        treino.setDataHora(dto.dataHora());
        treino.setTipo(dto.tipo());
        treino.setDuracaoMin(dto.duracaoMin());
        treino.setObservacoes(dto.observacoes());
        treino.setDistanciaKm(dto.distanciaKm());
        treino.setUsuario(usuario);

        return treino;
    }

    public TreinoResponseDTO toResponseDTO(Treino treino) {
        if (treino == null) {
            return null;
        }

        return new TreinoResponseDTO(
                treino.getId(),
                treino.getDataHora(),
                treino.getTipo(),
                treino.getDuracaoMin(),
                treino.getObservacoes(),
                treino.getDistanciaKm(),
                treino.getUsuario() != null ? treino.getUsuario().getId() : null,
                treino.getUsuario() != null ? treino.getUsuario().getNome() : null,
                treino.getExercicios() != null ?
                        treino.getExercicios().stream()
                                .map(exercicioMapper::toResponseDTO)
                                .collect(Collectors.toSet()) : null
        );
    }

    public void updateEntityFromDTO(TreinoRequestDTO dto, Treino treino) {
        if (dto == null || treino == null) {
            return;
        }

        treino.setDataHora(dto.dataHora());
        treino.setTipo(dto.tipo());
        treino.setDuracaoMin(dto.duracaoMin());
        treino.setObservacoes(dto.observacoes());
        treino.setDistanciaKm(dto.distanciaKm());
    }
}
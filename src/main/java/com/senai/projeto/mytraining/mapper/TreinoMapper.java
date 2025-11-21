package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.TreinoRequestDTO;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import com.senai.projeto.mytraining.model.Treino;
import com.senai.projeto.mytraining.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Tag(name = "TreinoMapper", description = "Mapeador entre DTOs e entidades de Treino")
public class TreinoMapper {

    private final ExercicioMapper exercicioMapper;

    @Operation(summary = "Converter TreinoRequestDTO para Treino", description = "Mapeia dados de entrada e associa usuário")
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

    @Operation(summary = "Converter Treino para TreinoResponseDTO", description = "Mapeia entidade JPA para resposta da API, incluindo exercícios")
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

    @Operation(summary = "Atualizar Treino a partir de DTO", description = "Atualiza campos de entidade existente")
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

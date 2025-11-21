package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import com.senai.projeto.mytraining.model.Exercicio;
import com.senai.projeto.mytraining.model.Treino;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;

@Component
@Tag(name = "ExercicioMapper", description = "Mapeador entre DTOs e entidades de Exercício")
public class ExercicioMapper {

    @Operation(summary = "Converter ExercicioRequestDTO para Exercicio", description = "Mapeia dados de entrada e associa treino")
    public Exercicio toEntity(ExercicioRequestDTO dto, Treino treino) {
        if (dto == null) {
            return null;
        }

        Exercicio exercicio = new Exercicio();
        exercicio.setNome(dto.nome());
        exercicio.setSeries(dto.series());
        exercicio.setRepeticoes(dto.repeticoes());
        exercicio.setCargaKg(dto.cargaKg());
        exercicio.setObservacoes(dto.observacoes());
        exercicio.setTreino(treino);

        return exercicio;
    }

    @Operation(summary = "Converter Exercicio para ExercicioResponseDto", description = "Mapeia entidade JPA para resposta da API")
    public ExercicioResponseDto toResponseDTO(Exercicio exercicio) {
        if (exercicio == null) {
            return null;
        }

        return new ExercicioResponseDto(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getSeries(),
                exercicio.getRepeticoes(),
                exercicio.getCargaKg(),
                exercicio.getObservacoes(),
                exercicio.getTreino() != null ? exercicio.getTreino().getId() : null
        );
    }

    @Operation(summary = "Atualizar Exercicio a partir de DTO", description = "Atualiza campos de entidade existente")
    public void updateEntityFromDTO(ExercicioRequestDTO dto, Exercicio exercicio) {
        if (dto == null || exercicio == null) {
            return;
        }

        exercicio.setNome(dto.nome());
        exercicio.setSeries(dto.series());
        exercicio.setRepeticoes(dto.repeticoes());
        exercicio.setCargaKg(dto.cargaKg());
        exercicio.setObservacoes(dto.observacoes());
    }
}

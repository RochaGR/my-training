package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDTO;
import com.senai.projeto.mytraining.model.Exercicio;
import com.senai.projeto.mytraining.model.Treino;
import org.springframework.stereotype.Component;

@Component
public class ExercicioMapper {

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

    public ExercicioResponseDTO toResponseDTO(Exercicio exercicio) {
        if (exercicio == null) {
            return null;
        }

        return new ExercicioResponseDTO(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getSeries(),
                exercicio.getRepeticoes(),
                exercicio.getCargaKg(),
                exercicio.getObservacoes(),
                exercicio.getTreino() != null ? exercicio.getTreino().getId() : null
        );
    }

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
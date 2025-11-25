package com.senai.projeto.mytraining.hateoas;

import com.senai.projeto.mytraining.controller.ExercicioController;
import com.senai.projeto.mytraining.controller.TreinoController;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ExercicioModelAssembler implements RepresentationModelAssembler<ExercicioResponseDto, EntityModel<ExercicioResponseDto>> {

    @Override
    public EntityModel<ExercicioResponseDto> toModel(ExercicioResponseDto exercicio) {
        EntityModel<ExercicioResponseDto> model = EntityModel.of(exercicio);

        model.add(linkTo(methodOn(ExercicioController.class)
                .buscarPorId(exercicio.id()))
                .withSelfRel());

        model.add(linkTo(methodOn(ExercicioController.class)
                .atualizar(exercicio.id(), null))
                .withRel("atualizar"));

        model.add(linkTo(methodOn(ExercicioController.class)
                .deletar(exercicio.id()))
                .withRel("deletar"));

        model.add(linkTo(methodOn(ExercicioController.class)
                .listarTodos())
                .withRel("all-exercicios"));

        if (exercicio.aLong() != null) {
            model.add(linkTo(methodOn(TreinoController.class)
                    .buscarPorId(exercicio.aLong()))
                    .withRel("treino"));
        }

        return model;
    }
}
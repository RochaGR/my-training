package com.senai.projeto.mytraining.hateoas;

import com.senai.projeto.mytraining.controller.TreinoController;
import com.senai.projeto.mytraining.controller.UsuarioController;
import com.senai.projeto.mytraining.controller.ExercicioController;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TreinoModelAssembler implements RepresentationModelAssembler<TreinoResponseDTO, EntityModel<TreinoResponseDTO>> {

    @Override
    public EntityModel<TreinoResponseDTO> toModel(TreinoResponseDTO treino) {
        EntityModel<TreinoResponseDTO> model = EntityModel.of(treino);

        model.add(linkTo(methodOn(TreinoController.class)
                .buscarPorId(treino.id()))
                .withSelfRel());

        model.add(linkTo(methodOn(TreinoController.class)
                .atualizar(treino.id(), null))
                .withRel("atualizar"));

        model.add(linkTo(methodOn(TreinoController.class)
                .deletar(treino.id()))
                .withRel("deletar"));

        model.add(linkTo(methodOn(TreinoController.class)
                .listarTodos())
                .withRel("all-treinos"));

        if (treino.usuarioId() != null) {
            model.add(linkTo(methodOn(UsuarioController.class)
                    .buscarPorId(treino.usuarioId()))
                    .withRel("usuario"));
        }

        model.add(linkTo(methodOn(ExercicioController.class)
                .listarPorTreino(treino.id()))
                .withRel("exercicios"));

        return model;
    }
}
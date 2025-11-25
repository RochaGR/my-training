package com.senai.projeto.mytraining.hateoas;

import com.senai.projeto.mytraining.controller.DesafioController;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DesafioModelAssembler implements RepresentationModelAssembler<DesafioResponseDTO, EntityModel<DesafioResponseDTO>> {

    @Override
    public EntityModel<DesafioResponseDTO> toModel(DesafioResponseDTO desafio) {
        EntityModel<DesafioResponseDTO> model = EntityModel.of(desafio);

        model.add(linkTo(methodOn(DesafioController.class)
                .buscarPorId(desafio.id()))
                .withSelfRel());

        model.add(linkTo(methodOn(DesafioController.class)
                .atualizar(desafio.id(), null))
                .withRel("atualizar"));

        model.add(linkTo(methodOn(DesafioController.class)
                .deletar(desafio.id()))
                .withRel("deletar"));

        model.add(linkTo(methodOn(DesafioController.class)
                .listarTodos())
                .withRel("all-desafios"));

        model.add(linkTo(methodOn(DesafioController.class)
                .buscarPorStatus(desafio.status()))
                .withRel("desafios-mesmo-status"));

        return model;
    }
}
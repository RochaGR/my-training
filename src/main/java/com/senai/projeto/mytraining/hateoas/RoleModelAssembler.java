package com.senai.projeto.mytraining.hateoas;

import com.senai.projeto.mytraining.controller.RoleController;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RoleModelAssembler implements RepresentationModelAssembler<RoleResponseDTO, EntityModel<RoleResponseDTO>> {

    @Override
    public EntityModel<RoleResponseDTO> toModel(RoleResponseDTO role) {
        EntityModel<RoleResponseDTO> model = EntityModel.of(role);

        model.add(linkTo(methodOn(RoleController.class)
                .buscarPorId(role.id()))
                .withSelfRel());

        model.add(linkTo(methodOn(RoleController.class)
                .atualizar(role.id(), null))
                .withRel("atualizar"));

        model.add(linkTo(methodOn(RoleController.class)
                .deletar(role.id()))
                .withRel("deletar"));

        model.add(linkTo(methodOn(RoleController.class)
                .listarTodas())
                .withRel("all-roles"));

        model.add(linkTo(methodOn(RoleController.class)
                .buscarPorNome(role.nome()))
                .withRel("buscar-por-nome"));

        return model;
    }
}
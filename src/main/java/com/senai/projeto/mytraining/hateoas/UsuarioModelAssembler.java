package com.senai.projeto.mytraining.hateoas;

import com.senai.projeto.mytraining.controller.UsuarioController;
import com.senai.projeto.mytraining.controller.TreinoController;
import com.senai.projeto.mytraining.controller.DesafioController;
import com.senai.projeto.mytraining.dto.response.UsuarioResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponseDTO, EntityModel<UsuarioResponseDTO>> {

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(UsuarioResponseDTO usuario) {
        EntityModel<UsuarioResponseDTO> model = EntityModel.of(usuario);

        model.add(linkTo(methodOn(UsuarioController.class)
                .buscarPorId(usuario.id()))
                .withSelfRel());

        model.add(linkTo(methodOn(UsuarioController.class)
                .atualizar(usuario.id(), null))
                .withRel("atualizar"));

        model.add(linkTo(methodOn(UsuarioController.class)
                .deletar(usuario.id()))
                .withRel("deletar"));

        model.add(linkTo(methodOn(UsuarioController.class)
                .listarTodos())
                .withRel("all-usuarios"));

        model.add(linkTo(methodOn(TreinoController.class)
                .listarPorUsuario(usuario.id()))
                .withRel("treinos"));

        model.add(linkTo(methodOn(UsuarioController.class)
                .buscarPorEmail(usuario.email()))
                .withRel("buscar-por-email"));

        return model;
    }
}
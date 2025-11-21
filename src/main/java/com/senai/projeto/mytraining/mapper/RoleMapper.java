package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
import com.senai.projeto.mytraining.model.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Component;

@Component
@Tag(name = "RoleMapper", description = "Mapeador entre DTOs e entidades de Role")
public class RoleMapper {

    @Operation(summary = "Converter RoleRequestDTO para Role", description = "Mapeia dados de entrada para entidade JPA")
    public Role toEntity(RoleRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        role.setNome(dto.nome());

        return role;
    }

    @Operation(summary = "Converter Role para RoleResponseDTO", description = "Mapeia entidade JPA para resposta da API")
    public RoleResponseDTO toResponseDTO(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleResponseDTO(
                role.getId(),
                role.getNome()
        );
    }

    @Operation(summary = "Atualizar Role a partir de DTO", description = "Atualiza campos de entidade existente")
    public void updateEntityFromDTO(RoleRequestDTO dto, Role role) {
        if (dto == null || role == null) {
            return;
        }

        role.setNome(dto.nome());
    }
}
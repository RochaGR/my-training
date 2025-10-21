package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
import com.senai.projeto.mytraining.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        role.setNome(dto.nome());

        return role;
    }

    public RoleResponseDTO toResponseDTO(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleResponseDTO(
                role.getId(),
                role.getNome()
        );
    }

    public void updateEntityFromDTO(RoleRequestDTO dto, Role role) {
        if (dto == null || role == null) {
            return;
        }

        role.setNome(dto.nome());
    }
}
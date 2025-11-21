package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.UsuarioRequestDTO;
import com.senai.projeto.mytraining.dto.response.UsuarioResponseDTO;
import com.senai.projeto.mytraining.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Tag(name = "UsuarioMapper", description = "Mapeador entre DTOs e entidades de Usuario")
public class UsuarioMapper {

    private final RoleMapper roleMapper;
    private final DesafioMapper desafioMapper;

    @Operation(summary = "Converter UsuarioRequestDTO para Usuario", description = "Mapeia dados de entrada para entidade JPA")
    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());

        return usuario;
    }

    @Operation(summary = "Converter Usuario para UsuarioResponseDTO", description = "Mapeia entidade JPA para resposta da API, incluindo roles e desafios")
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRoles() != null ?
                        usuario.getRoles().stream()
                                .map(roleMapper::toResponseDTO)
                                .collect(Collectors.toSet()) : null,
                usuario.getDesafios() != null ?
                        usuario.getDesafios().stream()
                                .map(desafioMapper::toResponseDTO)
                                .collect(Collectors.toSet()) : null
        );
    }

    @Operation(summary = "Atualizar Usuario a partir de DTO", description = "Atualiza campos de entidade existente (não inclui senha)")
    public void updateEntityFromDTO(UsuarioRequestDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) {
            return;
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());

        // Senha só deve ser atualizada se fornecida
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(dto.senha());
        }
    }
}
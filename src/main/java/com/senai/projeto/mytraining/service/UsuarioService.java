package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.UsuarioRequestDTO;
import com.senai.projeto.mytraining.dto.response.UsuarioResponseDTO;
import com.senai.projeto.mytraining.mapper.UsuarioMapper;
import com.senai.projeto.mytraining.model.Role;
import com.senai.projeto.mytraining.model.Usuario;
import com.senai.projeto.mytraining.repository.RoleRepository;
import com.senai.projeto.mytraining.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Tag(name = "UsuarioService", description = "Serviço de gerenciamento de usuários")
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário com roles opcionais")
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.rolesIds()));
            usuario.setRoles(roles);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }

    @Operation(summary = "Buscar usuário por email", description = "Retorna um usuário específico pelo email")
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toResponseDTO);
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna lista com todos os usuários cadastrados")
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário existente")
    public Optional<UsuarioResponseDTO> atualizar(Long id, UsuarioRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();
        usuarioMapper.updateEntityFromDTO(dto, usuario);

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.rolesIds()));
            usuario.setRoles(roles);
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return Optional.of(usuarioMapper.toResponseDTO(usuarioAtualizado));
    }

    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    public boolean deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }
}
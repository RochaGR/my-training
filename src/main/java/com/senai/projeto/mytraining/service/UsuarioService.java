package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.UsuarioRequestDTO;
import com.senai.projeto.mytraining.dto.response.UsuarioResponseDTO;
import com.senai.projeto.mytraining.mapper.UsuarioMapper;
import com.senai.projeto.mytraining.model.Desafio;
import com.senai.projeto.mytraining.model.Role;
import com.senai.projeto.mytraining.model.Usuario;
import com.senai.projeto.mytraining.repository.DesafioRepository;
import com.senai.projeto.mytraining.repository.RoleRepository;
import com.senai.projeto.mytraining.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final DesafioRepository desafioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(dto.senha());

        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.rolesIds()));
            usuario.setRoles(roles);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> atualizar(Long id, UsuarioRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();
        usuarioMapper.updateEntityFromDTO(dto, usuario);

        if (dto.senha() != null && !dto.senha().isBlank()) {
        }

        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.rolesIds()));
            usuario.setRoles(roles);
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return Optional.of(usuarioMapper.toResponseDTO(usuarioAtualizado));
    }

    public boolean deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }

    public Optional<UsuarioResponseDTO> adicionarDesafio(Long usuarioId, Long desafioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        Optional<Desafio> desafioOptional = desafioRepository.findById(desafioId);

        if (usuarioOptional.isEmpty() || desafioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();
        Desafio desafio = desafioOptional.get();

        usuario.getDesafios().add(desafio);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return Optional.of(usuarioMapper.toResponseDTO(usuarioAtualizado));
    }

    public Optional<UsuarioResponseDTO> removerDesafio(Long usuarioId, Long desafioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        Optional<Desafio> desafioOptional = desafioRepository.findById(desafioId);

        if (usuarioOptional.isEmpty() || desafioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();
        Desafio desafio = desafioOptional.get();

        usuario.getDesafios().remove(desafio);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return Optional.of(usuarioMapper.toResponseDTO(usuarioAtualizado));
    }
}
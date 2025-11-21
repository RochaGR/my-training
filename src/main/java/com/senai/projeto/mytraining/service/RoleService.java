package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
import com.senai.projeto.mytraining.mapper.RoleMapper;
import com.senai.projeto.mytraining.model.Role;
import com.senai.projeto.mytraining.repository.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Tag(name = "RoleService", description = "Serviço de gerenciamento de roles/permissões")
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Operation(summary = "Criar nova role", description = "Cria uma nova função/permissão no sistema")
    public RoleResponseDTO criar(RoleRequestDTO dto) {
        Role role = roleMapper.toEntity(dto);
        Role roleSalva = roleRepository.save(role);
        return roleMapper.toResponseDTO(roleSalva);
    }

    @Operation(summary = "Buscar role por ID", description = "Retorna uma role específica pelo ID")
    @Transactional(readOnly = true)
    public Optional<RoleResponseDTO> buscarPorId(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toResponseDTO);
    }

    @Operation(summary = "Buscar role por nome", description = "Retorna uma role específica pelo nome")
    @Transactional(readOnly = true)
    public Optional<RoleResponseDTO> buscarPorNome(String nome) {
        return roleRepository.findByNome(nome)
                .map(roleMapper::toResponseDTO);
    }

    @Operation(summary = "Listar todas as roles", description = "Retorna lista com todas as roles cadastradas")
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> listarTodas() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Atualizar role", description = "Atualiza uma role existente")
    public Optional<RoleResponseDTO> atualizar(Long id, RoleRequestDTO dto) {
        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isEmpty()) {
            return Optional.empty();
        }

        Role role = roleOptional.get();
        roleMapper.updateEntityFromDTO(dto, role);
        Role roleAtualizada = roleRepository.save(role);
        return Optional.of(roleMapper.toResponseDTO(roleAtualizada));
    }

    @Operation(summary = "Deletar role", description = "Remove uma role do sistema")
    public boolean deletar(Long id) {
        if (!roleRepository.existsById(id)) {
            return false;
        }
        roleRepository.deleteById(id);
        return true;
    }
}
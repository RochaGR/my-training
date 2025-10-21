package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
import com.senai.projeto.mytraining.mapper.RoleMapper;
import com.senai.projeto.mytraining.model.Role;
import com.senai.projeto.mytraining.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponseDTO criar(RoleRequestDTO dto) {
        Role role = roleMapper.toEntity(dto);
        Role roleSalva = roleRepository.save(role);
        return roleMapper.toResponseDTO(roleSalva);
    }

    @Transactional(readOnly = true)
    public Optional<RoleResponseDTO> buscarPorId(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Optional<RoleResponseDTO> buscarPorNome(String nome) {
        return roleRepository.findByNome(nome)
                .map(roleMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> listarTodas() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

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

    public boolean deletar(Long id) {
        if (!roleRepository.existsById(id)) {
            return false;
        }
        roleRepository.deleteById(id);
        return true;
    }
}
package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.mapper.DesafioMapper;
import com.senai.projeto.mytraining.model.Desafio;
import com.senai.projeto.mytraining.repository.DesafioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DesafioService {

    private final DesafioRepository desafioRepository;
    private final DesafioMapper desafioMapper;

    public DesafioResponseDTO criar(DesafioRequestDTO dto) {
        Desafio desafio = desafioMapper.toEntity(dto);
        Desafio desafioSalvo = desafioRepository.save(desafio);
        return desafioMapper.toResponseDTO(desafioSalvo);
    }

    @Transactional(readOnly = true)
    public Optional<DesafioResponseDTO> buscarPorId(Long id) {
        return desafioRepository.findById(id)
                .map(desafioMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<DesafioResponseDTO> listarTodos() {
        return desafioRepository.findAll().stream()
                .map(desafioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DesafioResponseDTO> listarTodosPaginado(Pageable pageable) {
        return desafioRepository.findAll(pageable)
                .map(desafioMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<DesafioResponseDTO> buscarPorStatus(Desafio.Status status) {
        return desafioRepository.findByStatus(status).stream()
                .map(desafioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<DesafioResponseDTO> atualizar(Long id, DesafioRequestDTO dto) {
        Optional<Desafio> desafioOptional = desafioRepository.findById(id);

        if (desafioOptional.isEmpty()) {
            return Optional.empty();
        }

        Desafio desafio = desafioOptional.get();
        desafioMapper.updateEntityFromDTO(dto, desafio);
        Desafio desafioAtualizado = desafioRepository.save(desafio);
        return Optional.of(desafioMapper.toResponseDTO(desafioAtualizado));
    }

    public boolean deletar(Long id) {
        if (!desafioRepository.existsById(id)) {
            return false;
        }
        desafioRepository.deleteById(id);
        return true;
    }
}
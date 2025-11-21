package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import com.senai.projeto.mytraining.mapper.ExercicioMapper;
import com.senai.projeto.mytraining.model.Exercicio;
import com.senai.projeto.mytraining.model.Treino;
import com.senai.projeto.mytraining.repository.ExercicioRepository;
import com.senai.projeto.mytraining.repository.TreinoRepository;
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
@Tag(name = "ExercicioService", description = "Serviço de gerenciamento de exercícios")
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;
    private final TreinoRepository treinoRepository;
    private final ExercicioMapper exercicioMapper;

    @Operation(summary = "Criar novo exercício", description = "Cria um novo exercício vinculado a um treino")
    public Optional<ExercicioResponseDto> criar(ExercicioRequestDTO dto) {
        Optional<Treino> treinoOptional = treinoRepository.findById(dto.treinoId());

        if (treinoOptional.isEmpty()) {
            return Optional.empty();
        }

        Exercicio exercicio = exercicioMapper.toEntity(dto, treinoOptional.get());
        Exercicio exercicioSalvo = exercicioRepository.save(exercicio);
        return Optional.of(exercicioMapper.toResponseDTO(exercicioSalvo));
    }

    @Operation(summary = "Buscar exercício por ID", description = "Retorna um exercício específico pelo ID")
    @Transactional(readOnly = true)
    public Optional<ExercicioResponseDto> buscarPorId(Long id) {
        return exercicioRepository.findById(id)
                .map(exercicioMapper::toResponseDTO);
    }

    @Operation(summary = "Listar todos os exercícios", description = "Retorna lista com todos os exercícios cadastrados")
    @Transactional(readOnly = true)
    public List<ExercicioResponseDto> listarTodos() {
        return exercicioRepository.findAll().stream()
                .map(exercicioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Listar exercícios de um treino", description = "Retorna todos os exercícios de um treino específico")
    @Transactional(readOnly = true)
    public Optional<List<ExercicioResponseDto>> listarPorTreino(Long treinoId) {
        if (!treinoRepository.existsById(treinoId)) {
            return Optional.empty();
        }

        List<ExercicioResponseDto> exercicios = exercicioRepository.findByTreinoId(treinoId).stream()
                .map(exercicioMapper::toResponseDTO)
                .collect(Collectors.toList());

        return Optional.of(exercicios);
    }

    @Operation(summary = "Atualizar exercício", description = "Atualiza dados de um exercício existente")
    public Optional<ExercicioResponseDto> atualizar(Long id, ExercicioRequestDTO dto) {
        Optional<Exercicio> exercicioOptional = exercicioRepository.findById(id);

        if (exercicioOptional.isEmpty()) {
            return Optional.empty();
        }

        Exercicio exercicio = exercicioOptional.get();
        exercicioMapper.updateEntityFromDTO(dto, exercicio);
        Exercicio exercicioAtualizado = exercicioRepository.save(exercicio);
        return Optional.of(exercicioMapper.toResponseDTO(exercicioAtualizado));
    }

    @Operation(summary = "Deletar exercício", description = "Remove um exercício do sistema")
    public boolean deletar(Long id) {
        if (!exercicioRepository.existsById(id)) {
            return false;
        }
        exercicioRepository.deleteById(id);
        return true;
    }
}
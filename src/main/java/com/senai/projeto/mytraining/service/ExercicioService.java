package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDTO;
import com.senai.projeto.mytraining.mapper.ExercicioMapper;
import com.senai.projeto.mytraining.model.Exercicio;
import com.senai.projeto.mytraining.model.Treino;
import com.senai.projeto.mytraining.repository.ExercicioRepository;
import com.senai.projeto.mytraining.repository.TreinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;
    private final TreinoRepository treinoRepository;
    private final ExercicioMapper exercicioMapper;

    public Optional<ExercicioResponseDTO> criar(ExercicioRequestDTO dto) {
        Optional<Treino> treinoOptional = treinoRepository.findById(dto.treinoId());

        if (treinoOptional.isEmpty()) {
            return Optional.empty();
        }

        Exercicio exercicio = exercicioMapper.toEntity(dto, treinoOptional.get());
        Exercicio exercicioSalvo = exercicioRepository.save(exercicio);
        return Optional.of(exercicioMapper.toResponseDTO(exercicioSalvo));
    }

    @Transactional(readOnly = true)
    public Optional<ExercicioResponseDTO> buscarPorId(Long id) {
        return exercicioRepository.findById(id)
                .map(exercicioMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ExercicioResponseDTO> listarTodos() {
        return exercicioRepository.findAll().stream()
                .map(exercicioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<List<ExercicioResponseDTO>> listarPorTreino(Long treinoId) {
        if (!treinoRepository.existsById(treinoId)) {
            return Optional.empty();
        }

        List<ExercicioResponseDTO> exercicios = exercicioRepository.findByTreinoId(treinoId).stream()
                .map(exercicioMapper::toResponseDTO)
                .collect(Collectors.toList());

        return Optional.of(exercicios);
    }

    public Optional<ExercicioResponseDTO> atualizar(Long id, ExercicioRequestDTO dto) {
        Optional<Exercicio> exercicioOptional = exercicioRepository.findById(id);

        if (exercicioOptional.isEmpty()) {
            return Optional.empty();
        }

        Exercicio exercicio = exercicioOptional.get();
        exercicioMapper.updateEntityFromDTO(dto, exercicio);
        Exercicio exercicioAtualizado = exercicioRepository.save(exercicio);
        return Optional.of(exercicioMapper.toResponseDTO(exercicioAtualizado));
    }

    public boolean deletar(Long id) {
        if (!exercicioRepository.existsById(id)) {
            return false;
        }
        exercicioRepository.deleteById(id);
        return true;
    }
}
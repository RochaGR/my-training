package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.TreinoRequestDTO;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import com.senai.projeto.mytraining.mapper.TreinoMapper;
import com.senai.projeto.mytraining.model.Treino;
import com.senai.projeto.mytraining.model.Usuario;
import com.senai.projeto.mytraining.repository.TreinoRepository;
import com.senai.projeto.mytraining.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "TreinoService", description = "Serviço de gerenciamento de treinos")
public class TreinoService {

    private final TreinoRepository treinoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TreinoMapper treinoMapper;

    @Operation(summary = "Criar treino por usuário ID", description = "Cria novo treino vinculado a um usuário específico")
    public Optional<TreinoResponseDTO> criar(TreinoRequestDTO dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.usuarioId());

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Treino treino = treinoMapper.toEntity(dto, usuarioOptional.get());
        Treino treinoSalvo = treinoRepository.save(treino);
        return Optional.of(treinoMapper.toResponseDTO(treinoSalvo));
    }

    @Operation(summary = "Criar treino por email", description = "Cria novo treino vinculado ao usuário autenticado (via email)")
    public Optional<TreinoResponseDTO> criar(TreinoRequestDTO dto, String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        TreinoRequestDTO dtoComUsuario = new TreinoRequestDTO(
                dto.dataHora(),
                dto.tipo(),
                dto.duracaoMin(),
                dto.observacoes(),
                dto.distanciaKm(),
                usuarioOptional.get().getId(),
                dto.exercicios()
        );

        Treino treino = treinoMapper.toEntity(dtoComUsuario, usuarioOptional.get());
        Treino treinoSalvo = treinoRepository.save(treino);
        return Optional.of(treinoMapper.toResponseDTO(treinoSalvo));
    }

    @Operation(summary = "Buscar treino por ID", description = "Retorna um treino específico pelo ID")
    @Transactional(readOnly = true)
    public Optional<TreinoResponseDTO> buscarPorId(Long id) {
        return treinoRepository.findById(id)
                .map(treinoMapper::toResponseDTO);
    }

    @Operation(summary = "Listar todos os treinos", description = "Retorna lista com todos os treinos cadastrados")
    @Transactional(readOnly = true)
    public List<TreinoResponseDTO> listarTodos() {
        return treinoRepository.findAll().stream()
                .map(treinoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Listar treinos com paginação", description = "Retorna treinos paginados e ordenados")
    @Transactional(readOnly = true)
    public Page<TreinoResponseDTO> listarTodosPaginado(Pageable pageable) {
        return treinoRepository.findAll(pageable)
                .map(treinoMapper::toResponseDTO);
    }

    @Operation(summary = "Listar treinos de um usuário", description = "Retorna todos os treinos de um usuário específico pelo ID")
    @Transactional(readOnly = true)
    public Optional<List<TreinoResponseDTO>> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            return Optional.empty();
        }

        List<TreinoResponseDTO> treinos = treinoRepository.findByUsuarioId(usuarioId).stream()
                .map(treinoMapper::toResponseDTO)
                .collect(Collectors.toList());

        return Optional.of(treinos);
    }

    @Operation(summary = "Listar meus treinos", description = "Retorna todos os treinos do usuário autenticado (via email)")
    @Transactional(readOnly = true)
    public Optional<List<TreinoResponseDTO>> listarPorEmail(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        List<TreinoResponseDTO> treinos = treinoRepository.findByUsuarioId(usuarioOptional.get().getId()).stream()
                .map(treinoMapper::toResponseDTO)
                .collect(Collectors.toList());

        return Optional.of(treinos);
    }

    @Operation(summary = "Atualizar treino", description = "Atualiza dados de um treino existente")
    public Optional<TreinoResponseDTO> atualizar(Long id, TreinoRequestDTO dto) {
        Optional<Treino> treinoOptional = treinoRepository.findById(id);

        if (treinoOptional.isEmpty()) {
            return Optional.empty();
        }

        Treino treino = treinoOptional.get();
        treinoMapper.updateEntityFromDTO(dto, treino);
        Treino treinoAtualizado = treinoRepository.save(treino);
        return Optional.of(treinoMapper.toResponseDTO(treinoAtualizado));
    }

    @Operation(summary = "Deletar treino", description = "Remove um treino do sistema")
    public boolean deletar(Long id) {
        if (!treinoRepository.existsById(id)) {
            return false;
        }
        treinoRepository.deleteById(id);
        return true;
    }
}
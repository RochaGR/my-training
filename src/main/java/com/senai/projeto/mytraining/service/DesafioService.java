package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.mapper.DesafioMapper;
import com.senai.projeto.mytraining.model.Desafio;
import com.senai.projeto.mytraining.model.Usuario;
import com.senai.projeto.mytraining.repository.DesafioRepository;
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
@Tag(name = "DesafioService", description = "Serviço de gerenciamento de desafios")
public class DesafioService {

    private final DesafioRepository desafioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DesafioMapper desafioMapper;

    @Operation(summary = "Criar desafio sem associação", description = "Cria um desafio genérico sem usuário")
    public DesafioResponseDTO criar(DesafioRequestDTO dto) {
        Desafio desafio = desafioMapper.toEntity(dto);
        Desafio desafioSalvo = desafioRepository.save(desafio);
        return desafioMapper.toResponseDTO(desafioSalvo);
    }

    @Operation(summary = "Criar desafio para usuário", description = "Cria um novo desafio vinculado a um usuário específico")
    public Optional<DesafioResponseDTO> criar(DesafioRequestDTO dto, String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Desafio desafio = desafioMapper.toEntity(dto, usuarioOptional.get());
        Desafio desafioSalvo = desafioRepository.save(desafio);
        return Optional.of(desafioMapper.toResponseDTO(desafioSalvo));
    }

    @Operation(summary = "Buscar desafio por ID", description = "Retorna um desafio específico pelo ID")
    @Transactional(readOnly = true)
    public Optional<DesafioResponseDTO> buscarPorId(Long id) {
        return desafioRepository.findById(id)
                .map(desafioMapper::toResponseDTO);
    }

    @Operation(summary = "Listar todos os desafios", description = "Retorna lista com todos os desafios cadastrados")
    @Transactional(readOnly = true)
    public List<DesafioResponseDTO> listarTodos() {
        return desafioRepository.findAll().stream()
                .map(desafioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Listar desafios de um usuário", description = "Retorna todos os desafios de um usuário específico (via email)")
    @Transactional(readOnly = true)
    public Optional<List<DesafioResponseDTO>> listarPorEmail(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        List<DesafioResponseDTO> desafios = desafioRepository.findByUsuarioId(usuarioOptional.get().getId()).stream()
                .map(desafioMapper::toResponseDTO)
                .collect(Collectors.toList());

        return Optional.of(desafios);
    }

    @Operation(summary = "Listar desafios com paginação", description = "Retorna desafios paginados e ordenados")
    @Transactional(readOnly = true)
    public Page<DesafioResponseDTO> listarTodosPaginado(Pageable pageable) {
        return desafioRepository.findAll(pageable)
                .map(desafioMapper::toResponseDTO);
    }

    @Operation(summary = "Buscar desafios por status", description = "Retorna desafios filtrados por status (PENDENTE, CONCLUIDO, CANCELADO)")
    @Transactional(readOnly = true)
    public List<DesafioResponseDTO> buscarPorStatus(Desafio.Status status) {
        return desafioRepository.findByStatus(status).stream()
                .map(desafioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Atualizar desafio", description = "Atualiza dados de um desafio e verifica conclusão automática")
    public Optional<DesafioResponseDTO> atualizar(Long id, DesafioRequestDTO dto) {
        Optional<Desafio> desafioOptional = desafioRepository.findById(id);

        if (desafioOptional.isEmpty()) {
            return Optional.empty();
        }

        Desafio desafio = desafioOptional.get();
        desafioMapper.updateEntityFromDTO(dto, desafio);

        // Verificar se o desafio foi concluído automaticamente
        verificarConclusaoAutomatica(desafio);

        Desafio desafioAtualizado = desafioRepository.save(desafio);
        return Optional.of(desafioMapper.toResponseDTO(desafioAtualizado));
    }

    @Operation(summary = "Verificar conclusão automática", description = "Marca desafio como CONCLUIDO quando progresso >= objetivo")
    private void verificarConclusaoAutomatica(Desafio desafio) {
        if (desafio.getProgressoAtual() != null &&
                desafio.getObjetivoValor() != null &&
                desafio.getProgressoAtual() >= desafio.getObjetivoValor() &&
                desafio.getStatus() != Desafio.Status.CONCLUIDO) {
            desafio.setStatus(Desafio.Status.CONCLUIDO);
        }
    }

    @Operation(summary = "Deletar desafio", description = "Remove um desafio do sistema")
    public boolean deletar(Long id) {
        if (!desafioRepository.existsById(id)) {
            return false;
        }
        desafioRepository.deleteById(id);
        return true;
    }
}
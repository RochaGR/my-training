package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.TreinoRequestDTO;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import com.senai.projeto.mytraining.service.TreinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/treinos")
@RequiredArgsConstructor
public class TreinoController {

    private final TreinoService treinoService;

    @PostMapping
    public ResponseEntity<TreinoResponseDTO> criar(
            @Valid @RequestBody TreinoRequestDTO dto,
            Authentication authentication) {
        String email = authentication.getName();
        return treinoService.criar(dto, email)
                .map(treino -> ResponseEntity.status(HttpStatus.CREATED).body(treino))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/meus-treinos")
    public ResponseEntity<List<TreinoResponseDTO>> listarMeusTreinos(Authentication authentication) {
        String email = authentication.getName();
        return treinoService.listarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponseDTO> buscarPorId(@PathVariable Long id) {
        return treinoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TreinoResponseDTO>> listarTodos() {
        List<TreinoResponseDTO> treinos = treinoService.listarTodos();
        return ResponseEntity.ok(treinos);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<TreinoResponseDTO>> listarTodosPaginado(
            @PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TreinoResponseDTO> treinos = treinoService.listarTodosPaginado(pageable);
        return ResponseEntity.ok(treinos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TreinoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return treinoService.listarPorUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreinoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TreinoRequestDTO dto) {
        return treinoService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (treinoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
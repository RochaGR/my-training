package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.model.Desafio;
import com.senai.projeto.mytraining.service.DesafioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desafios")
@RequiredArgsConstructor
public class DesafioController {

    private final DesafioService desafioService;

    @PostMapping
    public ResponseEntity<DesafioResponseDTO> criar(@Valid @RequestBody DesafioRequestDTO dto) {
        DesafioResponseDTO desafio = desafioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(desafio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesafioResponseDTO> buscarPorId(@PathVariable Long id) {
        return desafioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DesafioResponseDTO>> listarTodos() {
        List<DesafioResponseDTO> desafios = desafioService.listarTodos();
        return ResponseEntity.ok(desafios);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<DesafioResponseDTO>> listarTodosPaginado(
            @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DesafioResponseDTO> desafios = desafioService.listarTodosPaginado(pageable);
        return ResponseEntity.ok(desafios);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DesafioResponseDTO>> buscarPorStatus(@PathVariable Desafio.Status status) {
        List<DesafioResponseDTO> desafios = desafioService.buscarPorStatus(status);
        return ResponseEntity.ok(desafios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesafioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DesafioRequestDTO dto) {
        return desafioService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (desafioService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
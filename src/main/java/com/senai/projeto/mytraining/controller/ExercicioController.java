package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import com.senai.projeto.mytraining.service.ExercicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
public class ExercicioController {

    private final ExercicioService exercicioService;

    @PostMapping
    public ResponseEntity<ExercicioResponseDto> criar(@Valid @RequestBody ExercicioRequestDTO dto) {
        return exercicioService.criar(dto)
                .map(exercicio -> ResponseEntity.status(HttpStatus.CREATED).body(exercicio))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExercicioResponseDto> buscarPorId(@PathVariable Long id) {
        return exercicioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ExercicioResponseDto>> listarTodos() {
        List<ExercicioResponseDto> exercicios = exercicioService.listarTodos();
        return ResponseEntity.ok(exercicios);
    }

    @GetMapping("/treino/{treinoId}")
    public ResponseEntity<List<ExercicioResponseDto>> listarPorTreino(@PathVariable Long treinoId) {
        return exercicioService.listarPorTreino(treinoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExercicioResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ExercicioRequestDTO dto) {
        return exercicioService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (exercicioService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
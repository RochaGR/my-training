package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import com.senai.projeto.mytraining.service.ExercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Exercícios", description = "Gerenciamento de exercícios dentro dos treinos")
public class ExercicioController {

    private final ExercicioService exercicioService;

    @PostMapping
    @Operation(summary = "Criar novo exercício", description = "Cria um novo exercício vinculado a um treino")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ExercicioResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ExercicioResponseDto> criar(@Valid @RequestBody ExercicioRequestDTO dto) {
        return exercicioService.criar(dto)
                .map(exercicio -> ResponseEntity.status(HttpStatus.CREATED).body(exercicio))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar exercício por ID", description = "Retorna um exercício específico pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercício encontrado",
                    content = @Content(schema = @Schema(implementation = ExercicioResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ExercicioResponseDto> buscarPorId(
            @Parameter(description = "ID do exercício") @PathVariable Long id) {
        return exercicioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os exercícios", description = "Retorna todos os exercícios cadastrados")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Lista de exercícios retornada")
    public ResponseEntity<List<ExercicioResponseDto>> listarTodos() {
        List<ExercicioResponseDto> exercicios = exercicioService.listarTodos();
        return ResponseEntity.ok(exercicios);
    }

    @GetMapping("/treino/{treinoId}")
    @Operation(summary = "Listar exercícios de um treino", description = "Retorna todos os exercícios de um treino específico")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercícios encontrados"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<ExercicioResponseDto>> listarPorTreino(
            @Parameter(description = "ID do treino") @PathVariable Long treinoId) {
        return exercicioService.listarPorTreino(treinoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar exercício", description = "Atualiza um exercício existente")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ExercicioResponseDto> atualizar(
            @Parameter(description = "ID do exercício") @PathVariable Long id,
            @Valid @RequestBody ExercicioRequestDTO dto) {
        return exercicioService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar exercício", description = "Remove um exercício do sistema")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exercício deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do exercício") @PathVariable Long id) {
        if (exercicioService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
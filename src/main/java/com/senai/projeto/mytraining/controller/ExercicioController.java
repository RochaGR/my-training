package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import com.senai.projeto.mytraining.hateoas.ExercicioModelAssembler;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Exercícios", description = "Gerenciamento de exercícios dentro dos treinos com HATEOAS")
public class ExercicioController {

    private final ExercicioService exercicioService;
    private final ExercicioModelAssembler assembler;

    @PostMapping
    @Operation(summary = "Criar novo exercício", description = "Cria um novo exercício vinculado a um treino")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<ExercicioResponseDto>> criar(@Valid @RequestBody ExercicioRequestDTO dto) {
        return exercicioService.criar(dto)
                .map(exercicio -> {
                    EntityModel<ExercicioResponseDto> model = assembler.toModel(exercicio);
                    return ResponseEntity.status(HttpStatus.CREATED).body(model);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar exercício por ID", description = "Retorna um exercício específico pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercício encontrado"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<ExercicioResponseDto>> buscarPorId(
            @Parameter(description = "ID do exercício") @PathVariable Long id) {
        return exercicioService.buscarPorId(id)
                .map(exercicio -> ResponseEntity.ok(assembler.toModel(exercicio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os exercícios", description = "Retorna todos os exercícios cadastrados")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Lista de exercícios retornada")
    public ResponseEntity<List<EntityModel<ExercicioResponseDto>>> listarTodos() {
        List<ExercicioResponseDto> exercicios = exercicioService.listarTodos();
        List<EntityModel<ExercicioResponseDto>> models = exercicios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @GetMapping("/treino/{treinoId}")
    @Operation(summary = "Listar exercícios de um treino", description = "Retorna todos os exercícios de um treino específico")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercícios encontrados"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<EntityModel<ExercicioResponseDto>>> listarPorTreino(
            @Parameter(description = "ID do treino") @PathVariable Long treinoId) {
        return exercicioService.listarPorTreino(treinoId)
                .map(exercicios -> {
                    List<EntityModel<ExercicioResponseDto>> models = exercicios.stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(models);
                })
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
    public ResponseEntity<EntityModel<ExercicioResponseDto>> atualizar(
            @Parameter(description = "ID do exercício") @PathVariable Long id,
            @Valid @RequestBody ExercicioRequestDTO dto) {
        return exercicioService.atualizar(id, dto)
                .map(exercicio -> ResponseEntity.ok(assembler.toModel(exercicio)))
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

package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.hateoas.DesafioModelAssembler;
import com.senai.projeto.mytraining.model.Desafio;
import com.senai.projeto.mytraining.service.DesafioService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/desafios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Desafios", description = "Gerenciamento de desafios fitness com HATEOAS")
public class DesafioController {

    private final DesafioService desafioService;
    private final DesafioModelAssembler assembler;
    private final PagedResourcesAssembler<DesafioResponseDTO> pagedAssembler;

    @PostMapping
    @Operation(summary = "Criar novo desafio", description = "Cria um novo desafio para o usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Desafio criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<DesafioResponseDTO>> criar(
            @Valid @RequestBody DesafioRequestDTO dto,
            Authentication authentication) {
        String email = authentication.getName();
        return desafioService.criar(dto, email)
                .map(desafio -> {
                    EntityModel<DesafioResponseDTO> model = assembler.toModel(desafio);
                    return ResponseEntity.status(HttpStatus.CREATED).body(model);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/meus-desafios")
    @Operation(summary = "Listar meus desafios", description = "Retorna todos os desafios do usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de desafios retornada"),
            @ApiResponse(responseCode = "404", description = "Nenhum desafio encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<EntityModel<DesafioResponseDTO>>> listarMeusDesafios(Authentication authentication) {
        String email = authentication.getName();
        return desafioService.listarPorEmail(email)
                .map(desafios -> {
                    List<EntityModel<DesafioResponseDTO>> models = desafios.stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(models);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar desafio por ID", description = "Retorna um desafio específico pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desafio encontrado"),
            @ApiResponse(responseCode = "404", description = "Desafio não encontrado")
    })
    public ResponseEntity<EntityModel<DesafioResponseDTO>> buscarPorId(
            @Parameter(description = "ID do desafio") @PathVariable Long id) {
        return desafioService.buscarPorId(id)
                .map(desafio -> ResponseEntity.ok(assembler.toModel(desafio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os desafios", description = "Retorna todos os desafios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de desafios retornada")
    public ResponseEntity<List<EntityModel<DesafioResponseDTO>>> listarTodos() {
        List<DesafioResponseDTO> desafios = desafioService.listarTodos();
        List<EntityModel<DesafioResponseDTO>> models = desafios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar desafios com paginação", description = "Retorna desafios paginados ordenados por data de início")
    @ApiResponse(responseCode = "200", description = "Página de desafios retornada")
    public ResponseEntity<PagedModel<EntityModel<DesafioResponseDTO>>> listarTodosPaginado(
            @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DesafioResponseDTO> desafios = desafioService.listarTodosPaginado(pageable);
        PagedModel<EntityModel<DesafioResponseDTO>> pagedModel = pagedAssembler.toModel(desafios, assembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar desafios por status", description = "Retorna desafios filtrados pelo status")
    @ApiResponse(responseCode = "200", description = "Desafios encontrados")
    public ResponseEntity<List<EntityModel<DesafioResponseDTO>>> buscarPorStatus(
            @Parameter(description = "Status do desafio") @PathVariable Desafio.Status status) {
        List<DesafioResponseDTO> desafios = desafioService.buscarPorStatus(status);
        List<EntityModel<DesafioResponseDTO>> models = desafios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar desafio", description = "Atualiza um desafio existente")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desafio atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Desafio não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<DesafioResponseDTO>> atualizar(
            @Parameter(description = "ID do desafio") @PathVariable Long id,
            @Valid @RequestBody DesafioRequestDTO dto) {
        return desafioService.atualizar(id, dto)
                .map(desafio -> ResponseEntity.ok(assembler.toModel(desafio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar desafio", description = "Remove um desafio do sistema")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Desafio deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Desafio não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do desafio") @PathVariable Long id) {
        if (desafioService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
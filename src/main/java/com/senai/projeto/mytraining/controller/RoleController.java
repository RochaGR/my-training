package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
import com.senai.projeto.mytraining.hateoas.RoleModelAssembler;
import com.senai.projeto.mytraining.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Roles", description = "Gerenciamento de funções/permissões de usuários com HATEOAS")
public class RoleController {

    private final RoleService roleService;
    private final RoleModelAssembler assembler;

    @PostMapping
    @Operation(summary = "Criar nova role", description = "Cria uma nova função/permissão no sistema")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<RoleResponseDTO>> criar(@Valid @RequestBody RoleRequestDTO dto) {
        RoleResponseDTO role = roleService.criar(dto);
        EntityModel<RoleResponseDTO> model = assembler.toModel(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar role por ID", description = "Retorna uma role específica pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role encontrada"),
            @ApiResponse(responseCode = "404", description = "Role não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<RoleResponseDTO>> buscarPorId(
            @Parameter(description = "ID da role") @PathVariable Long id) {
        return roleService.buscarPorId(id)
                .map(role -> ResponseEntity.ok(assembler.toModel(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar role por nome", description = "Retorna uma role específica pelo seu nome")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role encontrada"),
            @ApiResponse(responseCode = "404", description = "Role não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<RoleResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome da role") @PathVariable String nome) {
        return roleService.buscarPorNome(nome)
                .map(role -> ResponseEntity.ok(assembler.toModel(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todas as roles", description = "Retorna todas as funções/permissões cadastradas")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Lista de roles retornada")
    public ResponseEntity<List<EntityModel<RoleResponseDTO>>> listarTodas() {
        List<RoleResponseDTO> roles = roleService.listarTodas();
        List<EntityModel<RoleResponseDTO>> models = roles.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar role", description = "Atualiza uma role existente")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Role não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<EntityModel<RoleResponseDTO>> atualizar(
            @Parameter(description = "ID da role") @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO dto) {
        return roleService.atualizar(id, dto)
                .map(role -> ResponseEntity.ok(assembler.toModel(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar role", description = "Remove uma role do sistema")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Role não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da role") @PathVariable Long id) {
        if (roleService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

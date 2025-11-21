package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Roles", description = "Gerenciamento de funções/permissões de usuários")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @Operation(summary = "Criar nova role", description = "Cria uma nova função/permissão no sistema")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role criada com sucesso",
                    content = @Content(schema = @Schema(implementation = RoleResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<RoleResponseDTO> criar(@Valid @RequestBody RoleRequestDTO dto) {
        RoleResponseDTO role = roleService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar role por ID", description = "Retorna uma role específica pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role encontrada"),
            @ApiResponse(responseCode = "404", description = "Role não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<RoleResponseDTO> buscarPorId(
            @Parameter(description = "ID da role") @PathVariable Long id) {
        return roleService.buscarPorId(id)
                .map(ResponseEntity::ok)
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
    public ResponseEntity<RoleResponseDTO> buscarPorNome(
            @Parameter(description = "Nome da role (ex: ROLE_ADMIN)") @PathVariable String nome) {
        return roleService.buscarPorNome(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todas as roles", description = "Retorna todas as funções/permissões cadastradas")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Lista de roles retornada")
    public ResponseEntity<List<RoleResponseDTO>> listarTodas() {
        List<RoleResponseDTO> roles = roleService.listarTodas();
        return ResponseEntity.ok(roles);
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
    public ResponseEntity<RoleResponseDTO> atualizar(
            @Parameter(description = "ID da role") @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO dto) {
        return roleService.atualizar(id, dto)
                .map(ResponseEntity::ok)
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
package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desafios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Desafios", description = "Gerenciamento de desafios fitness")
public class DesafioController {

    private final DesafioService desafioService;

    @PostMapping
    @Operation(summary = "Criar novo desafio", description = "Cria um novo desafio para o usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Desafio criado com sucesso",
                    content = @Content(schema = @Schema(implementation = DesafioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<DesafioResponseDTO> criar(
            @Valid @RequestBody DesafioRequestDTO dto,
            Authentication authentication) {
        String email = authentication.getName();
        return desafioService.criar(dto, email)
                .map(desafio -> ResponseEntity.status(HttpStatus.CREATED).body(desafio))
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
    public ResponseEntity<List<DesafioResponseDTO>> listarMeusDesafios(Authentication authentication) {
        String email = authentication.getName();
        return desafioService.listarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar desafio por ID", description = "Retorna um desafio específico pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desafio encontrado",
                    content = @Content(schema = @Schema(implementation = DesafioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Desafio não encontrado")
    })
    public ResponseEntity<DesafioResponseDTO> buscarPorId(
            @Parameter(description = "ID do desafio") @PathVariable Long id) {
        return desafioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os desafios", description = "Retorna todos os desafios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de desafios retornada")
    public ResponseEntity<List<DesafioResponseDTO>> listarTodos() {
        List<DesafioResponseDTO> desafios = desafioService.listarTodos();
        return ResponseEntity.ok(desafios);
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar desafios com paginação", description = "Retorna desafios paginados ordenados por data de início")
    @ApiResponse(responseCode = "200", description = "Página de desafios retornada")
    public ResponseEntity<Page<DesafioResponseDTO>> listarTodosPaginado(
            @PageableDefault(size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DesafioResponseDTO> desafios = desafioService.listarTodosPaginado(pageable);
        return ResponseEntity.ok(desafios);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar desafios por status", description = "Retorna desafios filtrados pelo status (PENDENTE, CONCLUIDO, CANCELADO)")
    @ApiResponse(responseCode = "200", description = "Desafios encontrados")
    public ResponseEntity<List<DesafioResponseDTO>> buscarPorStatus(
            @Parameter(description = "Status do desafio", schema = @Schema(implementation = Desafio.Status.class))
            @PathVariable Desafio.Status status) {
        List<DesafioResponseDTO> desafios = desafioService.buscarPorStatus(status);
        return ResponseEntity.ok(desafios);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar desafio", description = "Atualiza um desafio existente e verifica conclusão automática")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desafio atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Desafio não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<DesafioResponseDTO> atualizar(
            @Parameter(description = "ID do desafio") @PathVariable Long id,
            @Valid @RequestBody DesafioRequestDTO dto) {
        return desafioService.atualizar(id, dto)
                .map(ResponseEntity::ok)
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
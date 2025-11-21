package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.dto.request.TreinoRequestDTO;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import com.senai.projeto.mytraining.service.TreinoService;
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
@RequestMapping("/api/treinos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Treinos", description = "Gerenciamento de treinos e exercícios")
public class TreinoController {

    private final TreinoService treinoService;

    @PostMapping
    @Operation(summary = "Criar novo treino", description = "Cria um novo treino para o usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Treino criado com sucesso",
                    content = @Content(schema = @Schema(implementation = TreinoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<TreinoResponseDTO> criar(
            @Valid @RequestBody TreinoRequestDTO dto,
            Authentication authentication) {
        String email = authentication.getName();
        return treinoService.criar(dto, email)
                .map(treino -> ResponseEntity.status(HttpStatus.CREATED).body(treino))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/meus-treinos")
    @Operation(summary = "Listar meus treinos", description = "Retorna todos os treinos do usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de treinos retornada"),
            @ApiResponse(responseCode = "404", description = "Nenhum treino encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<TreinoResponseDTO>> listarMeusTreinos(Authentication authentication) {
        String email = authentication.getName();
        return treinoService.listarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar treino por ID", description = "Retorna um treino específico pelo seu ID")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Treino encontrado",
                    content = @Content(schema = @Schema(implementation = TreinoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<TreinoResponseDTO> buscarPorId(
            @Parameter(description = "ID do treino") @PathVariable Long id) {
        return treinoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os treinos", description = "Retorna todos os treinos cadastrados")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Lista de treinos retornada")
    public ResponseEntity<List<TreinoResponseDTO>> listarTodos() {
        List<TreinoResponseDTO> treinos = treinoService.listarTodos();
        return ResponseEntity.ok(treinos);
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar treinos com paginação", description = "Retorna treinos paginados ordenados por data")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Página de treinos retornada")
    public ResponseEntity<Page<TreinoResponseDTO>> listarTodosPaginado(
            @PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TreinoResponseDTO> treinos = treinoService.listarTodosPaginado(pageable);
        return ResponseEntity.ok(treinos);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar treinos de um usuário", description = "Retorna todos os treinos de um usuário específico")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Treinos encontrados"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<TreinoResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return treinoService.listarPorUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar treino", description = "Atualiza um treino existente")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Treino atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<TreinoResponseDTO> atualizar(
            @Parameter(description = "ID do treino") @PathVariable Long id,
            @Valid @RequestBody TreinoRequestDTO dto) {
        return treinoService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar treino", description = "Remove um treino do sistema")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Treino deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do treino") @PathVariable Long id) {
        if (treinoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
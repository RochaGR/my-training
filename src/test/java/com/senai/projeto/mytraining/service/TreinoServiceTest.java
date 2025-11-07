package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.TreinoRequestDTO;
import com.senai.projeto.mytraining.dto.response.TreinoResponseDTO;
import com.senai.projeto.mytraining.model.TipoTreino;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TreinoServiceTest {

    @Autowired
    private TreinoService treinoService;

    @Test
    void deveCriarTreino() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CORRIDA,
                40,
                "Treino intervalado",
                7.0,
                2L,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.criar(requestDTO);

        assertTrue(response.isPresent());
        assertNotNull(response.get().id());
        assertEquals(TipoTreino.CORRIDA, response.get().tipo());
        assertEquals(40, response.get().duracaoMin());
        assertEquals(7.0, response.get().distanciaKm());
    }

    @Test
    void deveRetornarVazioAoCriarTreinoComUsuarioInexistente() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CORRIDA,
                30,
                "Teste",
                5.0,
                999L,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.criar(requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveCriarTreinoComEmail() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.MUSCULACAO,
                60,
                "Treino de costas",
                null,
                null,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.criar(requestDTO, "user@test.com");

        assertTrue(response.isPresent());
        assertNotNull(response.get().id());
        assertEquals(TipoTreino.MUSCULACAO, response.get().tipo());
    }

    @Test
    void deveRetornarVazioAoCriarTreinoComEmailInexistente() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CORRIDA,
                30,
                "Teste",
                5.0,
                null,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.criar(requestDTO, "inexistente@test.com");

        assertFalse(response.isPresent());
    }

    @Test
    void deveListarTodosTreinos() {
        List<TreinoResponseDTO> treinos = treinoService.listarTodos();

        assertNotNull(treinos);
        assertTrue(treinos.size() >= 8);
    }

    @Test
    void deveBuscarTreinoPorId() {
        Optional<TreinoResponseDTO> response = treinoService.buscarPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals(TipoTreino.CORRIDA, response.get().tipo());
        assertEquals(30, response.get().duracaoMin());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        Optional<TreinoResponseDTO> response = treinoService.buscarPorId(999L);

        assertFalse(response.isPresent());
    }

    @Test
    void deveListarTreinosPaginado() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<TreinoResponseDTO> page = treinoService.listarTodosPaginado(pageable);

        assertNotNull(page);
        assertEquals(5, page.getSize());
        assertTrue(page.getTotalElements() >= 8);
    }

    @Test
    void deveListarTreinosPorUsuario() {
        Optional<List<TreinoResponseDTO>> treinos = treinoService.listarPorUsuario(2L);

        assertTrue(treinos.isPresent());
        assertFalse(treinos.get().isEmpty());
        treinos.get().forEach(treino -> assertEquals(2L, treino.usuarioId()));
    }

    @Test
    void deveRetornarVazioQuandoUsuarioNaoExistir() {
        Optional<List<TreinoResponseDTO>> treinos = treinoService.listarPorUsuario(999L);

        assertFalse(treinos.isPresent());
    }

    @Test
    void deveListarTreinosPorEmail() {
        Optional<List<TreinoResponseDTO>> treinos = treinoService.listarPorEmail("user@test.com");

        assertTrue(treinos.isPresent());
        assertFalse(treinos.get().isEmpty());
        assertEquals(3, treinos.get().size());
    }

    @Test
    void deveRetornarVazioQuandoEmailNaoExistir() {
        Optional<List<TreinoResponseDTO>> treinos = treinoService.listarPorEmail("inexistente@test.com");

        assertFalse(treinos.isPresent());
    }

    @Test
    void deveAtualizarTreino() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CORRIDA,
                45,
                "Treino atualizado",
                10.0,
                2L,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.atualizar(1L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals(45, response.get().duracaoMin());
        assertEquals("Treino atualizado", response.get().observacoes());
        assertEquals(10.0, response.get().distanciaKm());
    }

    @Test
    void deveRetornarVazioAoAtualizarTreinoInexistente() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CORRIDA,
                30,
                "Teste",
                5.0,
                2L,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.atualizar(999L, requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveDeletarTreino() {
        // Criar um treino para deletar
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CORRIDA,
                30,
                "Para deletar",
                5.0,
                2L,
                new HashSet<>()
        );
        Optional<TreinoResponseDTO> criado = treinoService.criar(requestDTO);
        assertTrue(criado.isPresent());

        // Deletar o treino criado
        boolean deletado = treinoService.deletar(criado.get().id());

        assertTrue(deletado);

        // Verificar que não existe mais
        Optional<TreinoResponseDTO> busca = treinoService.buscarPorId(criado.get().id());
        assertFalse(busca.isPresent());
    }

    @Test
    void deveRetornarFalseAoDeletarTreinoInexistente() {
        boolean deletado = treinoService.deletar(999L);

        assertFalse(deletado);
    }

    @Test
    void deveCriarTreinoDeCiclismo() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.CICLISMO,
                90,
                "Treino de resistência",
                30.0,
                2L,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.criar(requestDTO);

        assertTrue(response.isPresent());
        assertEquals(TipoTreino.CICLISMO, response.get().tipo());
        assertEquals(30.0, response.get().distanciaKm());
    }

    @Test
    void deveCriarTreinoDeMusculacao() {
        TreinoRequestDTO requestDTO = new TreinoRequestDTO(
                LocalDateTime.now(),
                TipoTreino.MUSCULACAO,
                70,
                "Treino completo",
                null,
                3L,
                new HashSet<>()
        );

        Optional<TreinoResponseDTO> response = treinoService.criar(requestDTO);

        assertTrue(response.isPresent());
        assertEquals(TipoTreino.MUSCULACAO, response.get().tipo());
        assertNull(response.get().distanciaKm());
    }
}
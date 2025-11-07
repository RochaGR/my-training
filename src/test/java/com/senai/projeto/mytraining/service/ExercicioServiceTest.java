package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
import com.senai.projeto.mytraining.dto.response.ExercicioResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ExercicioServiceTest {

    @Autowired
    private ExercicioService exercicioService;

    @Test
    void deveCriarExercicio() {
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Rosca Direta",
                3,
                12,
                20.0,
                "Controlar o movimento",
                2L // Treino de musculação
        );

        Optional<ExercicioResponseDto> response = exercicioService.criar(requestDTO);

        assertTrue(response.isPresent());
        assertNotNull(response.get().id());
        assertEquals("Rosca Direta", response.get().nome());
        assertEquals(3, response.get().series());
        assertEquals(12, response.get().repeticoes());
        assertEquals(20.0, response.get().cargaKg());
    }

    @Test
    void deveRetornarVazioAoCriarExercicioComTreinoInexistente() {
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Teste",
                3,
                10,
                50.0,
                null,
                999L
        );

        Optional<ExercicioResponseDto> response = exercicioService.criar(requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveListarTodosExercicios() {
        List<ExercicioResponseDto> exercicios = exercicioService.listarTodos();

        assertNotNull(exercicios);
        assertTrue(exercicios.size() >= 6);
    }

    @Test
    void deveBuscarExercicioPorId() {
        Optional<ExercicioResponseDto> response = exercicioService.buscarPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals("Agachamento", response.get().nome());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        Optional<ExercicioResponseDto> response = exercicioService.buscarPorId(999L);

        assertFalse(response.isPresent());
    }

    @Test
    void deveListarExerciciosPorTreino() {
        Optional<List<ExercicioResponseDto>> exercicios = exercicioService.listarPorTreino(2L);

        assertTrue(exercicios.isPresent());
        assertEquals(3, exercicios.get().size());
        exercicios.get().forEach(ex -> assertEquals(2L, ex.aLong()));
    }

    @Test
    void deveRetornarVazioQuandoTreinoNaoExistir() {
        Optional<List<ExercicioResponseDto>> exercicios = exercicioService.listarPorTreino(999L);

        assertFalse(exercicios.isPresent());
    }

    @Test
    void deveAtualizarExercicio() {
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Agachamento Livre",
                5,
                10,
                100.0,
                "Observações atualizadas",
                2L
        );

        Optional<ExercicioResponseDto> response = exercicioService.atualizar(1L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals("Agachamento Livre", response.get().nome());
        assertEquals(5, response.get().series());
        assertEquals(100.0, response.get().cargaKg());
    }

    @Test
    void deveRetornarVazioAoAtualizarExercicioInexistente() {
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Teste",
                3,
                10,
                50.0,
                null,
                2L
        );

        Optional<ExercicioResponseDto> response = exercicioService.atualizar(999L, requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveDeletarExercicio() {
        // Criar um exercício para deletar
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Para Deletar",
                3,
                10,
                30.0,
                null,
                2L
        );
        Optional<ExercicioResponseDto> criado = exercicioService.criar(requestDTO);
        assertTrue(criado.isPresent());

        // Deletar o exercício criado
        boolean deletado = exercicioService.deletar(criado.get().id());

        assertTrue(deletado);

        // Verificar que não existe mais
        Optional<ExercicioResponseDto> busca = exercicioService.buscarPorId(criado.get().id());
        assertFalse(busca.isPresent());
    }

    @Test
    void deveRetornarFalseAoDeletarExercicioInexistente() {
        boolean deletado = exercicioService.deletar(999L);

        assertFalse(deletado);
    }

    @Test
    void deveCriarExercicioSemCarga() {
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Flexão",
                4,
                15,
                null,
                "Peso corporal",
                2L
        );

        Optional<ExercicioResponseDto> response = exercicioService.criar(requestDTO);

        assertTrue(response.isPresent());
        assertNull(response.get().cargaKg());
    }

    @Test
    void deveCriarExercicioSemObservacoes() {
        ExercicioRequestDTO requestDTO = new ExercicioRequestDTO(
                "Barra Fixa",
                3,
                8,
                null,
                null,
                5L
        );

        Optional<ExercicioResponseDto> response = exercicioService.criar(requestDTO);

        assertTrue(response.isPresent());
        assertNull(response.get().observacoes());
    }
}
package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.model.Desafio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DesafioServiceTest {

    @Autowired
    private DesafioService desafioService;

    @Test
    void deveCriarDesafio() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Desafio 50km",
                "Correr 50km em fevereiro",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                50.0,
                0.0,
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.criar(requestDTO, "user@test.com");

        assertTrue(response.isPresent());
        assertNotNull(response.get().id());
        assertEquals("Desafio 50km", response.get().titulo());
        assertEquals(50.0, response.get().objetivoValor());
        assertEquals(Desafio.Status.PENDENTE, response.get().status());
    }

    @Test
    void deveRetornarVazioAoCriarDesafioComEmailInexistente() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Desafio Teste",
                "Descrição teste",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                30.0,
                0.0,
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.criar(requestDTO, "inexistente@test.com");

        assertFalse(response.isPresent());
    }

    @Test
    void deveListarTodosDesafios() {
        List<DesafioResponseDTO> desafios = desafioService.listarTodos();

        assertNotNull(desafios);
        assertTrue(desafios.size() >= 5);
    }

    @Test
    void deveBuscarDesafioPorId() {
        Optional<DesafioResponseDTO> response = desafioService.buscarPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals("Desafio 100km", response.get().titulo());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        Optional<DesafioResponseDTO> response = desafioService.buscarPorId(999L);

        assertFalse(response.isPresent());
    }

    @Test
    void deveListarDesafiosPorEmail() {
        Optional<List<DesafioResponseDTO>> desafios = desafioService.listarPorEmail("user@test.com");

        assertTrue(desafios.isPresent());
        assertFalse(desafios.get().isEmpty());
        assertEquals(2, desafios.get().size());
    }

    @Test
    void deveRetornarVazioQuandoEmailNaoExistir() {
        Optional<List<DesafioResponseDTO>> desafios = desafioService.listarPorEmail("inexistente@test.com");

        assertFalse(desafios.isPresent());
    }

    @Test
    void deveListarDesafiosPaginado() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<DesafioResponseDTO> page = desafioService.listarTodosPaginado(pageable);

        assertNotNull(page);
        assertEquals(3, page.getSize());
        assertTrue(page.getTotalElements() >= 5);
    }

    @Test
    void deveBuscarDesafiosPorStatus() {
        List<DesafioResponseDTO> pendentes = desafioService.buscarPorStatus(Desafio.Status.PENDENTE);

        assertNotNull(pendentes);
        assertFalse(pendentes.isEmpty());
        pendentes.forEach(d -> assertEquals(Desafio.Status.PENDENTE, d.status()));
    }

    @Test
    void deveBuscarDesafiosConcluidos() {
        List<DesafioResponseDTO> concluidos = desafioService.buscarPorStatus(Desafio.Status.CONCLUIDO);

        assertNotNull(concluidos);
        assertFalse(concluidos.isEmpty());
        concluidos.forEach(d -> assertEquals(Desafio.Status.CONCLUIDO, d.status()));
    }

    @Test
    void deveBuscarDesafiosCancelados() {
        List<DesafioResponseDTO> cancelados = desafioService.buscarPorStatus(Desafio.Status.CANCELADO);

        assertNotNull(cancelados);
        assertFalse(cancelados.isEmpty());
        cancelados.forEach(d -> assertEquals(Desafio.Status.CANCELADO, d.status()));
    }

    @Test
    void deveAtualizarDesafio() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Desafio 100km Atualizado",
                "Descrição atualizada",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                100.0,
                60.0,
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.atualizar(1L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals("Desafio 100km Atualizado", response.get().titulo());
        assertEquals(60.0, response.get().progressoAtual());
    }

    @Test
    void deveMarcarComoConcluidoQuandoAtingirObjetivo() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Desafio 100km",
                "Correr 100km",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                100.0,
                100.0, // Objetivo atingido
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.atualizar(1L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(Desafio.Status.CONCLUIDO, response.get().status());
    }

    @Test
    void deveMarcarComoConcluidoQuandoSuperarObjetivo() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Desafio 100km",
                "Correr 100km",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                100.0,
                120.0, // Superou o objetivo
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.atualizar(1L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(Desafio.Status.CONCLUIDO, response.get().status());
    }

    @Test
    void deveRetornarVazioAoAtualizarDesafioInexistente() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Teste",
                "Teste",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                30.0,
                0.0,
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.atualizar(999L, requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveDeletarDesafio() {
        // Criar um desafio para deletar
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Para Deletar",
                "Teste",
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                20.0,
                0.0,
                Desafio.Unidade.KM,
                Desafio.Status.PENDENTE
        );
        Optional<DesafioResponseDTO> criado = desafioService.criar(requestDTO, "user@test.com");
        assertTrue(criado.isPresent());

        // Deletar o desafio criado
        boolean deletado = desafioService.deletar(criado.get().id());

        assertTrue(deletado);

        // Verificar que não existe mais
        Optional<DesafioResponseDTO> busca = desafioService.buscarPorId(criado.get().id());
        assertFalse(busca.isPresent());
    }

    @Test
    void deveRetornarFalseAoDeletarDesafioInexistente() {
        boolean deletado = desafioService.deletar(999L);

        assertFalse(deletado);
    }

    @Test
    void deveCriarDesafioComDiferentesUnidades() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "Treinar 30 vezes",
                "Treinar 30 vezes no mês",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                30.0,
                0.0,
                Desafio.Unidade.REPETICOES,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.criar(requestDTO, "user@test.com");

        assertTrue(response.isPresent());
        assertEquals(Desafio.Unidade.REPETICOES, response.get().unidade());
    }

    @Test
    void deveCriarDesafioComMinutos() {
        DesafioRequestDTO requestDTO = new DesafioRequestDTO(
                "1000 minutos",
                "Treinar 1000 minutos no mês",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                1000.0,
                0.0,
                Desafio.Unidade.MINUTOS,
                Desafio.Status.PENDENTE
        );

        Optional<DesafioResponseDTO> response = desafioService.criar(requestDTO, "user@test.com");

        assertTrue(response.isPresent());
        assertEquals(Desafio.Unidade.MINUTOS, response.get().unidade());
    }
}
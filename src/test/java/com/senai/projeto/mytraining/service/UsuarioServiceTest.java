package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.UsuarioRequestDTO;
import com.senai.projeto.mytraining.dto.response.UsuarioResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void deveCriarUsuario() {
        Set<Long> roles = new HashSet<>();
        roles.add(2L); // ROLE_USER

        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "Novo Usuario",
                "novo@test.com",
                "senha123",
                roles
        );

        UsuarioResponseDTO response = usuarioService.criar(requestDTO);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Novo Usuario", response.nome());
        assertEquals("novo@test.com", response.email());
        assertFalse(response.roles().isEmpty());
    }

    @Test
    void deveListarTodosUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();

        assertNotNull(usuarios);
        assertTrue(usuarios.size() >= 5);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Optional<UsuarioResponseDTO> response = usuarioService.buscarPorId(2L);

        assertTrue(response.isPresent());
        assertEquals(2L, response.get().id());
        assertEquals("User Test", response.get().nome());
        assertEquals("user@test.com", response.get().email());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        Optional<UsuarioResponseDTO> response = usuarioService.buscarPorId(999L);

        assertFalse(response.isPresent());
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        Optional<UsuarioResponseDTO> response = usuarioService.buscarPorEmail("user@test.com");

        assertTrue(response.isPresent());
        assertEquals("user@test.com", response.get().email());
        assertEquals("User Test", response.get().nome());
    }

    @Test
    void deveRetornarVazioQuandoEmailNaoExistir() {
        Optional<UsuarioResponseDTO> response = usuarioService.buscarPorEmail("inexistente@test.com");

        assertFalse(response.isPresent());
    }

    @Test
    void deveAtualizarUsuario() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "User Test Atualizado",
                "user@test.com",
                null, // Não atualizar senha
                null
        );

        Optional<UsuarioResponseDTO> response = usuarioService.atualizar(2L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(2L, response.get().id());
        assertEquals("User Test Atualizado", response.get().nome());
        assertEquals("user@test.com", response.get().email());
    }

    @Test
    void deveAtualizarUsuarioComNovaSenha() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "User Test",
                "user@test.com",
                "novaSenha123",
                null
        );

        Optional<UsuarioResponseDTO> response = usuarioService.atualizar(2L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(2L, response.get().id());
    }

    @Test
    void deveRetornarVazioAoAtualizarUsuarioInexistente() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "Usuario Teste",
                "teste@test.com",
                "senha123",
                null
        );

        Optional<UsuarioResponseDTO> response = usuarioService.atualizar(999L, requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveDeletarUsuario() {
        // Criar um usuário para deletar
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "Usuario Para Deletar",
                "deletar@test.com",
                "senha123",
                null
        );
        UsuarioResponseDTO criado = usuarioService.criar(requestDTO);

        // Deletar o usuário criado
        boolean deletado = usuarioService.deletar(criado.id());

        assertTrue(deletado);

        // Verificar que não existe mais
        Optional<UsuarioResponseDTO> busca = usuarioService.buscarPorId(criado.id());
        assertFalse(busca.isPresent());
    }

    @Test
    void deveRetornarFalseAoDeletarUsuarioInexistente() {
        boolean deletado = usuarioService.deletar(999L);

        assertFalse(deletado);
    }

    @Test
    void deveCriarUsuarioComMultiplasRoles() {
        Set<Long> roles = new HashSet<>();
        roles.add(1L); // ROLE_ADMIN
        roles.add(2L); // ROLE_USER

        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "Admin Usuario",
                "admin.novo@test.com",
                "senha123",
                roles
        );

        UsuarioResponseDTO response = usuarioService.criar(requestDTO);

        assertNotNull(response);
        assertEquals(2, response.roles().size());
    }

    @Test
    void deveAtualizarRolesDoUsuario() {
        Set<Long> novasRoles = new HashSet<>();
        novasRoles.add(1L); // Adicionar ROLE_ADMIN

        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "User Test",
                "user@test.com",
                null,
                novasRoles
        );

        Optional<UsuarioResponseDTO> response = usuarioService.atualizar(2L, requestDTO);

        assertTrue(response.isPresent());
        assertFalse(response.get().roles().isEmpty());
    }
}
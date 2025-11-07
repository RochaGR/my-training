package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.RoleRequestDTO;
import com.senai.projeto.mytraining.dto.response.RoleResponseDTO;
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
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    void deveCriarRole() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_MANAGER");

        RoleResponseDTO response = roleService.criar(requestDTO);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("ROLE_MANAGER", response.nome());
    }

    @Test
    void deveListarTodasRoles() {
        List<RoleResponseDTO> roles = roleService.listarTodas();

        assertNotNull(roles);
        assertTrue(roles.size() >= 2);
    }

    @Test
    void deveBuscarRolePorId() {
        Optional<RoleResponseDTO> response = roleService.buscarPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals("ROLE_ADMIN", response.get().nome());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        Optional<RoleResponseDTO> response = roleService.buscarPorId(999L);

        assertFalse(response.isPresent());
    }

    @Test
    void deveBuscarRolePorNome() {
        Optional<RoleResponseDTO> response = roleService.buscarPorNome("ROLE_USER");

        assertTrue(response.isPresent());
        assertEquals("ROLE_USER", response.get().nome());
    }

    @Test
    void deveRetornarVazioQuandoNomeNaoExistir() {
        Optional<RoleResponseDTO> response = roleService.buscarPorNome("ROLE_INEXISTENTE");

        assertFalse(response.isPresent());
    }

    @Test
    void deveAtualizarRole() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_SUPER_ADMIN");

        Optional<RoleResponseDTO> response = roleService.atualizar(1L, requestDTO);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().id());
        assertEquals("ROLE_SUPER_ADMIN", response.get().nome());
    }

    @Test
    void deveRetornarVazioAoAtualizarRoleInexistente() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_TESTE");

        Optional<RoleResponseDTO> response = roleService.atualizar(999L, requestDTO);

        assertFalse(response.isPresent());
    }

    @Test
    void deveDeletarRole() {
        // Criar uma role para deletar
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_PARA_DELETAR");
        RoleResponseDTO criada = roleService.criar(requestDTO);

        // Deletar a role criada
        boolean deletado = roleService.deletar(criada.id());

        assertTrue(deletado);

        // Verificar que não existe mais
        Optional<RoleResponseDTO> busca = roleService.buscarPorId(criada.id());
        assertFalse(busca.isPresent());
    }

    @Test
    void deveRetornarFalseAoDeletarRoleInexistente() {
        boolean deletado = roleService.deletar(999L);

        assertFalse(deletado);
    }
}
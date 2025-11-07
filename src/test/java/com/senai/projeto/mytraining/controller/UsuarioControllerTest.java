package com.senai.projeto.mytraining.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.projeto.mytraining.dto.request.UsuarioRequestDTO;
import com.senai.projeto.mytraining.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() {
        UserDetails userDetails = new User(
                "user@test.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        token = jwtUtil.generateToken(userDetails);
    }

    @Test
    void deveCriarUsuario() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Novo Usuario Teste",
                "novo.usuario@test.com",
                "senha123",
                null
        );

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("novo.usuario@test.com"));
    }

    @Test
    void deveRetornar400ComEmailInvalido() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Usuario Teste",
                "email-invalido",
                "senha123",
                null
        );

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ComSenhaCurta() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Usuario Teste",
                "teste@test.com",
                "123", // senha muito curta
                null
        );

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ComNomeVazio() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "", // nome vazio
                "teste@test.com",
                "senha123",
                null
        );

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarUsuariosComToken() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornar403AoListarUsuariosSemToken() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveBuscarUsuarioPorIdComToken() throws Exception {
        mockMvc.perform(get("/api/usuarios/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void deveRetornar403AoBuscarUsuarioPorIdSemToken() throws Exception {
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoExistir() throws Exception {
        mockMvc.perform(get("/api/usuarios/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // Adicione ao UsuarioControllerTest.java

    @Test
    void deveAtualizarUsuario() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Nome Atualizado",
                "user@test.com",
                "novaSenha123",
                null
        );

        mockMvc.perform(put("/api/usuarios/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    void deveRetornar404AoAtualizarUsuarioInexistente() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Nome",
                "teste@test.com",
                "senha123",
                null
        );

        mockMvc.perform(put("/api/usuarios/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoDeletarUsuarioInexistente() throws Exception {
        mockMvc.perform(delete("/api/usuarios/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

}
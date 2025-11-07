package com.senai.projeto.mytraining.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void deveRetornar401ComCredenciaisInvalidas() throws Exception {
        String json = """
            {
                "email": "user@test.com",
                "senha": "senhaErrada"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar401ComEmailInexistente() throws Exception {
        String json = """
            {
                "email": "inexistente@test.com",
                "senha": "password123"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRegistrarNovoUsuario() throws Exception {
        String json = """
            {
                "nome": "Novo Usuario",
                "email": "novo.usuario@test.com",
                "senha": "senha123"
            }
        """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("novo.usuario@test.com"))
                .andExpect(jsonPath("$.roles").isArray());
    }



    @Test
    void deveRetornar400AoLoginSemEmail() throws Exception {
        String json = """
            {
                "senha": "password123"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoLoginSemSenha() throws Exception {
        String json = """
            {
                "email": "user@test.com"
            }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoRegistrarSemNome() throws Exception {
        String json = """
            {
                "email": "teste@test.com",
                "senha": "senha123"
            }
        """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoRegistrarComEmailInvalido() throws Exception {
        String json = """
            {
                "nome": "Usuario Teste",
                "email": "email-invalido",
                "senha": "senha123"
            }
        """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoRegistrarComSenhaCurta() throws Exception {
        String json = """
            {
                "nome": "Usuario Teste",
                "email": "teste@test.com",
                "senha": "123"
            }
        """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveRetornar400AoRegistrarComEmailDuplicado() throws Exception {
        String json = """
        {
            "nome": "Usuario Teste",
            "email": "user@test.com", // Email que já existe
            "senha": "senha123"
        }
    """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ComEmailNulo() throws Exception {
        String json = """
        {
            "nome": "Usuario Teste",
            "senha": "senha123"
        }
    """;

        mockMvc.perform(post("/api/auth/registro")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }


}
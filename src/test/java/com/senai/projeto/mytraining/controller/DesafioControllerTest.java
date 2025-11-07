package com.senai.projeto.mytraining.controller;

import com.senai.projeto.mytraining.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DesafioControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void deveCriarDesafio() throws Exception {
        String dataInicio = LocalDate.now().toString();
        String dataFim = LocalDate.now().plusDays(30).toString();

        String json = String.format("""
            {
                "titulo": "Desafio 50km",
                "descricao": "Correr 50km em um mês",
                "dataInicio": "%s",
                "dataFim": "%s",
                "objetivoValor": 50.0,
                "progressoAtual": 0.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """, dataInicio, dataFim);

        mockMvc.perform(post("/api/desafios")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Desafio 50km"))
                .andExpect(jsonPath("$.objetivoValor").value(50.0))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveRetornar403SemToken() throws Exception {
        String json = """
            {
                "titulo": "Teste",
                "dataInicio": "2024-02-01",
                "dataFim": "2024-02-28",
                "objetivoValor": 30.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """;

        mockMvc.perform(post("/api/desafios")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveListarMeusDesafios() throws Exception {
        mockMvc.perform(get("/api/desafios/meus-desafios")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveRetornar403AoListarMeusDesafiosSemToken() throws Exception {
        mockMvc.perform(get("/api/desafios/meus-desafios"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveBuscarDesafioPorId() throws Exception {
        // Primeiro criar um desafio para buscar
        String dataInicio = LocalDate.now().toString();
        String dataFim = LocalDate.now().plusDays(30).toString();

        String jsonCriar = String.format("""
            {
                "titulo": "Desafio Busca",
                "descricao": "Para teste de busca",
                "dataInicio": "%s",
                "dataFim": "%s",
                "objetivoValor": 25.0,
                "progressoAtual": 0.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """, dataInicio, dataFim);

        String response = mockMvc.perform(post("/api/desafios")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(jsonCriar))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extrair ID da resposta (simplificado)
        Long id = 1L;

        mockMvc.perform(get("/api/desafios/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deveRetornar404QuandoDesafioNaoExistir() throws Exception {
        mockMvc.perform(get("/api/desafios/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosDesafios() throws Exception {
        mockMvc.perform(get("/api/desafios")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveListarDesafiosPaginado() throws Exception {
        mockMvc.perform(get("/api/desafios/paginado")
                        .param("page", "0")
                        .param("size", "3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void deveBuscarDesafiosPorStatus() throws Exception {
        mockMvc.perform(get("/api/desafios/status/PENDENTE")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveAtualizarDesafio() throws Exception {
        // Primeiro criar um desafio
        String dataInicio = LocalDate.now().toString();
        String dataFim = LocalDate.now().plusDays(30).toString();

        String jsonCriar = String.format("""
            {
                "titulo": "Desafio Atualizar",
                "descricao": "Para teste de atualização",
                "dataInicio": "%s",
                "dataFim": "%s",
                "objetivoValor": 30.0,
                "progressoAtual": 0.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """, dataInicio, dataFim);

        mockMvc.perform(post("/api/desafios")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(jsonCriar))
                .andExpect(status().isCreated());

        // Atualizar o desafio
        String jsonAtualizar = """
            {
                "titulo": "Desafio Atualizado",
                "descricao": "Nova descrição",
                "dataInicio": "2024-01-01",
                "dataFim": "2024-01-31",
                "objetivoValor": 35.0,
                "progressoAtual": 15.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """;

        mockMvc.perform(put("/api/desafios/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(jsonAtualizar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Desafio Atualizado"))
                .andExpect(jsonPath("$.progressoAtual").value(15.0));
    }

    @Test
    void deveRetornar404AoAtualizarDesafioInexistente() throws Exception {
        String json = """
            {
                "titulo": "Teste",
                "dataInicio": "2024-02-01",
                "dataFim": "2024-02-28",
                "objetivoValor": 30.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """;

        mockMvc.perform(put("/api/desafios/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarDesafio() throws Exception {
        // Primeiro criar um desafio
        String dataInicio = LocalDate.now().toString();
        String dataFim = LocalDate.now().plusDays(10).toString();

        String jsonCriar = String.format("""
            {
                "titulo": "Para Deletar",
                "dataInicio": "%s",
                "dataFim": "%s",
                "objetivoValor": 20.0,
                "progressoAtual": 0.0,
                "unidade": "KM",
                "status": "PENDENTE"
            }
        """, dataInicio, dataFim);

        mockMvc.perform(post("/api/desafios")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(jsonCriar))
                .andExpect(status().isCreated());

        // Deletar o desafio
        mockMvc.perform(delete("/api/desafios/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoDeletarDesafioInexistente() throws Exception {
        mockMvc.perform(delete("/api/desafios/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // Adicione ao DesafioControllerTest.java

    @Test
    void deveRetornar400AoCriarDesafioComDataInvalida() throws Exception {
        String json = """
        {
            "titulo": "Desafio Teste",
            "dataInicio": "data-invalida",
            "dataFim": "2024-02-28",
            "objetivoValor": 30.0,
            "unidade": "KM",
            "status": "PENDENTE"
        }
    """;

        mockMvc.perform(post("/api/desafios")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
// Substitua/expanda o ExercicioControllerTest.java

package com.senai.projeto.mytraining.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.projeto.mytraining.dto.request.ExercicioRequestDTO;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ExercicioControllerTest {

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
    void deveCriarExercicio() throws Exception {
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                "Supino Reto",
                3,
                12,
                60.0,
                "Observações do exercício",
                1L
        );

        mockMvc.perform(post("/api/exercicios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Supino Reto"))
                .andExpect(jsonPath("$.series").value(3))
                .andExpect(jsonPath("$.repeticoes").value(12));
    }

    @Test
    void deveRetornar403SemToken() throws Exception {
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                "Teste",
                3,
                10,
                50.0,
                null,
                1L
        );

        mockMvc.perform(post("/api/exercicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornar400ComDadosInvalidos() throws Exception {
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                "", // nome vazio
                0,  // séries inválidas
                0,  // repetições inválidas
                -1.0, // carga negativa
                null,
                1L
        );

        mockMvc.perform(post("/api/exercicios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarExercicios() throws Exception {
        mockMvc.perform(get("/api/exercicios")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }



    @Test
    void deveRetornar404QuandoExercicioNaoExistir() throws Exception {
        mockMvc.perform(get("/api/exercicios/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarExercicio() throws Exception {
        // Primeiro criar
        ExercicioRequestDTO dtoCriar = new ExercicioRequestDTO(
                "Exercicio Original",
                3,
                10,
                50.0,
                null,
                1L
        );

        mockMvc.perform(post("/api/exercicios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoCriar)))
                .andExpect(status().isCreated());

        // Atualizar
        ExercicioRequestDTO dtoAtualizar = new ExercicioRequestDTO(
                "Exercicio Atualizado",
                4,
                12,
                60.0,
                "Nova observação",
                1L
        );

        mockMvc.perform(put("/api/exercicios/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAtualizar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Exercicio Atualizado"))
                .andExpect(jsonPath("$.series").value(4))
                .andExpect(jsonPath("$.repeticoes").value(12));
    }

    @Test
    void deveDeletarExercicio() throws Exception {
        // Primeiro criar
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                "Exercicio Deletar",
                3,
                10,
                40.0,
                null,
                1L
        );

        mockMvc.perform(post("/api/exercicios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Deletar
        mockMvc.perform(delete("/api/exercicios/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar400AoCriarExercicioSemTreinoId() throws Exception {
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                "Supino",
                3,
                10,
                60.0,
                null,
                null // treinoId nulo
        );

        mockMvc.perform(post("/api/exercicios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // Adicione ao ExercicioControllerTest.java

    @Test
    void deveRetornar400AoCriarExercicioSemNome() throws Exception {
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                null, // nome nulo
                3,
                10,
                50.0,
                null,
                1L
        );

        mockMvc.perform(post("/api/exercicios")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveRetornar404AoAtualizarExercicioInexistente() throws Exception {
        ExercicioRequestDTO dto = new ExercicioRequestDTO(
                "Teste",
                3,
                10,
                50.0,
                null,
                1L
        );

        mockMvc.perform(put("/api/exercicios/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
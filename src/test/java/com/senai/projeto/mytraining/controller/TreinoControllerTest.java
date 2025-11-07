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

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TreinoControllerTest {

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
    void deveCriarTreino() throws Exception {
        String json = """
            {
                "dataHora": "2024-02-01T07:00:00",
                "tipo": "CORRIDA",
                "duracaoMin": 40,
                "observacoes": "Treino teste",
                "distanciaKm": 7.0,
                "exercicios": []
            }
        """;

        mockMvc.perform(post("/api/treinos")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.tipo").value("CORRIDA"))
                .andExpect(jsonPath("$.duracaoMin").value(40))
                .andExpect(jsonPath("$.distanciaKm").value(7.0));
    }

    @Test
    void deveRetornar401SemToken() throws Exception {
        String json = """
            {
                "dataHora": "2024-02-01T07:00:00",
                "tipo": "CORRIDA",
                "duracaoMin": 30,
                "distanciaKm": 5.0,
                "exercicios": []
            }
        """;

        mockMvc.perform(post("/api/treinos")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveListarMeusTreinos() throws Exception {
        mockMvc.perform(get("/api/treinos/meus-treinos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)));
    }

    @Test
    void deveRetornar401AoListarMeusTreinosSemToken() throws Exception {
        mockMvc.perform(get("/api/treinos/meus-treinos"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveBuscarTreinoPorId() throws Exception {
        mockMvc.perform(get("/api/treinos/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("CORRIDA"))
                .andExpect(jsonPath("$.duracaoMin").value(30));
    }

    @Test
    void deveRetornar404QuandoTreinoNaoExistir() throws Exception {
        mockMvc.perform(get("/api/treinos/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosTreinos() throws Exception {
        mockMvc.perform(get("/api/treinos")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(8)));
    }

    @Test
    void deveListarTreinosPaginado() throws Exception {
        mockMvc.perform(get("/api/treinos/paginado")
                        .param("page", "0")
                        .param("size", "5")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(8)));
    }

    @Test
    void deveListarTreinosPorUsuario() throws Exception {
        mockMvc.perform(get("/api/treinos/usuario/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].usuarioId").value(2));
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoExistir() throws Exception {
        mockMvc.perform(get("/api/treinos/usuario/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarTreino() throws Exception {
        String json = """
            {
                "dataHora": "2024-02-01T08:00:00",
                "tipo": "CORRIDA",
                "duracaoMin": 50,
                "observacoes": "Treino atualizado",
                "distanciaKm": 10.0,
                "exercicios": []
            }
        """;

        mockMvc.perform(put("/api/treinos/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.duracaoMin").value(50))
                .andExpect(jsonPath("$.observacoes").value("Treino atualizado"));
    }

    @Test
    void deveRetornar404AoAtualizarTreinoInexistente() throws Exception {
        String json = """
            {
                "dataHora": "2024-02-01T08:00:00",
                "tipo": "CORRIDA",
                "duracaoMin": 30,
                "distanciaKm": 5.0,
                "exercicios": []
            }
        """;

        mockMvc.perform(put("/api/treinos/999")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404AoDeletarTreinoInexistente() throws Exception {
        mockMvc.perform(delete("/api/treinos/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCriarTreinoDeMusculacao() throws Exception {
        String json = """
            {
                "dataHora": "2024-02-01T18:00:00",
                "tipo": "MUSCULACAO",
                "duracaoMin": 60,
                "observacoes": "Treino de peito e tríceps",
                "exercicios": []
            }
        """;

        mockMvc.perform(post("/api/treinos")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("MUSCULACAO"))
                .andExpect(jsonPath("$.distanciaKm").isEmpty());
    }

    @Test
    void deveCriarTreinoDeCiclismo() throws Exception {
        String json = """
            {
                "dataHora": "2024-02-01T07:00:00",
                "tipo": "CICLISMO",
                "duracaoMin": 90,
                "observacoes": "Pedal longo",
                "distanciaKm": 40.0,
                "exercicios": []
            }
        """;

        mockMvc.perform(post("/api/treinos")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("CICLISMO"))
                .andExpect(jsonPath("$.distanciaKm").value(40.0));
    }

    @Test
    void deveRetornar400ComDadosInvalidos() throws Exception {
        String json = """
            {
                "tipo": "CORRIDA",
                "duracaoMin": 30
            }
        """;

        mockMvc.perform(post("/api/treinos")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveDeletarTreino() throws Exception {
        // Primeiro criar um treino
        String jsonCriar = """
        {
            "dataHora": "2024-02-01T07:00:00",
            "tipo": "CORRIDA",
            "duracaoMin": 30,
            "observacoes": "Treino para deletar",
            "distanciaKm": 5.0,
            "exercicios": []
        }
    """;

        mockMvc.perform(post("/api/treinos")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(jsonCriar))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/treinos/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

}
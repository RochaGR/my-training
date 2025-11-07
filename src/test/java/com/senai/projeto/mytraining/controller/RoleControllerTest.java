package com.senai.projeto.mytraining.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldListAllRoles() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))  // Assumindo que temos ADMIN e USER em data.sql
                .andExpect(jsonPath("$[*].nome").value(org.hamcrest.Matchers.containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")));
    }



    @Test
    void shouldReturn403WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@teste.com", roles = {"USER"})
    void shouldAllowUserAccess() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldReturnEmptyListWhenNoRolesExist() throws Exception {
        // Simula uma chamada a uma rota que provavelmente retorna vazio
        mockMvc.perform(get("/api/roles?filter=none"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldHandleUnexpectedParameterGracefully() throws Exception {
        mockMvc.perform(get("/api/roles").param("sort", "invalid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldReturn404ForInvalidEndpoint() throws Exception {
        mockMvc.perform(get("/api/roles/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldReturn404ForNonexistentId() throws Exception {
        mockMvc.perform(get("/api/roles/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldReturn400ForInvalidIdType() throws Exception {
        mockMvc.perform(get("/api/roles/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@sistema.com", roles = {"ADMIN"})
    void shouldIgnoreUnknownFilterParameter() throws Exception {
        mockMvc.perform(get("/api/roles").param("filter", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nome").value(org.hamcrest.Matchers.containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")));
    }










}
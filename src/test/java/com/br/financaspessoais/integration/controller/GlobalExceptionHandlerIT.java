package com.br.financaspessoais.integration.controller;

import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String LOGIN_URL = "/api/auth/login";

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar 404 quando usuário não for encontrado")
    void deveRetornar404QuandoUsuarioNaoForEncontrado() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("naoexiste@email.com", "qualquerSenha");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Usuário não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar 401 quando senha estiver incorreta")
    void deveRetornar401QuandoSenhaIncorreta() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("Teste")
                .email("teste@email.com")
                .senha("$2a$10$T1EzpBvKqABhEvEAKR1mZeaOETjjl03/v2RHDAhEjH9QZtkAvmQwa") // senha = "senhaCorreta"
                .build();
        usuarioRepository.save(usuario);

        LoginRequestDTO request = new LoginRequestDTO("teste@email.com", "senhaErrada");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("Credenciais inválidas"));
    }
}

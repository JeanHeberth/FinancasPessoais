package com.br.financaspessoais.integration.controller;

import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String LOGIN_URL = "/api/auth/login";

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario usuario = Usuario.builder()
                .nome("Maria")
                .email("maria@email.com")
                .senha(new BCryptPasswordEncoder().encode("senha123456"))
                .build();

        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso e retornar token")
    void deveAutenticarUsuarioComSucesso() throws Exception {
        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO("Maria", "maria@email.com", "senha123456");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "email": "maria@email.com",
                                        "senha": "senha123456"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    @DisplayName("Não deve autenticar usuário com credenciais inválidas")
    void naoDeveAutenticarUsuarioComCredenciaisInvalidas() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO("Maria", "maria@email.com", "senhaErrada");

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

}

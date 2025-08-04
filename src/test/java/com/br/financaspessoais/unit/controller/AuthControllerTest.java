package com.br.financaspessoais.unit.controller;

import com.br.financaspessoais.config.JwtUtil;
import com.br.financaspessoais.controller.AuthController;
import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve autenticar e retornar token se as credenciais estiverem corretas")
    void deveAutenticarComSucesso() throws Exception {
        // Arrange
        String senhaCriptografada = BCrypt.hashpw("senha123", BCrypt.gensalt());

        Usuario usuario = Usuario.builder()
                .email("teste@email.com")
                .senha(senhaCriptografada)
                .build();

        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(jwtUtil.gerarToken("teste@email.com")).thenReturn("token.jwt.mockado");

        LoginRequestDTO request = new LoginRequestDTO("teste@email.com", "senha123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token.jwt.mockado"));

        verify(usuarioRepository).findByEmail("teste@email.com");
        verify(jwtUtil).gerarToken("teste@email.com");
    }
}

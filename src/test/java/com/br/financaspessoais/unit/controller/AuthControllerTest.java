package com.br.financaspessoais.unit.controller;

import com.br.financaspessoais.config.JwtUtil;
import com.br.financaspessoais.controller.AuthController;
import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    private final AuthController authController = new AuthController(usuarioRepository, jwtUtil, bCryptPasswordEncoder);

    @Test
    @DisplayName("Deve autenticar e retornar token")
    void deveAutenticarComSucesso() {
        LoginRequestDTO request = new LoginRequestDTO("jean@email.com", "senha123");

        Usuario usuario = Usuario.builder()
                .id("user123")
                .nome("Jean")
                .email("jean@email.com")
                .senha("$2a$10$ExemploDeHashFixoMockado1234567890AbcDef6hiJkLMnopQrstu")
                .build();

        when(usuarioRepository.findByEmail("jean@email.com")).thenReturn(Optional.of(usuario));
        when(jwtUtil.gerarToken("jean@email.com")).thenReturn("token.jwt.mockado");

        when(bCryptPasswordEncoder.matches("senha123", usuario.getSenha())).thenReturn(true);

        // Act
        ResponseEntity<Map<String, String>> response = (ResponseEntity<Map<String, String>>) authController.login(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("token", "token.jwt.mockado");
    }
    }


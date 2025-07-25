package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        // Usuário com senha criptografada no banco
        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail("joao@email.com");
        usuario.setSenha(new BCryptPasswordEncoder().encode("senha123"));

        usuarioRepository.save(usuario);
    }

    @Test
    void deveAutenticarEReceberTokenComCredenciaisValidas() throws Exception {
        String payload = """
                {
                    "email": "joao@email.com",
                    "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}

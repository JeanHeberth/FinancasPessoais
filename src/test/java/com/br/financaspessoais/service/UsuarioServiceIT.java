package com.br.financaspessoais.service;

import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceIT {


    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @BeforeEach
    void limparBanco() {
        assertNotNull(usuarioRepository, "usuarioRepository não foi injetado");
        usuarioRepository.deleteAll();
    }

    @Test
    void deveSalvarUsuarioComSenhaCriptografadaNoBanco() {

        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome("Maria");
        dto.setEmail("maria@email.com");
        dto.setSenha("senha123456");

        // Act
        usuarioService.salvar(dto);

        Usuario salvo = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow();

        System.out.println("Senha criptografada no banco: " + salvo.getSenha());

        assertThat(salvo.getSenha()).isNotEqualTo(dto.getSenha());
        assertThat(BCrypt.checkpw(dto.getSenha(), salvo.getSenha())).isTrue();
    }
}


package com.br.financaspessoais.repository;

import com.br.financaspessoais.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryIT  {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método executado antes de cada teste, responsável por limpar o banco de dados, para
     * garantir que os testes sejam executados em um banco de dados vazio.
     */
    @BeforeEach
    public void limparBanco() {
        assertNotNull(usuarioRepository, "usuarioRepository não foi injetado");
        usuarioRepository.deleteAll();
    }

    @Test
    public void deveSalvarERecuperarUsuarioDoMongoDB() {
        Usuario usuario = Usuario.builder()
                .nome("Jean")
                .email("jean@email.com")
                .senha("123456")
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        assertNotNull(salvo.getId());
        assertEquals(salvo.getEmail(), "jean@email.com");

        Usuario encontrado = usuarioRepository.findByEmail("jean@email.com").orElse(null);
        assertNotNull(encontrado);
        assertEquals(encontrado.getNome(), "Jean");
    }
}

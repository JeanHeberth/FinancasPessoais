package com.br.financaspessoais.repository;

import com.br.financaspessoais.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@SpringBootTest
public class UsuarioRepositoryIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeMethod
    public void limparBanco() {
        assertNotNull(usuarioRepository, "usuarioRepository não foi injetado");
        usuarioRepository.deleteAll();
    }

    @Test
    public void deveSalvarERecuperarUsuarioDoMongoDB() {
        Usuario usuario = Usuario.builder()
                .nome("Jean")
                .email("jean@exemplo.com")
                .senha("123456")
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        assertNotNull(salvo.getId());
        assertEquals(salvo.getEmail(), "jean@exemplo.com");

        Usuario encontrado = usuarioRepository.findByEmail("jean@exemplo.com").orElse(null);
        assertNotNull(encontrado);
        assertEquals(encontrado.getNome(), "Jean");
    }
}

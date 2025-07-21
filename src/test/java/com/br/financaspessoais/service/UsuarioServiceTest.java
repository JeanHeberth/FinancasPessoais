package com.br.financaspessoais.service;

import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarUsuarioComSucesso() {
        Usuario usuario = Usuario.builder()
                .nome("Jean")
                .email("jean@exemplo.com")
                .senha("123456")
                .build();

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario salvo = usuarioService.salvar(usuario);

        assertNotNull(salvo);
        assertEquals(salvo.getNome(), "Jean");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void deveBuscarUsuarioPorEmail() {
        Usuario usuario = Usuario.builder()
                .nome("Jean")
                .email("jean@exemplo.com")
                .senha("123456")
                .build();

        when(usuarioRepository.findByEmail("jean@exemplo.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("jean@exemplo.com");

        assertTrue(resultado.isPresent());
        assertEquals(resultado.get().getNome(), "Jean");
    }
}

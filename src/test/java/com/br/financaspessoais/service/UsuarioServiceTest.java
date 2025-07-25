package com.br.financaspessoais.service;

import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
import com.br.financaspessoais.dto.out.UsuarioResponseDTO;
import com.br.financaspessoais.mapper.UsuarioMapper;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final UsuarioMapper usuarioMapper = mock(UsuarioMapper.class);

    private final UsuarioService usuarioService = new UsuarioService(usuarioRepository, usuarioMapper);

    @Test
    void deveSalvarSenhaCriptografada() {
        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome("João");
        dto.setEmail("joao@email.com");
        dto.setSenha("123456");

        Usuario usuarioConvertido = new Usuario();
        usuarioConvertido.setNome(dto.getNome());
        usuarioConvertido.setEmail(dto.getEmail());
        usuarioConvertido.setSenha(dto.getSenha());

        when(usuarioMapper.toEntity(dto)).thenReturn(usuarioConvertido);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioMapper.toResponseDTO(any())).thenReturn(UsuarioResponseDTO.builder()
                .nome("João")
                .email("joao@email.com")
                .build());

        // Act
        UsuarioResponseDTO response = usuarioService.salvar(dto);

        // Assert
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioSalvo = captor.getValue();

        assertThat(usuarioSalvo.getSenha()).isNotBlank();
        assertThat(usuarioSalvo.getSenha()).isNotEqualTo(dto.getSenha());
        assertThat(BCrypt.checkpw(dto.getSenha(), usuarioSalvo.getSenha())).isTrue();
    }


}

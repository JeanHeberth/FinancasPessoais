package com.br.financaspessoais.unit.service;

import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.enums.TipoLancamento;
import com.br.financaspessoais.mapper.LancamentoMapper;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.LancamentoRepository;
import com.br.financaspessoais.repository.UsuarioRepository;
import com.br.financaspessoais.service.LancamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestExecutionListeners(listeners = WithSecurityContextTestExecutionListener.class)
class LancamentoServiceTest {

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LancamentoMapper lancamentoMapper;

    @InjectMocks
    private LancamentoService lancamentoService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder()
                .id("user123")
                .nome("Jean")
                .email("jean@email.com")
                .senha("senha123")
                .build();

        // Mock do SecurityContext e Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("jean@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Deve listar lancamentos do usuario logado")
    @WithMockUser(username = "jean@email.com")
    void deveListarLancamentosDoUsuarioLogado() {

        // Arrange
        Lancamento l1 = Lancamento.builder()
                .descricao("Mercado")
                .valor(BigDecimal.valueOf(150.0))
                .data(LocalDateTime.now())
                .usuario(usuario)
                .build();

        Lancamento l2 = Lancamento.builder()
                .descricao("Combustivel")
                .valor(BigDecimal.valueOf(200.0))
                .data(LocalDateTime.now())
                .usuario(usuario)
                .build();

        when(usuarioRepository.findByEmail("jean@email.com")).thenReturn(Optional.of(usuario));
        when(lancamentoRepository.findByUsuarioId("user123")).thenReturn(List.of(l1, l2));

        LancamentoResponseDTO dto1 = new LancamentoResponseDTO("1",
                "Mercado",
                BigDecimal.valueOf(150.0),
                LocalDateTime.now(),
                TipoLancamento.SAIDA,
                "Alimentação",
                usuario.getNome().equals("jean@email.com") ? usuario : null
                );

        LancamentoResponseDTO dto2 = new LancamentoResponseDTO("2",
                "Combustivel",
                BigDecimal.valueOf(200.0),
                LocalDateTime.now(),
                TipoLancamento.SAIDA,
                "Transporte",
                usuario.getNome().equals("jean@email.com") ? usuario : null
                );

        when(lancamentoMapper.toResponseDTO(l1)).thenReturn(dto1);
        when(lancamentoMapper.toResponseDTO(l2)).thenReturn(dto2);

        // Act
        List<LancamentoResponseDTO> resultado = lancamentoService.listarPorUsuarioLogado();

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getDescricao()).isEqualTo("Mercado");
        assertThat(resultado.get(1).getDescricao()).isEqualTo("Combustivel");

        verify(usuarioRepository).findByEmail("jean@email.com");
        verify(lancamentoRepository).findByUsuarioId("user123");
    }
}


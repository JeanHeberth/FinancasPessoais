package com.br.financaspessoais.service;

import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.mapper.LancamentoMapper;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.LancamentoRepository;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceIT {

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoMapper lancamentoMapper;

    @BeforeEach
    void setup() {
        lancamentoRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("teste@email.com")
                .senha("senha123")
                .build();

        usuarioRepository.save(usuario);
    }

    @Test
    @WithMockUser(username = "teste@email.com")
    void deveListarLancamentosDoUsuarioLogado() {
        Usuario usuario = usuarioRepository.findByEmail("teste@email.com").orElseThrow();

        Lancamento l1 = Lancamento.builder()
                .descricao("Mercado")
                .categoria("Alimentação")
                .valor(BigDecimal.valueOf(150.0))
                .data(LocalDateTime.now())
                .usuario(usuario)
                .build();

        Lancamento l2 = Lancamento.builder()
                .descricao("Combustível")
                .categoria("Transporte")
                .valor(BigDecimal.valueOf(200.0))
                .data(LocalDateTime.now())
                .usuario(usuario)
                .build();

        lancamentoRepository.save(l1);
        lancamentoRepository.save(l2);

        List<LancamentoResponseDTO> resultados = lancamentoService.listarPorUsuarioLogado();

        assertThat(resultados).hasSize(2);
        assertThat(resultados.get(0).getDescricao()).isEqualTo("Mercado");
        assertThat(resultados.get(1).getDescricao()).isEqualTo("Combustível");
    }
}

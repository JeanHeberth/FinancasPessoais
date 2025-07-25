package com.br.financaspessoais.repository;

import com.br.financaspessoais.enums.TipoLancamento;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryIT {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        lancamentoRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("teste@email.com")
                .senha("senha123")
                .build();

        usuario = usuarioRepository.save(usuario);
    }

    @Test
    public void deveSalvarEListarLancamentosPorUsuario() {
        Lancamento lancamento = Lancamento.builder()
                .descricao("Conta de Luz")
                .valor(BigDecimal.valueOf(200.00))
                .data(LocalDateTime.now())
                .tipo(TipoLancamento.SAIDA)
                .categoria("Contas")
                .usuario(usuario)
                .build();

        lancamentoRepository.save(lancamento);

        List<Lancamento> resultados = lancamentoRepository.findByUsuarioId(usuario.getId());

        assertFalse(resultados.isEmpty());
        assertEquals("Conta de Luz", resultados.get(0).getDescricao());
    }

    @Test
    public void deveBuscarLancamentosPorPeriodo() {
        lancamentoRepository.save(Lancamento.builder()
                .descricao("Salário")
                .valor(BigDecimal.valueOf(3000))
                .data(LocalDateTime.of(2025, 7, 1, 10, 0))
                .tipo(TipoLancamento.ENTRADA)
                .categoria("Renda")
                .usuario(usuario)
                .build());

        lancamentoRepository.save(Lancamento.builder()
                .descricao("Mercado")
                .valor(BigDecimal.valueOf(500))
                .data(LocalDateTime.of(2025, 7, 10, 15, 30))
                .tipo(TipoLancamento.SAIDA)
                .categoria("Alimentação")
                .usuario(usuario)
                .build());

        LocalDateTime inicio = LocalDateTime.of(2025, 7, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 7, 31, 23, 59, 59);

        List<Lancamento> resultados = lancamentoRepository.findByUsuarioIdAndDataBetween(
                usuario.getId(),
                inicio,
                fim
        );

        assertEquals(2, resultados.size());
    }
}

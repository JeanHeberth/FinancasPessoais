package com.br.financaspessoais.repository;

import com.br.financaspessoais.enums.TipoLancamento;
import com.br.financaspessoais.model.Lancamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
public class LancamentoRepositoryIT{

    @Autowired
    private LancamentoRepository lancamentoRepository;


    @Test
    public void deveSalvarEListarLancamentosPorUsuario() {
        String usuarioId = "123";

        Lancamento lancamento = Lancamento.builder()
                .descricao("Conta de Luz")
                .valor(BigDecimal.valueOf(200.00))
                .data(LocalDateTime.now())
                .tipo(TipoLancamento.SAIDA)
                .categoria("Contas")
                .build();

        lancamentoRepository.save(lancamento);

        List<Lancamento> resultados = lancamentoRepository.findByUsuarioId(usuarioId);

        assertFalse(resultados.isEmpty());
        assertEquals(resultados.get(0).getDescricao(), "Conta de Luz");
    }

    @Test
    public void deveBuscarLancamentosPorPeriodo() {
        String usuarioId = "456";

        lancamentoRepository.save(Lancamento.builder()
                .descricao("Salário")
                .valor(BigDecimal.valueOf(3000))
                .data(LocalDateTime.of(2025, 7, 1, 10, 0)) // horário arbitrário
                .tipo(TipoLancamento.ENTRADA)
                .categoria("Renda")
                .build());

        lancamentoRepository.save(Lancamento.builder()
                .descricao("Mercado")
                .valor(BigDecimal.valueOf(500))
                .data(LocalDateTime.of(2025, 7, 10, 15, 30))
                .tipo(TipoLancamento.SAIDA)
                .categoria("Alimentação")
                .build());

        LocalDateTime inicio = LocalDateTime.of(2025, 7, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 7, 31, 23, 59, 59);

        List<Lancamento> resultados = lancamentoRepository.findByUsuarioIdAndDataBetween(
                usuarioId,
                inicio,
                fim
        );

        resultados.forEach(l -> {
            System.out.println(">>> Lançamento retornado:");
            System.out.println("Descrição: " + l.getDescricao());
            System.out.println("Data: " + l.getData());
            System.out.println("Tipo: " + l.getTipo());
            System.out.println("---");
        });

        assertEquals(resultados.size(), 2); // Agora deve funcionar corretamente
    }



}

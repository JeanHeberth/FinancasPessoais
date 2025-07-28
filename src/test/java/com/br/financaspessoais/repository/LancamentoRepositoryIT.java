package com.br.financaspessoais.repository;


import com.br.financaspessoais.enums.TipoLancamento;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@EnabledIfSystemProperty(named = "ambiente", matches = "local")
class LancamentoRepositoryIT {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    private Usuario usuario;

    @BeforeEach
    void setup() {
//        lancamentoRepository.deleteAll();
        usuario = Usuario.builder()
                .id("user123")
                .nome("Jean Heberth")
                .email("jean@email.com")
                .senha("senha123")
                .build();
    }

    @Test
    @DisplayName("Deve salvar e buscar lançamentos por usuário")
    void deveSalvarEListarLancamentosPorUsuario() {
        // Arrange
        Lancamento lancamento = Lancamento.builder()
                .descricao("Mercado")
                .valor(BigDecimal.valueOf(250.75))
                .tipo(TipoLancamento.SAIDA)
                .data(LocalDateTime.now())
                .usuario(usuario)
                .build();

        lancamentoRepository.save(lancamento);

        // Act
        List<Lancamento> resultado = lancamentoRepository.findByUsuarioId("user123");

        // Assert
        assertThat(resultado).hasSize(1);
        Lancamento encontrado = resultado.get(0);
        assertThat(encontrado.getDescricao()).isEqualTo("Mercado");
    }
}

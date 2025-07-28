package com.br.financaspessoais.repository;


import com.br.financaspessoais.enums.TipoLancamento;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class LancamentoRepositoryIT {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @Test
    @DisplayName("Deve salvar e buscar lançamentos por usuário")
    void deveSalvarEListarLancamentosPorUsuario() {

      Usuario usuario = usuarioRepository.save(Usuario.builder()
                .id("user123")
                .nome("Jean Heberth")
                .email("jean@email.com")
                .senha("senha123")
                .build()
      );




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
        List<Lancamento> resultado = lancamentoRepository.findAll();

        // Assert
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getUsuario()).isNotNull();
        assertThat(resultado.get(0).getUsuario().getId()).isEqualTo(usuario.getId());
    }
}

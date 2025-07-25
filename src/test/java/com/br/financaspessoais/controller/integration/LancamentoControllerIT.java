package com.br.financaspessoais.controller.integration;

import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.LancamentoRepository;
import com.br.financaspessoais.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.mongodb.port=0",
        "spring.data.mongodb.database=financas-testes",
        "spring.data.mongodb.socket-timeout=40000",
        "spring.data.mongodb.connect-timeout=40000"
})
public class LancamentoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @BeforeEach
    public void setup() {
        lancamentoRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("teste@email.com")
                .senha(new BCryptPasswordEncoder().encode("senha123"))
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        Lancamento l1 = Lancamento.builder()
                .descricao("Mercado")
                .categoria("Alimentação")
                .valor(BigDecimal.valueOf(150.0))
                .data(LocalDateTime.now())
                .usuario(salvo)
                .build();

        Lancamento l2 = Lancamento.builder()
                .descricao("Combustível")
                .categoria("Transporte")
                .valor(BigDecimal.valueOf(200.0))
                .data(LocalDateTime.now())
                .usuario(salvo)
                .build();

        lancamentoRepository.save(l1);
        lancamentoRepository.save(l2);
    }

    @Test
    @WithMockUser(username = "teste@email.com")
    public void deveRetornarLancamentosDoUsuarioAutenticado() throws Exception {
        mockMvc.perform(get("/api/lancamentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Mercado"))
                .andExpect(jsonPath("$[1].descricao").value("Combustível"));
    }
}

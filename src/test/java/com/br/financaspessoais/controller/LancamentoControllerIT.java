package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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

        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("$2a$10$senhaCriptografada"); // já criptografada
        Usuario salvo = usuarioRepository.save(usuario);

        Lancamento l1 = new Lancamento();
        l1.setDescricao("Mercado");
        l1.setCategoria("Alimentação");
        l1.setValor(BigDecimal.valueOf(150.0));
        l1.setData(LocalDateTime.now());
        l1.setUsuario(salvo);

        Lancamento l2 = new Lancamento();
        l2.setDescricao("Combustível");
        l2.setCategoria("Transporte");
        l2.setValor(BigDecimal.valueOf(200.0));
        l2.setData(LocalDateTime.now());
        l2.setUsuario(salvo);

        lancamentoRepository.save(l1);
        lancamentoRepository.save(l2);
    }

    @Test
    @WithMockUser(username = "teste@email.com")
    public void deveRetornarLancamentosDoUsuarioAutenticado() throws Exception {
        mockMvc.perform(get("/api/lancamentos/usuario") // o PathVariable é ignorado internamente
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].descricao").value("Mercado"))
                .andExpect(jsonPath("$[1].descricao").value("Combustível"));
    }
}

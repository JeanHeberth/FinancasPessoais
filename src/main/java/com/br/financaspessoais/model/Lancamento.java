package com.br.financaspessoais.model;

import com.br.financaspessoais.enums.TipoLancamento;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "lancamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lancamento {

    @Id
    private String id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    private TipoLancamento tipo;

    private String categoria;

    private String usuarioId; // Referência simples ao usuário
}

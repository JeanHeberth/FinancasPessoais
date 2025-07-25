package com.br.financaspessoais.model;

import com.br.financaspessoais.enums.TipoLancamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "lancamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lancamento {

    @Id
    private String id;

    private String descricao;

    private BigDecimal valor;

    @Field("data")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime data;

    private TipoLancamento tipo;

    private String categoria;

    @DBRef
    private Usuario usuario;
}

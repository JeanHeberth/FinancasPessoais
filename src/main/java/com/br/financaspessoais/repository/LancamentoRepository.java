package com.br.financaspessoais.repository;

import com.br.financaspessoais.model.Lancamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LancamentoRepository extends MongoRepository<Lancamento, String> {

    List<Lancamento> findByUsuarioId(String usuarioId);

    List<Lancamento> findByUsuarioIdAndDataBetween(String usuarioId, LocalDateTime inicio, LocalDateTime fim);
}

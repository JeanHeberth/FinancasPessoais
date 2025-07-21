package com.br.financaspessoais.service;

import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    public List<Lancamento> listarPorUsuario(String usuarioId) {
        return lancamentoRepository.findByUsuarioId(usuarioId);
    }

    public List<Lancamento> listarPorPeriodo(String usuarioId, LocalDate inicio, LocalDate fim) {
        return lancamentoRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim);
    }

    public Optional<Lancamento> buscarPorId(String id) {
        return lancamentoRepository.findById(id);
    }

    public void deletar(String id) {
        lancamentoRepository.deleteById(id);
    }
}

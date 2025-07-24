package com.br.financaspessoais.service;

import com.br.financaspessoais.dto.in.DashboardResumoDTO;
import com.br.financaspessoais.enums.TipoLancamento;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    public List<Lancamento> listarTodos() {
        return lancamentoRepository.findAll();
    }

    public Lancamento salvar(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    public List<Lancamento> listarPorUsuario(String usuarioId) {
        return lancamentoRepository.findByUsuarioId(usuarioId);
    }

    public List<Lancamento> listarPorPeriodo(String usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        return lancamentoRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim);
    }

    public Optional<Lancamento> buscarPorId(String id) {
        return lancamentoRepository.findById(id);
    }

    public void deletar(String id) {
        lancamentoRepository.deleteById(id);
    }

    public DashboardResumoDTO calcularResumoDoMes(String usuarioId, YearMonth mes) {
        LocalDateTime inicio = mes.atDay(1).atStartOfDay();
        LocalDateTime fim = mes.atEndOfMonth().atTime(23, 59, 59);

        List<Lancamento> lancamentos = lancamentoRepository
                .findByUsuarioIdAndDataBetween(usuarioId, inicio, fim);

        BigDecimal entradas = lancamentos.stream()
                .filter(l -> l.getTipo() == TipoLancamento.ENTRADA)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saidas = lancamentos.stream()
                .filter(l -> l.getTipo() == TipoLancamento.SAIDA)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = entradas.subtract(saidas);

        return new DashboardResumoDTO(entradas, saidas, saldo);
    }

    public Lancamento atualizar(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

}

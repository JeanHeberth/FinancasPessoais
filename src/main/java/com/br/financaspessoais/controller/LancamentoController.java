package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.DashboardResumoDTO;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity<Lancamento> criarLancamento(@RequestBody Lancamento lancamento) {
        return ResponseEntity.ok(lancamentoService.salvar(lancamento));
    }

    @GetMapping
    public List<Lancamento> listarTodos() {
        return lancamentoService.listarTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Lancamento> listarPorUsuario(@PathVariable String usuarioId) {
        return lancamentoService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}/periodo")
    public List<Lancamento> listarPorUsuarioEPeriodo(@PathVariable String usuarioId, @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio, @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return lancamentoService.listarPorPeriodo(usuarioId, inicio, fim);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        lancamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}/dashboard")
    public DashboardResumoDTO resumoMensal(@PathVariable String usuarioId, @RequestParam(required = false) Integer ano, @RequestParam(required = false) Integer mes) {
        YearMonth periodo = (ano != null && mes != null) ? YearMonth.of(ano, mes) : YearMonth.now();

        return lancamentoService.calcularResumoDoMes(usuarioId, periodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lancamento> atualizar(@PathVariable String id, @RequestBody Lancamento lancamento) {
        return ResponseEntity.ok(lancamentoService.atualizar(lancamento));
    }
}


package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.LancamentoRequestDTO;
import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.service.LancamentoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/lancamentos")
@Slf4j
public class LancamentoController {

    @Autowired
    private LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity<LancamentoResponseDTO> criarLancamento(@RequestBody @Valid LancamentoRequestDTO lancamentoRequestDto) {
        log.info("📩 Recebido lançamento: {}", lancamentoRequestDto);
        LancamentoResponseDTO salvo = lancamentoService.salvar(lancamentoRequestDto);
        log.info("✅ Lançamento salvo: {}", salvo);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping()
    public List<LancamentoResponseDTO> listarPorUsuario() {
        return lancamentoService.listarPorUsuarioLogado();
    }

    @GetMapping("/usuario/{usuarioId}/periodo")
    public List<LancamentoResponseDTO> listarPorUsuarioEPeriodo(@PathVariable @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio, @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return lancamentoService.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(lancamentoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        lancamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LancamentoResponseDTO> atualizar(@PathVariable @Valid String id, @RequestBody LancamentoRequestDTO lancamentoRequestDto) {
        return ResponseEntity.ok(lancamentoService.atualizar(id, lancamentoRequestDto));
    }
}


package com.br.financaspessoais.service;

import com.br.financaspessoais.dto.in.LancamentoRequestDTO;
import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.mapper.LancamentoMapper;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final LancamentoMapper lancamentoMapper;


    public List<LancamentoResponseDTO> listarTodos() {
        return lancamentoRepository
                .findAll()
                .stream()
                .map(lancamentoMapper::toResponseDTO)
                .toList();
    }

    public LancamentoResponseDTO salvar(LancamentoRequestDTO lancamentoRequestDto) {
        log.info("📦 Salvando no repositório: {}", lancamentoRequestDto);
        Lancamento lancamento = lancamentoMapper.toEntity(lancamentoRequestDto);
        return lancamentoMapper.toResponseDTO(lancamentoRepository.save(lancamento));
    }

    public List<LancamentoResponseDTO> listarPorUsuario(String usuarioId) {
        return lancamentoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(lancamentoMapper::toResponseDTO)
                .toList();
    }

    public List<LancamentoResponseDTO> listarPorPeriodo(String usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        return lancamentoRepository.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim)
                .stream()
                .map(lancamentoMapper::toResponseDTO)
                .toList();
    }

    public Optional<LancamentoResponseDTO> buscarPorId(String id) {
        return lancamentoRepository.findById(id)
                .map(lancamentoMapper::toResponseDTO);
    }

    public void deletar(String id) {
        lancamentoRepository.deleteById(id);
    }

    public LancamentoResponseDTO atualizar(String id, LancamentoRequestDTO lancamentoRequestDto) {
        Lancamento lancamento = lancamentoMapper.toEntity(lancamentoRequestDto);
        lancamento.setId(id);
        return lancamentoMapper.toResponseDTO(lancamentoRepository.save(lancamento));
    }

}

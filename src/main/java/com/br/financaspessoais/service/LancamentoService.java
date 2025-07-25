package com.br.financaspessoais.service;

import com.br.financaspessoais.dto.in.LancamentoRequestDTO;
import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.mapper.LancamentoMapper;
import com.br.financaspessoais.model.Lancamento;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.LancamentoRepository;
import com.br.financaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final LancamentoMapper lancamentoMapper;
    private final UsuarioRepository usuarioRepository;


//    public List<LancamentoResponseDTO> listarTodos() {
//        return lancamentoRepository
//                .findAll()
//                .stream()
//                .map(lancamentoMapper::toResponseDTO)
//                .toList();
//    }

    public LancamentoResponseDTO salvar(LancamentoRequestDTO lancamentoRequestDto) {
        log.info("📦 Salvando no repositório: {}", lancamentoRequestDto);

        Usuario usuarioLogado = getUsuarioLogado();

        Lancamento lancamento = lancamentoMapper.toEntity(lancamentoRequestDto);

        lancamento.setUsuario(usuarioLogado);

        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
        return lancamentoMapper.toResponseDTO(lancamentoSalvo);
    }

    public List<LancamentoResponseDTO> listarPorUsuarioLogado() {

        Usuario usuarioLogado = getUsuarioLogado();

        return lancamentoRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(lancamentoMapper::toResponseDTO)
                .toList();

    }

    public List<LancamentoResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        Usuario usuario = getUsuarioLogado();

        return lancamentoRepository.findByUsuarioIdAndDataBetween(usuario.getId(), inicio, fim)
                .stream()
                .map(lancamentoMapper::toResponseDTO)
                .toList();
    }


    public LancamentoResponseDTO buscarPorId(String id) {
        Usuario usuario = getUsuarioLogado();

        return lancamentoRepository.findById(id)
                .filter(l -> l.getUsuario().getId().equals(usuario.getId()))
                .map(lancamentoMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Lançamento nao encontrado"));
    }


    public void deletar(String id) {
        Usuario usuario = getUsuarioLogado();

        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lançamento não encontrado"));

        if (!lancamento.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acesso negado ao lançamento");
        }

        lancamentoRepository.deleteById(id);
    }


    public LancamentoResponseDTO atualizar(String id, LancamentoRequestDTO lancamentoRequestDto) {
        Usuario usuarioLogado = getUsuarioLogado();
        log.info("📦 Atualizando no repositório: {}", lancamentoRequestDto);

        Lancamento lancamentoExistente = lancamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lançamento nao encontrado"));

        if (!lancamentoExistente.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        ;

        lancamentoExistente.setDescricao(lancamentoRequestDto.getDescricao());
        lancamentoExistente.setValor(lancamentoRequestDto.getValor());
        lancamentoExistente.setData(lancamentoRequestDto.getData());
        lancamentoExistente.setTipo(lancamentoRequestDto.getTipo());
        lancamentoExistente.setCategoria(lancamentoRequestDto.getCategoria());

        Lancamento lancamentoAtualizado = lancamentoRepository.save(lancamentoExistente);
        return lancamentoMapper.toResponseDTO(lancamentoAtualizado);
    }

    public Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("📦 Listando lancamentos do usuário: {}", email);

        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com o e-mail: {}", email);
                    return new RuntimeException("Usuário não encontrado");
                });

        log.info("📦 Listando lançamentos do usuário: {}", email);
        return usuarioLogado;
    }
}

package com.br.financaspessoais.service;


import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
import com.br.financaspessoais.dto.out.UsuarioResponseDTO;
import com.br.financaspessoais.mapper.UsuarioMapper;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponseDTO salvar(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository
                .findAll()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

}

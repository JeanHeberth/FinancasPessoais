package com.br.financaspessoais.service;


import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
import com.br.financaspessoais.dto.out.UsuarioResponseDTO;
import com.br.financaspessoais.enums.ForcaSenha;
import com.br.financaspessoais.mapper.UsuarioMapper;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponseDTO salvar(UsuarioRequestDTO usuarioRequestDTO) {

        ForcaSenha forca = verificarForcaSenha(usuarioRequestDTO.getSenha());
        if (forca == ForcaSenha.FRACA) {
            throw new IllegalArgumentException("Senha muito fraca. Por favor, escolha uma senha mais forte.");
        }

        if (usuarioRepository.findByEmail(usuarioRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado. Por favor, escolha outro email.");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);

        String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository
                .findAll()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.map(usuarioMapper::toResponseDTO);
    }

    public ForcaSenha verificarForcaSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            return ForcaSenha.FRACA;
        }

        boolean temMaiuscula = senha.chars().anyMatch(Character::isUpperCase);
        boolean temMinuscula = senha.chars().anyMatch(Character::isLowerCase);
        boolean temNumero = senha.chars().anyMatch(Character::isDigit);
        boolean temEspecial = senha.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:'\",.<>/?".indexOf(ch) >= 0);

        int criteriosAtendidos = 0;
        if (temMaiuscula) criteriosAtendidos++;
        if (temMinuscula) criteriosAtendidos++;
        if (temNumero) criteriosAtendidos++;
        if (temEspecial) criteriosAtendidos++;

        if (criteriosAtendidos >= 3) {
            return ForcaSenha.FORTE;
        } else if (criteriosAtendidos == 2) {
            return ForcaSenha.MEDIA;
        } else {
            return ForcaSenha.FRACA;
        }
    }
}

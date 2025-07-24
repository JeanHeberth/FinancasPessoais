package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail());
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body("Email ou senha inválidos");
        }
        if (!optionalUsuario.get().getSenha().equals(loginRequestDTO.getSenha())) {
            return ResponseEntity.badRequest().body("Email ou senha inválidos");
        }
        Usuario usuario = optionalUsuario.get();
        boolean senhaValida = BCrypt.checkpw(loginRequestDTO.getSenha(), usuario.getSenha());

        if (!senhaValida) {
            return ResponseEntity.badRequest().body("Email ou senha inválidos");
        }
        return ResponseEntity.ok().body("Autenticado com sucesso");
    }

}

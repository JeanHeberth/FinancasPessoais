package com.br.financaspessoais.controller;

import com.br.financaspessoais.config.JwtUtil;
import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        log.info("🔐 Tentando autenticar o e-mail: {}", loginRequestDTO.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("❌ Usuário não encontrado: {}", loginRequestDTO.getEmail());
                    return new RuntimeException("Usuário não encontrado");
                });

        log.debug("📥 Senha enviada: {}", loginRequestDTO.getSenha());
        log.debug("📦 Senha armazenada: {}", usuario.getSenha());

        boolean senhaOk = BCrypt.checkpw(loginRequestDTO.getSenha(), usuario.getSenha());

        if (!senhaOk) {
            log.warn("❌ Senha incorreta para e-mail: {}", loginRequestDTO.getEmail());
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());
        log.info("✅ Token gerado com sucesso para {}: {}", usuario.getEmail(), token);

        return ResponseEntity.ok(Map.of("token", token));
    }
}

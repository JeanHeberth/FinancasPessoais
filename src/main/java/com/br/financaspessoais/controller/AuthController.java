package com.br.financaspessoais.controller;

import com.br.financaspessoais.config.JwtUtil;
import com.br.financaspessoais.dto.in.LoginRequestDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCrypt;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        final String email = loginRequestDTO.getEmail();
        final String senha = loginRequestDTO.getSenha();

        log.info("🔐 Tentando autenticar o e-mail: {}", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("❌ Usuário não encontrado: {}", email);
                    return new RuntimeException("Usuário não encontrado");
                });

        if (!bCrypt.matches(senha, usuario.getSenha())) {
            log.warn("❌ Senha incorreta para e-mail: {}", email);
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtUtil.gerarToken(email);
        log.info("✅ Token gerado com sucesso para {}: {}", email, token);

        return ResponseEntity.ok(Map.of("token", token));
    }
}

package com.br.financaspessoais.service;

import com.br.financaspessoais.model.PasswordResetToken;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.PasswordResetTokenRepository;
import com.br.financaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void solicitarRedefinicaoSenha(String email) {
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Remove tokens anteriores, se houver
        passwordResetTokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        var redefinicaoSenha = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiracao(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(redefinicaoSenha);

        String link = "http://localhost:4200/redefinir-senha?token=" + token;
        String corpo = "Clique no link abaixo para redefinir sua senha: " + link;

        emailService.enviarEmail(email, "Redefinição de Senha", corpo);
    }

    public void redefinirSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        Usuario usuario = usuarioRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String novaSenhaHash = bCryptPasswordEncoder.encode(novaSenha);
        usuario.setSenha(novaSenhaHash);

        usuarioRepository.save(usuario);
        passwordResetTokenRepository.delete(resetToken);
    }
}

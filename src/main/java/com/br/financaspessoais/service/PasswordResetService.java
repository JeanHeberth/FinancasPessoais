package com.br.financaspessoais.service;

import com.br.financaspessoais.model.PasswordResetToken;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.repository.PasswordResetTokenRepository;
import com.br.financaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final EmailTemplateFileService emailTemplateFileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    public void solicitarRedefinicaoSenha(String email) {
        log.info("Solicitação de redefinição de senha para: {}", email);
        
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));

        // Remove tokens anteriores para este email
        passwordResetTokenRepository.deleteByEmail(email);

        // Gera um token único e seguro
        String token = UUID.randomUUID().toString();
        
        // Cria o token de redefinição com expiração de 1 hora
        var redefinicaoSenha = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiracao(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(redefinicaoSenha);

        // Monta o link de redefinição
        String linkRedefinicao = frontendUrl + "/redefinir-senha?token=" + token;
        
        // Usa o EmailTemplateFileService para criar o email
        String corpoEmail = emailTemplateFileService.criarEmailRedefinicaoSenha(
            usuario.getNome(), 
            linkRedefinicao
        );

        emailService.enviarEmail(email, "Redefinição de Senha - Finanças Pessoais", corpoEmail);
        
        log.info("Email de redefinição enviado para: {}", email);
    }

    public void redefinirSenha(String token, String novaSenha) {
        log.info("Tentativa de redefinição de senha com token: {}", token.substring(0, 8) + "...");
        
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido ou não encontrado"));

        // Verifica se o token não expirou
        if (resetToken.getExpiracao().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Token expirado. Solicite uma nova redefinição de senha.");
        }

        Usuario usuario = usuarioRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Valida a força da nova senha
        validarNovaSenha(novaSenha);

        // Criptografa e salva a nova senha
        String novaSenhaHash = bCryptPasswordEncoder.encode(novaSenha);
        usuario.setSenha(novaSenhaHash);

        usuarioRepository.save(usuario);
        
        // Remove o token usado
        passwordResetTokenRepository.delete(resetToken);
        
        // Usa o EmailTemplateFileService para criar o email de confirmação
        String emailConfirmacao = emailTemplateFileService.criarEmailConfirmacaoSenha(usuario.getNome());
        emailService.enviarEmail(usuario.getEmail(), "Senha Redefinida com Sucesso", emailConfirmacao);
        
        log.info("Senha redefinida com sucesso para usuário: {}", usuario.getEmail());
    }
    
    public boolean validarToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .map(resetToken -> resetToken.getExpiracao().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    private void validarNovaSenha(String novaSenha) {
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new IllegalArgumentException("A nova senha não pode estar vazia");
        }
        
        if (novaSenha.length() < 8) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 8 caracteres");
        }
        
        if (novaSenha.length() > 100) {
            throw new IllegalArgumentException("A nova senha não pode ter mais de 100 caracteres");
        }
        
        // Verifica se contém pelo menos uma letra e um número
        boolean temLetra = novaSenha.matches(".*[a-zA-Z].*");
        boolean temNumero = novaSenha.matches(".*\\d.*");
        
        if (!temLetra || !temNumero) {
            throw new IllegalArgumentException("A nova senha deve conter pelo menos uma letra e um número");
        }
    }
}

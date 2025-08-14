package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.ResetarSenhaDTO;
import com.br.financaspessoais.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/password-reset")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Map<String, String>> solicitarResetDeSenha(@RequestParam String email) {
        try {
            passwordResetService.solicitarRedefinicaoSenha(email);
            return ResponseEntity.ok(Map.of(
                "mensagem", "Se o email existir em nossa base, você receberá as instruções para redefinir sua senha."
            ));
        } catch (Exception e) {
            // Por segurança, sempre retorna a mesma mensagem
            return ResponseEntity.ok(Map.of(
                "mensagem", "Se o email existir em nossa base, você receberá as instruções para redefinir sua senha."
            ));
        }
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@RequestBody @Valid ResetarSenhaDTO dto) {
        passwordResetService.redefinirSenha(dto.getToken(), dto.getNovaSenha());
        return ResponseEntity.ok(Map.of(
            "mensagem", "Senha redefinida com sucesso!"
        ));
    }
    
    @GetMapping("/validar-token")
    public ResponseEntity<Map<String, Boolean>> validarToken(@RequestParam String token) {
        boolean valido = passwordResetService.validarToken(token);
        return ResponseEntity.ok(Map.of("valido", valido));
    }
}

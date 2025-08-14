package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.ResetarSenhaDTO;
import com.br.financaspessoais.repository.PasswordResetTokenRepository;
import com.br.financaspessoais.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;


    @PostMapping("esqueci-senha")
    public ResponseEntity<Void> solicitarResetDeSenha(@RequestParam String email) {
        passwordResetService.solicitarRedefinicaoSenha(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid ResetarSenhaDTO dto) {
        passwordResetService.redefinirSenha(dto.getToken(), dto.getNovaSenha());
        return ResponseEntity.ok().build();
    }




}

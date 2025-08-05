package com.br.financaspessoais.controller;

import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
import com.br.financaspessoais.dto.out.UsuarioResponseDTO;
import com.br.financaspessoais.model.Usuario;
import com.br.financaspessoais.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.salvar(usuarioRequestDTO);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        Optional<UsuarioResponseDTO> usuarioResponseDTO = usuarioService.buscarPorEmail(email);
        return usuarioResponseDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}

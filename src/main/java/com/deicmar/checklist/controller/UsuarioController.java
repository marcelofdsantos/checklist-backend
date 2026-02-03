package com.deicmar.checklist.controller;

import com.deicmar.checklist.dto.usuario.UsuarioRequest;
import com.deicmar.checklist.dto.usuario.UsuarioResponse;
import com.deicmar.checklist.model.enums.Perfil;
import com.deicmar.checklist.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/ativos")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<UsuarioResponse>> listarAtivos() {
        List<UsuarioResponse> usuarios = usuarioService.listarAtivos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/perfil/{perfil}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<UsuarioResponse>> listarPorPerfil(@PathVariable Perfil perfil) {
        List<UsuarioResponse> usuarios = usuarioService.listarPorPerfil(perfil);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/re/{re}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<UsuarioResponse> buscarPorRe(@PathVariable String re) {
        UsuarioResponse usuario = usuarioService.buscarPorRe(re);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/senha")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> atualizarSenha(
            @PathVariable Long id,
            @RequestBody String novaSenha) {
        UsuarioResponse response = usuarioService.atualizarSenha(id, novaSenha);
        return ResponseEntity.ok(response);
    }
}

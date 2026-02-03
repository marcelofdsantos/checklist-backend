package com.deicmar.checklist.controller;

import com.deicmar.checklist.dto.empilhadeira.BloquearEmpilhadeiraRequest;
import com.deicmar.checklist.dto.empilhadeira.EmpilhadeiraRequest;
import com.deicmar.checklist.dto.empilhadeira.EmpilhadeiraResponse;
import com.deicmar.checklist.service.EmpilhadeiraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empilhadeiras")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
public class EmpilhadeiraController {

    private final EmpilhadeiraService empilhadeiraService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<EmpilhadeiraResponse> criar(@Valid @RequestBody EmpilhadeiraRequest request) {
        EmpilhadeiraResponse response = empilhadeiraService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmpilhadeiraResponse>> listarTodas() {
        List<EmpilhadeiraResponse> empilhadeiras = empilhadeiraService.listarTodas();
        return ResponseEntity.ok(empilhadeiras);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<EmpilhadeiraResponse>> listarAtivas() {
        List<EmpilhadeiraResponse> empilhadeiras = empilhadeiraService.listarAtivas();
        return ResponseEntity.ok(empilhadeiras);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<EmpilhadeiraResponse>> listarDisponiveis() {
        List<EmpilhadeiraResponse> empilhadeiras = empilhadeiraService.listarDisponiveis();
        return ResponseEntity.ok(empilhadeiras);
    }

    @GetMapping("/bloqueadas")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<EmpilhadeiraResponse>> listarBloqueadas() {
        List<EmpilhadeiraResponse> empilhadeiras = empilhadeiraService.listarBloqueadas();
        return ResponseEntity.ok(empilhadeiras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpilhadeiraResponse> buscarPorId(@PathVariable Long id) {
        EmpilhadeiraResponse empilhadeira = empilhadeiraService.buscarPorId(id);
        return ResponseEntity.ok(empilhadeira);
    }

    @PatchMapping("/{id}/bloquear")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<EmpilhadeiraResponse> bloquear(
            @PathVariable Long id,
            @Valid @RequestBody BloquearEmpilhadeiraRequest request) {
        EmpilhadeiraResponse response = empilhadeiraService.bloquear(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desbloquear")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<EmpilhadeiraResponse> desbloquear(@PathVariable Long id) {
        EmpilhadeiraResponse response = empilhadeiraService.desbloquear(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        empilhadeiraService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}

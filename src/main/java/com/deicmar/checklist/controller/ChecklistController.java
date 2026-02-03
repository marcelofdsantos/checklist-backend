package com.deicmar.checklist.controller;

import com.deicmar.checklist.dto.checklist.ChecklistRequest;
import com.deicmar.checklist.dto.checklist.ChecklistResponse;
import com.deicmar.checklist.service.ChecklistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'OPERADOR')")
    public ResponseEntity<ChecklistResponse> criar(@Valid @RequestBody ChecklistRequest request) {
        ChecklistResponse response = checklistService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<ChecklistResponse>> listarTodos() {
        List<ChecklistResponse> checklists = checklistService.listarTodos();
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'OPERADOR')")
    public ResponseEntity<ChecklistResponse> buscarPorId(@PathVariable Long id) {
        ChecklistResponse checklist = checklistService.buscarPorId(id);
        return ResponseEntity.ok(checklist);
    }

    @GetMapping("/empilhadeira/{empilhadeiraId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<ChecklistResponse>> listarPorEmpilhadeira(@PathVariable Long empilhadeiraId) {
        List<ChecklistResponse> checklists = checklistService.listarPorEmpilhadeira(empilhadeiraId);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/operador/{operadorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'OPERADOR')")
    public ResponseEntity<List<ChecklistResponse>> listarPorOperador(@PathVariable Long operadorId) {
        List<ChecklistResponse> checklists = checklistService.listarPorOperador(operadorId);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/data/{data}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<ChecklistResponse>> listarPorData(@PathVariable String data) {
        List<ChecklistResponse> checklists = checklistService.listarPorData(data);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<List<ChecklistResponse>> listarPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        List<ChecklistResponse> checklists = checklistService.listarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(checklists);
    }
}

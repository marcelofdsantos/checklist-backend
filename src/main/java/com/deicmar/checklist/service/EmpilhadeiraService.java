package com.deicmar.checklist.service;

import com.deicmar.checklist.dto.empilhadeira.BloquearEmpilhadeiraRequest;
import com.deicmar.checklist.dto.empilhadeira.EmpilhadeiraRequest;
import com.deicmar.checklist.dto.empilhadeira.EmpilhadeiraResponse;
import com.deicmar.checklist.exception.BusinessException;
import com.deicmar.checklist.exception.ResourceNotFoundException;
import com.deicmar.checklist.mapper.EmpilhadeiraMapper;
import com.deicmar.checklist.model.entity.Empilhadeira;
import com.deicmar.checklist.repository.EmpilhadeiraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmpilhadeiraService {

    private final EmpilhadeiraRepository empilhadeiraRepository;
    private final EmpilhadeiraMapper empilhadeiraMapper;

    @Transactional
    public EmpilhadeiraResponse criar(EmpilhadeiraRequest request) {
        log.debug("Criando empilhadeira modelo: {}", request.getModelo());
        
        Empilhadeira empilhadeira = empilhadeiraMapper.toEntity(request);
        Empilhadeira empilhadeiraSalva = empilhadeiraRepository.save(empilhadeira);
        
        log.info("Empilhadeira criada com sucesso. ID: {}, Modelo: {}", 
                empilhadeiraSalva.getId(), empilhadeiraSalva.getModelo());
        
        return empilhadeiraMapper.toResponse(empilhadeiraSalva);
    }

    @Transactional(readOnly = true)
    public List<EmpilhadeiraResponse> listarTodas() {
        log.debug("Listando todas as empilhadeiras");
        return empilhadeiraRepository.findAll().stream()
                .map(empilhadeiraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmpilhadeiraResponse> listarAtivas() {
        log.debug("Listando empilhadeiras ativas");
        return empilhadeiraRepository.findByAtivaTrue().stream()
                .map(empilhadeiraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmpilhadeiraResponse> listarDisponiveis() {
        log.debug("Listando empilhadeiras disponíveis (não bloqueadas)");
        return empilhadeiraRepository.findByBloqueadaFalseAndAtivaTrue().stream()
                .map(empilhadeiraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmpilhadeiraResponse> listarBloqueadas() {
        log.debug("Listando empilhadeiras bloqueadas");
        return empilhadeiraRepository.findByBloqueadaTrue().stream()
                .map(empilhadeiraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpilhadeiraResponse buscarPorId(Long id) {
        log.debug("Buscando empilhadeira por ID: {}", id);
        Empilhadeira empilhadeira = empilhadeiraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empilhadeira não encontrada com ID: " + id));
        return empilhadeiraMapper.toResponse(empilhadeira);
    }

    @Transactional
    public EmpilhadeiraResponse bloquear(Long id, BloquearEmpilhadeiraRequest request) {
        log.debug("Bloqueando empilhadeira ID: {}", id);
        
        Empilhadeira empilhadeira = empilhadeiraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empilhadeira não encontrada com ID: " + id));
        
        if (empilhadeira.getBloqueada()) {
            throw new BusinessException("Empilhadeira já está bloqueada");
        }
        
        empilhadeira.setBloqueada(true);
        empilhadeira.setMotivoBloqueio(request.getMotivo());
        
        Empilhadeira empilhadeiraAtualizada = empilhadeiraRepository.save(empilhadeira);
        log.info("Empilhadeira bloqueada com sucesso. ID: {}", id);
        
        return empilhadeiraMapper.toResponse(empilhadeiraAtualizada);
    }

    @Transactional
    public EmpilhadeiraResponse desbloquear(Long id) {
        log.debug("Desbloqueando empilhadeira ID: {}", id);
        
        Empilhadeira empilhadeira = empilhadeiraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empilhadeira não encontrada com ID: " + id));
        
        if (!empilhadeira.getBloqueada()) {
            throw new BusinessException("Empilhadeira não está bloqueada");
        }
        
        empilhadeira.setBloqueada(false);
        empilhadeira.setMotivoBloqueio(null);
        
        Empilhadeira empilhadeiraAtualizada = empilhadeiraRepository.save(empilhadeira);
        log.info("Empilhadeira desbloqueada com sucesso. ID: {}", id);
        
        return empilhadeiraMapper.toResponse(empilhadeiraAtualizada);
    }

    @Transactional
    public void inativar(Long id) {
        log.debug("Inativando empilhadeira ID: {}", id);
        
        Empilhadeira empilhadeira = empilhadeiraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empilhadeira não encontrada com ID: " + id));
        
        empilhadeira.setAtiva(false);
        empilhadeiraRepository.save(empilhadeira);
        log.info("Empilhadeira inativada com sucesso. ID: {}", id);
    }
}

package com.deicmar.checklist.service;

import com.deicmar.checklist.dto.checklist.ChecklistRequest;
import com.deicmar.checklist.dto.checklist.ChecklistResponse;
import com.deicmar.checklist.dto.checklist.ItemChecklistRequest;
import com.deicmar.checklist.exception.BusinessException;
import com.deicmar.checklist.exception.ResourceNotFoundException;
import com.deicmar.checklist.mapper.ChecklistMapper;
import com.deicmar.checklist.model.entity.Checklist;
import com.deicmar.checklist.model.entity.Empilhadeira;
import com.deicmar.checklist.model.entity.ItemChecklist;
import com.deicmar.checklist.model.entity.Usuario;
import com.deicmar.checklist.model.enums.ResultadoChecklist;
import com.deicmar.checklist.model.enums.StatusItem;
import com.deicmar.checklist.model.enums.TipoItem;
import com.deicmar.checklist.repository.ChecklistRepository;
import com.deicmar.checklist.repository.EmpilhadeiraRepository;
import com.deicmar.checklist.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpilhadeiraRepository empilhadeiraRepository;
    private final ChecklistMapper checklistMapper;

    @Transactional
    public ChecklistResponse criar(ChecklistRequest request) {
        log.debug("Criando checklist para empilhadeira ID: {}", request.getEmpilhadeiraId());
        
        // Buscar operador
        Usuario operador = usuarioRepository.findById(request.getOperadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Operador não encontrado com ID: " + request.getOperadorId()));
        
        if (!operador.getAtivo()) {
            throw new BusinessException("Operador inativo");
        }
        
        // Buscar empilhadeira
        Empilhadeira empilhadeira = empilhadeiraRepository.findById(request.getEmpilhadeiraId())
                .orElseThrow(() -> new ResourceNotFoundException("Empilhadeira não encontrada com ID: " + request.getEmpilhadeiraId()));
        
        if (!empilhadeira.getAtiva()) {
            throw new BusinessException("Empilhadeira inativa");
        }
        
        if (empilhadeira.getBloqueada()) {
            throw new BusinessException("Empilhadeira bloqueada: " + empilhadeira.getMotivoBloqueio());
        }
        
        // Validar horímetros
        if (request.getHorimetroFinal() != null && request.getHorimetroFinal() < request.getHorimetroInicial()) {
            throw new BusinessException("Horímetro final não pode ser menor que o inicial");
        }

        // Parse data e hora com mensagens claras em caso de formato inválido
        LocalDate data;
        try {
            data = LocalDate.parse(request.getData());
        } catch (DateTimeParseException ex) {
            throw new BusinessException("Formato de data inválido. Use YYYY-MM-DD");
        }

        LocalTime horaVistoria;
        try {
            DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("HH:mm")
                    .optionalStart().appendPattern(":ss").optionalEnd()
                    .toFormatter();
            horaVistoria = LocalTime.parse(request.getHoraVistoria(), timeFormatter);
        } catch (DateTimeParseException ex) {
            throw new BusinessException("Formato de hora inválido. Use HH:mm[:ss]");
        }

        // Evitar criar duplicate checklists para a mesma empilhadeira e data
        List<Checklist> existentesNoDia = checklistRepository.findByEmpilhadeiraIdAndData(empilhadeira.getId(), data);
        if (existentesNoDia != null && !existentesNoDia.isEmpty()) {
            throw new BusinessException("Já existe checklist para a empilhadeira ID " + empilhadeira.getId() + " na data " + data);
        }

        // Criar checklist
        Checklist checklist = Checklist.builder()
                .data(data)
                .horaVistoria(horaVistoria)
                .turno(request.getTurno())
                .horimetroInicial(request.getHorimetroInicial())
                .horimetroFinal(request.getHorimetroFinal())
                .operador(operador)
                .empilhadeira(empilhadeira)
                .observacaoGeral(request.getObservacaoGeral())
                .build();
        
        // Calcular resultado e criar itens
        ResultadoChecklist resultado = calcularResultado(request.getItens());
        checklist.setResultado(resultado);
        
        // Adicionar itens
        for (ItemChecklistRequest itemRequest : request.getItens()) {
            ItemChecklist item = ItemChecklist.builder()
                    .descricao(itemRequest.getDescricao())
                    .tipo(itemRequest.getTipo())
                    .status(itemRequest.getStatus())
                    .observacao(itemRequest.getObservacao())
                    .build();
            
            checklist.adicionarItem(item);
        }
        
        Checklist checklistSalvo;
        try {
            checklistSalvo = checklistRepository.save(checklist);
        } catch (Exception ex) {
            log.error("Erro ao persistir checklist no banco", ex);
            throw new BusinessException("Erro ao salvar checklist: " + ex.getMessage());
        }

        // Bloquear empilhadeira se reprovado
        if (resultado == ResultadoChecklist.REPROVADO) {
            empilhadeira.setBloqueada(true);
            empilhadeira.setMotivoBloqueio("Checklist reprovado em " + checklist.getData());
            empilhadeiraRepository.save(empilhadeira);
            log.info("Empilhadeira ID {} bloqueada devido a checklist reprovado", empilhadeira.getId());
        }
        
        log.info("Checklist criado com sucesso. ID: {}, Resultado: {}", checklistSalvo.getId(), resultado);
        
        return checklistMapper.toResponse(checklistSalvo);
    }
    
    private ResultadoChecklist calcularResultado(List<ItemChecklistRequest> itens) {
        boolean temItemImpeditivo = itens.stream()
                .anyMatch(item -> item.getTipo() == TipoItem.IMPEDITIVO && item.getStatus() == StatusItem.NAO_CONFORME);
        
        return temItemImpeditivo ? ResultadoChecklist.REPROVADO : ResultadoChecklist.APROVADO;
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> listarTodos() {
        log.debug("Listando todos os checklists");
        return checklistRepository.findAllOrderByDataDesc().stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChecklistResponse buscarPorId(Long id) {
        log.debug("Buscando checklist por ID: {}", id);
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist não encontrado com ID: " + id));
        return checklistMapper.toResponse(checklist);
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> listarPorEmpilhadeira(Long empilhadeiraId) {
        log.debug("Listando checklists por empilhadeira ID: {}", empilhadeiraId);
        
        if (!empilhadeiraRepository.existsById(empilhadeiraId)) {
            throw new ResourceNotFoundException("Empilhadeira não encontrada com ID: " + empilhadeiraId);
        }
        
        return checklistRepository.findByEmpilhadeiraId(empilhadeiraId).stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> listarPorOperador(Long operadorId) {
        log.debug("Listando checklists por operador ID: {}", operadorId);
        
        if (!usuarioRepository.existsById(operadorId)) {
            throw new ResourceNotFoundException("Operador não encontrado com ID: " + operadorId);
        }
        
        return checklistRepository.findByOperadorId(operadorId).stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> listarPorData(String data) {
        log.debug("Listando checklists por data: {}", data);
        LocalDate localDate = LocalDate.parse(data);
        
        return checklistRepository.findByData(localDate).stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> listarPorPeriodo(String dataInicio, String dataFim) {
        log.debug("Listando checklists por período: {} a {}", dataInicio, dataFim);
        
        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        
        if (fim.isBefore(inicio)) {
            throw new BusinessException("Data fim não pode ser anterior à data início");
        }
        
        return checklistRepository.findByDataBetween(inicio, fim).stream()
                .map(checklistMapper::toResponse)
                .collect(Collectors.toList());
    }
}

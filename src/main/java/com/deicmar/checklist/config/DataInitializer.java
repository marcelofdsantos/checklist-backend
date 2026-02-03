package com.deicmar.checklist.config;

import com.deicmar.checklist.model.entity.Empilhadeira;
import com.deicmar.checklist.model.entity.Usuario;
import com.deicmar.checklist.model.enums.Perfil;
import com.deicmar.checklist.repository.EmpilhadeiraRepository;
import com.deicmar.checklist.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final EmpilhadeiraRepository empilhadeiraRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            log.info("===================================================");
            log.info("INICIALIZANDO DADOS DO SISTEMA");
            log.info("===================================================");
            
            // Criar usuários se não existirem
            criarUsuarios();
            
            // Criar empilhadeiras se não existirem
            criarEmpilhadeiras();
            
            log.info("===================================================");
            log.info("DADOS INICIALIZADOS COM SUCESSO!");
            log.info("===================================================");
        };
    }
    
    private void criarUsuarios() {
        // Admin
        if (!usuarioRepository.existsByRe("ADMIN")) {
            Usuario admin = Usuario.builder()
                    .re("ADMIN")
                    .nomeCompleto("Administrador do Sistema")
                    .senha(passwordEncoder.encode("admin123"))
                    .perfil(Perfil.ADMIN)
                    .ativo(true)
                    .build();
            usuarioRepository.save(admin);
            log.info("✓ Usuário ADMIN criado - RE: ADMIN, Senha: admin123");
        }
        
        // Supervisor
        if (!usuarioRepository.existsByRe("SUP001")) {
            Usuario supervisor = Usuario.builder()
                    .re("SUP001")
                    .nomeCompleto("Supervisor Geral")
                    .senha(passwordEncoder.encode("senha123"))
                    .perfil(Perfil.SUPERVISOR)
                    .ativo(true)
                    .build();
            usuarioRepository.save(supervisor);
            log.info("✓ Usuário SUPERVISOR criado - RE: SUP001, Senha: senha123");
        }
        
        // Operadores
        if (!usuarioRepository.existsByRe("OPR001")) {
            Usuario operador1 = Usuario.builder()
                    .re("OPR001")
                    .nomeCompleto("João da Silva")
                    .senha(passwordEncoder.encode("senha123"))
                    .perfil(Perfil.OPERADOR)
                    .ativo(true)
                    .build();
            usuarioRepository.save(operador1);
            log.info("✓ Usuário OPERADOR criado - RE: OPR001, Senha: senha123");
        }
        
        if (!usuarioRepository.existsByRe("OPR002")) {
            Usuario operador2 = Usuario.builder()
                    .re("OPR002")
                    .nomeCompleto("Maria Santos")
                    .senha(passwordEncoder.encode("senha123"))
                    .perfil(Perfil.OPERADOR)
                    .ativo(true)
                    .build();
            usuarioRepository.save(operador2);
            log.info("✓ Usuário OPERADOR criado - RE: OPR002, Senha: senha123");
        }
        
        if (!usuarioRepository.existsByRe("OPR003")) {
            Usuario operador3 = Usuario.builder()
                    .re("OPR003")
                    .nomeCompleto("Pedro Oliveira")
                    .senha(passwordEncoder.encode("senha123"))
                    .perfil(Perfil.OPERADOR)
                    .ativo(true)
                    .build();
            usuarioRepository.save(operador3);
            log.info("✓ Usuário OPERADOR criado - RE: OPR003, Senha: senha123");
        }

        if (!usuarioRepository.existsByRe("313682")) {
            Usuario operador3 = Usuario.builder()
                    .re("313682")
                    .nomeCompleto("Marcelo santos")
                    .senha(passwordEncoder.encode("senha123"))
                    .perfil(Perfil.OPERADOR)
                    .ativo(true)
                    .build();
            usuarioRepository.save(operador3);
            log.info("✓ Usuário OPERADOR criado - RE: 313682, Senha: senha123");
        }
    }
    
    private void criarEmpilhadeiras() {
        if (empilhadeiraRepository.count() == 0) {
            // Empilhadeira 1
            Empilhadeira emp1 = Empilhadeira.builder()
                    .modelo("YALE GP030")
                    .tipo("Elétrica")
                    .capacidade(3000)
                    .bloqueada(false)
                    .ativa(true)
                    .build();
            empilhadeiraRepository.save(emp1);
            
            // Empilhadeira 2
            Empilhadeira emp2 = Empilhadeira.builder()
                    .modelo("MODELO -> TOYOTA/FROTA -> 224 ")
                    .tipo("Elétrica")
                    .capacidade(2000)
                    .bloqueada(false)
                    .ativa(true)
                    .build();
            empilhadeiraRepository.save(emp2);
            
            // Empilhadeira 3
            Empilhadeira emp3 = Empilhadeira.builder()
                    .modelo("HYSTER H50FT")
                    .tipo("GLP")
                    .capacidade(5000)
                    .bloqueada(false)
                    .ativa(true)
                    .build();
            empilhadeiraRepository.save(emp3);
            
            // Empilhadeira 4
            Empilhadeira emp4 = Empilhadeira.builder()
                    .modelo("CATERPILLAR GP25N")
                    .tipo("Diesel")
                    .capacidade(2500)
                    .bloqueada(false)
                    .ativa(true)
                    .build();
            empilhadeiraRepository.save(emp4);
            
            // Empilhadeira 5 (bloqueada para testes)
            Empilhadeira emp5 = Empilhadeira.builder()
                    .modelo("LINDE E16")
                    .tipo("Elétrica")
                    .capacidade(1600)
                    .bloqueada(true)
                    .motivoBloqueio("Manutenção preventiva programada")
                    .ativa(true)
                    .build();
            empilhadeiraRepository.save(emp5);
            
            // Empilhadeira 6
            Empilhadeira emp6 = Empilhadeira.builder()
                    .modelo("STILL RX60-30")
                    .tipo("Elétrica")
                    .capacidade(3000)
                    .bloqueada(false)
                    .ativa(true)
                    .build();
            empilhadeiraRepository.save(emp6);
            
            log.info("✓ 6 Empilhadeiras criadas (5 disponíveis, 1 bloqueada)");
        }
    }
}

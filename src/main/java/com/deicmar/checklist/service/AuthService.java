package com.deicmar.checklist.service;

import com.deicmar.checklist.dto.auth.LoginRequest;
import com.deicmar.checklist.dto.auth.LoginResponse;
import com.deicmar.checklist.exception.BusinessException;
import com.deicmar.checklist.model.entity.Usuario;
import com.deicmar.checklist.repository.UsuarioRepository;
import com.deicmar.checklist.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.debug("Tentativa de login para RE: {}", request.getRe());
        
        Usuario usuario = usuarioRepository.findByRe(request.getRe())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!usuario.getAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getRe(), usuario.getPerfil().name());
        
        log.info("Login realizado com sucesso para RE: {}", request.getRe());
        
        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .re(usuario.getRe())
                .nomeCompleto(usuario.getNomeCompleto())
                .perfil(usuario.getPerfil())
                .build();
    }
}

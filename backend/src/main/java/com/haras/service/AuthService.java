package com.haras.service;

import com.haras.dto.LoginRequest;
import com.haras.dto.LoginResponse;
import com.haras.security.JwtService;
import com.haras.security.PessoaUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        // Lança BadCredentialsException se email/senha não baterem (tratado no GlobalExceptionHandler -> 401)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        PessoaUserDetails user = (PessoaUserDetails) auth.getPrincipal();
        String token = jwtService.gerarToken(user);
        return new LoginResponse(token, user.getIdPessoa(), user.getUsername(), user.getRoles());
    }
}

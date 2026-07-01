package com.haras.dto;

import java.util.List;

public record LoginResponse(
        String token,
        int idPessoa,
        String email,
        List<String> roles
) {}

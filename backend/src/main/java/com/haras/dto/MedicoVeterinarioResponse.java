package com.haras.dto;

public record MedicoVeterinarioResponse(
        Integer idPessoa,
        String nomePessoa,
        String emailPessoa,
        String numCrmv,
        String ufCrmv
) {}

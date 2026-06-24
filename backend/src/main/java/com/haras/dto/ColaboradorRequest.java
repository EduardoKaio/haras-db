package com.haras.dto;

import jakarta.validation.constraints.NotNull;

public record ColaboradorRequest(
        @NotNull(message = "Pessoa é obrigatória")
        Integer idPessoa
) {}

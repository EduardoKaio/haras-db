package com.haras.dto;

import jakarta.validation.constraints.NotNull;

public record EquinoVinculoRequest(
        @NotNull(message = "Equino é obrigatório")
        Integer idEquino
) {}

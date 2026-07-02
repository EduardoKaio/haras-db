package com.haras.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record LimpezaRequest(
        @NotNull(message = "Data/hora é obrigatória")
        LocalDateTime dataHora,

        @NotEmpty(message = "Informe ao menos um equino")
        List<@NotNull(message = "Equino inválido") Integer> idsEquinos,

        @NotEmpty(message = "Informe ao menos um tratador")
        List<@NotNull(message = "Tratador inválido") Integer> idsTratadores
) {}

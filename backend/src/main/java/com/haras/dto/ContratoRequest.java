package com.haras.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ContratoRequest(
        @NotNull(message = "Colaborador é obrigatório")
        Integer idPessoa,

        @NotNull(message = "Data de início é obrigatória")
        @PastOrPresent(message = "Data de início não pode estar no futuro")
        LocalDate dataInicio,

        LocalDate dataFim,

        @NotNull(message = "Salário é obrigatório")
        @Positive(message = "Salário deve ser maior que zero")
        Double salario
) {}

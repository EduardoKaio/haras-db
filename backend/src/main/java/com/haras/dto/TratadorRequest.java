package com.haras.dto;

import jakarta.validation.constraints.NotNull;

public record TratadorRequest(
        @NotNull(message = "Colaborador é obrigatório")
        Integer idPessoa
) {}

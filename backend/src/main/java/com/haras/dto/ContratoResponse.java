package com.haras.dto;

import java.time.LocalDate;

public record ContratoResponse(
        Integer idContrato,
        Integer idPessoa,
        String nomeColaborador,
        LocalDate dataInicio,
        LocalDate dataFim,
        Double salario
) {}

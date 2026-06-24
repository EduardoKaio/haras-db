package com.haras.model;

import java.time.LocalDate;

public record Contrato(
        Integer idContrato,
        Integer idPessoa,
        LocalDate dataInicio,
        LocalDate dataFim,
        Double salario
) {}

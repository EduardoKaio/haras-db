package com.haras.model;

import java.time.LocalDate;

public record Equino(
        Integer idEquino,
        String nome,
        String raca,
        Double peso,
        String funcao,
        LocalDate dataNascimento,
        String status,
        String registro,
        String registroPai,
        String registroMae,
        String pelagem
) {}

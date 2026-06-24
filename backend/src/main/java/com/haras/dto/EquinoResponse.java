package com.haras.dto;

import java.time.LocalDate;

public record EquinoResponse(
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

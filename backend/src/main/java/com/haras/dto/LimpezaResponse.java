package com.haras.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LimpezaResponse(
        Integer idLimpeza,
        LocalDateTime dataHora,
        List<EquinoResumo> equinos,
        List<TratadorResumo> tratadores
) {
    public record EquinoResumo(Integer idEquino, String nome) {}

    public record TratadorResumo(Integer idPessoa, String nomePessoa) {}
}

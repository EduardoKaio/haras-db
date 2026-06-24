package com.haras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * idPessoa só é usado na criação (escolhe uma Pessoa existente). No update,
 * o id vem da URL e o corpo só precisa de numCrmv/ufCrmv — mandar idPessoa
 * também não tem efeito, o controller ignora.
 */
public record MedicoVeterinarioRequest(
        @NotNull(message = "Pessoa é obrigatória")
        Integer idPessoa,

        @NotBlank(message = "Número do CRMV é obrigatório")
        @Size(max = 5, message = "Número do CRMV deve ter no máximo 5 caracteres")
        String numCrmv,

        @NotBlank(message = "UF do CRMV é obrigatória")
        @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
        String ufCrmv
) {}

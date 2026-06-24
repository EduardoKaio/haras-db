package com.haras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquinoRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
        String nome,

        @NotBlank(message = "Raça é obrigatória")
        @Size(max = 45, message = "Raça deve ter no máximo 45 caracteres")
        String raca,

        @NotNull(message = "Peso é obrigatório")
        @Positive(message = "Peso deve ser maior que zero")
        Double peso,

        @NotBlank(message = "Função é obrigatória")
        @Size(max = 25, message = "Função deve ter no máximo 25 caracteres")
        String funcao,

        @NotNull(message = "Data de nascimento é obrigatória")
        @PastOrPresent(message = "Data de nascimento não pode estar no futuro")
        LocalDate dataNascimento,

        @NotBlank(message = "Status é obrigatório")
        @Size(max = 25, message = "Status deve ter no máximo 25 caracteres")
        String status,

        @NotBlank(message = "Registro é obrigatório")
        @Size(max = 25, message = "Registro deve ter no máximo 25 caracteres")
        String registro,

        @Size(max = 25, message = "Registro do pai deve ter no máximo 25 caracteres")
        String registroPai,

        @Size(max = 25, message = "Registro da mãe deve ter no máximo 25 caracteres")
        String registroMae,

        @NotBlank(message = "Pelagem é obrigatória")
        @Size(max = 25, message = "Pelagem deve ter no máximo 25 caracteres")
        String pelagem
) {}

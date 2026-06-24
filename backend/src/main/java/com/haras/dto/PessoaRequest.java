package com.haras.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * senha não tem @NotBlank aqui de propósito: na criação é obrigatória,
 * mas na edição pode vir em branco (o front nunca recebe a senha atual de volta,
 * então "em branco" significa "manter a senha existente"). Validação fica em PessoaController.
 */
public record PessoaRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos numéricos")
        String cpf,

        boolean gerente,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @Size(max = 45, message = "Senha deve ter no máximo 45 caracteres")
        String senha
) {}

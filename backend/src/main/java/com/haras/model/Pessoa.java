package com.haras.model;

import java.time.LocalDate;

public record Pessoa(
        Integer idPessoa,
        String nome,
        LocalDate dataNascimento,
        String cpf,
        boolean gerente,
        String email,
        String senha
) {}

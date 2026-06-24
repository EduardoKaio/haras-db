package com.haras.dto;

import java.time.LocalDate;

public record PessoaResponse(
        Integer idPessoa,
        String nome,
        LocalDate dataNascimento,
        String cpf,
        boolean gerente,
        String email
) {}

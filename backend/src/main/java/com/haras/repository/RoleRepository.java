package com.haras.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Resolve os papéis (roles) de uma pessoa a partir das tabelas de especialização do DER.
 * O papel GERENTE não sai daqui — vem da flag is_gerente na própria Pessoa (ver PessoaUserDetails).
 */
@Repository
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findRoles(int idPessoa) {
        String sql = """
                SELECT 'VETERINARIO' AS role FROM MedicoVeterinario WHERE id_pessoa = ?
                UNION ALL SELECT 'PROPRIETARIO' FROM Proprietario     WHERE id_pessoa = ?
                UNION ALL SELECT 'TRATADOR'     FROM Tratador          WHERE id_pessoa = ?
                UNION ALL SELECT 'TREINADOR'    FROM Treinador         WHERE id_pessoa = ?
                UNION ALL SELECT 'COMPETIDOR'   FROM Competidor        WHERE id_pessoa = ?
                """;
        return jdbcTemplate.queryForList(sql, String.class,
                idPessoa, idPessoa, idPessoa, idPessoa, idPessoa);
    }
}

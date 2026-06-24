package com.haras.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ColaboradorRepository {

    private static final String SELECT_BASE = """
            SELECT c.id_pessoa, p.nome AS nome_pessoa, p.email AS email_pessoa
            FROM Colaborador c
            JOIN Pessoa p ON p.id_pessoa = c.id_pessoa
            """;

    private final JdbcTemplate jdbcTemplate;

    public ColaboradorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record ColaboradorComPessoa(Integer idPessoa, String nomePessoa, String emailPessoa) {}

    private static ColaboradorComPessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ColaboradorComPessoa(
                rs.getInt("id_pessoa"), rs.getString("nome_pessoa"), rs.getString("email_pessoa"));
    }

    public List<ColaboradorComPessoa> findAll() {
        return jdbcTemplate.query(SELECT_BASE + " ORDER BY p.nome", ColaboradorRepository::mapRow);
    }

    public Optional<ColaboradorComPessoa> findById(int idPessoa) {
        return jdbcTemplate.query(SELECT_BASE + " WHERE c.id_pessoa = ?", ColaboradorRepository::mapRow, idPessoa)
                .stream().findFirst();
    }

    public boolean existsById(int idPessoa) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Colaborador WHERE id_pessoa = ?", Integer.class, idPessoa);
        return count != null && count > 0;
    }

    public void insert(int idPessoa) {
        jdbcTemplate.update("INSERT INTO Colaborador (id_pessoa) VALUES (?)", idPessoa);
    }

    public void delete(int idPessoa) {
        jdbcTemplate.update("DELETE FROM Colaborador WHERE id_pessoa = ?", idPessoa);
    }
}

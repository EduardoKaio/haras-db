package com.haras.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TratadorRepository {

    private static final String SELECT_BASE = """
            SELECT t.id_pessoa, p.nome AS nome_pessoa, p.email AS email_pessoa
            FROM Tratador t
            JOIN Pessoa p ON p.id_pessoa = t.id_pessoa
            """;

    private final JdbcTemplate jdbcTemplate;

    public TratadorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record TratadorComPessoa(Integer idPessoa, String nomePessoa, String emailPessoa) {}

    private static TratadorComPessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TratadorComPessoa(
                rs.getInt("id_pessoa"), rs.getString("nome_pessoa"), rs.getString("email_pessoa"));
    }

    public List<TratadorComPessoa> findAll() {
        return jdbcTemplate.query(SELECT_BASE + " ORDER BY p.nome", TratadorRepository::mapRow);
    }

    public Optional<TratadorComPessoa> findById(int idPessoa) {
        return jdbcTemplate.query(SELECT_BASE + " WHERE t.id_pessoa = ?", TratadorRepository::mapRow, idPessoa)
                .stream().findFirst();
    }

    public boolean existsById(int idPessoa) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Tratador WHERE id_pessoa = ?", Integer.class, idPessoa);
        return count != null && count > 0;
    }

    public void insert(int idPessoa) {
        jdbcTemplate.update("INSERT INTO Tratador (id_pessoa) VALUES (?)", idPessoa);
    }

    public void delete(int idPessoa) {
        jdbcTemplate.update("DELETE FROM Tratador WHERE id_pessoa = ?", idPessoa);
    }
}

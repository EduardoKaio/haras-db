package com.haras.repository;

import com.haras.model.Contrato;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ContratoRepository {

    private static final String SELECT_BASE = """
            SELECT ct.id_contrato, ct.id_pessoa, ct.data_inicio, ct.data_fim, ct.salario, p.nome AS nome_pessoa
            FROM Contrato ct
            JOIN Colaborador c ON c.id_pessoa = ct.id_pessoa
            JOIN Pessoa p ON p.id_pessoa = c.id_pessoa
            """;

    private final JdbcTemplate jdbcTemplate;

    public ContratoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record ContratoComColaborador(
            Integer idContrato, Integer idPessoa, java.time.LocalDate dataInicio,
            java.time.LocalDate dataFim, Double salario, String nomePessoa) {}

    private static ContratoComColaborador mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date dataFim = rs.getDate("data_fim");
        return new ContratoComColaborador(
                rs.getInt("id_contrato"),
                rs.getInt("id_pessoa"),
                rs.getDate("data_inicio").toLocalDate(),
                dataFim != null ? dataFim.toLocalDate() : null,
                rs.getDouble("salario"),
                rs.getString("nome_pessoa"));
    }

    public List<ContratoComColaborador> findAll() {
        return jdbcTemplate.query(SELECT_BASE + " ORDER BY ct.data_inicio DESC", ContratoRepository::mapRow);
    }

    public Optional<ContratoComColaborador> findById(int id) {
        return jdbcTemplate.query(SELECT_BASE + " WHERE ct.id_contrato = ?", ContratoRepository::mapRow, id)
                .stream().findFirst();
    }

    public int insert(Contrato contrato) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Contrato (data_inicio, data_fim, salario, id_pessoa) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(contrato.dataInicio()));
            ps.setDate(2, contrato.dataFim() != null ? Date.valueOf(contrato.dataFim()) : null);
            ps.setDouble(3, contrato.salario());
            ps.setInt(4, contrato.idPessoa());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void update(int id, Contrato contrato) {
        jdbcTemplate.update(
                "UPDATE Contrato SET data_inicio = ?, data_fim = ?, salario = ?, id_pessoa = ? WHERE id_contrato = ?",
                Date.valueOf(contrato.dataInicio()),
                contrato.dataFim() != null ? Date.valueOf(contrato.dataFim()) : null,
                contrato.salario(), contrato.idPessoa(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Contrato WHERE id_contrato = ?", id);
    }
}

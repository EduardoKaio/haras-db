package com.haras.repository;

import com.haras.dto.LimpezaResponse.EquinoResumo;
import com.haras.dto.LimpezaResponse.TratadorResumo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class LimpezaRepository {

    private final JdbcTemplate jdbcTemplate;

    public LimpezaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record LimpezaRow(Integer idLimpeza, LocalDateTime dataHora) {}

    private static LimpezaRow mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new LimpezaRow(rs.getInt("id_limpeza"), rs.getTimestamp("data_hora").toLocalDateTime());
    }

    public List<LimpezaRow> findAll() {
        return jdbcTemplate.query("SELECT * FROM Limpeza ORDER BY data_hora DESC", LimpezaRepository::mapRow);
    }

    public Optional<LimpezaRow> findById(int idLimpeza) {
        return jdbcTemplate.query("SELECT * FROM Limpeza WHERE id_limpeza = ?", LimpezaRepository::mapRow, idLimpeza)
                .stream().findFirst();
    }

    public int insert(LocalDateTime dataHora) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Limpeza (data_hora) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(dataHora));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void delete(int idLimpeza) {
        jdbcTemplate.update("DELETE FROM Limpeza WHERE id_limpeza = ?", idLimpeza);
    }

    // N:N com Equino
    public void vincularEquino(int idLimpeza, int idEquino) {
        jdbcTemplate.update(
                "INSERT INTO Limpeza_has_Equino (id_limpeza, id_equino) VALUES (?, ?)", idLimpeza, idEquino);
    }

    public void desvincularEquinos(int idLimpeza) {
        jdbcTemplate.update("DELETE FROM Limpeza_has_Equino WHERE id_limpeza = ?", idLimpeza);
    }

    public List<EquinoResumo> listarEquinos(int idLimpeza) {
        return jdbcTemplate.query("""
                SELECT e.id_equino, e.nome
                FROM Equino e
                JOIN Limpeza_has_Equino le ON le.id_equino = e.id_equino
                WHERE le.id_limpeza = ?
                ORDER BY e.nome
                """,
                (rs, rowNum) -> new EquinoResumo(rs.getInt("id_equino"), rs.getString("nome")),
                idLimpeza);
    }

    // N:N com Tratador
    public void vincularTratador(int idLimpeza, int idPessoa) {
        jdbcTemplate.update(
                "INSERT INTO Tratador_has_Limpeza (id_pessoa, id_limpeza) VALUES (?, ?)", idPessoa, idLimpeza);
    }

    public void desvincularTratadores(int idLimpeza) {
        jdbcTemplate.update("DELETE FROM Tratador_has_Limpeza WHERE id_limpeza = ?", idLimpeza);
    }

    public List<TratadorResumo> listarTratadores(int idLimpeza) {
        return jdbcTemplate.query("""
                SELECT t.id_pessoa, p.nome AS nome_pessoa
                FROM Tratador t
                JOIN Tratador_has_Limpeza tl ON tl.id_pessoa = t.id_pessoa
                JOIN Pessoa p ON p.id_pessoa = t.id_pessoa
                WHERE tl.id_limpeza = ?
                ORDER BY p.nome
                """,
                (rs, rowNum) -> new TratadorResumo(rs.getInt("id_pessoa"), rs.getString("nome_pessoa")),
                idLimpeza);
    }
}

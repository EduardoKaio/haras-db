package com.haras.repository;

import com.haras.model.Equino;
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
public class EquinoRepository {

    private final JdbcTemplate jdbcTemplate;

    public EquinoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Equino mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Equino(
                rs.getInt("id_equino"),
                rs.getString("nome"),
                rs.getString("raca"),
                rs.getDouble("peso"),
                rs.getString("funcao"),
                rs.getDate("data_nascimento").toLocalDate(),
                rs.getString("status"),
                rs.getString("registro"),
                rs.getString("registro_pai"),
                rs.getString("registro_mae"),
                rs.getString("pelagem"));
    }

    public List<Equino> findAll() {
        return jdbcTemplate.query("SELECT * FROM Equino ORDER BY nome", EquinoRepository::mapRow);
    }

    public Optional<Equino> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM Equino WHERE id_equino = ?", EquinoRepository::mapRow, id)
                .stream().findFirst();
    }

    public boolean existsByRegistro(String registro) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Equino WHERE registro = ?", Integer.class, registro);
        return count != null && count > 0;
    }

    public boolean existsByRegistroExcludingId(String registro, int excludeId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Equino WHERE registro = ? AND id_equino <> ?",
                Integer.class, registro, excludeId);
        return count != null && count > 0;
    }

    public int insert(Equino equino) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Equino (nome, raca, peso, funcao, data_nascimento, status, registro, registro_pai, registro_mae, pelagem) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, equino.nome());
            ps.setString(2, equino.raca());
            ps.setDouble(3, equino.peso());
            ps.setString(4, equino.funcao());
            ps.setDate(5, Date.valueOf(equino.dataNascimento()));
            ps.setString(6, equino.status());
            ps.setString(7, equino.registro());
            ps.setString(8, equino.registroPai());
            ps.setString(9, equino.registroMae());
            ps.setString(10, equino.pelagem());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void update(int id, Equino equino) {
        jdbcTemplate.update(
                "UPDATE Equino SET nome = ?, raca = ?, peso = ?, funcao = ?, data_nascimento = ?, status = ?, "
                        + "registro = ?, registro_pai = ?, registro_mae = ?, pelagem = ? WHERE id_equino = ?",
                equino.nome(), equino.raca(), equino.peso(), equino.funcao(),
                Date.valueOf(equino.dataNascimento()), equino.status(), equino.registro(),
                equino.registroPai(), equino.registroMae(), equino.pelagem(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Equino WHERE id_equino = ?", id);
    }
}

package com.haras.repository;

import com.haras.model.Equino;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProprietarioRepository {

    private static final String SELECT_BASE = """
            SELECT pr.id_pessoa, p.nome AS nome_pessoa, p.email AS email_pessoa
            FROM Proprietario pr
            JOIN Pessoa p ON p.id_pessoa = pr.id_pessoa
            """;

    private final JdbcTemplate jdbcTemplate;

    public ProprietarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record ProprietarioComPessoa(Integer idPessoa, String nomePessoa, String emailPessoa) {}

    private static ProprietarioComPessoa mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new ProprietarioComPessoa(
                rs.getInt("id_pessoa"), rs.getString("nome_pessoa"), rs.getString("email_pessoa"));
    }

    public List<ProprietarioComPessoa> findAll() {
        return jdbcTemplate.query(SELECT_BASE + " ORDER BY p.nome", ProprietarioRepository::mapRow);
    }

    public Optional<ProprietarioComPessoa> findById(int idPessoa) {
        return jdbcTemplate.query(SELECT_BASE + " WHERE pr.id_pessoa = ?", ProprietarioRepository::mapRow, idPessoa)
                .stream().findFirst();
    }

    public boolean existsById(int idPessoa) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Proprietario WHERE id_pessoa = ?", Integer.class, idPessoa);
        return count != null && count > 0;
    }

    public void insert(int idPessoa) {
        jdbcTemplate.update("INSERT INTO Proprietario (id_pessoa) VALUES (?)", idPessoa);
    }

    public void delete(int idPessoa) {
        jdbcTemplate.update("DELETE FROM Proprietario WHERE id_pessoa = ?", idPessoa);
    }

    public List<Equino> listarEquinos(int idPessoa) {
        return jdbcTemplate.query("""
                SELECT e.* FROM Equino e
                JOIN Proprietario_has_Equino pe ON pe.id_equino = e.id_equino
                WHERE pe.id_pessoa = ?
                ORDER BY e.nome
                """,
                (rs, rowNum) -> new Equino(
                        rs.getInt("id_equino"), rs.getString("nome"), rs.getString("raca"), rs.getDouble("peso"),
                        rs.getString("funcao"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("status"),
                        rs.getString("registro"), rs.getString("registro_pai"), rs.getString("registro_mae"),
                        rs.getString("pelagem")),
                idPessoa);
    }

    public boolean existeVinculo(int idPessoa, int idEquino) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Proprietario_has_Equino WHERE id_pessoa = ? AND id_equino = ?",
                Integer.class, idPessoa, idEquino);
        return count != null && count > 0;
    }

    public void vincularEquino(int idPessoa, int idEquino) {
        jdbcTemplate.update(
                "INSERT INTO Proprietario_has_Equino (id_pessoa, id_equino) VALUES (?, ?)", idPessoa, idEquino);
    }

    public void desvincularEquino(int idPessoa, int idEquino) {
        jdbcTemplate.update(
                "DELETE FROM Proprietario_has_Equino WHERE id_pessoa = ? AND id_equino = ?", idPessoa, idEquino);
    }
}

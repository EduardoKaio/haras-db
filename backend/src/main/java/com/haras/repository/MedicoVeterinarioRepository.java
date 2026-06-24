package com.haras.repository;

import com.haras.model.MedicoVeterinario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicoVeterinarioRepository {

    private static final String SELECT_BASE = """
            SELECT mv.id_pessoa, mv.num_crmv, mv.uf_crmv, p.nome AS nome_pessoa, p.email AS email_pessoa
            FROM MedicoVeterinario mv
            JOIN Pessoa p ON p.id_pessoa = mv.id_pessoa
            """;

    private final JdbcTemplate jdbcTemplate;

    public MedicoVeterinarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public record MedicoVeterinarioComPessoa(
            Integer idPessoa, String numCrmv, String ufCrmv, String nomePessoa, String emailPessoa) {}

    private static MedicoVeterinarioComPessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MedicoVeterinarioComPessoa(
                rs.getInt("id_pessoa"),
                rs.getString("num_crmv"),
                rs.getString("uf_crmv"),
                rs.getString("nome_pessoa"),
                rs.getString("email_pessoa"));
    }

    public List<MedicoVeterinarioComPessoa> findAll() {
        return jdbcTemplate.query(SELECT_BASE + " ORDER BY p.nome", MedicoVeterinarioRepository::mapRow);
    }

    public Optional<MedicoVeterinarioComPessoa> findById(int idPessoa) {
        return jdbcTemplate.query(SELECT_BASE + " WHERE mv.id_pessoa = ?", MedicoVeterinarioRepository::mapRow, idPessoa)
                .stream().findFirst();
    }

    public boolean existsById(int idPessoa) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM MedicoVeterinario WHERE id_pessoa = ?", Integer.class, idPessoa);
        return count != null && count > 0;
    }

    public boolean existsByCrmv(String numCrmv, String ufCrmv) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM MedicoVeterinario WHERE num_crmv = ? AND uf_crmv = ?",
                Integer.class, numCrmv, ufCrmv);
        return count != null && count > 0;
    }

    public boolean existsByCrmvExcludingId(String numCrmv, String ufCrmv, int excludeId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM MedicoVeterinario WHERE num_crmv = ? AND uf_crmv = ? AND id_pessoa <> ?",
                Integer.class, numCrmv, ufCrmv, excludeId);
        return count != null && count > 0;
    }

    public void insert(MedicoVeterinario medico) {
        jdbcTemplate.update(
                "INSERT INTO MedicoVeterinario (id_pessoa, num_crmv, uf_crmv) VALUES (?, ?, ?)",
                medico.idPessoa(), medico.numCrmv(), medico.ufCrmv());
    }

    public void update(int idPessoa, MedicoVeterinario medico) {
        jdbcTemplate.update(
                "UPDATE MedicoVeterinario SET num_crmv = ?, uf_crmv = ? WHERE id_pessoa = ?",
                medico.numCrmv(), medico.ufCrmv(), idPessoa);
    }

    public void delete(int idPessoa) {
        jdbcTemplate.update("DELETE FROM MedicoVeterinario WHERE id_pessoa = ?", idPessoa);
    }
}

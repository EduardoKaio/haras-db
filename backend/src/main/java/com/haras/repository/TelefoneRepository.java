package com.haras.repository;

import com.haras.model.Telefone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TelefoneRepository {

    private final JdbcTemplate jdbcTemplate;

    public TelefoneRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Telefone> findByPessoaId(int idPessoa) {
        return jdbcTemplate.query(
                "SELECT * FROM Telefone WHERE id_pessoa = ? ORDER BY telefone",
                (rs, rowNum) -> new Telefone(rs.getString("telefone"), rs.getInt("id_pessoa")),
                idPessoa);
    }

    public void insert(int idPessoa, String telefone) {
        jdbcTemplate.update("INSERT INTO Telefone (telefone, id_pessoa) VALUES (?, ?)", telefone, idPessoa);
    }

    public int delete(int idPessoa, String telefone) {
        return jdbcTemplate.update("DELETE FROM Telefone WHERE id_pessoa = ? AND telefone = ?", idPessoa, telefone);
    }
}

package com.haras.repository;

import com.haras.model.Pessoa;
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
public class PessoaRepository {

    private final JdbcTemplate jdbcTemplate;

    public PessoaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Pessoa(
                rs.getInt("id_pessoa"),
                rs.getString("nome"),
                rs.getDate("data_nascimento").toLocalDate(),
                rs.getString("cpf"),
                rs.getBoolean("is_gerente"),
                rs.getString("email"),
                rs.getString("senha")
        );
    }

    public List<Pessoa> findAll() {
        return jdbcTemplate.query("SELECT * FROM Pessoa ORDER BY nome", PessoaRepository::mapRow);
    }

    public Optional<Pessoa> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM Pessoa WHERE id_pessoa = ?", PessoaRepository::mapRow, id)
                .stream().findFirst();
    }

    public boolean existsByCpf(String cpf) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Pessoa WHERE cpf = ?", Integer.class, cpf);
        return count != null && count > 0;
    }

    public boolean existsByCpfExcludingId(String cpf, int excludeId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Pessoa WHERE cpf = ? AND id_pessoa <> ?", Integer.class, cpf, excludeId);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Pessoa WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsByEmailExcludingId(String email, int excludeId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Pessoa WHERE email = ? AND id_pessoa <> ?", Integer.class, email, excludeId);
        return count != null && count > 0;
    }

    public int insert(Pessoa pessoa) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Pessoa (nome, data_nascimento, cpf, is_gerente, email, senha) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pessoa.nome());
            ps.setDate(2, Date.valueOf(pessoa.dataNascimento()));
            ps.setString(3, pessoa.cpf());
            ps.setBoolean(4, pessoa.gerente());
            ps.setString(5, pessoa.email());
            ps.setString(6, pessoa.senha());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void update(int id, Pessoa pessoa) {
        jdbcTemplate.update(
                "UPDATE Pessoa SET nome = ?, data_nascimento = ?, cpf = ?, is_gerente = ?, email = ?, senha = ? WHERE id_pessoa = ?",
                pessoa.nome(), Date.valueOf(pessoa.dataNascimento()), pessoa.cpf(), pessoa.gerente(),
                pessoa.email(), pessoa.senha(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Pessoa WHERE id_pessoa = ?", id);
    }
}

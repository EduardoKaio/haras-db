package com.haras.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PingRepository {

    private final JdbcTemplate jdbcTemplate;

    public PingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countPessoas() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Pessoa", Integer.class);
        return count != null ? count : 0;
    }

    public String currentTimestamp() {
        return jdbcTemplate.queryForObject("SELECT NOW()", String.class);
    }
}

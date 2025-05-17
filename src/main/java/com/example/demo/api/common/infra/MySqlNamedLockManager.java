package com.example.demo.api.common.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MySqlNamedLockManager {
    private final JdbcTemplate jdbcTemplate;

    public boolean tryLock(String lockKey, long timeoutSeconds) {
        String sql = "SELECT GET_LOCK(?, ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, lockKey, timeoutSeconds);
        return Boolean.TRUE.equals(result);
    }

    public void releaseLock(String lockKey) {
        String sql = "SELECT RELEASE_LOCK(?)";
        jdbcTemplate.queryForObject(sql, Boolean.class, lockKey);
    }
}


package com.example.demo.api.common.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class MySqlNamedLockManager {
    private final DataSource dataSource;

    public <T> T executeWithLock(String lockKey, long timeoutSeconds, LockCallback<T> callback) {
        String getLockSql = "SELECT GET_LOCK(?, ?)";
        String releaseLockSql = "SELECT RELEASE_LOCK(?)";

        try (Connection connection = dataSource.getConnection()) {
            // GET_LOCK
            try (PreparedStatement stmt = connection.prepareStatement(getLockSql)) {
                stmt.setString(1, lockKey);
                stmt.setLong(2, timeoutSeconds);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next() || rs.getInt(1) != 1) {
                        throw new IllegalStateException("Failed to acquire lock: " + lockKey);
                    }
                }
            }

            try {
                return callback.doInLock();
            } finally {
                // RELEASE_LOCK
                try (PreparedStatement stmt = connection.prepareStatement(releaseLockSql)) {
                    stmt.setString(1, lockKey);
                    stmt.executeQuery();
                } catch (Exception e) {
                    log.error("Failed to release lock: " + lockKey, e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lock execution failed: " + lockKey, e);
        }
    }

    @FunctionalInterface
    public interface LockCallback<T> {
        T doInLock();
    }
}


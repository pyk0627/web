package com.keshe.crop.repository;

import com.keshe.crop.model.Role;
import com.keshe.crop.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setDisplayName(rs.getString("display_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setEnabled(rs.getBoolean("enabled"));
        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return user;
    };

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM app_user WHERE username = ?", ROW_MAPPER, username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM app_user WHERE id = ?", ROW_MAPPER, id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_user WHERE username = ?", Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_user WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsByEmailExceptId(String email, Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_user WHERE email = ? AND id <> ?", Integer.class, email, id);
        return count != null && count > 0;
    }

    public Long save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO app_user (username, password_hash, display_name, email, phone, role, enabled) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getDisplayName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getRole().name());
            ps.setBoolean(7, user.isEnabled());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM app_user ORDER BY CASE WHEN role = 'ADMIN' THEN 0 ELSE 1 END, id ASC", ROW_MAPPER);
    }

    public void updateRole(Long id, Role role) {
        jdbcTemplate.update("UPDATE app_user SET role = ? WHERE id = ?", role.name(), id);
    }

    public void updateEnabled(Long id, boolean enabled) {
        jdbcTemplate.update("UPDATE app_user SET enabled = ? WHERE id = ?", enabled, id);
    }

    public void updatePassword(Long id, String passwordHash) {
        jdbcTemplate.update("UPDATE app_user SET password_hash = ? WHERE id = ?", passwordHash, id);
    }

    public void updateProfile(Long id, String displayName, String email, String phone) {
        jdbcTemplate.update(
                "UPDATE app_user SET display_name = ?, email = ?, phone = ? WHERE id = ?",
                displayName, email, phone, id);
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM app_user WHERE id = ?", id);
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM app_user", Long.class);
        return count == null ? 0 : count;
    }

    public long countAdmins() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_user WHERE role = 'ADMIN' AND enabled = 1", Long.class);
        return count == null ? 0 : count;
    }
}

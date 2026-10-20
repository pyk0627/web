package com.keshe.crop.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavoriteRepository {

    private final JdbcTemplate jdbcTemplate;

    public FavoriteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean exists(Long userId, Long cropId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM favorite WHERE user_id = ? AND crop_id = ?",
                Integer.class, userId, cropId);
        return count != null && count > 0;
    }

    public void add(Long userId, Long cropId) {
        jdbcTemplate.update(
                "INSERT IGNORE INTO favorite (user_id, crop_id) VALUES (?, ?)", userId, cropId);
    }

    public void remove(Long userId, Long cropId) {
        jdbcTemplate.update(
                "DELETE FROM favorite WHERE user_id = ? AND crop_id = ?", userId, cropId);
    }

    public List<Long> findCropIdsByUserId(Long userId) {
        return jdbcTemplate.queryForList(
                "SELECT crop_id FROM favorite WHERE user_id = ? ORDER BY created_at DESC, crop_id DESC",
                Long.class, userId);
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM favorite", Long.class);
        return count == null ? 0 : count;
    }

    public long countByUserId(Long userId) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM favorite WHERE user_id = ?", Long.class, userId);
        return count == null ? 0 : count;
    }
}

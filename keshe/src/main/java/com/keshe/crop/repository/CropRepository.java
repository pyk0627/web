package com.keshe.crop.repository;

import com.keshe.crop.model.Crop;
import com.keshe.crop.model.FamilyCount;
import com.keshe.crop.model.PageResult;
import com.keshe.crop.model.SearchCriteria;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CropRepository {

    private static final String OIL_JOIN =
            " LEFT JOIN (SELECT crop_id, MIN(oil_content) AS oil_min, MAX(oil_content) AS oil_max, " +
                    "COUNT(*) AS oil_count FROM oil_record WHERE oil_content IS NOT NULL GROUP BY crop_id) o " +
                    "ON o.crop_id = c.id ";

    private static final String SELECT_BASE =
            "SELECT c.*, o.oil_min, o.oil_max, COALESCE(o.oil_count, 0) AS oil_count " +
                    "FROM crop c " + OIL_JOIN;

    private static final RowMapper<Crop> ROW_MAPPER = (rs, rowNum) -> {
        Crop crop = new Crop();
        crop.setId(rs.getLong("id"));
        crop.setChineseName(rs.getString("chinese_name"));
        crop.setEnglishName(rs.getString("english_name"));
        crop.setLatinName(rs.getString("latin_name"));
        crop.setAlias(rs.getString("alias"));
        crop.setFamily(rs.getString("family"));
        crop.setGenus(rs.getString("genus"));
        crop.setFeatures(rs.getString("features"));
        crop.setDistribution(rs.getString("distribution"));
        crop.setCultivation(rs.getString("cultivation"));
        crop.setUses(rs.getString("uses"));
        crop.setImageUrl(rs.getString("image_url"));
        crop.setReferenceTitle(rs.getString("reference_title"));
        crop.setReferenceUrl(rs.getString("reference_url"));
        crop.setOilMin(rs.getBigDecimal("oil_min"));
        crop.setOilMax(rs.getBigDecimal("oil_max"));
        crop.setOilCount(rs.getInt("oil_count"));
        if (rs.getTimestamp("created_at") != null) {
            crop.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            crop.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return crop;
    };

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public CropRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public PageResult<Crop> search(SearchCriteria criteria) {
        criteria.normalize();
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");

        if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {
            where.append(" AND (c.chinese_name LIKE :keyword OR c.english_name LIKE :keyword ")
                    .append("OR c.latin_name LIKE :keyword OR c.alias LIKE :keyword ")
                    .append("OR c.family LIKE :keyword OR c.genus LIKE :keyword ")
                    .append("OR c.features LIKE :keyword OR c.distribution LIKE :keyword ")
                    .append("OR c.cultivation LIKE :keyword OR c.uses LIKE :keyword) ");
            params.addValue("keyword", "%" + criteria.getKeyword() + "%");
        }
        if (criteria.getFamily() != null && !criteria.getFamily().isBlank()) {
            where.append(" AND c.family = :family ");
            params.addValue("family", criteria.getFamily());
        }
        if (criteria.getGenus() != null && !criteria.getGenus().isBlank()) {
            where.append(" AND c.genus = :genus ");
            params.addValue("genus", criteria.getGenus());
        }
        if (criteria.isOnlyWithOil()) {
            where.append(" AND o.oil_count > 0 ");
        }
        if (criteria.getOilMin() != null || criteria.getOilMax() != null) {
            where.append(" AND EXISTS (SELECT 1 FROM oil_record r WHERE r.crop_id = c.id ")
                    .append("AND r.oil_content IS NOT NULL ");
            if (criteria.getOilMin() != null) {
                where.append("AND r.oil_content >= :oilMin ");
                params.addValue("oilMin", criteria.getOilMin());
            }
            if (criteria.getOilMax() != null) {
                where.append("AND r.oil_content <= :oilMax ");
                params.addValue("oilMax", criteria.getOilMax());
            }
            where.append(") ");
        }

        String countSql = "SELECT COUNT(*) FROM crop c " + OIL_JOIN + where;
        Long totalObj = namedJdbcTemplate.queryForObject(countSql, params, Long.class);
        long total = totalObj == null ? 0 : totalObj;
        int totalPages = total == 0 ? 1 : (int) Math.ceil((double) total / criteria.getSize());
        int page = Math.min(Math.max(1, criteria.getPage()), totalPages);
        int offset = (page - 1) * criteria.getSize();

        String orderBy;
        switch (criteria.getSort()) {
            case "oilDesc" -> orderBy = " ORDER BY CASE WHEN o.oil_max IS NULL THEN 1 ELSE 0 END, o.oil_max DESC, c.chinese_name ASC ";
            case "oilAsc" -> orderBy = " ORDER BY CASE WHEN o.oil_min IS NULL THEN 1 ELSE 0 END, o.oil_min ASC, c.chinese_name ASC ";
            case "newest" -> orderBy = " ORDER BY c.id DESC ";
            default -> orderBy = " ORDER BY c.chinese_name ASC ";
        }

        params.addValue("limit", criteria.getSize());
        params.addValue("offset", offset);
        String dataSql = SELECT_BASE + where + orderBy + " LIMIT :limit OFFSET :offset";
        List<Crop> crops = namedJdbcTemplate.query(dataSql, params, ROW_MAPPER);
        return new PageResult<>(crops, page, criteria.getSize(), total);
    }

    public Optional<Crop> findById(Long id) {
        try {
            Crop crop = jdbcTemplate.queryForObject(
                    SELECT_BASE + " WHERE c.id = ?", ROW_MAPPER, id);
            return Optional.ofNullable(crop);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Crop> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(
                SELECT_BASE + " WHERE c.id IN (" + placeholders + ") ORDER BY c.chinese_name",
                ROW_MAPPER, ids.toArray());
    }

    public List<Crop> findFeatured(int limit) {
        return jdbcTemplate.query(
                SELECT_BASE + " WHERE o.oil_count > 0 ORDER BY c.id DESC LIMIT ?",
                ROW_MAPPER, limit);
    }

    public List<Crop> findRecent(int limit) {
        return jdbcTemplate.query(
                SELECT_BASE + " ORDER BY c.id DESC LIMIT ?", ROW_MAPPER, limit);
    }

    public List<Crop> findRelated(String family, Long excludeId, int limit) {
        return jdbcTemplate.query(
                SELECT_BASE + " WHERE c.family = ? AND c.id <> ? ORDER BY c.chinese_name LIMIT ?",
                ROW_MAPPER, family, excludeId, limit);
    }

    public List<Crop> findAllForSelect() {
        return jdbcTemplate.query(
                "SELECT id, chinese_name FROM crop ORDER BY chinese_name",
                (rs, rowNum) -> {
                    Crop crop = new Crop();
                    crop.setId(rs.getLong("id"));
                    crop.setChineseName(rs.getString("chinese_name"));
                    return crop;
                });
    }

    public List<String> findFamilies() {
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT family FROM crop WHERE family IS NOT NULL AND family <> '' ORDER BY family",
                String.class);
    }

    public List<String> findGenera() {
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT genus FROM crop WHERE genus IS NOT NULL AND genus <> '' ORDER BY genus",
                String.class);
    }

    public List<String> findGeneraByFamily(String family) {
        return jdbcTemplate.queryForList(
                "SELECT DISTINCT genus FROM crop WHERE family = ? AND genus IS NOT NULL AND genus <> '' ORDER BY genus",
                String.class, family);
    }

    public List<FamilyCount> countByFamily() {
        return jdbcTemplate.query(
                "SELECT family, COUNT(*) AS amount FROM crop WHERE family IS NOT NULL AND family <> '' " +
                        "GROUP BY family ORDER BY amount DESC, family ASC",
                (rs, rowNum) -> new FamilyCount(rs.getString("family"), rs.getLong("amount")));
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM crop", Long.class);
        return count == null ? 0 : count;
    }

    public long countWithOil() {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(DISTINCT crop_id) FROM oil_record", Long.class);
        return count == null ? 0 : count;
    }

    public boolean existsByName(String chineseName, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM crop WHERE chinese_name = ?";
        Integer count;
        if (excludeId == null) {
            count = jdbcTemplate.queryForObject(sql, Integer.class, chineseName);
        } else {
            count = jdbcTemplate.queryForObject(sql + " AND id <> ?", Integer.class, chineseName, excludeId);
        }
        return count != null && count > 0;
    }

    public Long save(Crop crop) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO crop (chinese_name, english_name, latin_name, alias, family, genus, " +
                            "features, distribution, cultivation, uses, image_url, reference_title, reference_url) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            bindCrop(ps, crop);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    public void update(Crop crop) {
        jdbcTemplate.update(
                "UPDATE crop SET chinese_name = ?, english_name = ?, latin_name = ?, alias = ?, " +
                        "family = ?, genus = ?, features = ?, distribution = ?, cultivation = ?, uses = ?, " +
                        "image_url = ?, reference_title = ?, reference_url = ?, updated_at = CURRENT_TIMESTAMP " +
                        "WHERE id = ?",
                crop.getChineseName(), crop.getEnglishName(), crop.getLatinName(), crop.getAlias(),
                crop.getFamily(), crop.getGenus(), crop.getFeatures(), crop.getDistribution(),
                crop.getCultivation(), crop.getUses(), crop.getImageUrl(), crop.getReferenceTitle(),
                crop.getReferenceUrl(), crop.getId());
    }

    private void bindCrop(PreparedStatement ps, Crop crop) throws java.sql.SQLException {
        ps.setString(1, crop.getChineseName());
        ps.setString(2, crop.getEnglishName());
        ps.setString(3, crop.getLatinName());
        ps.setString(4, crop.getAlias());
        ps.setString(5, crop.getFamily());
        ps.setString(6, crop.getGenus());
        ps.setString(7, crop.getFeatures());
        ps.setString(8, crop.getDistribution());
        ps.setString(9, crop.getCultivation());
        ps.setString(10, crop.getUses());
        ps.setString(11, crop.getImageUrl());
        ps.setString(12, crop.getReferenceTitle());
        ps.setString(13, crop.getReferenceUrl());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM crop WHERE id = ?", id);
    }
}

package com.keshe.crop.repository;

import com.keshe.crop.model.OilRecord;
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
import java.util.List;
import java.util.Optional;

@Repository
public class OilRecordRepository {

    private static final RowMapper<OilRecord> ROW_MAPPER = (rs, rowNum) -> {
        OilRecord record = new OilRecord();
        record.setId(rs.getLong("id"));
        record.setCropId(rs.getLong("crop_id"));
        record.setCollectionUnit(rs.getString("collection_unit"));
        record.setTestUnit(rs.getString("test_unit"));
        record.setTestPart(rs.getString("test_part"));
        record.setOrigin(rs.getString("origin"));
        record.setOilContent(rs.getBigDecimal("oil_content"));
        record.setIodineValue(rs.getBigDecimal("iodine_value"));
        record.setAcidValue(rs.getBigDecimal("acid_value"));
        record.setSaponificationValue(rs.getBigDecimal("saponification_value"));
        record.setC12(rs.getBigDecimal("c12"));
        record.setC14(rs.getBigDecimal("c14"));
        record.setC16(rs.getBigDecimal("c16"));
        record.setC16_1(rs.getBigDecimal("c16_1"));
        record.setC18(rs.getBigDecimal("c18"));
        record.setC18_1(rs.getBigDecimal("c18_1"));
        record.setC18_2(rs.getBigDecimal("c18_2"));
        record.setC18_3(rs.getBigDecimal("c18_3"));
        record.setC20(rs.getBigDecimal("c20"));
        record.setC20_1(rs.getBigDecimal("c20_1"));
        record.setRemark(rs.getString("remark"));
        return record;
    };

    private static final RowMapper<OilRecord> WITH_CROP_MAPPER = (rs, rowNum) -> {
        OilRecord record = ROW_MAPPER.mapRow(rs, rowNum);
        if (record != null) {
            record.setCropName(rs.getString("crop_name"));
        }
        return record;
    };

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public OilRecordRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public List<OilRecord> findByCropId(Long cropId) {
        return jdbcTemplate.query(
                "SELECT * FROM oil_record WHERE crop_id = ? " +
                        "ORDER BY CASE WHEN oil_content IS NULL THEN 1 ELSE 0 END, oil_content DESC, id ASC",
                ROW_MAPPER, cropId);
    }

    public List<OilRecord> findAll(Long cropId, String keyword) {
        StringBuilder sql = new StringBuilder(
                "SELECT o.*, c.chinese_name AS crop_name FROM oil_record o " +
                        "JOIN crop c ON c.id = o.crop_id WHERE 1 = 1 ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (cropId != null) {
            sql.append("AND o.crop_id = :cropId ");
            params.addValue("cropId", cropId);
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append("AND (c.chinese_name LIKE :keyword OR o.origin LIKE :keyword ")
                    .append("OR o.collection_unit LIKE :keyword OR o.test_unit LIKE :keyword) ");
            params.addValue("keyword", "%" + keyword.trim() + "%");
        }
        sql.append("ORDER BY c.chinese_name, o.id");
        return namedJdbcTemplate.query(sql.toString(), params, WITH_CROP_MAPPER);
    }

    public List<OilRecord> findRecent(int limit) {
        return jdbcTemplate.query(
                "SELECT o.*, c.chinese_name AS crop_name FROM oil_record o " +
                        "JOIN crop c ON c.id = o.crop_id ORDER BY o.id DESC LIMIT ?",
                WITH_CROP_MAPPER, limit);
    }

    public Optional<OilRecord> findById(Long id) {
        try {
            OilRecord record = jdbcTemplate.queryForObject(
                    "SELECT o.*, c.chinese_name AS crop_name FROM oil_record o " +
                            "JOIN crop c ON c.id = o.crop_id WHERE o.id = ?",
                    WITH_CROP_MAPPER, id);
            return Optional.ofNullable(record);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(OilRecord record) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO oil_record (crop_id, collection_unit, test_unit, test_part, origin, " +
                            "oil_content, iodine_value, acid_value, saponification_value, " +
                            "c12, c14, c16, c16_1, c18, c18_1, c18_2, c18_3, c20, c20_1, remark) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            bindRecord(ps, record);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    public void update(OilRecord record) {
        jdbcTemplate.update(
                "UPDATE oil_record SET crop_id = ?, collection_unit = ?, test_unit = ?, test_part = ?, " +
                        "origin = ?, oil_content = ?, iodine_value = ?, acid_value = ?, saponification_value = ?, " +
                        "c12 = ?, c14 = ?, c16 = ?, c16_1 = ?, c18 = ?, c18_1 = ?, c18_2 = ?, c18_3 = ?, " +
                        "c20 = ?, c20_1 = ?, remark = ? WHERE id = ?",
                record.getCropId(), record.getCollectionUnit(), record.getTestUnit(), record.getTestPart(),
                record.getOrigin(), record.getOilContent(), record.getIodineValue(), record.getAcidValue(),
                record.getSaponificationValue(), record.getC12(), record.getC14(), record.getC16(),
                record.getC16_1(), record.getC18(), record.getC18_1(), record.getC18_2(), record.getC18_3(),
                record.getC20(), record.getC20_1(), record.getRemark(), record.getId());
    }

    private void bindRecord(PreparedStatement ps, OilRecord record) throws java.sql.SQLException {
        ps.setLong(1, record.getCropId());
        ps.setString(2, record.getCollectionUnit());
        ps.setString(3, record.getTestUnit());
        ps.setString(4, record.getTestPart());
        ps.setString(5, record.getOrigin());
        ps.setObject(6, record.getOilContent());
        ps.setObject(7, record.getIodineValue());
        ps.setObject(8, record.getAcidValue());
        ps.setObject(9, record.getSaponificationValue());
        ps.setObject(10, record.getC12());
        ps.setObject(11, record.getC14());
        ps.setObject(12, record.getC16());
        ps.setObject(13, record.getC16_1());
        ps.setObject(14, record.getC18());
        ps.setObject(15, record.getC18_1());
        ps.setObject(16, record.getC18_2());
        ps.setObject(17, record.getC18_3());
        ps.setObject(18, record.getC20());
        ps.setObject(19, record.getC20_1());
        ps.setString(20, record.getRemark());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM oil_record WHERE id = ?", id);
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM oil_record", Long.class);
        return count == null ? 0 : count;
    }

    public long countByCropId(Long cropId) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM oil_record WHERE crop_id = ?", Long.class, cropId);
        return count == null ? 0 : count;
    }
}

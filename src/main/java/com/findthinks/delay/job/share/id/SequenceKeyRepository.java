package com.findthinks.delay.job.share.id;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceKeyRepository {

    private static final String LOAD_SEQUENCE_BY_KEY = "SELECT * FROM sequence_key WHERE `key` = ?";
    private static final String LOAD_SEQUENCE_BY_ID = "SELECT * FROM sequence_key WHERE id = ?";
    private static final String COMPARE_AND_SET = "UPDATE sequence_key SET start_with = ? WHERE id = ? AND start_with = ?";

    private JdbcTemplate jdbcTemplate;

    public SequenceKeyRepository() {
    }

    public SequenceKeyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SequenceKey loadSequenceKey(String key) {
        return jdbcTemplate.queryForObject(LOAD_SEQUENCE_BY_KEY, new SequenceKeyRowMapper(), key);
    }

    public SequenceKey loadSequenceById(Integer id) {
        return jdbcTemplate.queryForObject(LOAD_SEQUENCE_BY_ID, new SequenceKeyRowMapper(), id);
    }

    public int compareAndSet(Long newValue, Long oldValue, Integer id) {
        return jdbcTemplate.update(COMPARE_AND_SET, newValue, id, oldValue);
    }

    private class SequenceKeyRowMapper implements RowMapper<SequenceKey> {
        @Override
        public SequenceKey mapRow(ResultSet rs, int rowNum) throws SQLException {
            SequenceKey seq = new SequenceKey();
            seq.setId(rs.getInt("id"));
            seq.setIncSpan(rs.getInt("inc_span"));
            seq.setStartWith(rs.getLong("start_with"));
            seq.setDescription(rs.getString("description"));
            seq.setKey(rs.getString("key"));
            return seq;
        }
    }
}
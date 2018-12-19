package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)",
                                        Statement.RETURN_GENERATED_KEYS);
                        ps.setLong(1, timeEntry.getProjectId());
                        ps.setLong(2, timeEntry.getUserId());
                        ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                        ps.setInt(4, timeEntry.getHours());
                        return ps;
                    }
                },
                keyHolder);

        return find(keyHolder.getKey().intValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        try {
            Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", timeEntryId);

            return new TimeEntry((long) foundEntry.get("id"),
                    (long) foundEntry.get("project_id"),
                    (long) foundEntry.get("user_id"),
                    ((Date) foundEntry.get("date")).toLocalDate(),
                    (int) foundEntry.get("hours"));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("Select * from time_entries", new TimeEntryMapper());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        jdbcTemplate.update("Update time_entries Set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                id );

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
    }

    class TimeEntryMapper implements RowMapper<TimeEntry> {
        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeEntry(
                    rs.getLong("id"),
                    rs.getLong("project_id"),
                    rs.getLong("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("hours"));
        }
    }
}


package freecycling.example.freecycling.Core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MeetingTimeTableDAO {

    private JdbcTemplate jdbcTemplate;

    public MeetingTimeTableDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(MeetingTimeTable meeting) {
        String sql = "INSERT INTO MeetingTimeTable(MeetingSite, MeetingTime, Availability) VALUES (?,?,?)";
        try {
            jdbcTemplate.update(sql, meeting.getMeetingSite(), meeting.getMeetingTime(), meeting.getAvailability());
        } catch (Exception ex) {
        }
    }

    public MeetingTimeTable findById(int id) {
        String sql = "SELECT * FROM MeetingTimeTable WHERE idMeetingTime=?";
        MeetingTimeTable result = null;
        RowMapper<MeetingTimeTable> rowMapper = new MeetingTimeTableRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<MeetingTimeTable> findAll() {
        String sql = "SELECT * FROM MeetingTimeTable";
        List<MeetingTimeTable> results = null;
        RowMapper<MeetingTimeTable> rowMapper = new MeetingTimeTableRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception ex) {
            results = new ArrayList<MeetingTimeTable>();
        }
        return results;
    }

    public List<MeetingTimeTable> findBySite(String site) {
        String sql = "SELECT * FROM MeetingTimeTable WHERE MeetingSite=?";
        List<MeetingTimeTable> results = null;
        RowMapper<MeetingTimeTable> rowMapper = new MeetingTimeTableRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper, site);
        } catch (Exception ex) {
            results = new ArrayList<MeetingTimeTable>();
        }
        return results;
    }

    public List<MeetingTimeTable> findByDate(String date) {
        String sql = "SELECT * FROM MeetingTimeTable WHERE MeetingTime LIKE ?";
        List<MeetingTimeTable> results = null;
        RowMapper<MeetingTimeTable> rowMapper = new MeetingTimeTableRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper, date + "%");
        } catch (Exception ex) {
            results = new ArrayList<MeetingTimeTable>();
        }
        return results;
    }
}
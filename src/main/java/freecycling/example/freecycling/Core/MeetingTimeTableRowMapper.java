package freecycling.example.freecycling.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MeetingTimeTableRowMapper implements RowMapper<MeetingTimeTable> {
    @Override
    public MeetingTimeTable mapRow(ResultSet row, int rowNum) throws SQLException {
        MeetingTimeTable m = new MeetingTimeTable();
        m.setIdMeetingTimeTable(row.getInt("idMeetingTimeTable"));
        m.setMeetingSite(row.getString("MeetingSite"));
        m.setMeetingTime(row.getString("MeetingTime"));
        m.setAvailability(row.getString("Availability"));
        return m;
    }
}

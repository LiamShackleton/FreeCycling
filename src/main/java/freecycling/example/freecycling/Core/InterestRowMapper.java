package freecycling.example.freecycling.Core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

public class InterestRowMapper implements RowMapper<Interest> {
    @Override
    public Interest mapRow(ResultSet row, int rowNum) throws SQLException {
        Interest i = new Interest();
        i.setIdInterestTable(row.getInt("idInterestTable"));
        i.setUser(row.getString("User"));
        
        String item = row.getString("Item");
        if (row.wasNull()) {
            item = null;
        }
        i.setItem(item);
        
        Timestamp timeStamp = row.getTimestamp("TimeStamp");
        if (row.wasNull()) {
            timeStamp = null;
        }
        i.setTimeStamp(timeStamp);
        
        String note = row.getString("Note");
        if (row.wasNull()) {
            note = null;
        }
        i.setNote(note);
        
        return i;
    }
}
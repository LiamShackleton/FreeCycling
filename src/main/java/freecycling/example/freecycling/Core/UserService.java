package freecycling.example.freecycling.Core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet row, int rowNum) throws SQLException {
        User u = new User();
        u.setUserId(row.getString("UserId"));
        u.setUserName(row.getString("UserName"));
        u.setPass(row.getString("Pass"));
        u.setEmail(row.getString("Email"));
        return u;
    }
}
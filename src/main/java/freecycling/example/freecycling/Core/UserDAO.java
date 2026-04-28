package freecycling.example.freecycling.Core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(User user) {
        String insertSQL = "INSERT INTO User_Table(UserName, Pass, Email) VALUES (?,?,?)";
        try {
            jdbcTemplate.update(insertSQL, user.getUserName(), user.getPass(), user.getEmail());
        } catch (Exception ex) {
        }
    }

    public User findById(int id) {
        String sql = "SELECT * FROM User_Table WHERE UserId=?";
        User result = null;
        RowMapper<User> rowMapper = new UserRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public User findByUserName(String userName) {
        String sql = "SELECT * FROM User_Table WHERE UserName=?";
        User result = null;
        RowMapper<User> rowMapper = new UserRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, userName);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM User_Table";
        List<User> results = null;
        RowMapper<User> rowMapper = new UserRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception ex) {
            results = new ArrayList<User>();
        }
        return results;
    }
}
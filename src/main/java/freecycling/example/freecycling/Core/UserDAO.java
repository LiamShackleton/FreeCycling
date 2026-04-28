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

    public String save(User user) {
        String idSQL = "select uuid()";
        String key = null;
        try {
            key = jdbcTemplate.queryForObject(idSQL, String.class);
        } catch (Exception ex) {
            key = "Error";
        }
        if (key.equals("Error"))
            return key;

        String insertSQL = "insert into User_Table(UserId, UserName, Pass, Email) values (?,?,?,?)";
        jdbcTemplate.update(insertSQL, key, user.getUserName(), user.getPass(), user.getEmail());
        return key;
    }

    public User findById(int id) {
        String sql = "select * from User_Table where UserId=?";
        User result = null;
        RowMapper<User> rowMapper = new UserRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public User findByUserName(String userName) {
        String sql = "select * from User_Table where UserName=?";
        User result = null;
        RowMapper<User> rowMapper = new UserRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, userName);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<User> findAll() {
        String sql = "select * from User_Table";
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

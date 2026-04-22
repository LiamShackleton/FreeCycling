package freecycling.example.freecycling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class InterestDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Interest interest) {
        String insertSQL = "insert into InterestTable(User, Item, TimeStamp) values (?,?,?)";
        jdbcTemplate.update(insertSQL, interest.getUser(), interest.getItem(), interest.getTimeStamp());
    }

    public Interest findById(String id) {
        String sql = "select * from InterestTable where idInterestTable=?";
        Interest result = null;
        RowMapper<Interest> rowMapper = new InterestRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<Interest> findByUser(String userid) {
        String sql = "select * from InterestTable where User=?";
        List<Interest> results = null;
        RowMapper<Interest> rowMapper = new InterestRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper, userid);
        } catch (Exception ex) {
            results = new ArrayList<Interest>();
        }
        return results;
    }

    public List<Interest> findAll() {
        String sql = "select * from InterestTable";
        List<Interest> results = null;
        RowMapper<Interest> rowMapper = new InterestRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception ex) {
            results = new ArrayList<Interest>();
        }
        return results;
    }
}
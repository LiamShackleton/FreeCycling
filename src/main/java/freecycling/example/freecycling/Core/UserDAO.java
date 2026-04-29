package freecycling.example.freecycling.Core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import freecycling.example.freecycling.services.PasswordService;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private PasswordService passwordService;

    /*public void save(User user) {
        String insertSQL = "INSERT INTO User_Table(UserName, Pass, Email) VALUES (?,?,?)";
        try {
            jdbcTemplate.update(insertSQL, user.getUserName(), user.getPass(), user.getEmail());
        } catch (Exception ex) {
        }
    }*/

    public String save(User user) {
	    // First make sure this is not a duplicate
	    String sql = "SELECT * FROM User_Table WHERE UserName=?";
	    RowMapper<User> rowMapper = new UserRowMapper();
	    User old = null;
	    try {
	        old = jdbcTemplate.queryForObject(sql, rowMapper, user.getUserName());
	    } catch(Exception ex) {
	
	    }
	    if(old != null)
	        return "Duplicate";
	
	    // Have MySQL generate a unique id
	    String idSQL = "select uuid()";
	    String key = null;
	    try {
	        key = jdbcTemplate.queryForObject(idSQL, String.class);
	    } catch(Exception ex) {
	        key = "Error";
	    }
	    if(key.equals("Error"))
	        return key;
	
	    String hash = passwordService.hashPassword(user.getPass());
	    String insertSQL = "insert into User_Table(UserId, UserName, Pass, Email) values (?, ?, ?, ?)";
	    jdbcTemplate.update(insertSQL,key,user.getUserName(),hash,user.getEmail());
	    return key;
	}

    public User findById(String id) {
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

        public User findByNameAndPassword(String name,String password) {
    	String sql = "SELECT * FROM User_Table WHERE UserName=?";
        RowMapper<User> rowMapper = new UserRowMapper();
        User result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, name);
        } catch(Exception ex) {
            
        }
        if(result != null && passwordService.verifyHash(password, result.getPass())) {
        	result.setPass("Undisclosed");
        } else {
        	result = null;
        }
        return result;	
    }
}
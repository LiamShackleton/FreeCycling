package freecycling.example.freecycling.Core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TransferSiteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(TransferSite site) {
        String insertSQL = "INSERT INTO TransferTable(TransferSiteName, TransferAddress) VALUES (?,?)";
        try {
            jdbcTemplate.update(insertSQL, site.getTransferSiteName(), site.getTransferAddress());
        } catch (Exception ex) {
        }
    }

    public TransferSite findById(int id) {
        String sql = "SELECT * FROM TransferTable WHERE idTransferSite=?";
        TransferSite result = null;
        RowMapper<TransferSite> rowMapper = new TransferSiteRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<TransferSite> findAll() {
        String sql = "SELECT * FROM TransferTable";
        List<TransferSite> results = null;
        RowMapper<TransferSite> rowMapper = new TransferSiteRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception ex) {
            results = new ArrayList<TransferSite>();
        }
        return results;
    }

    public List<TransferSite> findByName(String name) {
        String sql = "SELECT * FROM TransferTable WHERE TransferSiteName=?";
        List<TransferSite> results = null;
        RowMapper<TransferSite> rowMapper = new TransferSiteRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper, name);
        } catch (Exception ex) {
            results = new ArrayList<TransferSite>();
        }
        return results;
    }
}
package freecycling.example.free.cycling;

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
        String insertSQL = "insert into TransferTable(TransferSiteName, TransferAddress, Address) values (?,?,?)";
        jdbcTemplate.update(insertSQL, site.getTransferSiteName(), site.getTransferAddress(), site.getAddress());
    }

    public TransferSite findById(int id) {
        String sql = "select * from TransferTable where idTransferSite=?";
        TransferSite result = null;
        RowMapper<TransferSite> rowMapper = new TransferSiteRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<TransferSite> findAll() {
        String sql = "select * from TransferTable";
        List<TransferSite> results = null;
        RowMapper<TransferSite> rowMapper = new TransferSiteRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception ex) {
            results = new ArrayList<TransferSite>();
        }
        return results;
    }
}

package freecycling.example.freecycling.Core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TransferSiteRowMapper implements RowMapper<TransferSite> {
    @Override
    public TransferSite mapRow(ResultSet row, int rowNum) throws SQLException {
        TransferSite t = new TransferSite();
        t.setIdTransferSite(row.getInt("idTransferSite"));
        t.setTransferSiteName(row.getString("TransferSiteName"));
        t.setTransferAddress(row.getString("TransferAddress"));
        t.setAddress(row.getString("Address"));
        return t;
    }
}
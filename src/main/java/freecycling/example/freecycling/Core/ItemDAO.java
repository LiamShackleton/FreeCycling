package freecycling.example.freecycling.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ItemDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Item item) {
        String idSQL = "select uuid()";
        try {
            jdbcTemplate.queryForObject(idSQL, String.class);
        } catch (Exception ex) {
            return 0;
        }

        String insertSQL = "insert into ItemTable(ItemName, ItemDonor, ItemRecipient, ItemImageURL, ItemAvailability, ItemType, ItemDescription) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(insertSQL, item.getItemName(), item.getItemDonor(), item.getItemRecipient(), item.getItemImageUrl(), item.getItemAvailability(), item.getItemType(), item.getItemDescription());
        return 1;
    }

    public Item findById(String id) {
        String sql = "select * from ItemTable where idItemTable=?";
        Item result = null;
        RowMapper<Item> rowMapper = new ItemRowMapper();
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception ex) {
        }
        return result;
    }

    public List<Item> findByUser(String userid) {
        String sql = "select * from ItemTable where ItemDonor=?";
        List<Item> results = null;
        RowMapper<Item> rowMapper = new ItemRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper, userid);
        } catch (Exception ex) {
            results = new ArrayList<Item>();
        }
        return results;
    }

    public List<Item> findAvailable() {
        String sql = "select * from ItemTable where ItemAvailability='available'";
        List<Item> results = null;
        RowMapper<Item> rowMapper = new ItemRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception ex) {
            results = new ArrayList<Item>();
        }
        return results;
    }

    public List<Item> findAvailableByType(String type) {
        String sql = "select * from ItemTable where ItemAvailability='available' and ItemType=?";
        List<Item> results = null;
        RowMapper<Item> rowMapper = new ItemRowMapper();
        try {
            results = jdbcTemplate.query(sql, rowMapper, type);
        } catch (Exception ex) {
            results = new ArrayList<Item>();
        }
        return results;
    }

    public void saveRecipient(String itemid, User recipient) {
        String sql = "update ItemTable set ItemRecipient=? where idItemTable=?";
        jdbcTemplate.update(sql, recipient.getUserName(), itemid);
    }
}

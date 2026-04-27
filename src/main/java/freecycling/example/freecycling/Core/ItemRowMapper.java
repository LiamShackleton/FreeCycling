package freecycling.example.freecycling.Core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ItemRowMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet row, int rowNum) throws SQLException {
        Item i = new Item();
        i.setItemId(row.getInt("idItemTable"));
        i.setItemName(row.getString("ItemName"));
        i.setItemDonor(row.getString("ItemDonor"));
        
        String recipient = row.getString("ItemRecipient");
        if (row.wasNull()) {
            recipient = null;
        }
        i.setItemRecipient(recipient);
        
        String imageUrl = row.getString("ItemImageURL");
        if (row.wasNull()) {
            imageUrl = null;
        }
        i.setItemImageUrl(imageUrl);
        
        i.setItemAvailability(row.getString("ItemAvailability"));
        
        String itemType = row.getString("ItemType");
        if (row.wasNull()) {
            itemType = null;
        }
        i.setItemType(itemType);
        
        String description = row.getString("ItemDescription");
        if (row.wasNull()) {
            description = null;
        }
        i.setItemDescription(description);
        
        return i;
    }
}
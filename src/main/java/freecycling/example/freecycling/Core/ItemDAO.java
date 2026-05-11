package freecycling.example.freecycling.Core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDAO {
    
    @Autowired
    private ItemRepository itemRepository;

    public int save(Item item) {
        try {
            itemRepository.save(item);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    public Item findById(String id) {
        try {
            return itemRepository.findById(Integer.parseInt(id)).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Item> findByUser(String userid) {
        return itemRepository.findByItemDonor(userid);
    }

    public List<Item> findAvailable() {
        return itemRepository.findByItemAvailability("available");
    }

    public List<Item> findAvailableByType(String type) {
        return itemRepository.findByItemAvailabilityAndItemType("available", type);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void saveRecipient(String itemid, String recipientId) {
        try {
            Item item = itemRepository.findById(Integer.parseInt(itemid)).orElse(null);
            if (item != null) {
                item.setItemRecipient(recipientId);
                itemRepository.save(item);
            }
        } catch (Exception ex) {
            // Silently handle exceptions
        }
    }

    public void updateAvailability(String itemid, String availability) {
        try {
            Item item = itemRepository.findById(Integer.parseInt(itemid)).orElse(null);
            if (item != null) {
                item.setItemAvailability(availability);
                itemRepository.save(item);
            }
        } catch (Exception ex) {
            // Silently handle exceptions
        }
    }
}
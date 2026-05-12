package freecycling.example.freecycling.Core;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemService {

    private final ItemDAO itemDAO;

    public ItemService(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public String save(ItemDTO item) {
        return itemDAO.save(item);
    }

    public Item findById(String id) {
        return itemDAO.findById(id);
    }

    public List<Item> findByUser(String userId) {
        return itemDAO.findByUser(userId);
    }

    public List<Item> findAvailable() {
        return itemDAO.findAvailable();
    }

    public List<Item> findAvailableByType(String type) {
        return itemDAO.findAvailableByType(type);
    }

    public List<Item> findAll() {
        return itemDAO.findAll();
    }

    public void saveRecipient(String itemId, String recipientId) {
        itemDAO.saveRecipient(itemId, recipientId);
    }

    public void updateAvailability(String itemId, String availability) {
        itemDAO.updateAvailability(itemId, availability);
    }
}
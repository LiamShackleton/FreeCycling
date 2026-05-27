package freecycling.example.freecycling.Core;

import freecycling.example.freecycling.Interface.DTOs.ItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ItemService {

    private ItemRepository repository;
    private UserRepository userRepository;

    public ItemService(ItemRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String save(ItemDTO item, String authenticatedUserId) {
        User user = userRepository.findById(Integer.parseInt(authenticatedUserId)).orElse(null);
        if (user == null) {
            return "Bad Id";
        }

        Item newItem = new Item();
        newItem.setItemName(item.getItemName());
        newItem.setItemDonor(authenticatedUserId);
        newItem.setItemRecipient(item.getItemRecipient());
        newItem.setItemImageUrl(item.getItemImageUrl());
        newItem.setItemAvailability(item.getItemAvailability());
        newItem.setItemType(item.getItemType());
        newItem.setItemDescription(item.getItemDescription());

        repository.save(newItem);

        return String.valueOf(newItem.getItemId());
    }

    @Transactional
    public void saveRecipient(String itemid, String recipientId) {
        Item item = repository.findById(Integer.parseInt(itemid)).orElse(null);
        if (item != null) {
            item.setItemRecipient(recipientId);
            repository.save(item);
        }
    }

    @Transactional
    public void updateAvailability(String itemid, String availability) {
        Item item = repository.findById(Integer.parseInt(itemid)).orElse(null);
        if (item != null) {
            item.setItemAvailability(availability);
            repository.save(item);
        }
    }

    public Item findById(String id) {
        try {
            return repository.findById(Integer.parseInt(id)).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Item> findByUser(String userid) {
        return repository.findByItemDonor(userid);
    }

    public List<Item> findAvailable() {
        return repository.findByItemAvailability("available");
    }

    public List<Item> findAvailableByType(String type) {
        return repository.findByItemAvailabilityAndItemType("available", type);
    }

    public List<Item> findAll() {
        return repository.findAll();
    }
}
package freecycling.example.freecycling.Core;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDAO {
    
	@Autowired
    private EntityManager em;

    @Transactional
    public String save(ItemDTO item) {
        User user = em.find(User.class, Integer.parseInt(item.getItemDonor()));
        if (user == null) 
            return "Bad Id";
        
        Item newItem = new Item();
        newItem.setItemName(item.getItemName());
        newItem.setItemDonor(item.getItemDonor());
        newItem.setItemRecipient(item.getItemRecipient());
        newItem.setItemImageUrl(item.getItemImageUrl());
        newItem.setItemAvailability(item.getItemAvailability());
        newItem.setItemType(item.getItemType());
        newItem.setItemDescription(item.getItemDescription());
        
        em.persist(newItem);
        
        return String.valueOf(newItem.getItemId());
    }

    @Transactional
    public void saveRecipient(String itemid, String recipientId) {
        try {
            Item item = em.find(Item.class, Integer.parseInt(itemid));
            if (item != null) {
                item.setItemRecipient(recipientId);
                em.merge(item);
            }
        } catch (Exception ex) {
            // Silently handle exceptions
        }
    }

    @Transactional
    public void updateAvailability(String itemid, String availability) {
        try {
            Item item = em.find(Item.class, Integer.parseInt(itemid));
            if (item != null) {
                item.setItemAvailability(availability);
                em.merge(item);
            }
        } catch (Exception ex) {
            // Silently handle exceptions
        }
    }

    @Transactional
    public Item findById(String id) {
        try {
            return em.find(Item.class, Integer.parseInt(id));
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional
    public List<Item> findByUser(String userid) {
        return em.createQuery("SELECT i FROM Item i WHERE i.itemDonor = :userid", Item.class)
                .setParameter("userid", userid)
                .getResultList();
    }

    @Transactional
    public List<Item> findAvailable() {
        return em.createQuery("SELECT i FROM Item i WHERE i.itemAvailability = :availability", Item.class)
                .setParameter("availability", "available")
                .getResultList();
    }

    @Transactional
    public List<Item> findAvailableByType(String type) {
        return em.createQuery("SELECT i FROM Item i WHERE i.itemAvailability = :availability AND i.itemType = :type", Item.class)
                .setParameter("availability", "available")
                .setParameter("type", type)
                .getResultList();
    }

    @Transactional
    public List<Item> findAll() {
        return em.createQuery("SELECT i FROM Item i", Item.class)
                .getResultList();
    }
}

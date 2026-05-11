package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    
    List<Item> findByItemDonor(String donorId);
    
    List<Item> findByItemAvailability(String availability);
    
    List<Item> findByItemAvailabilityAndItemType(String availability, String itemType);
    
    // Custom query methods for finding by recipient (for saveRecipient use case)
}

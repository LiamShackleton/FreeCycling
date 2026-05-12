package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByItemDonor(String donorId);
    List<Item> findByItemAvailability(String availability);
    List<Item> findByItemAvailabilityAndItemType(String availability, String type);
}
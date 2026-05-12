package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findByUser(String userId);
    List<Interest> findByItem(String itemId);
}
package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    
    List<Interest> findByUser(String user);
    
    List<Interest> findByItem(String item);
}
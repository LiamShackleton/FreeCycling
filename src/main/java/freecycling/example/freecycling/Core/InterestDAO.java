package freecycling.example.freecycling.Core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterestDAO {
    
    @Autowired
    private InterestRepository interestRepository;

    public void save(Interest interest) {
        interestRepository.save(interest);
    }

    public Interest findById(String id) {
        try {
            return interestRepository.findById(Integer.parseInt(id)).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Interest> findByUser(String userid) {
        return interestRepository.findByUser(userid);
    }

    public List<Interest> findAll() {
        return interestRepository.findAll();
    }

    public List<Interest> findByItem(String itemId) {
        return interestRepository.findByItem(itemId);
    }
}
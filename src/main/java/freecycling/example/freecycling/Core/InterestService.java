package freecycling.example.freecycling.Core;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InterestService {

    private final InterestDAO interestDAO;

    public InterestService(InterestDAO interestDAO) {
        this.interestDAO = interestDAO;
    }

    public void save(Interest interest) {
        interestDAO.save(interest);
    }

    public Interest findById(String id) {
        return interestDAO.findById(id);
    }

    public List<Interest> findByUser(String userId) {
        return interestDAO.findByUser(userId);
    }

    public List<Interest> findAll() {
        return interestDAO.findAll();
    }

    public List<Interest> findByItem(String itemId) {
        return interestDAO.findByItem(itemId);
    }
}
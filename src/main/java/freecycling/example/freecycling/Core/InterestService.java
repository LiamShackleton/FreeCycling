package freecycling.example.freecycling.Core;

import freecycling.example.freecycling.Interface.DTOs.InterestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InterestService {

    private InterestRepository repository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    public InterestService(InterestRepository repository, UserRepository userRepository, ItemRepository itemRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public String save(InterestDTO interest, String authenticatedUserId) {
        User user = userRepository.findById(Integer.parseInt(authenticatedUserId)).orElse(null);
        if (user == null) {
            return "Bad Id";
        }

        Item item = itemRepository.findById(Integer.parseInt(interest.getItem())).orElse(null);
        if (item == null) {
            return "Bad Item Id";
        }

        Interest newInterest = new Interest();
        newInterest.setUser(authenticatedUserId);
        newInterest.setItem(interest.getItem());
        newInterest.setTimeStamp(interest.getTimeStamp());
        newInterest.setNote(interest.getNote());

        repository.save(newInterest);

        return String.valueOf(newInterest.getIdInterestTable());
    }

    public Interest findById(String id) {
        try {
            return repository.findById(Integer.parseInt(id)).orElse(null);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Interest> findByUser(String userid) {
        return repository.findByUser(userid);
    }

    public List<Interest> findAvailable() {
        // This method may not be applicable for Interest, but keeping for consistency
        return repository.findAll();
    }

    public List<Interest> findAll() {
        return repository.findAll();
    }

    public List<Interest> findByItem(String itemId) {
        return repository.findByItem(itemId);
    }
}
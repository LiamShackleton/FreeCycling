package freecycling.example.freecycling.Core;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterestDAO {
    
	@Autowired
    private EntityManager em;

    @Transactional
    public String save(InterestDTO interest) {
        User user = em.find(User.class, Integer.parseInt(interest.getUser()));
        if (user == null) 
            return "Bad Id";
        
        Item item = em.find(Item.class, Integer.parseInt(interest.getItem()));
        if (item == null)
            return "Bad Item Id";
        
        Interest newInterest = new Interest();
        newInterest.setUser(interest.getUser());
        newInterest.setItem(interest.getItem());
        newInterest.setTimeStamp(interest.getTimeStamp());
        newInterest.setNote(interest.getNote());
        
        em.persist(newInterest);
        
        return String.valueOf(newInterest.getIdInterestTable());
    }

    @Transactional
    public Interest findById(String id) {
        try {
            return em.find(Interest.class, Integer.parseInt(id));
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional
    public List<Interest> findByUser(String userid) {
        return em.createQuery("SELECT i FROM Interest i WHERE i.user = :userid", Interest.class)
                .setParameter("userid", userid)
                .getResultList();
    }

    @Transactional
    public List<Interest> findAvailable() {
        // This method may not be applicable for Interest, but keeping for consistency
        return em.createQuery("SELECT i FROM Interest i", Interest.class)
                .getResultList();
    }

    @Transactional
    public List<Interest> findAll() {
        return em.createQuery("SELECT i FROM Interest i", Interest.class)
                .getResultList();
    }

    @Transactional
    public List<Interest> findByItem(String itemId) {
        return em.createQuery("SELECT i FROM Interest i WHERE i.item = :itemId", Interest.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }
}

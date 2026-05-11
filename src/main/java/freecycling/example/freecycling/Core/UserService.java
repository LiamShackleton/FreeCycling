package freecycling.example.freecycling.Core;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void save(User user) {
        repository.save(user);
    }

    public User findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public User findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
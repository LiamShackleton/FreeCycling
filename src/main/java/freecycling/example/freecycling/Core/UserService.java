package freecycling.example.freecycling.Core;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(int id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (!maybeUser.isPresent())
            return null;
        return maybeUser.get();
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
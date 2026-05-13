package freecycling.example.freecycling.Core;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freecycling.example.freecycling.services.PasswordService;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordService passwordService;

    public void save(User user) {
        // Hash the password before saving
        String hashedPassword = passwordService.hashPassword(user.getPass());
        user.setPass(hashedPassword);
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

    public boolean verifyPassword(String password, String hash) {
        return passwordService.verifyHash(password, hash);
    }
}
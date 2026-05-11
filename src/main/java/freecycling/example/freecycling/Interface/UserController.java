package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.User;
import freecycling.example.freecycling.Core.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService service;

    // POST - register a new user
    // Called with POST /users
    @PostMapping
    public ResponseEntity<String> save(@RequestBody User user) {
        if (user.getUserName().isBlank() || user.getPass().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty username or password");
        }
        service.save(user);
        return ResponseEntity.ok().body("User saved successfully");
    }

    // POST - login
    // Called with POST /users/login
    @PostMapping("/login")
    public ResponseEntity<String> checkLogin(@RequestBody User user) {
        User result = service.findByUserName(user.getUserName());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        if (!result.getPass().equals(user.getPass())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok().body(String.valueOf(result.getUserId()));
    }

    // GET - find a user by id
    // Called with GET /users/5
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User result = service.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(result);
    }

    // GET - find a user by username
    // Called with GET /users/username/John
    @GetMapping("/username/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName) {
        User result = service.findByUserName(userName);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(result);
    }

    // GET - get all users
    // Called with GET /users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> results = service.findAll();
        return ResponseEntity.ok().body(results);
    }
}
package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Interface.DTOs.LoginDTO;
import freecycling.example.freecycling.Core.User;
import freecycling.example.freecycling.Core.UserService;
import freecycling.example.freecycling.security.FreecyclingUserDetails;
import freecycling.example.freecycling.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private JwtService jwtService;

    // POST - register a new user
    // Called with POST /users
    @PostMapping
    public ResponseEntity<String> save(@RequestBody User user) {
        if (user.getUserName().isBlank() || user.getPass().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty username or password");
        }
        service.save(user);
        User saved = service.findByUserName(user.getUserName());
        String token = jwtService.makeJwt(String.valueOf(saved.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    // POST - login using LoginDTO
    // Called with POST /users/login
    @PostMapping("/login")
    public ResponseEntity<String> checkLogin(@RequestBody LoginDTO login) {
        User result = service.findByUserName(login.getUserName());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        if (!service.verifyPassword(login.getPass(), result.getPass())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        String token = jwtService.makeJwt(String.valueOf(result.getUserId()));
        return ResponseEntity.ok().body(token);
    }

    // GET - find a user by id (protected)
    // Called with GET /users/5
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User result = service.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(result);
    }

    // GET - get current logged in user
    // Called with GET /users/me
    @GetMapping("/me")
    public ResponseEntity<User> getMe(Authentication authentication) {
        FreecyclingUserDetails details = (FreecyclingUserDetails) authentication.getPrincipal();
        int userId = Integer.parseInt(details.getUsername());
        User result = service.findById(userId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(result);
    }

    // GET - get all users (protected)
    // Called with GET /users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> results = service.findAll();
        return ResponseEntity.ok().body(results);
    }
}
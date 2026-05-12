package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.Interest;
import freecycling.example.freecycling.Core.InterestDAO;
import freecycling.example.freecycling.Core.InterestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interests")
@CrossOrigin(origins = "*")
public class InterestController {

    @Autowired
    private InterestDAO interestDAO;

    // POST - save a new interest item
    // Called with POST /interests
    @PostMapping
    public ResponseEntity<String> save(@RequestBody InterestDTO interest) {
        String result = interestDAO.save(interest);
        if ("Bad Id".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user or item ID");
        }
        return ResponseEntity.ok().body("Interest saved successfully");
    }

    // GET - find an interest object by id
    // Called with GET /interests/5
    @GetMapping("/{id}")
    public ResponseEntity<InterestDTO> findById(@PathVariable int id) {
        Interest result = interestDAO.findById(String.valueOf(id));
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(new InterestDTO(result));
    }

    // GET - find interests by user
    // Called with GET /interests/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InterestDTO>> findByUser(@PathVariable String userId) {
        List<Interest> results = interestDAO.findByUser(userId);
        return ResponseEntity.ok().body(results.stream()
                .map(InterestDTO::new)
                .collect(Collectors.toList()));
    }

    // GET - get all interests
    // Called with GET /interests
    @GetMapping
    public ResponseEntity<List<InterestDTO>> findAll() {
        List<Interest> results = interestDAO.findAll();
        return ResponseEntity.ok().body(results.stream()
                .map(InterestDTO::new)
                .collect(Collectors.toList()));
    }

    // GET - find user ids interested in item by item id
    // Called with GET /interests/item/{itemId}
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<String>> findUserIdsByItem(@PathVariable String itemId) {
        List<Interest> interests = interestDAO.findByItem(itemId);
        return ResponseEntity.ok().body(interests.stream()
                .map(Interest::getUser)
                .collect(Collectors.toList()));
    }
}
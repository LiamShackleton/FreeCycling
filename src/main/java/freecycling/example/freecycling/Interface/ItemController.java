package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.Item;
import freecycling.example.freecycling.Interface.DTOs.ItemDTO;
import freecycling.example.freecycling.Core.ItemService;
import freecycling.example.freecycling.security.FreecyclingUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // GET - find all available items with full details
    // Called with GET /items/getAvailable
    @GetMapping("/getAvailable")
    public ResponseEntity<List<ItemDTO>> findAvailableItems() {
        List<Item> items = itemService.findAvailable();
        List<ItemDTO> results = new ArrayList<ItemDTO>();
        for(Item i : items) {
            results.add(new ItemDTO(i));
        }
        return ResponseEntity.ok().body(results);
    }

    // POST - register a new item
    // Called with POST /items
    @PostMapping
    public ResponseEntity<String> saveItem(Authentication authentication, @RequestBody ItemDTO item) {
        FreecyclingUserDetails details = (FreecyclingUserDetails) authentication.getPrincipal();
        String result = itemService.save(item, details.getUsername());
        if ("Bad Id".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid itemDonor");
        }
        return ResponseEntity.ok().body(result);
    }

    // PATCH - update the recipient for an item
    // Called with PATCH /items/recipient?itemId=5&recipientId=3
    @PatchMapping("/recipient")
    public ResponseEntity<String> saveRecipient(@RequestParam String itemId, @RequestParam String recipientId) {
        try {
            itemService.saveRecipient(itemId, recipientId);
            return ResponseEntity.ok().body("Recipient updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating recipient: " + ex.getMessage());
        }
    }

    // PATCH - update the availability for an item
    // Called with PATCH /items/availability?itemId=5&availability=available
    @PatchMapping("/availability")
    public ResponseEntity<String> updateAvailability(@RequestParam String itemId, @RequestParam String availability) {
        try {
            itemService.updateAvailability(itemId, availability);
            return ResponseEntity.ok().body("Availability updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating availability: " + ex.getMessage());
        }
    }

    // GET - find an item by id
    // Called with GET /items/5
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> findById(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(new ItemDTO(item));
    }

    // GET - get all items
    // Called with GET /items
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<Item> items = itemService.findAll();
        List<ItemDTO> results = new ArrayList<ItemDTO>();
        for(Item i : items) {
            results.add(new ItemDTO(i));
        }
        return ResponseEntity.ok().body(results);
    }
}

package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.Item;
import freecycling.example.freecycling.Core.ItemDAO;
import freecycling.example.freecycling.Core.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemDAO itemDAO;

    // GET - get all available items
    // Called with GET /items/getAvailable
    @GetMapping("/getAvailable")
    public ResponseEntity<List<Integer>> getAvailableItems() {
        List<Integer> results = itemDAO.findAvailable().stream()
                .map(Item::getItemId)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok().body(results);
    }

    // POST - register a new item
    // Called with POST /items
    @PostMapping
    public ResponseEntity<String> saveItem(@RequestBody ItemDTO item) {
        String result = itemDAO.save(item);
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
            itemDAO.saveRecipient(itemId, recipientId);
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
            itemDAO.updateAvailability(itemId, availability);
            return ResponseEntity.ok().body("Availability updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating availability: " + ex.getMessage());
        }
    }

    // GET - find an item by id
    // Called with GET /items/5
    @GetMapping("/{id}")
    public ResponseEntity<Integer> getItemById(@PathVariable String id) {
        Item item = itemDAO.findById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(item.getItemId());
    }

    // GET - get all items
    // Called with GET /items
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<Item> items = itemDAO.findAll();
        List<ItemDTO> dtos = items.stream()
                .map(item -> {
                    ItemDTO dto = new ItemDTO();
                    dto.setItemId(item.getItemId());
                    dto.setItemName(item.getItemName());
                    dto.setItemDonor(item.getItemDonor());
                    dto.setItemRecipient(item.getItemRecipient());
                    dto.setItemImageUrl(item.getItemImageUrl());
                    dto.setItemAvailability(item.getItemAvailability());
                    dto.setItemType(item.getItemType());
                    dto.setItemDescription(item.getItemDescription());
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }
}

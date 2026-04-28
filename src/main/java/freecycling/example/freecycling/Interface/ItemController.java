package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.Item;
import freecycling.example.freecycling.Core.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Item> getAvailableItems() {
        return itemDAO.findAvailable();
    }

    // POST - register a new item
    // Called with POST /items
    @PostMapping
    public int saveItem(@RequestBody Item item) {
        itemDAO.save(item);
        return 1;
    }

    // PATCH - update the recipient for an item
    // Called with PATCH /items/recipient?itemId=5&recipientId=3
    @PatchMapping("/recipient")
    public int saveRecipient(@RequestParam String itemId, @RequestParam String recipientId) {
        itemDAO.saveRecipient(itemId, recipientId);
        return 1;
    }

    // PATCH - update the availability for an item
    // Called with PATCH /items/availability?itemId=5&availability=available
    @PatchMapping("/availability")
    public int updateAvailability(@RequestParam String itemId, @RequestParam String availability) {
        itemDAO.updateAvailability(itemId, availability);
        return 1;
    }

    // GET - find an item by id
    // Called with GET /items/5
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable String id) {
        return itemDAO.findById(id);
    }

    // GET - get all items
    // Called with GET /items
    @GetMapping
    public List<Item> getAllItems() {
        return itemDAO.findAll();
    }
}

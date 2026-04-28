package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.Item;
import freecycling.example.freecycling.Core.ItemDAO;
import freecycling.example.freecycling.Core.User;
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

    // POST - register a recipient for an item
    // Called with POST /items/recipient?itemId=5
    @PostMapping("/recipient")
    public int saveRecipient(@RequestParam String itemId, @RequestBody User recipient) {
        itemDAO.saveRecipient(itemId, recipient);
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

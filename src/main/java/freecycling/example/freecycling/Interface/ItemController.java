package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.core.Item;
import freecycling.example.freecycling.core.ItemDAO;
import freecycling.example.freecycling.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemDAO itemDAO;

    @GetMapping("/getAvailable")
    public List<Item> getAvailableItems() {
        return itemDAO.findAvailable();
    }

    @PostMapping
    public int saveItem(@RequestBody Item item) {
        itemDAO.save(item);
        return 1;
    }

    @PostMapping("/recipient")
    public int saveRecipient(@RequestParam String itemId, @RequestBody User recipient) {
        itemDAO.saveRecipient(itemId, recipient);
        return 1;
    }
}

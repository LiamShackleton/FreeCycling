package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.TransferSite;
import freecycling.example.freecycling.Core.TransferSiteService;

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
@RequestMapping("/transfersites")
@CrossOrigin(origins = "*")
public class TransferSiteController {

    @Autowired
    private TransferSiteService service;

    // POST - register a new transfer site
    // Called with POST /transfersites
    @PostMapping
    public ResponseEntity<String> save(@RequestBody TransferSite site) {
        if (site.getTransferSiteName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transfer site name cannot be empty");
        }
        service.save(site);
        return ResponseEntity.ok().body("Transfer site saved");
    }

    // GET - find a transfer site by id
    // Called with GET /transfersites/5
    @GetMapping("/{id}")
    public ResponseEntity<TransferSite> getSiteById(@PathVariable int id) {
        TransferSite result = service.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(result);
    }

    // GET - get all transfer sites
    // Called with GET /transfersites
    @GetMapping
    public ResponseEntity<List<TransferSite>> getAllSites() {
        List<TransferSite> results = service.findAll();
        return ResponseEntity.ok().body(results);
    }

    // GET - find transfer sites by name
    // Called with GET /transfersites/name/Main Library
    @GetMapping("/name/{name}")
    public ResponseEntity<List<TransferSite>> getSiteByName(@PathVariable String name) {
        List<TransferSite> results = service.findByName(name);
        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(results);
    }
}
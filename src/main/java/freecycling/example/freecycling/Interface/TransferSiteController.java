package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.TransferSite;
import freecycling.example.freecycling.Interface.DTOs.TransferSiteDTO;
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

import java.util.ArrayList;
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
    public ResponseEntity<String> save(@RequestBody TransferSiteDTO siteDto) {
        String key = service.save(siteDto);
        if (key.equals("Bad Id")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Transfer Site Data");
        }
        return ResponseEntity.ok().body(key);
    }

    // GET - find a transfer site by id
    // Called with GET /transfersites/5
    @GetMapping("/{id}")
    public ResponseEntity<TransferSiteDTO> getSiteById(@PathVariable int id) {
        TransferSite result = service.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(new TransferSiteDTO(result));
    }

    // GET - get all transfer sites
    // Called with GET /transfersites
    @GetMapping
    public ResponseEntity<List<TransferSiteDTO>> getAllSites() {
        List<TransferSite> sites = service.findAll();
        List<TransferSiteDTO> results = new ArrayList<>();
        for (TransferSite site : sites) {
            results.add(new TransferSiteDTO(site));
        }
        return ResponseEntity.ok().body(results);
    }

    // GET - find transfer sites by name
    // Called with GET /transfersites/name/Main Library
    @GetMapping("/name/{name}")
    public ResponseEntity<List<TransferSiteDTO>> getSiteByName(@PathVariable String name) {
        List<TransferSite> results = service.findByName(name);
        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<TransferSiteDTO> dtoResults = new ArrayList<>();
        for (TransferSite site : results) {
            dtoResults.add(new TransferSiteDTO(site));
        }
        return ResponseEntity.ok().body(dtoResults);
    }
}

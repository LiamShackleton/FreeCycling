package freecycling.example.freecycling.Core;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransferSiteService {

    private TransferSiteRepository repository;

    public TransferSiteService(TransferSiteRepository repository) {
        this.repository = repository;
    }

    public void save(TransferSite site) {
        repository.save(site);
    }

    public TransferSite findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<TransferSite> findAll() {
        return repository.findAll();
    }

    public List<TransferSite> findByName(String name) {
        return repository.findByTransferSiteName(name);
    }
}
package freecycling.example.freecycling.Core;

import freecycling.example.freecycling.Interface.DTOs.TransferSiteDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransferSiteService {

    private TransferSiteRepository repository;

    public TransferSiteService(TransferSiteRepository repository) {
        this.repository = repository;
    }

    public String save(TransferSiteDTO siteDto) {
        TransferSite site = new TransferSite();
        site.setTransferSiteName(siteDto.getTransferSiteName());
        site.setTransferAddress(siteDto.getTransferAddress());
        
        repository.save(site);
        return String.valueOf(site.getIdTransferTable());
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
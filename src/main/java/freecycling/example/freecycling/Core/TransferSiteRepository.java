package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransferSiteRepository extends JpaRepository<TransferSite, Integer> {
    List<TransferSite> findByTransferSiteName(String TransferSiteName);
}
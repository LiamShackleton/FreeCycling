package freecycling.example.freecycling.Core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TransferTable")
public class TransferSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTransferSite;
    
    @Column(name = "TransferSiteName")
    private String transferSiteName;
    
    @Column(name = "TransferAddress")
    private String transferAddress;

    public TransferSite() {
    }

    public int getIdTransferSite() {
        return idTransferSite;
    }

    public void setIdTransferSite(int idTransferSite) {
        this.idTransferSite = idTransferSite;
    }

    public String getTransferSiteName() {
        return transferSiteName;
    }

    public void setTransferSiteName(String transferSiteName) {
        this.transferSiteName = transferSiteName;
    }

    public String getTransferAddress() {
        return transferAddress;
    }

    public void setTransferAddress(String transferAddress) {
        this.transferAddress = transferAddress;
    }
}

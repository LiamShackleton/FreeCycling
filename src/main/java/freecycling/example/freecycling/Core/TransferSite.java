package freecycling.example.freecycling.Core;

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
    private String TransferSiteName;
    private String TransferAddress;

    public TransferSite() {
    }

    public int getIdTransferSite() {
        return idTransferSite;
    }

    public void setIdTransferSite(int idTransferSite) {
        this.idTransferSite = idTransferSite;
    }

    public String getTransferSiteName() {
        return TransferSiteName;
    }

    public void setTransferSiteName(String TransferSiteName) {
        this.TransferSiteName = TransferSiteName;
    }

    public String getTransferAddress() {
        return TransferAddress;
    }

    public void setTransferAddress(String TransferAddress) {
        this.TransferAddress = TransferAddress;
    }
}
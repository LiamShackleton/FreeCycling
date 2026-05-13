package freecycling.example.freecycling.Interface.DTOs;

import freecycling.example.freecycling.Core.TransferSite;

public class TransferSiteDTO {
    
    private int idTransferSite;
    
    private String transferSiteName;
    
    private String transferAddress;

    public TransferSiteDTO() {
    }

    public TransferSiteDTO(TransferSite transferSite) {
        this.idTransferSite = transferSite.getIdTransferSite();
        this.transferSiteName = transferSite.getTransferSiteName();
        this.transferAddress = transferSite.getTransferAddress();
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
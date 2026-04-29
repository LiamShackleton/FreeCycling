package freecycling.example.freecycling.Core;

public class TransferSite {
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

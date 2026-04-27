package freecycling.example.freecycling.Core;

public class Item {
    private int itemId;
    private String itemName;
    private String itemDonor;
    private String itemRecipient;
    private String itemImageUrl;
    private String itemAvailability;
    private String itemType;
    private String itemDescription;

    public Item() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDonor() {
        return itemDonor;
    }

    public void setItemDonor(String itemDonor) {
        this.itemDonor = itemDonor;
    }

    public String getItemRecipient() {
        return itemRecipient;
    }

    public void setItemRecipient(String itemRecipient) {
        this.itemRecipient = itemRecipient;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public String getItemAvailability() {
        return itemAvailability;
    }

    public void setItemAvailability(String itemAvailability) {
        this.itemAvailability = itemAvailability;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
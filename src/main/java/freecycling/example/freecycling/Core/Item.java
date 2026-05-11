package freecycling.example.freecycling.Core;

import jakarta.persistence.*;

@Entity
@Table(name = "ItemTable")
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idItemTable")
    private int itemId;
    
    @Column(name = "ItemName", nullable = false, length = 45)
    private String itemName;
    
    @Column(name = "ItemDonor", nullable = false, length = 45)
    private String itemDonor;
    
    @Column(name = "ItemRecipient", length = 45)
    private String itemRecipient;
    
    @Column(name = "ItemImageURL", length = 45)
    private String itemImageUrl;
    
    @Column(name = "ItemAvailability", nullable = false, length = 45)
    private String itemAvailability;
    
    @Column(name = "ItemType", length = 45)
    private String itemType;
    
    @Column(name = "ItemDescription", length = 45)
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
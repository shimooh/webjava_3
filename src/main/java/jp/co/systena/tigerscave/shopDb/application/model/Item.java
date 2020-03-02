package jp.co.systena.tigerscave.shopDb.application.model;

import javax.validation.constraints.Pattern;

public class Item {

    @Pattern(regexp="^[0-9]*$")
    private String itemId;
    @Pattern(regexp="^[0-9]*$")
    private String price;
    private String itemName;
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}

package jp.co.systena.tigerscave.shopDb.application.model;

import javax.validation.constraints.Min;

public class ListForm {
    /** 商品ID */
    private int itemId;

    /** 個数 */
    @Min(1) //設定できる最小値を定義
    private int num;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
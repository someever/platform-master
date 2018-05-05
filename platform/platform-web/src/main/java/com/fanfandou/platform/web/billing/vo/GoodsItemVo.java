package com.fanfandou.platform.web.billing.vo;

/**
 * Created by wudi.
 * Descreption:充值赠送物品.
 * Date:2017/6/23
 */
public class GoodsItemVo {

    private String typeId;

    private int itemId;

    private int value;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

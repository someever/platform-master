package com.fanfandou.platform.web.billing.vo;

/**
 * Created by wudi.
 * Descreption:正常客户端充值请求.
 * Date:2017/7/18
 */
public class AppStoreVo {
    private int gameId;

    private int siteId;

    private int areaCode;

    private String ReceiptData;

    private String orderId;

    private int payAmount;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public String getReceiptData() {
        return ReceiptData;
    }

    public void setReceiptData(String receiptData) {
        ReceiptData = receiptData;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }
}

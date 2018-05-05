package com.fanfandou.platform.web.billing.callback.Utils.quick;

/**
 * Created by wudi.
 * Descreption:quick sdk 实体类.
 * Date:2017/7/29
 */
public class QuickData {

    private boolean isTest;

    private String channel;

    private String channelUid;

    private String gameOrder;

    private String orderNo;

    private String payTime;

    private String amount;

    private String status;

    private String extraParams;

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelUid() {
        return channelUid;
    }

    public void setChannelUid(String channelUid) {
        this.channelUid = channelUid;
    }

    public String getGameOrder() {
        return gameOrder;
    }

    public void setGameOrder(String gameOrder) {
        this.gameOrder = gameOrder;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }
}

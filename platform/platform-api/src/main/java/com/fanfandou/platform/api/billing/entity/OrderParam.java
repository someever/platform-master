package com.fanfandou.platform.api.billing.entity;

import com.fanfandou.common.base.BaseVo;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wudi.
 * Descreption:用于订单的创建.
 * Date:2016/5/10
 */
public class OrderParam extends BaseVo implements Serializable {

    private long userId;
    private long roleId;//角色ID
    private int siteId;//渠道ID
    private int gameId;//游戏ID
    private String areaCode;//区服CODE
    private String reOrderId;
    private String orderDesc;//订单说明
    private int currency;//币种，没有就填RMB
    private int amount;//价格
    private String goodsCode;//商品CODE
    private int goodsId;//商品ID
    private int goodsCount;//宝石数量
    private int payAmount;//价格
    private int payType;//如果是模拟充值 id = 4
    private String ipAddress;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Currency getCurrency() {
        return Currency.valueOf(currency);
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public PayType getPayType() {
        return PayType.valueOf(payType);
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getReOrderId() {
        return reOrderId;
    }

    public void setReOrderId(String reOrderId) {
        this.reOrderId = reOrderId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

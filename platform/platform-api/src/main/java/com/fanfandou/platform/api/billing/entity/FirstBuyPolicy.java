package com.fanfandou.platform.api.billing.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:首充策略.
 * Date:2016/5/19
 */
public class FirstBuyPolicy implements Serializable {
    //首充策略ID
    private String firstBuyId;

    //是否开启 1 为开启
    private Integer firstPay = 0;

    //首充奖励倍数
    private Integer operateCount;

    //首充奖励包
    private String itemPackage;

    private int packageId;


    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getFirstBuyId() {
        return firstBuyId;
    }

    public void setFirstBuyId(String firstBuyId) {
        this.firstBuyId = firstBuyId;
    }

    public int getOperateCount() {
        return operateCount;
    }

    public void setOperateCount(int operateCount) {
        this.operateCount = operateCount;
    }

    public String getItemPackage() {
        return itemPackage;
    }

    public void setItemPackage(String itemPackage) {
        this.itemPackage = itemPackage;
    }

    public int getFirstPay() {
        return firstPay;
    }

    public void setFirstPay(int firstPay) {
        this.firstPay = firstPay;
    }
}

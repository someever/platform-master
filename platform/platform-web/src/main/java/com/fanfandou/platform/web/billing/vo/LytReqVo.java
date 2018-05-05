package com.fanfandou.platform.web.billing.vo;

/**
 * Created by wudi.
 * Descreption:乐盈支付回调.
 * Date:2017/6/20
 */
public class LytReqVo {

    private int code;

    private String appId;

    private String account;

    private String role_id;

    private String commodity_id;

    private int price;

    private String trade_id;

    private String cp_order_id;

    private String server_id;

    private String cp_extension;

    private String sign;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getCp_order_id() {
        return cp_order_id;
    }

    public void setCp_order_id(String cp_order_id) {
        this.cp_order_id = cp_order_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getCp_extension() {
        return cp_extension;
    }

    public void setCp_extension(String cp_extension) {
        this.cp_extension = cp_extension;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

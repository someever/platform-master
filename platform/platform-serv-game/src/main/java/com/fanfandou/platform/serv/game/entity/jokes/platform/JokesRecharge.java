package com.fanfandou.platform.serv.game.entity.jokes.platform;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:支付协议字段.
 * Date:2017/6/23
 */
public class JokesRecharge implements Serializable {

    private int action;

    private String transId;

    private int msgId;

    private int roleid;

    private int gem;

    private int money;

    private String orderNo;

    private int type;

    private String extraData;

    private String sign;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}

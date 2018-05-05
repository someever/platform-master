package com.fanfandou.platform.serv.game.entity.jokes.gm;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:删除当前配置的活动.
 * Date:2017/8/1
 */
public class JokesDelActivity implements Serializable {

    private int action;

    private int msgId;

    private int id;

    private int type;

    private String sign;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

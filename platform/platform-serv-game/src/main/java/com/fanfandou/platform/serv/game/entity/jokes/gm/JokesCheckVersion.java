package com.fanfandou.platform.serv.game.entity.jokes.gm;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:十冷版本校验.
 * Date:2017/8/5
 */
public class JokesCheckVersion implements Serializable {

    private int action;

    private int msgId;

    private String resid;

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

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

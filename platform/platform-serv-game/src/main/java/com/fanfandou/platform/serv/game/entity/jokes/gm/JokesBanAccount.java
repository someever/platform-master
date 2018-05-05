package com.fanfandou.platform.serv.game.entity.jokes.gm;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:封号.
 * Date:2017/6/19
 */
public class JokesBanAccount implements Serializable {

    private int action;

    private int roleid;

    private int time;//分钟

    private int type; //1禁言，2解封

    private String reason;

    private String sign;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

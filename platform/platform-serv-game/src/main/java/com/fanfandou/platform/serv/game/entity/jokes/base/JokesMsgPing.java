
package com.fanfandou.platform.serv.game.entity.jokes.base;

import java.io.Serializable;


/**
 * Created by wudi.
 * Descreption:十冷gm心跳.
 * Date:2017/3/13
 */
public class JokesMsgPing implements Serializable {

    private int action;
    private int msgId;
    private String serverid;
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

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
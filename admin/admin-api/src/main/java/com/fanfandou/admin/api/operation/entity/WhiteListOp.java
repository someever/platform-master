package com.fanfandou.admin.api.operation.entity;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:白名单.
 * Date:2017/3/31
 */
public class WhiteListOp implements Serializable {

    private boolean turnOff;

    private String whiteContent;

    public boolean isTurnOff() {
        return turnOff;
    }

    public void setTurnOff(boolean turnOff) {
        this.turnOff = turnOff;
    }

    public String getWhiteContent() {
        return whiteContent;
    }

    public void setWhiteContent(String whiteContent) {
        this.whiteContent = whiteContent;
    }
}

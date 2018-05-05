package com.fanfandou.platform.serv.game.entity.jokes.gm;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:玩家角色信息.
 * Date:2017/6/20
 */
public class JokesRoleInfoRes implements Serializable {

    private int roleId;

    private int infoType;

    private String extData;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getInfoType() {
        return infoType;
    }

    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }
}

package com.fanfandou.platform.api.game.entity;

import java.io.Serializable;

/**
 * Created by wudi.
 * Descreption:角色完整信息.
 * Date:2017/6/20
 */
public class GameRoleInfoView  implements Serializable {


    private String roleName;

    private int gold;

    private int gem;

    private int level;

    private int vipLevel;

    private int fightPower;

    private int lastLoginTime;

    private int lastLogoutTime;

    private int state;

    public int getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(int lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getFightPower() {
        return fightPower;
    }

    public void setFightPower(int fightPower) {
        this.fightPower = fightPower;
    }

    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }


}

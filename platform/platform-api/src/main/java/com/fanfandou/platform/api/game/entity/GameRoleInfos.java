package com.fanfandou.platform.api.game.entity;

/**
 * Created by wudi.
 * Descreption:角色完整信息.
 * Date:2017/6/20
 */
public class GameRoleInfos {

    private int roleId;

    private String roleName;

    private int gold;

    private int gem;

    private int level;

    private int vipLevel;

    private int fightPower;

    private int lastLoginTime;

    private int site;

    private String areaCode;

    private String areaName;

    private int totalPay;

    private String accountStatus;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}

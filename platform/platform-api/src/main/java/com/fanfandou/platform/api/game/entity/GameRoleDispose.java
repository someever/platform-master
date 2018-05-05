package com.fanfandou.platform.api.game.entity;

import java.io.Serializable;
import java.util.Date;

public class GameRoleDispose implements Serializable {
    private Long id;

    private Long roleId;

    private String roleName;

    private Long userId;

    private Integer gameId;

    private Integer siteId;

    private Integer areaId;

    private String closureReason;

    private Integer closureTime;

    private Date createTime;

    private Integer roleStatus;

    private String disablesendmsgReason;

    private Date disablesendmsgTime;

    private String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getClosureReason() {
        return closureReason;
    }

    public void setClosureReason(String closureReason) {
        this.closureReason = closureReason == null ? null : closureReason.trim();
    }

    public Integer getClosureTime() {
        return closureTime;
    }

    public void setClosureTime(Integer closureTime) {
        this.closureTime = closureTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(Integer roleStatus) {
        this.roleStatus = roleStatus;
    }

    public String getDisablesendmsgReason() {
        return disablesendmsgReason;
    }

    public void setDisablesendmsgReason(String disablesendmsgReason) {
        this.disablesendmsgReason = disablesendmsgReason == null ? null : disablesendmsgReason.trim();
    }

    public Date getDisablesendmsgTime() {
        return disablesendmsgTime;
    }

    public void setDisablesendmsgTime(Date disablesendmsgTime) {
        this.disablesendmsgTime = disablesendmsgTime;
    }
}
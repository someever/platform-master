package com.fanfandou.platform.api.game.entity;

import com.fanfandou.common.entity.ValidStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class GameRole implements Serializable {
    private Long id;

    private Long roleId;

    private String roleName;

    private Long userId;

    private Integer gameId;

    private Integer siteId;

    private Integer areaId;

    private String areaName;

    private Integer areaCode;

    private String roleHeadIcon;

    private Integer roleLevel;

    private String roleDesc;

    private Date lastLoginTime;

    private Date lastLogoutTime;

    private Date createTime;

    private ValidStatus validStatus = ValidStatus.VALID;

    private long totalPayAmount = 0;

    private int totalPayTimes = 0;

    private Integer totalLoginTimes;

    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getRoleHeadIcon() {
        return roleHeadIcon;
    }

    public void setRoleHeadIcon(String roleHeadIcon) {
        this.roleHeadIcon = roleHeadIcon == null ? null : roleHeadIcon.trim();
    }

    public Integer getRoleLevel() {
        return roleLevel != null ? roleLevel : 0;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public long getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(long totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public int getTotalPayTimes() {
        return totalPayTimes;
    }

    public void setTotalPayTimes(int totalPayTimes) {
        this.totalPayTimes = totalPayTimes;
    }

    public Integer getTotalLoginTimes() {
        return totalLoginTimes;
    }

    public void setTotalLoginTimes(Integer totalLoginTimes) {
        this.totalLoginTimes = totalLoginTimes;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
package com.fanfandou.platform.web.game.vo;

import com.fanfandou.common.entity.ValidStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wudi.
 * Descreption:TODO..
 * Date:2017/6/7
 */
public class GameRoleVo implements Serializable {

    private Long id;

    private Long roleId;

    private String roleName;

    private Long userId;

    private Integer gameId;

    private Integer siteId;

    private Integer areaId;

    private Integer areaCode;

    private String roleHeadIcon;

    private Integer roleLevel;

    private String roleDesc;

    private Date lastLoginTime;

    private Date lastLogoutTime;

    private String createTime;

    private ValidStatus validStatus = ValidStatus.VALID;

    private Long totalPayAmount;

    private Integer totalPayTimes;

    private Integer totalLoginTimes;

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
        this.roleName = roleName;
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
        this.roleHeadIcon = roleHeadIcon;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Long getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(Long totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public Integer getTotalPayTimes() {
        return totalPayTimes;
    }

    public void setTotalPayTimes(Integer totalPayTimes) {
        this.totalPayTimes = totalPayTimes;
    }

    public Integer getTotalLoginTimes() {
        return totalLoginTimes;
    }

    public void setTotalLoginTimes(Integer totalLoginTimes) {
        this.totalLoginTimes = totalLoginTimes;
    }
}

package com.fanfandou.platform.api.activity.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

public class GameActivityCon implements Serializable {
    private Integer id;

    private Integer gameActivityId;

    private String activityName;

    private Date beginTime;

    private Date endTime;

    private String redundancy;

    private String activityJson;

    private Date createTime;

    private Integer gameId;

    private String areaId;

    private String taskIds;

    private Integer channelType;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRedundancy() {
        return redundancy;
    }

    public void setRedundancy(String redundancy) {
        this.redundancy = redundancy == null ? null : redundancy.trim();
    }

    public String getActivityJson() {
        return activityJson;
    }

    public Integer getGameActivityId() {
        return gameActivityId;
    }

    public void setGameActivityId(Integer gameActivityId) {
        this.gameActivityId = gameActivityId;
    }

    public void setActivityJson(String activityJson) {
        this.activityJson = activityJson == null ? null : activityJson.trim();
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
package com.fanfandou.admin.api.operation.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi.
 * Descreption:补丁版本判断.
 * Date:2017/2/23
 */
public class GameVersionCheck implements Serializable {

    /**
     * 是否开启整包更新.
     */
    private boolean appNeedUpdate;

    /**
     * 资源是否要更新.
     */
    private boolean resourceNeedUpdate;

    /**
     * 游戏资源版本号.
     */
    private int resourceVerCode;

    /**
     * 游戏资源补丁名称.
     */
    private String resourceVerName;

    /**
     * 描述.
     */
    private String resourceVerDesc;

    /**
     * 补丁更新配置.
     */
    private GameUpdateConfig appUpdateConfig;

    /**
     * 整包版本号.
     */
    private int appVerCode;

    /**
     * 整包名称.
     */
    private String appVerName;

    /**
     * 整包描述.
     */
    private String appVerDesc;

    /**
     * 多配置.
     */
    private List<GameUpdateConfig> resourceUpdateConfigs = new ArrayList<GameUpdateConfig>();

    public boolean isResourceNeedUpdate() {
        return resourceNeedUpdate;
    }

    public void setResourceNeedUpdate(boolean resourceNeedUpdate) {
        this.resourceNeedUpdate = resourceNeedUpdate;
    }

    public int getResourceVerCode() {
        return resourceVerCode;
    }

    public void setResourceVerCode(int resourceVerCode) {
        this.resourceVerCode = resourceVerCode;
    }

    public String getResourceVerName() {
        return resourceVerName;
    }

    public void setResourceVerName(String resourceVerName) {
        this.resourceVerName = resourceVerName;
    }

    public String getResourceVerDesc() {
        return resourceVerDesc;
    }

    public void setResourceVerDesc(String resourceVerDesc) {
        this.resourceVerDesc = resourceVerDesc;
    }

    public GameUpdateConfig getAppUpdateConfig() {
        return appUpdateConfig;
    }

    public void setAppUpdateConfig(GameUpdateConfig appUpdateConfig) {
        this.appUpdateConfig = appUpdateConfig;
    }

    public List<GameUpdateConfig> getResourceUpdateConfigs() {
        return resourceUpdateConfigs;
    }

    public void setResourceUpdateConfigs(List<GameUpdateConfig> resourceUpdateConfigs) {
        this.resourceUpdateConfigs = resourceUpdateConfigs;
    }

    public boolean isAppNeedUpdate() {
        return appNeedUpdate;
    }

    public void setAppNeedUpdate(boolean appNeedUpdate) {
        this.appNeedUpdate = appNeedUpdate;
    }

    public int getAppVerCode() {
        return appVerCode;
    }

    public void setAppVerCode(int appVerCode) {
        this.appVerCode = appVerCode;
    }

    public String getAppVerName() {
        return appVerName;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public String getAppVerDesc() {
        return appVerDesc;
    }

    public void setAppVerDesc(String appVerDesc) {
        this.appVerDesc = appVerDesc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

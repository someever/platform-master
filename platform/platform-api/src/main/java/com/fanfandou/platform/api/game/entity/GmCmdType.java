package com.fanfandou.platform.api.game.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by wudi.
 * Descreption:如文件名.
 * Date:2017/6/5
 */
public enum GmCmdType {

    LOADGAMECFG(1,"loadGameCfg"),
    VERSION(2, "version")
    ;

    private int id;
    private String code;

    GmCmdType(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    /**
     * 根据id获取GmCmdType.
     * @param id id.
     * @return GmCmdType.
     */
    public static GmCmdType valueOf(int id) {
        GmCmdType[] types = values();
        for (GmCmdType type : types) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

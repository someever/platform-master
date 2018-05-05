package com.fanfandou.platform.serv.game.entity.jokes;

import com.fanfandou.platform.api.game.entity.MessageType;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesActivity;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesBanAccount;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesBanTalk;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesCallBack;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesCheckVersion;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesDelActivity;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesGmSend;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesMsgMail;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesRoleInfoReq;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesScrollNotice;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesShutDownServer;
import com.fanfandou.platform.serv.game.entity.jokes.platform.JokesRecharge;
import com.fanfandou.platform.serv.game.entity.tol.base.Msg_Connect;
import com.fanfandou.platform.serv.game.entity.tol.base.Msg_Ping_Req;
import com.fanfandou.platform.serv.game.entity.tol.base.Msg_Ping_Res;

/**
 * Created by wudi.
 * Descreption:十冷消息类型.
 * Date:2017/5/17
 */
public enum JokesCommonType {

    MSG_CONNECT(0, Msg_Connect.class),
    MSG_PING_REQ(1, Msg_Ping_Req.class),
    MSG_PING_RES(2, Msg_Ping_Res.class, MessageType.NOT_PROCESS),

    //停服
    MSG_SHUTDOWN_SERVER(3101, JokesShutDownServer.class, "actionId", MessageType.NOT_PROCESS),
    //发送物品邮件奖励
    MSG_HILINK_MAIL_TOONE(3102, JokesMsgMail.class, "actionId", MessageType.ASYNC_PROCESS),
    //发送跑马灯
    MSG_HILINK_NOTICE_TOALL(3103, JokesScrollNotice.class, "actionId", MessageType.NOT_PROCESS),
    //充值
    MSG_HILINK_PAY(3104, JokesRecharge.class, "actionId", MessageType.ASYNC_PROCESS),
    //封号
    MSG_BAN_ACCOUNT(3107, JokesBanAccount.class, "actionId", MessageType.NOT_PROCESS),
    //给游戏发送GM指令加载不同配置
    MSG_HILINK_GM_CMD(3108, JokesGmSend.class, "actionId", MessageType.NOT_PROCESS),
    //游戏主动推送
    MSG_HILINK_CALLBACK(3109, JokesCallBack.class, "actionId", MessageType.ASYNC_PROCESS),
    //心跳
    MSG_PING(3110, Msg_Ping_Res.class, "actionId", MessageType.NOT_PROCESS),
    //获取角色信息
    MSG_GET_ROLEINFO(3112, JokesRoleInfoReq.class, "actionId", MessageType.SYNC_PROCESS),
    //禁言
    MSG_BAN_TALK(3113, JokesBanTalk.class, "actionId", MessageType.NOT_PROCESS),
    //游戏活动开关
    MSG_ACTIVITY_CONTROLLER(3114, JokesActivity.class, "actionId", MessageType.ASYNC_PROCESS),
    //删除活动
    MSG_DEL_ACTIVITY_CONTROLLER(3115, JokesDelActivity.class, "actionId", MessageType.ASYNC_PROCESS),
    //推送版本
    MSG_PUSH_GAME_VERSION(3116, JokesCheckVersion.class, "actionId", MessageType.NOT_PROCESS)
    ;




    private int id;
    private Class clazz;
    //通讯流水号在协议中的字段名，用来反射获取流水号
    private String msgIdFieldName;
    //接收消息的处理类型(仅res消息使用)
    private MessageType msgType;

    JokesCommonType(int id, Class clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    JokesCommonType(int id, Class clazz, String msgIdFieldName) {
        this.id = id;
        this.clazz = clazz;
        this.msgIdFieldName = msgIdFieldName;
    }

    JokesCommonType(int id, Class clazz, String msgIdFieldName, MessageType msgType) {
        this.id = id;
        this.clazz = clazz;
        this.msgIdFieldName = msgIdFieldName;
        this.msgType = msgType;
    }

    JokesCommonType(int id, Class clazz, MessageType msgType) {
        this.id = id;
        this.clazz = clazz;
        this.msgType = msgType;
    }

    public int getId() {
        return id;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getMsgIdFieldName() {
        return msgIdFieldName;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    /**
     * 根据id获取CommonMsgType.
     *
     * @param id id.
     * @return Gender.
     */
    public static JokesCommonType valueOf(int id) {
        JokesCommonType[] types = values();
        for (JokesCommonType type : types) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}

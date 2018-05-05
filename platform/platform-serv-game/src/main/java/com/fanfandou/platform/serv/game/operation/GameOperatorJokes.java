package com.fanfandou.platform.serv.game.operation;

import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.entity.resource.DicItem;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.EnterAddress;
import com.fanfandou.platform.api.game.entity.GmCmdType;
import com.fanfandou.platform.api.game.entity.Message;
import com.fanfandou.platform.api.game.exception.GameException;
import com.fanfandou.platform.serv.game.connection.codec.JokesConnectionCodec;
import com.fanfandou.platform.serv.game.connection.netty.NettyGameConnector;
import com.fanfandou.platform.serv.game.entity.jokes.JokesCommonType;
import com.fanfandou.platform.serv.game.entity.jokes.base.JokesMsgPing;
import com.fanfandou.platform.serv.game.entity.jokes.base.JokesParams;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesActivity;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesBanAccount;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesBanTalk;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesCheckVersion;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesDelActivity;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesGmSend;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesMsgMail;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesRoleInfoReq;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesScrollNotice;
import com.fanfandou.platform.serv.game.entity.jokes.gm.JokesShutDownServer;
import com.fanfandou.platform.serv.game.entity.jokes.platform.JokesRecharge;
import com.fanfandou.platform.serv.game.entity.tol.gm.GmKeyValueList;
import com.fanfandou.platform.serv.game.service.GameWorker;
import com.fanfandou.platform.serv.game.service.GameWorkerJokes;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wudi.
 * Descreption:十冷协议定义.
 * Date:2017/3/9
 */
public class GameOperatorJokes extends AbstractGameOperator {


    //private GameConnector logicConnector;
    private static final long LOGIC_PINT_PERIOD = 30L;

    @Override
    protected void initConnector() {
        EnterAddress areaServerAddr = gameArea.getServerEnterAddrObj();
        logger.info("GameOperatorJokes >> 开始初始化连接 ：" + areaServerAddr.getIpAddress());
        connector = new NettyGameConnector(new JokesConnectionCodec(), messageHandler,
                new InetSocketAddress(areaServerAddr.getIpAddress(), areaServerAddr.getPort()));
        connector.connect();
    }

    @Override
    protected void ping() {
        JokesMsgPing pingReq = new JokesMsgPing();
        pingReq.setAction(JokesParams.ACTION_BROADCAST_SERVERID);
        pingReq.setMsgId((short) 3000);
        pingReq.setServerid(String.valueOf(gameArea.getAreaCode()));
        pingReq.setSign(sign(JokesParams.ACTION_BROADCAST_SERVERID));

        Message<JokesMsgPing> pingMsg = new Message<JokesMsgPing>("1", pingReq,
                JokesCommonType.MSG_PING_REQ.getId());
        try {
            logger.info("start jokes ping .......");
            connector.send(pingMsg);
        } catch (GameException e) {
            logger.error("GameOperatorTol.ping -> ping faild and reconnected!", e);
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e1) {
                logger.error("sleep error", e1);
            }
            if (connector != null && !connector.isConnected()) {
                connector.connect();
            }
        }
    }

    private String sign(int action) {
        return CipherUtils.initMd5().encrypt("action=" + action + "key=" + "05A9C0A6CD768B798C186814243EEAC5");
    }


    @Override
    protected GameWorker getGameWorker() {
        return new GameWorkerJokes(gameCode, gameArea.getId());
    }

    @Override
    public void sendGMCmd(long dispatchId, String value, GmCmdType gmCmdType) throws ServiceException {
        JokesGmSend gmSend = new JokesGmSend();
        gmSend.setAction(JokesParams.ACTION_COMMAND);
        gmSend.setMsgId((int)dispatchId);
        gmSend.setCmd(gmCmdType.getCode());
        gmSend.setValue(value);
        gmSend.setSign(sign(JokesParams.ACTION_COMMAND));
        Message<JokesGmSend> message = new Message<JokesGmSend>(dispatchId + "", gmSend,
                JokesCommonType.MSG_HILINK_GM_CMD.getId());
        connector.send(message);
    }

    @Override
    public void sendScrollNotice(long dispatchId, long startTime, long endTime, String noticeContent, int publishCount)
            throws GameException {
        JokesScrollNotice noticeReq = new JokesScrollNotice();
        noticeReq.setAction(JokesParams.ACTION_ANNOUNCE_TO_ALL);
        noticeReq.setMsgId((short)3000);
        noticeReq.setContent(noticeContent);
        noticeReq.setInterval(publishCount);//协议要的是秒
        noticeReq.setBegintime(DateUtil.getFormatedDateString(8, startTime));
        noticeReq.setEndtime(DateUtil.getFormatedDateString(8, endTime));
        noticeReq.setId((int)dispatchId);
        noticeReq.setOp(1);
        noticeReq.setTransId(String.valueOf(dispatchId));
        noticeReq.setSign(sign(JokesParams.ACTION_ANNOUNCE_TO_ALL));
        logger.info("sendScrollNotice playing>>>>>>>>>>");
        Message<JokesScrollNotice> message = new Message<JokesScrollNotice>(dispatchId + "", noticeReq,
                JokesCommonType.MSG_HILINK_NOTICE_TOALL.getId());
        connector.send(message);
    }

    @Override
    public void pushMsgToClient(long userId, DicItem msgType, long msgLongVal, String msgStrVal)
            throws ServiceException {

    }

    @Override
    public void routePurchaseByGem(long userId, GoodsItemPackage itemPackage) throws ServiceException {

    }

    @Override
    public void sendItem(long dispatchId, long roleId, GoodsItemPackage itemPackage) throws GameException {


    }

    @Override
    public void sendMail(long dispatchId, String from, String title, List<String> roleIds, GoodsItemPackage itemPackage)
            throws ServiceException {
        logger.info("开始发送邮件");
        int [] roleIdInts = new int[roleIds.size()];
        for (int i = 0; i < roleIds.size(); i++) {
            roleIdInts[i] = Integer.parseInt(roleIds.get(i));
        }
        JokesMsgMail mailReq = new JokesMsgMail();
        if (!CollectionUtils.isEmpty(roleIds) && roleIds.get(0).equals("0")) {
            mailReq.setMailtarget(2);
            mailReq.setAction(JokesParams.ACTION_MAIL);
        } else {
            mailReq.setMailtarget(1);
            mailReq.setRoleid(roleIdInts);
        }
        if (itemPackage.getPackageDesc() == null) {
            itemPackage.setPackageDesc("");
        }
        String itemDesc = itemPackage.getPackageDesc().replace("\n", "");
        mailReq.setAction(JokesParams.ACTION_MAIL);
        mailReq.setContent(itemDesc);
        mailReq.setMailid((int)dispatchId);
        mailReq.setMsgId((short) 3000);
        mailReq.setOp(1);
        mailReq.setTitle(title);
        mailReq.setFrom(from);
        mailReq.setTransId(dispatchId + "");
        mailReq.setSign(sign(JokesParams.ACTION_MAIL));

        //拼接appendix
        List<GoodsItem> goodsItems = itemPackage.getGoodsItems();
        List<JSONObject> appendix = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItems) && !StringUtils.isEmpty(goodsItems.get(0).getItemType().getValue())) {
            for (GoodsItem goodsItem : goodsItems) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("1", Integer.parseInt(goodsItem.getItemType().getValue()));//type
                jsonObject.put("2", goodsItem.getItemId());//itemid
                jsonObject.put("3", goodsItem.getValue());//num
                appendix.add(jsonObject);
            }
        }

        mailReq.setAppendix(appendix);
        Message<JokesMsgMail> message = new Message<JokesMsgMail>(dispatchId + "", mailReq,
                JokesCommonType.MSG_HILINK_MAIL_TOONE.getId());
        connector.send(message);

    }

    @Override
    public void deliverPayPackage(long dispatchId, long userId, GoodsItemPackage itemPackage, String orderNo, int type, String extraData) throws ServiceException {
        JokesRecharge recharge = new JokesRecharge();
        recharge.setAction(JokesParams.ACTION_RECHARGE);
        recharge.setOrderNo(orderNo);
        recharge.setGem(itemPackage.getGoodsItems().get(0).getValue());
        recharge.setMoney(itemPackage.getValue() * 100);
        recharge.setMsgId(3000);
        recharge.setRoleid((int)userId);
        recharge.setType(type);
        recharge.setTransId(dispatchId + "");
        recharge.setExtraData(extraData);
        recharge.setSign(sign(JokesParams.ACTION_RECHARGE));
        Message<JokesRecharge> message = new Message<JokesRecharge>(dispatchId + "", recharge, JokesCommonType.MSG_HILINK_PAY.getId());
        connector.send(message);
    }

    @Override
    public String getLoginKey(long dispatchId, long roleId) throws GameException {
        return null;
    }

    @Override
    public void sendMsg(Message msg) throws GameException {

    }

    @Override
    public void sendAntiAddiction(long userId, int onlineSeconds) throws GameException {

    }

    @Override
    public void sendActivityTask(long userId, int areaId, int activityId, long startTime) throws ServiceException {

    }

    @Override
    public void settleActivityTask(long userId, int areaId, int activityId, int taskId) throws ServiceException {

    }

    @Override
    public Map<Integer, Object> sendGmCommand(int gmMsgType, long operateId, long userId, int areaId,
                                              GmKeyValueList keyValueList) throws ServiceException {
        return null;
    }

    @Override
    public void banTalk(long roleId, int type, int time, String reason) throws ServiceException {
        JokesBanTalk banTalk = new JokesBanTalk();
        banTalk.setAction(JokesParams.ACTION_BAN_TALK);
        banTalk.setRoleid((int)roleId);
        banTalk.setTime(time);
        banTalk.setType(type);
        banTalk.setReason(reason);
        banTalk.setSign(sign(JokesParams.ACTION_BAN_TALK));
        Message<JokesBanTalk> message = new Message<JokesBanTalk>(roleId + ""
                ,banTalk, JokesCommonType.MSG_BAN_TALK.getId() );
        connector.send(message);
    }

    @Override
    public void banAccount(long roleId, int type, int time, String reason) throws ServiceException {
        JokesBanAccount banAccount = new JokesBanAccount();
        banAccount.setAction(JokesParams.ACTION_BAN);
        banAccount.setReason(reason);
        banAccount.setRoleid((int)roleId);
        banAccount.setType(type);
        banAccount.setTime(time);
        banAccount.setSign(sign(JokesParams.ACTION_BAN));
        Message<JokesBanAccount> message = new Message<JokesBanAccount>(roleId + ""
                , banAccount, JokesCommonType.MSG_BAN_ACCOUNT.getId());
        connector.send(message);
    }

    @Override
    public String getRoleInfos(String transId, long roleId, int infoType, int value) throws ServiceException {
        JokesRoleInfoReq roleInfoReq = new JokesRoleInfoReq();
        roleInfoReq.setAction(JokesParams.ACTION_GET_ROLE_INFO);
        roleInfoReq.setInfoType(infoType);
        roleInfoReq.setMsgId(3000);
        roleInfoReq.setRoleid((int)roleId);
        roleInfoReq.setValue(value);
        roleInfoReq.setTransId(transId);
        roleInfoReq.setSign(sign(JokesParams.ACTION_GET_ROLE_INFO));
        Message<JokesRoleInfoReq> message = new Message<JokesRoleInfoReq>(transId + "", roleInfoReq
                , JokesCommonType.MSG_GET_ROLEINFO.getId());
        connector.send(message);
        Message response = messageHandler.getResponse(transId + "");
        if (response != null && response.getProtocolType() == JokesCommonType.MSG_GET_ROLEINFO.getId()) {
            return ((JSONObject)response.getMsgBody()).toJSONString();
        }
        return null;
    }

    @Override
    public void gameActivityController(long operateId, GameActivityCon gameActivityCon) throws ServiceException {
        logger.info("start activity controller");
        JokesActivity jokesActivity = new JokesActivity();
        jokesActivity.setAction(JokesParams.ACTION_GAME_ACTIVITY);
        jokesActivity.setActivity(gameActivityCon.getActivityJson());
        jokesActivity.setTaskIds(gameActivityCon.getTaskIds());
        jokesActivity.setType(gameActivityCon.getType());
        jokesActivity.setActivityName(gameActivityCon.getActivityName());
        jokesActivity.setBeginTime((int)(gameActivityCon.getBeginTime().getTime() / 1000));
        jokesActivity.setEndTime((int)(gameActivityCon.getEndTime().getTime() / 1000));
        jokesActivity.setId(gameActivityCon.getId());
        jokesActivity.setMsgId((int)operateId);
        jokesActivity.setSign(sign(JokesParams.ACTION_GAME_ACTIVITY));
        Message<JokesActivity> message = new Message<JokesActivity>(operateId + "", jokesActivity, JokesCommonType.MSG_ACTIVITY_CONTROLLER.getId());
        connector.send(message);
    }

    @Override
    public void delActivity(long operateId, int id, int type) throws ServiceException {
        logger.info("start del activity");
        JokesDelActivity jokesDelActivity = new JokesDelActivity();
        jokesDelActivity.setAction(JokesParams.ACTION_DEL_GAME_ACTIVITY);
        jokesDelActivity.setMsgId((int)operateId);
        jokesDelActivity.setId(id);
        jokesDelActivity.setType(type);
        jokesDelActivity.setSign(sign(JokesParams.ACTION_DEL_GAME_ACTIVITY));
        Message<JokesDelActivity> message = new Message<JokesDelActivity>(operateId + "", jokesDelActivity, JokesCommonType.MSG_DEL_ACTIVITY_CONTROLLER.getId());
        connector.send(message);

    }

    @Override
    public void shutDownServer(String transId) throws ServiceException {
        JokesShutDownServer shutDownServer = new JokesShutDownServer();
        shutDownServer.setAction(JokesParams.ACTION_SHUTDOWN_SERVER);
        shutDownServer.setContent("游戏正在维护中");
        shutDownServer.setMsgId(3000);
        shutDownServer.setTime((int)(new Date().getTime() / 1000) + 10);
        shutDownServer.setTransId(transId + "");
        shutDownServer.setSign(sign(JokesParams.ACTION_SHUTDOWN_SERVER));
        Message<JokesShutDownServer> message = new Message<JokesShutDownServer>(transId, shutDownServer,
                JokesCommonType.MSG_SHUTDOWN_SERVER.getId());
        connector.send(message);
    }

    @Override
    public void pushGameVersion(String transId, String resId) throws ServiceException {
        JokesCheckVersion checkVersion = new JokesCheckVersion();
        checkVersion.setAction(JokesParams.ACTION_PUSH_GAME_VERSION);
        checkVersion.setMsgId(3000);
        checkVersion.setResid(resId);
        checkVersion.setSign(sign(JokesParams.ACTION_PUSH_GAME_VERSION));
        Message<JokesCheckVersion> message = new Message<JokesCheckVersion>(transId, checkVersion, JokesCommonType.MSG_PUSH_GAME_VERSION.getId());
        connector.send(message);
    }
}

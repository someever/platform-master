package com.fanfandou.platform.serv.game.operation;

import com.fanfandou.common.entity.resource.DicItem;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.game.entity.GameOperation;
import com.fanfandou.platform.api.game.entity.GmCmdType;
import com.fanfandou.platform.api.game.entity.Message;
import com.fanfandou.platform.api.game.exception.GameException;
import com.fanfandou.platform.serv.game.entity.tol.gm.GmKeyValueList;

import java.util.List;
import java.util.Map;

/**
 * Description: 与游戏间的操作，各游戏分别实现.
 * <p/>
 * Date: 2016-05-06 11:30.
 * author: Arvin.
 */
public interface GameOperator {

    void init(GameCode gameCode, int areaId);

    void dispatchOperation(GameOperation gameoperation) throws ServiceException;

    void sendGMCmd(long dispatchId, String value, GmCmdType gmCmdType) throws ServiceException;

    void sendScrollNotice(long dispatchId, long startTime, long endTime,
                          String noticeContent, int publishCount) throws GameException;

    void pushMsgToClient(long userId, DicItem msgType, long msgLongVal, String msgStrVal) throws ServiceException;

    void routePurchaseByGem(long userId, GoodsItemPackage itemPackage) throws ServiceException;

    void sendItem(long dispatchId, long roleId, GoodsItemPackage itemPackage) throws GameException;

    void sendMail(long dispatchId, String from, String title, List<String> roleIds, GoodsItemPackage itemPackage) throws ServiceException;

    void deliverPayPackage(long dispatchId, long userId, GoodsItemPackage itemPackage, String orderNo, int type, String extraData) throws ServiceException;

    String getLoginKey(long dispatchId, long roleId) throws GameException;

    void sendMsg(Message msg) throws GameException;

    void sendAntiAddiction(long userId, int onlineSeconds) throws GameException;

    void sendActivityTask(long userId, int areaId, int activityId, long startTime) throws ServiceException;

    void settleActivityTask(long userId, int areaId, int activityId, int taskId) throws ServiceException;

    Map<Integer, Object> sendGmCommand(int gmMsgType, long operateId, long userId, int areaId, GmKeyValueList keyValueList)
            throws ServiceException;

    void banTalk(long roleId, int type, int time, String reason) throws ServiceException;

    void banAccount(long roleId, int type, int time, String reason) throws ServiceException;

    String getRoleInfos(String transId, long roleId, int infoType, int value) throws ServiceException;

    void gameActivityController(long operateId, GameActivityCon gameActivityCon) throws ServiceException;

    void delActivity(long operateId, int id, int type) throws ServiceException;

    void shutDownServer(String transId) throws ServiceException;

    void pushGameVersion(String transId, String resId) throws ServiceException;
}

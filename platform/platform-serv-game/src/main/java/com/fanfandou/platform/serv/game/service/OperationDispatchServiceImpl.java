package com.fanfandou.platform.serv.game.service;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.ActStatus;
import com.fanfandou.common.entity.resource.DicItem;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.*;
import com.fanfandou.platform.api.game.exception.GameException;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.serv.game.dao.GameOperationMapper;
import com.fanfandou.platform.serv.game.entity.tol.gm.GmKeyValue;
import com.fanfandou.platform.serv.game.entity.tol.gm.GmKeyValueList;
import com.fanfandou.platform.serv.game.operation.GameOperator;
import com.fanfandou.platform.serv.game.operation.GameOperatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description: 游戏通讯相关业务实现.
 * <p/>
 * Date: 2016-05-17 15:01.
 * author: Arvin.
 */
@Service("operationDispatchService")
public class OperationDispatchServiceImpl extends BaseLogger implements OperationDispatchService {

    private static final long TASK_PERIOD = 30L;

    @Autowired
    private ThreadPoolTaskExecutor operationExecutor;

    @Autowired
    private GameOperatorFactory gameOperatorFactory;

    @Autowired
    private GameOperationMapper gameOperationMapper;

    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;

    @Autowired
    private GameAreaService gameAreaService;


    public OperationDispatchServiceImpl() {
        ScheduledExecutorService reloadSchedule = Executors
                .newScheduledThreadPool(2);
        reloadSchedule.scheduleAtFixedRate(new OperationDispatchTimerTask(), TASK_PERIOD, TASK_PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void init(GameCode gameCode, int areaId) throws ServiceException {
        gameOperatorFactory.getOperator(gameCode, areaId);
    }

    @Override
    public String getLoginKey(GameCode gameCode, int areaId, long roleId) throws GameException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        return gameOperator.getLoginKey(roleId * 10 + 1, roleId);
    }

    @Override
    public void deliverPayPackage(GameCode gameCode, long userId, int areaId, GoodsItemPackage goodsItemPackage, String orderNo, int type, String extraData) throws ServiceException {
        if (extraData == null) {
            extraData = "";
        }
        GameOperation operation = new GameOperation();
        operation.setGameId(gameCode.getId());
        operation.setAreaId(areaId);
        operation.setOptType(OperationType.DELIVER_OF_PAY);
        operation.setUserId(userId);
        operation.setOptContent(goodsItemPackage.toJson());
        operation.setOptDesc(orderNo);
        operation.setChargeType(type);
        operation.setExtraData(extraData);
        dispatch(operation);
    }

    @Override
    public void sendGmCmd(GameCode gameCode, int areaId, GmCmdType gmCmdType, String values) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.sendGMCmd(0, values, gmCmdType);
    }

    @Override
    public void routePurchaseByGem(GameCode gameCode, int areaId, long userId, GoodsItemPackage goodsItemPackage) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.routePurchaseByGem(userId, goodsItemPackage);
    }

    @Override
    public void pushMsgToClient(GameCode gameCode, int areaId, long userId, DicItem msgType,
                                long msgLongVal, String msgStrVal) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.pushMsgToClient(userId, msgType, msgLongVal, msgStrVal);
    }

    @Override
    public void sendMsg(GameCode gameCode, int areaId, Message msg) throws GameException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.sendMsg(msg);
    }

    @Override
    public void sendPackage(GameCode gameCode, int areaId, String from, String title,
                            GoodsItemPackage goodsItemPackage, long userId,
                            long roleId, List<String> roleIds, OperationType optType) throws ServiceException {
        logger.info("发送邮件准备进入队列");
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        // long dispatchId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
        // gameOperator.sendMail(dispatchId, from, title, roleIds, goodsItemPackage);
        GameOperation operation = new GameOperation();
        operation.setGameId(gameCode.getId());
        operation.setAreaId(areaId);
        operation.setOptType(optType);
        operation.setRoleId(roleId);
        operation.setRoleIds(roleIds);
        operation.setUserId(userId);
        operation.setFrom(from);
        operation.setTitle(title);
        operation.setOptContent(goodsItemPackage.toJson());
        dispatch(operation);
    }

    @Override
    public void sendAntiAddiction(GameCode gameCode, int areaId, long userId, int onlineSeconds) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.sendAntiAddiction(userId, onlineSeconds);
    }

    @Override
    public void sendScrollNotice(GameCode gameCode, long noticeId, int areaId, long startTime, long endTime,
                                 String noticeContent, int publishCount) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.sendScrollNotice(noticeId, startTime, endTime, noticeContent, publishCount);
    }

    @Override
    public void sendActivityTask(GameCode gameCode, long userId, int areaId, int areaCode, int activityId, long startTime) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.sendActivityTask(userId, areaCode, activityId, startTime);
    }

    @Override
    public void settleActivityTask(GameCode gameCode, long userId, int areaId, int areaCode, int activityId, int taskId) throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.settleActivityTask(userId, areaCode, activityId, taskId);
    }

    @Override
    public Map<Integer, Object> sendGmCommand(GameCode gameCode, int areaId, int areaCode, int userId, DicItem dicItem,
                                              Map<Integer, Object> gmContent) throws ServiceException {
        logger.debug("sendGmCommand >>>>>> start");
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        GmKeyValueList gmKeyValueList = new GmKeyValueList();
        List<GmKeyValue> gmKeyValues = gmKeyValueList.getMValuesList();
        Iterator iter = gmContent.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            GmKeyValue keyValue = new GmKeyValue();
            Object value = entry.getValue();
            keyValue.setMParam((int) entry.getKey());
            if (value instanceof String) {
                keyValue.setMValueString((String) value);
            } else if (value instanceof Integer) {
                keyValue.setMValueUint32((Integer) value);
            } else if (value instanceof Long) {
                keyValue.setMValueUint64((Long) value);
            } else if (value instanceof Boolean) {
                keyValue.setMValueBool((Boolean) value);
            }
            gmKeyValues.add(keyValue);
        }
        logger.debug("sendGmCommand >>>>>> map size :" + gmKeyValues.size());
        return gameOperator.sendGmCommand(Integer.parseInt(dicItem.getValue()), tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION), userId, areaCode, gmKeyValueList);
    }

    @Override
    public void banAccount(GameCode gameCode, int areaId, long roleId, int time, String reason, int banType, int type)
            throws ServiceException {
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        if (banType == 1) {
            gameOperator.banTalk(roleId, type, time, reason);
        } else {
            gameOperator.banAccount(roleId, type, time, reason);
        }
    }

    @Override
    public String getRoleInfos(GameCode gameCode, int areaId, long roleId, int infoType, int value) throws ServiceException {
        long transId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        return gameOperator.getRoleInfos(transId + "", roleId, infoType, value);
    }

    @Override
    public void gameActivityController(GameCode gameCode, int areaId, GameActivityCon gameActivityCon) throws ServiceException {
        /*long transId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.gameActivityController(transId, gameActivityCon);*/
        GameOperation operation = new GameOperation();
        operation.setOptContent(gameActivityCon.toJson());
        operation.setOptType(OperationType.ACTIVITY_SNED);
        operation.setGameId(gameCode.getId());
        operation.setAreaId(areaId);
        dispatch(operation);
    }

    @Override
    public void delGameActivityController(GameCode gameCode, int areaId, int id, int type) throws ServiceException {
        /*long transId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.delActivity(transId ,id, type);*/
        GameOperation operation = new GameOperation();
        operation.setGameId(gameCode.getId());
        operation.setAreaId(areaId);
        operation.setOptType(OperationType.DEL_ACTIVITY);
        operation.setId(id);
        operation.setType(type);
        dispatch(operation);
    }

    @Override
    public void shutdownServer(GameCode gameCode, int areaId) throws ServiceException {
        long transId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
        GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
        gameOperator.shutDownServer(transId + "");
    }


    public void pushGameVersion(GameCode gameCode, int areaId, String resId) throws ServiceException {
        if (areaId == 0) {
            List<GameArea> gameAreas = gameAreaService.getAreasListByGameId(gameCode.getId());
            for (int i = 0; i < gameAreas.size(); i++) {
                long transId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
                GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, gameAreas.get(i).getId());
                gameOperator.pushGameVersion(transId + "", resId);
            }
        } else {
            long transId = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION);
            GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, areaId);
            gameOperator.pushGameVersion(transId + "", resId);
        }

    }

    private void dispatch(GameOperation operation) throws ServiceException {
        //设置id
        operation.setOptId(tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_OPERATION));
        operation.setCreateTime(new Date());
        //先保存
        gameOperationMapper.insert(operation);
        logger.info("operation id = " + operation.getOptId() + "开始进入队列");
        //acting
        if (fetchOperation(operation)) {
            //加入发送队列
            logger.info("operation id = " + operation.getOptId() + "已经进入队列");
            operationExecutor.execute(new OperationDispatchThread(operation));
        }

    }

    /**
     * 修改状态为acted，操作完成时执行.
     *
     * @param gameOperation 完成的operation
     * @return 修改状态是否成功
     */
    @Override
    public boolean completeOperation(GameOperation gameOperation) {
        GameOperation updateOperation = new GameOperation(true);
        updateOperation.setOptId(gameOperation.getOptId());
        updateOperation.setDeliverTime(new Date());
        updateOperation.setDeliverStatus(ActStatus.ACTED);
        gameOperationMapper.updateByPrimaryKeySelective(updateOperation);
        return true;
    }

    /**
     * 修改状态为locked，表示该operation正在执行中，在游戏服未返回确认消息前保持这个状态.
     *
     * @param gameOperation 正在执行的opertaion
     * @return 修改状态是否成功
     */
    private boolean lockOperation(GameOperation gameOperation) {
        GameOperationExample operationExample = new GameOperationExample();

        operationExample.createCriteria()
                .andDeliverStatusEqualTo(ActStatus.ACTING)
                .andOptIdEqualTo(gameOperation.getOptId());
        GameOperation updateOperation = new GameOperation(true);
        updateOperation.setDeliverStatus(ActStatus.LOCKED);
        gameOperationMapper.updateByExampleSelective(updateOperation, operationExample);
        return true;
    }

    /**
     * 对于即将执行的operation修改状态为acting.
     *
     * @param gameOperation 要执行的operation
     * @return 修改状态是否成功.
     */
    private boolean fetchOperation(GameOperation gameOperation) {
        logger.info("fetchOperation start");
        GameOperationExample operationExample = new GameOperationExample();
        List<ActStatus> deliverStatus = new ArrayList<>();
        deliverStatus.add(ActStatus.LOCKED);
        deliverStatus.add(ActStatus.UNACT);
        operationExample.createCriteria()
                .andDeliverStatusIn(deliverStatus)
                .andOptIdEqualTo(gameOperation.getOptId());
        GameOperation updateOperation = new GameOperation(true);
        updateOperation.setDeliverStatus(ActStatus.ACTING);
        updateOperation.setLastTryTime(new Date());
        updateOperation.setTryTimes(gameOperation.getTryTimes() + 1);
        //重试次数超过三次不再发送
        if (updateOperation.getTryTimes() > 3) {
            updateOperation.setDeliverStatus(ActStatus.REJECTED);
        }
        logger.info("fetchOperation down");
        //TODO 取节点名称 ，updateOperation.setDeliverServerName();
        return gameOperationMapper.updateByExampleSelective(updateOperation, operationExample) > 0;

    }


    /**
     * 重新加载未执行的operation放入执行队列.
     */
    private List<GameOperation> reloadUndeliveredOperation() {
        GameOperationExample operationExample = new GameOperationExample();
        List<ActStatus> deliverStatus = new ArrayList<>();
        deliverStatus.add(ActStatus.LOCKED);
        deliverStatus.add(ActStatus.UNACT);
        operationExample.createCriteria().andDeliverStatusIn(deliverStatus);
        return gameOperationMapper.selectByExample(operationExample);

    }

    /**
     * 物品发送线程.<br/>
     * 用于物品发送时，创建线程放入线程池中执行
     */
    class OperationDispatchThread implements Runnable {

        private GameOperation gameOperation;

        public OperationDispatchThread(GameOperation gameOperation) {
            this.gameOperation = gameOperation;
        }

        @Override
        public void run() {
            GameCode gameCode = GameCode.getById(gameOperation.getGameId());
            try {

                //发送
                GameOperator gameOperator = gameOperatorFactory.getOperator(gameCode, gameOperation.getAreaId());
                gameOperator.dispatchOperation(gameOperation);
                logger.info("已进入线程队列");
                //锁
                lockOperation(gameOperation);
                //TODO 发送结果什么时候处理
            } catch (ServiceException e) {
                logger.error("OperationDispatchServiceImpl.OperationDispatchThread: get game operator error", e);
            }
        }
    }

    /**
     * 定时从数据库中取出未执行完成的operation来执行.
     */
    class OperationDispatchTimerTask extends TimerTask {

        @Override
        public void run() {
            //从数据库获取未执行的operation并且状态锁定
            List<GameOperation> reloadOperations = reloadUndeliveredOperation();
            for (GameOperation operation : reloadOperations) {
                if (new Date().getTime() - operation.getLastTryTime().getTime() < 60000L) {
                    continue;
                }
                //执行发送
                if (fetchOperation(operation)) {
                    //加入发送队列
                    operationExecutor.execute(new OperationDispatchThread(operation));
                }
            }
        }
    }
}

package com.fanfandou.platform.serv.game.service;

import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.SpringUtils;
import com.fanfandou.platform.api.activity.service.PromoteCodeBatchService;
import com.fanfandou.platform.api.game.entity.GameOperation;
import com.fanfandou.platform.api.game.entity.Message;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.api.user.service.UserService;
import com.fanfandou.platform.serv.game.entity.jokes.JokesCommonType;

/**
 * Created by wudi.
 * Descreption:十冷游戏worker.
 * Date:2017/3/8
 */
public class GameWorkerJokes extends AbstractGameWorker {

    private OperationDispatchService operationDispatchService;
    private GameRoleService gameRoleService;
    private UserService userService;
    private GameAreaService gameAreaService;
    private PromoteCodeBatchService promoteCodeBatchService;

    public GameWorkerJokes(GameCode gameCode, int areaId) {
        this.gameCode = gameCode;
        this.areaId = areaId;
        init();
    }

    //可以处理一些参数的初始化
    private void init() {
        operationDispatchService = (OperationDispatchService) SpringUtils.getBean("operationDispatchService");
        gameRoleService = (GameRoleService) SpringUtils.getBean("gameRoleService");
        //userService = (UserService) SpringUtils.getBean("userService");
        gameAreaService = (GameAreaService) SpringUtils.getBean("gameAreaService");
        promoteCodeBatchService = (PromoteCodeBatchService) SpringUtils.getBean("promoteCodeBatchService");
    }

    @Override
    protected Runnable getWorkThread(Message msg) {
        JokesCommonType commonType = JokesCommonType.valueOf(msg.getProtocolType());
        if (commonType == null) {
            return null;
        }
        switch (commonType) {
            case MSG_HILINK_MAIL_TOONE:
                logger.info("MSG_HILINK_MAIL_TOONE > " + msg.getMsgBody().toString() );
                return new JokesEmailThread(msg.getMsgBody().toString());
            case MSG_HILINK_CALLBACK:
                logger.info("MSG_HILINK_CALLBACK > " + msg.getMsgBody().toString() );
                return new JokesCallBackThread(msg.getMsgBody().toString());
            case MSG_HILINK_PAY:
                logger.info("MSG_HILINK_PAY > " + msg.getMsgBody().toString() );
                return new JokesPayCallBackThread(msg.getMsgBody().toString());
            case MSG_ACTIVITY_CONTROLLER:
                logger.info("MSG_ACTIVITY_CONTROLLER > " + msg.getMsgBody().toString() );
                return new JokesActivityCallBackThread(msg.getMsgBody().toString());
            case  MSG_DEL_ACTIVITY_CONTROLLER:
                return new JokesActivityDelCallBackThread(msg.getMsgBody().toString());

            default:
                return null;
        }
    }

    /**
     * 邮件发送回调.
     */
    class JokesEmailThread implements Runnable {
        private String msgGm;

        public JokesEmailThread(String msgGm) {
            this.msgGm = msgGm;
        }

        @Override
        public void run() {
            JSONObject jsonObject = JSONObject.parseObject(msgGm);
            int msgId = jsonObject.getIntValue("msgId");
            String transId = jsonObject.getString("transId");
            if (msgId == 3001 || msgId == 3000) {
                completeOperation(transId);
            } else {
                logger.error("邮件发送失败");
            }
        }
    }

    class JokesPayCallBackThread implements Runnable {
        private String msgGm;

        public JokesPayCallBackThread(String msgGm) {
            this.msgGm = msgGm;
        }

        @Override
        public void run() {
            JSONObject jsonObject = JSONObject.parseObject(msgGm);
            int msgId = jsonObject.getIntValue("msgId");
            String transId = jsonObject.getString("transId");
            if (msgId == 3001 || msgId == 3000) {
                completeOperation(transId);
            } else {
                logger.error("充值发送失败");
            }
        }
    }

    class JokesActivityCallBackThread implements Runnable {
        private String msgGm;

        public JokesActivityCallBackThread(String msgGm) {
            this.msgGm = msgGm;
        }

        @Override
        public void run() {
            JSONObject jsonObject = JSONObject.parseObject(msgGm);
            int msgId = jsonObject.getIntValue("msgId");
            logger.info("活动MsgId = " + msgId);
            String transId = jsonObject.getString("transId");
            if (msgId != 0) {
                completeOperation(msgId + "");
            } else {
                logger.error("活动发送失败");
            }
        }
    }

    class JokesActivityDelCallBackThread implements Runnable {
        private String msgGm;

        public JokesActivityDelCallBackThread(String msgGm) {
            this.msgGm = msgGm;
        }

        @Override
        public void run() {
            JSONObject jsonObject = JSONObject.parseObject(msgGm);
            int msgId = jsonObject.getIntValue("msgId");
            logger.info("活动MsgId = " + msgId);
            if (msgId != 0) {
                completeOperation(msgId + "");
            } else {
                logger.error("活动发送失败");
            }
        }
    }

    /**
     * 游戏主动推送..
     */
    class JokesCallBackThread implements Runnable {
        private String msgGm;

        public JokesCallBackThread(String msgGm) {
            this.msgGm = msgGm;
        }

        @Override
        public void run() {
            JSONObject jsonObject = JSONObject.parseObject(msgGm);
            int roleId = jsonObject.getIntValue("srcRoleId");
            try {
                promoteCodeBatchService.getTicket(gameCode.getId(), areaId, 0, roleId);
            } catch (ServiceException e) {
                logger.error(e.getMessage());

            }
        }
    }

    private void completeOperation(String optId) {
        GameOperation operation = new GameOperation();
        operation.setOptId(Long.valueOf(optId));
        operationDispatchService.completeOperation(operation);
    }
}

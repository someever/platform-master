package com.fanfandou.admin.operation.service.impl;

import com.fanfandou.admin.operation.entity.MailOrder;
import com.fanfandou.admin.operation.entity.MailOrderFailure;
import com.fanfandou.admin.operation.entity.MailOrderTask;
import com.fanfandou.admin.operation.service.MailOrderFailureService;
import com.fanfandou.admin.operation.service.MailOrderService;
import com.fanfandou.admin.operation.service.MailOrderTaskService;
import com.fanfandou.admin.operation.service.MailSendService;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.entity.OperationType;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangzhenwei on 2016/8/1.
 * Description 邮件发送.
 */
@Service("mailSendService")
public class MailSendServiceImpl implements MailSendService {

    private static final long TASK_PERIOD = 30000L;

    @Autowired
    private ThreadPoolTaskExecutor approveExecutor;

    @Autowired
    private MailOrderTaskService mailOrderTaskService;

    @Autowired
    private MailOrderService mailOrderService;

    @Autowired
    private OperationDispatchService operationDispatchService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private MailOrderFailureService mailOrderFailureService;

    public MailSendServiceImpl() {
        Timer pingTimer = new Timer();
        pingTimer.schedule(new MailOrderTimerTask(), TASK_PERIOD, TASK_PERIOD);
    }

    /**
     * 发送.
     *
     * @param mailOrderTask 订单任务
     */
    private void sendMailOrder(MailOrderTask mailOrderTask) {
        //修改状态 2发送中
        mailOrderTask.setSendStatus(2);
        mailOrderTask.setSendCount(mailOrderTask.getSendStatus() + 1);
        //更新
        mailOrderTaskService.updMailOrderTask(mailOrderTask);
        int mailOrderId = mailOrderTask.getMailOrderId();
        MailOrder mailOrder = new MailOrder();
        try {
            mailOrder = mailOrderService.selMailOrderById(mailOrderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailOrder.setSendStatus(2);
        try {
            mailOrderService.updMailOrder(mailOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //执行发送
        approveExecutor.execute(new MailOrderTaskDispatchThread(mailOrderTask));
    }

    /**
     * 邮件发送线程
     * 用于邮件发送时，创建线程放入线程池中执行.
     */
    private class MailOrderTaskDispatchThread implements Runnable {

        private MailOrderTask mailOrderTask;

        public MailOrderTaskDispatchThread(MailOrderTask mailOrderTask) {
            this.mailOrderTask = mailOrderTask;
        }

        @Override
        public void run() {
            MailOrderFailure mailOrderFailure = new MailOrderFailure();
            mailOrderFailure.setMailTitle(mailOrderTask.getMailTitle());
            mailOrderFailure.setMailOrderId(mailOrderTask.getMailOrderId());
            GameCode gameCode = GameCode.getById(mailOrderTask.getGameId());
            //记录数据错误
            if (gameCode == null) {
                mailOrderFailure.setFailureReasons("游戏Id不存在");
                mailOrderFailure.setRemark("邮件发送空值异常");
                mailOrderFailureService.addMailOrderFailure(mailOrderFailure);
            }


            String mailTitle = mailOrderTask.getMailTitle();//获取邮件标题
            String proposer = mailOrderTask.getProposer();//获取邮件发送人
            int mailOrderId = mailOrderTask.getMailOrderId();//获取邮件id
            MailOrder mailOrder = new MailOrder();
            try {
                mailOrder = mailOrderService.selMailOrderById(mailOrderId);
            } catch (Exception e) {
                e.printStackTrace();
            }


            int sendByType = mailOrder.getSendByType();//发送邮件类型
            //组装gameItemPackage
            GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
            goodsItemPackage.setAwardPackageId(String.valueOf(mailOrderTask.getId()));
            List<GoodsItem> goodsItemList = mailOrderService.getGoodsItemList(mailOrderTask.getItemJson());
            goodsItemPackage.setGoodsItems(goodsItemList);
            String mailContent = mailOrder.getMailContent();
            goodsItemPackage.setPackageDesc(mailContent);
            goodsItemPackage.setTitle(mailOrder.getMailTitle());

            int exNum = 0;//标记成功或者失败，1代表失败
            String failedReason = mailOrderTask.getFailedReason();//失败理由
            String[] roleIds = mailOrderTask.getRoleIds().split(",");//发送的对象id集合
            List<String> roleListArr = new ArrayList<>();//失败的对象id集合
            List<String> roleList = new ArrayList<>();//操作对象id的集合

            for (String roleIdStr : roleIds) {
                roleList.add(roleIdStr);
            }
            if (roleList.size() > 1) {//集合
                OperationType optType = OperationType.MULTI_SEND_ITEM;
                for (String roleIdStr : roleIds) {//循环角色id
                    Long roleId = 0L;

                    if (!roleIdStr.equals("")) {
                        roleId = Long.parseLong(roleIdStr);
                    }

                    Long userId = 0L;
                    GameRole gameRole = new GameRole();
                    try {
                        gameRole = gameRoleService.getRoleById(gameCode, roleId);//根据id查询角色

                        //记录数据错误
                        if (gameRole == null) {
                            mailOrderFailure.setFailureReasons("游戏角色" + roleId + "不存在");
                            mailOrderFailure.setRemark("邮件发送空值异常");
                            mailOrderFailureService.addMailOrderFailure(mailOrderFailure);//邮件异常持久化

                        } else {
                            userId = gameRole.getUserId();
                        }

                    } catch (Exception e) {
                        mailOrderFailure.setFailureReasons("游戏角色" + roleId + "不存在");
                        mailOrderFailure.setRemark("邮件发送空值异常");
                        mailOrderFailureService.addMailOrderFailure(mailOrderFailure);//邮件异常持久化
                        e.printStackTrace();
                    }
                    int mailType = mailOrder.getMailType();//邮件类型id

                    if (mailOrderTask.getGameId() == 27) {//27代表十冷，有特殊的
                        optType = OperationType.MULTI_SEND_ITEM;
                    } else {
                        optType = OperationType.SEND_ITEM;
                    }

                    if (mailType == 4) {//代表充值补偿
                        optType = OperationType.DELIVER_OF_PAY;
                    }
                }

                try {
                    int areaId = Integer.parseInt(mailOrderTask.getAreaIds()); //获取区服id
                    //调用发送
                    operationDispatchService.sendPackage(gameCode, areaId, proposer, mailTitle, goodsItemPackage, 0, 0, roleList, optType);

                } catch (Exception e) {
                    exNum = 1;//1代表失败
                    if (sendByType == 2) {//记录角色id
                        failedReason = failedReason + "," + roleList;
                    }
                    if (sendByType == 1) {//记录平台id
                        failedReason = failedReason + "," + 0;
                    }
                    if (sendByType == 3) {//记录角色名称
                        String roleName = "";
                        for (int i = 0; i < roleList.size(); i++) {
                            GameRole gameRole = gameRoleService.getRoleById(gameCode, Integer.parseInt(roleList.get(i)));//根据id查询角色

                            if (gameRole != null) {
                                if (roleName.equals("")) {
                                    roleName = "," + gameRole.getRoleName();
                                } else {
                                    roleName = "," + gameRole.getRoleName();
                                }

                            }
                        }


                        failedReason = failedReason + "," + roleName;
                    }

                    mailOrderFailure.setFailureReasons(failedReason);
                    mailOrderFailure.setRemark("邮件检测异常信息");
                    mailOrderFailureService.addMailOrderFailure(mailOrderFailure);//邮件异常信息持久化

                }
            } else {
                Long userId = 0L;
                Long roleId = 0L;
                GameRole gameRole = new GameRole();
                OperationType optType;
                if (mailOrderTask.getGameId() == 27) {//27代表十冷，特定的发送类型
                    optType = OperationType.MULTI_SEND_ITEM;
                } else {
                    optType = OperationType.SEND_ITEM;
                }

                if (Integer.parseInt(roleList.get(0)) == 0) {//当roleid==0时，为全服邮件
                    try {
                        if (mailOrderTask.getGameId() == 1) {//1代表无限幻想，单物品发送，roleList为空值
                            String[] areaIds = mailOrderTask.getAreaIds().split(",");
                            for (int i = 0; i < areaIds.length; i++) {
                                //调用发送
                                operationDispatchService.sendPackage(gameCode, Integer.parseInt(areaIds[i]), proposer, mailTitle, goodsItemPackage, userId, roleId, null, optType);
                            }

                        } else {//目前为十冷设计
                            String[] areaIds = mailOrderTask.getAreaIds().split(",");
                            for (int i = 0; i < areaIds.length; i++) {
                                //调用发送
                                operationDispatchService.sendPackage(gameCode, Integer.parseInt(areaIds[i]), proposer, mailTitle, goodsItemPackage, userId, roleId, roleList, optType);
                            }
                            //调用发送

                        }


                    } catch (Exception e) {
                        exNum = 1;//1代表失败
                        if (sendByType == 2) {//记录角色id
                            failedReason = failedReason + "," + roleId;
                        }
                        if (sendByType == 1) {//记录平台id
                            failedReason = failedReason + "," + userId;
                        }
                        if (sendByType == 3) {//记录角色名称
                            String roleName = "";
                            if (gameRole != null) {
                                roleName = gameRole.getRoleName();
                            }

                            failedReason = failedReason + "," + roleName;
                        }

                        mailOrderFailure.setFailureReasons(failedReason);
                        mailOrderFailure.setRemark("邮件检测异常信息");
                        mailOrderFailureService.addMailOrderFailure(mailOrderFailure);//邮件异常信息持久化

                    }
                } else {
                    for (int i = 0; i < roleList.size(); i++) {

                        if (!roleList.get(i).equals("")) {
                            roleId = Long.parseLong(roleList.get(i));
                        }

                        try {
                            gameRole = gameRoleService.getRoleById(gameCode, roleId);//根据角色id查询角色

                            //记录数据错误
                            if (gameRole == null) {
                                mailOrderFailure.setFailureReasons("游戏角色" + roleId + "不存在");
                                mailOrderFailure.setRemark("邮件发送空值异常");
                                mailOrderFailureService.addMailOrderFailure(mailOrderFailure);
                                roleListArr.add(roleList.get(i));
                            } else {
                                userId = gameRole.getUserId();
                            }

                        } catch (Exception e) {
                            //记录数据错误
                            mailOrderFailure.setFailureReasons("游戏角色" + roleId + "不存在");
                            mailOrderFailure.setRemark("邮件发送空值异常");
                            mailOrderFailureService.addMailOrderFailure(mailOrderFailure);
                            roleListArr.add(roleList.get(i));
                            e.printStackTrace();
                        }

                        int mailType = mailOrder.getMailType();
                        if (mailType == 4) {//当类型为充值补偿
                            optType = OperationType.DELIVER_OF_PAY;
                        }

                    }

                    /**
                     * 剔除有错误信息的roleId
                     */
                    for (int i = 0; i < roleList.size(); i++) {
                        for (int j = 0; j < roleListArr.size(); j++) {
                            if (roleList.get(i).equals(roleListArr.get(j))) {
                                roleList.remove(i);
                            }
                        }
                    }
                    try {
                        if (mailOrderTask.getGameId() == 1) {//1代表无限幻想，单物品发送，roleList为空值
                            int areaId = Integer.parseInt(mailOrderTask.getAreaIds()); //获取区服id
                            //调用发送
                            operationDispatchService.sendPackage(gameCode, areaId, proposer, mailTitle, goodsItemPackage, userId, roleId, null, optType);
                        } else {//目前为十冷设计
                            int areaId = Integer.parseInt(mailOrderTask.getAreaIds()); //获取区服id
                            //调用发送
                            operationDispatchService.sendPackage(gameCode, areaId, proposer, mailTitle, goodsItemPackage, userId, roleId, roleList, optType);
                        }


                    } catch (Exception e) {
                        exNum = 1;//1代表失败
                        if (sendByType == 2) {//记录角色id
                            failedReason = failedReason + "," + roleId;
                        }
                        if (sendByType == 1) {//记录平台id
                            failedReason = failedReason + "," + userId;
                        }
                        if (sendByType == 3) {//记录角色名称
                            String roleName = "";
                            if (gameRole != null) {
                                roleName = gameRole.getRoleName();
                            }

                            failedReason = failedReason + "," + roleName;
                        }

                        mailOrderFailure.setFailureReasons(failedReason);
                        mailOrderFailure.setRemark("邮件检测异常信息");
                        mailOrderFailureService.addMailOrderFailure(mailOrderFailure);//邮件异常信息持久化

                    }
                }


            }


            if (exNum == 1) {
                //修改状态 4发送失败
                mailOrderTask.setFailedReason(failedReason);
                mailOrderTask.setSendStatus(4);
                //更新
                mailOrderTaskService.updMailOrderTask(mailOrderTask);
                mailOrder.setSendStatus(4);
                try {
                    mailOrderService.updMailOrder(mailOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //修改状态 3发送成功
                mailOrderTask.setFailedReason(failedReason);
                mailOrderTask.setSendStatus(3);
                //更新
                mailOrderTaskService.updMailOrderTask(mailOrderTask);
                mailOrder.setSendStatus(3);
                try {
                    mailOrderService.updMailOrder(mailOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            System.out.println("发送。。");
        }
    }

    /**
     * 定时从数据库中取出未发送的邮件task任务来执行.
     */
    class MailOrderTimerTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("定时取出未发送的邮件");
            //从数据库获取未执行的task
            List<MailOrderTask> list = mailOrderTaskService.selectNotSend();
            for (MailOrderTask mailOrderTask : list) {
                System.out.println("开始调用");
                //超过五次的任务将不会被发送
                if (mailOrderTask.getSendCount() < 5) {
                    sendMailOrder(mailOrderTask);
                }

            }
        }
    }
}

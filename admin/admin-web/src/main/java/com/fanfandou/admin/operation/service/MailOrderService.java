package com.fanfandou.admin.operation.service;

import com.fanfandou.admin.operation.entity.MailOrder;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.billing.entity.GoodsItem;

import java.util.Date;
import java.util.List;

/**
 * Created by wangzhenwei on 2016/6/30.
 * Description 游戏物品类型service接口
 */
public interface MailOrderService {

    /**
     * 添加.
     */
    void addMailOrder(MailOrder mailOrder) throws ServiceException;

    /**
     * 删除 by id.
     */
    void delMailOrder(List<Integer> idList) throws ServiceException;

    /**
     * 更新角色.
     */
    void updMailOrder(MailOrder stu) throws ServiceException ;

    /**
     * 查询所有.
     */
    List<MailOrder> selectAll();

    /**
     * 通过id查找.
     */
    MailOrder selMailOrderById(int id) throws ServiceException;

    /**
     * 提交.
     *
     * @param mailOrderId 订单id
     */
    void submitOrder(int mailOrderId);

    /**
     * 审核.
     *
     * @param mailOrderId     订单id
     * @param status          3审核成功 4审核失败
     * @param approvalContent 审核批注
     */
    void updApprovalOrder(int mailOrderId, int status, String approvalContent, Date timingTime, int timingCheck);

    /**
     * 一键提交.
     *
     * @param orderIds 订单id字符串
     */
    void updOneKeySubmit(String orderIds);

    /**
     * 一键审核.
     *
     * @param mailOrderIds    订单id Str
     * @param status          3审核成功 4审核失败
     * @param approvalContent 审核批注
     */
    void updOneKeyApprove(String mailOrderIds, int status, String approvalContent, Date timingTime, int timingCheck);

    /**
     * json转ItemList.
     */
    List<GoodsItem> getGoodsItemList(String itemJson);

    /**
     * ItemList转json.
     */
    String setGoodsItemJson(List<GoodsItem> list);

    /**
     * excel导入.
     *
     * @param url         路径
     * @param fileName    文件名
     * @param applyReason 申请原因
     * @return 失败原因的集合
     */
    List<String> updImportExcel(String url, String fileName, String applyReason) throws Exception;

    /**
     * 发送GM指令
     * @param gmCmdValue 内容
     * @param gameId 游戏id
     * @param gmCmdType 类 型
     * @param areaIds 区服
     */
    void gmInstruct(String gmCmdValue, int gameId, int gmCmdType, String areaIds) throws Exception;
}

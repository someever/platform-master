package com.fanfandou.platform.serv.billing.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.fanfandou.admin.api.system.entity.DataDictionary;
import com.fanfandou.admin.api.system.service.DataDictionaryService;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.constant.DicItemKeyConstant;
import com.fanfandou.common.constant.DicKeyConstant;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.resource.DicItem;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.platform.api.activity.entity.PromoteAwardPackage;
import com.fanfandou.platform.api.activity.service.PromoteAwardPackageService;
import com.fanfandou.platform.api.billing.entity.BillingGoods;
import com.fanfandou.platform.api.billing.entity.BillingOrder;
import com.fanfandou.platform.api.billing.entity.BillingOrderExample;
import com.fanfandou.platform.api.billing.entity.FirstBuyPolicy;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.billing.entity.OrderParam;
import com.fanfandou.platform.api.billing.entity.OrderStatus;
import com.fanfandou.platform.api.billing.entity.PayStatus;
import com.fanfandou.platform.api.billing.entity.RepairOrder;
import com.fanfandou.platform.api.billing.entity.ShopType;
import com.fanfandou.platform.api.billing.exception.BillingException;
import com.fanfandou.platform.api.billing.service.GoodsService;
import com.fanfandou.platform.api.billing.service.OrderService;
import com.fanfandou.platform.api.billing.service.WalletSerivce;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.entity.OperationType;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.api.user.entity.UserAccount;
import com.fanfandou.platform.api.user.service.AccountService;
import com.fanfandou.platform.serv.billing.dao.BillingGoodsMapper;
import com.fanfandou.platform.serv.billing.dao.BillingOrderMapper;
import com.fanfandou.platform.serv.billing.dao.RepairOrderMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wudi.
 * Descreption:订单实现类.
 * Date:2016/5/3
 */
@Service("orderService")
public class OrderServiceImpl extends BaseLogger implements OrderService {

    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;

    @Autowired
    private BillingOrderMapper billingOrderMapper;

    @Autowired
    private BillingGoodsMapper billingGoodsMapper;

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Autowired
    private OperationDispatchService operationDispatchService;

    @Autowired
    private WalletSerivce walletService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PromoteAwardPackageService promoteAwardPackageService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private AccountService accountService;

    @Override
    public void createOrder(String orderId, String relateOrder) throws ServiceException {
        BillingOrder billingOrder = cacheService
                .get(IcachedConstant.BILLING_ORDER_PARAMS + orderId, BillingOrder.class);

        if (billingOrder == null) {
            throw new BillingException(BillingException.PAY_ORDER_NOT_EXISTS, "订单不存在");
        }

        billingOrder.setReOrderId(relateOrder);
        billingOrder.setOrderStatus(OrderStatus.PAID);
        billingOrder.setPayStatus(PayStatus.PAID);
        billingOrder.setOrderId(orderId);
        billingOrder.setPayTime(new Date());

        billingOrderMapper.insertSelective(billingOrder);
        cacheService.del(IcachedConstant.BILLING_ORDER_PARAMS + orderId);
        logger.info("createOrder in database is complete");
    }

    @Override
    public String createOrderId(OrderParam orderParam) throws ServiceException {
        logger.info("start createOrderId ");
        String code = orderParam.getAreaCode();

        int areaId = gameAreaService.getGameAreaByCode(orderParam.getGameId(), code).getId();
        BillingGoods billingGoods = goodsService.queryGoods(orderParam.getGoodsCode());

        String firstPay = cacheService
                .hGet(IcachedConstant.BILLING_IS_FIRSTBUY + 27 + orderParam.getSiteId() + orderParam.getGoodsCode(), orderParam.getRoleId() + "", String.class);

        if (billingGoods.getShopType().getId() != 1 && !StringUtils.isEmpty(firstPay)) {
                throw new BillingException(BillingException.PAY_ORDER_DELIVERED, "该礼包只能购买一次哦");
        }

        orderParam.setGoodsCode(orderParam.getGoodsCode());
        BillingOrder billingOrder = BillingOrder.convertBillingOrder(orderParam);
        billingOrder.setAreaId(areaId);
        billingOrder.setAreaCode(orderParam.getAreaCode());
        billingOrder.setRoleId(orderParam.getRoleId());
        billingOrder.setOrderStatus(OrderStatus.UNPAID);
        billingOrder.setPayStatus(PayStatus.UNPAID);

        billingOrder.setPayAmount(billingGoods.getGoodsAmount());

        //订单前6位数字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String orderDate = sdf.format(new Date());

        //生成随机2位数字
        String ran = String.valueOf(new Random().nextInt(100));

        //生成主键
        long seqValue = tableSequenceGenerator.generate(TableSequenceConstant
                .PLATFORM_BILLING_PAY_ORDER);

        billingOrder.setId(seqValue);

        String orderId = orderDate + ran + seqValue;

        //缓存入redis
        cacheService.put(IcachedConstant.BILLING_ORDER_PARAMS + orderId, billingOrder, 1800 * 48);
        //playerLogger.prepaid(orderParam.getRoleId() + ":" + orderId);
        logger.info("createOrderId complete orderId = " + orderId);
        return orderId;
    }

    @Override
    public void deliverOrder(String orderId) throws ServiceException {
        logger.info("deliverOrder orderId = " + orderId);
        //根据订单查询订单详情
        BillingOrder billingOrder = billingOrderMapper.selectByOrderId(orderId);

        if (billingOrder == null) {
            throw new BillingException(BillingException.PAY_ORDER_DEDUCTE_FAIL, "订单无效");
        } else if (billingOrder.getPayStatus().equals(PayStatus.UNPAID)) {
            throw new BillingException(BillingException.PAY_ORDER_UNPAID, "订单未付款");
        }

        int gameId = billingOrder.getGameId();
        int siteId = billingOrder.getSiteId();
        long roleId = billingOrder.getRoleId();

        int count = 0;

        BillingGoods billingGoods =
                billingGoodsMapper.selectByGoodsCode(billingOrder.getGoodsCode());
        //订单状态正在发送，开始发货
        billingOrder.setOrderStatus(OrderStatus.DELIVERING);
        billingOrderMapper.updateByOrderIdSelective(billingOrder);

        GameCode gameCode = GameCode.getById(gameId);

        GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
        GoodsItem goodsCountItem = new GoodsItem();
        DataDictionary dataDictionary = null;
        try {
            dataDictionary = dataDictionaryService.getValueByUniform(DicKeyConstant.OPERATION_ITEM_ITEMTYPE, -1, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<DicItem> dicItems = JSON.parseArray(dataDictionary.getItemJson(), DicItem.class);
        for (DicItem dicItem : dicItems) {
            String itemType = DicItemKeyConstant.GOODS_ITEM_TYPE_ITEM;
            if (dicItem.getKey().equals(itemType)) {
                goodsCountItem.setItemType(dicItem);
            }
        }
        logger.info("gameId = " + billingGoods.getGameId());
        List<String> roleIds = new ArrayList<>();
        roleIds.add(roleId + "");
        logger.info("首充玩家信息：" + gameId + "," + siteId + "," + billingGoods.getGoodsCode() + "," + roleId);
        if (siteId == 108 || siteId == 109 || siteId == 110) {
            siteId = 108;
        }
        if (siteId == 64 || siteId == 111 || siteId == 112) {
            siteId = 63;
        }
        String firstPay = cacheService
                .hGet(IcachedConstant.BILLING_IS_FIRSTBUY + gameId + siteId + billingGoods.getGoodsCode(), roleId + "", String.class);
        //logger.info("首充配置 first1： " + firstBuyPolicy.getFirstPay());
        logger.info("首充配置 first2： " + firstPay);
        if (StringUtils.isEmpty(firstPay)) {
            logger.info("玩家" + billingOrder.getRoleId() + "满足首充1条件");
            // if (billingGoods.getShopType().equals(ShopType.RMBSHOP)) {
            GoodsItemPackage goodsItemPackageFirst = new GoodsItemPackage();
            logger.info("玩家" + billingOrder.getRoleId() + "满足首充2条件，开始发放首充奖励");
            //获取首充的奖励礼包ID
            if (StringUtils.isEmpty(billingGoods.getAwardId())) {
                billingGoods.setAwardId("0");
            }
            int awardId = Integer.parseInt(billingGoods.getAwardId());
            PromoteAwardPackage awardPackage = promoteAwardPackageService.getAwardPackageById(awardId);
            List<GoodsItem> goodsItems = new ArrayList<>();
            if (awardPackage != null) {
                goodsItems = JSON.parseArray(awardPackage.getItemsPackage(), GoodsItem.class);
                goodsItemPackageFirst.getGoodsItems().addAll(goodsItems);
                operationDispatchService.sendPackage(gameCode, billingOrder.getAreaId(), "邮件奖励",
                        awardPackage.getPackageGreet(), goodsItemPackageFirst, billingOrder.getUserId(), roleId, roleIds, OperationType.MULTI_SEND_ITEM);
            } else {
                awardPackage = new PromoteAwardPackage();
                awardPackage.setPackageGreet("首充返利,请查收");
            }
            //首充送一倍宝石
            if (billingGoods.getShopType().getId() == 1) {
                logger.info("正常首充送宝石");
                count = billingGoods.getGoodsCount();
                /*GoodsItem goodsItem = new GoodsItem();
                DicItem dicItem = new DicItem();
                dicItem.setValue("2");
                goodsItem.setItemType(dicItem);
                goodsItem.setValue(billingGoods.getGoodsCount());
                goodsItems.add(goodsItem);*/
            }

            if (billingGoods.getShopType().getId() != 1) {
                int type = 2;
                if (billingGoods.getShopType().getId() == 3) {
                    type = 3;
                }
                logger.info("购买游戏内活动商品");
                GoodsItemPackage activityItemPackage = new GoodsItemPackage();
                GoodsItem acticeItem = new GoodsItem();
                acticeItem.setItemId(Integer.parseInt(billingGoods.getGoodsCode()));
                acticeItem.setValue(0);
                activityItemPackage.setValue(billingGoods.getGoodsAmount());
                activityItemPackage.getGoodsItems().add(acticeItem);
                operationDispatchService.deliverPayPackage(gameCode, billingOrder.getRoleId(), billingOrder.getAreaId(), activityItemPackage, orderId, type, billingGoods.getGoodsCode());
            }

            //  }
        } else {
            String goodsitems = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_GIFTBAG + billingGoods.getGameId(),
                    billingGoods.getGoodsId() + "", String.class);
            List<GoodsItem> goodsItemList = JSON.parseArray(goodsitems, GoodsItem.class);
            if (!CollectionUtils.isEmpty(goodsItemList) && goodsItemList.get(0).getValue() != 0) {
                logger.info("付费返还开启");
                count = goodsItemList.get(0).getValue();
                /*GoodsItemPackage goodsItemPackages = new GoodsItemPackage();
                goodsItemPackages.getGoodsItems().addAll(goodsItemList);
                operationDispatchService.sendPackage(gameCode, billingOrder.getAreaId(), "付费返还通知",
                        "付费返还,请查收", goodsItemPackages, billingOrder.getUserId(), roleId, roleIds, OperationType.MULTI_SEND_ITEM);*/
            }
        }
        if (billingGoods.getShopType().getId() == 1) {
            goodsCountItem.setValue(billingGoods.getGoodsCount() + count);
            goodsCountItem.setItemId(Integer.parseInt(billingGoods.getGoodsCode()));
            //人民币
            goodsItemPackage.setValue(billingGoods.getGoodsAmount());
            //商品数量
            goodsItemPackage.getGoodsItems().add(goodsCountItem);
            //充值发货
            operationDispatchService.deliverPayPackage(gameCode, billingOrder.getRoleId(), billingOrder.getAreaId(), goodsItemPackage, orderId, 1, billingGoods.getGoodsCode());
        }
        //改变首充状态,该用户以及该笔商品已消费首充
        cacheService.hPut(IcachedConstant.BILLING_IS_FIRSTBUY + gameId + siteId + billingGoods.getGoodsCode(), roleId + "", "pay");
        //如果发货成功，则修改订单状态，已发货
        billingOrder.setOrderStatus(OrderStatus.DELIVERED);
        billingOrderMapper.updateByOrderIdSelective(billingOrder);
    }


    @Override
    public void pressDeliverOrder(String orderId) throws ServiceException {
        logger.info("deliverOrder orderId = " + orderId);
        //根据订单查询订单详情
        BillingOrder billingOrder = billingOrderMapper.selectByOrderId(orderId);

        if (billingOrder == null) {
            throw new BillingException(BillingException.PAY_ORDER_DEDUCTE_FAIL, "订单无效");
        } else if (billingOrder.getPayStatus().equals(PayStatus.UNPAID)) {
            throw new BillingException(BillingException.PAY_ORDER_UNPAID, "订单未付款");
        }

        int gameId = billingOrder.getGameId();
        int siteId = billingOrder.getSiteId();
        long roleId = billingOrder.getRoleId();

        int count = 0;

        BillingGoods billingGoods =
                billingGoodsMapper.selectByGoodsCode(billingOrder.getGoodsCode());
        //订单状态正在发送，开始发货
        billingOrder.setOrderStatus(OrderStatus.DELIVERING);
        billingOrderMapper.updateByOrderIdSelective(billingOrder);


        GameCode gameCode = GameCode.getById(gameId);

        GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
        GoodsItem goodsCountItem = new GoodsItem();
        DataDictionary dataDictionary = null;
        try {
            dataDictionary = dataDictionaryService.getValueByUniform(DicKeyConstant.OPERATION_ITEM_ITEMTYPE, -1, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<DicItem> dicItems = JSON.parseArray(dataDictionary.getItemJson(), DicItem.class);
        for (DicItem dicItem : dicItems) {
            String itemType = DicItemKeyConstant.GOODS_ITEM_TYPE_ITEM;
            if (dicItem.getKey().equals(itemType)) {
                goodsCountItem.setItemType(dicItem);
            }
        }
        logger.info("gameId = " + billingGoods.getGameId());
        List<String> roleIds = new ArrayList<>();
        roleIds.add(roleId + "");
        logger.info("首充玩家信息：" + gameId + "," + siteId + "," + billingGoods.getGoodsCode() + "," + roleId);
        String firstPay = cacheService
                .hGet(IcachedConstant.BILLING_IS_FIRSTBUY + gameId + siteId + billingGoods.getGoodsCode(), roleId + "", String.class);
        //logger.info("首充配置 first1： " + firstBuyPolicy.getFirstPay());
        logger.info("首充配置 first2： " + firstPay);
        if (true) {
            logger.info("玩家" + billingOrder.getRoleId() + "满足首充1条件");
            // if (billingGoods.getShopType().equals(ShopType.RMBSHOP)) {
            GoodsItemPackage goodsItemPackageFirst = new GoodsItemPackage();
            logger.info("玩家" + billingOrder.getRoleId() + "满足首充2条件，开始发放首充奖励");
            //获取首充的奖励礼包ID
            if (StringUtils.isEmpty(billingGoods.getAwardId())) {
                billingGoods.setAwardId("0");
            }
            int awardId = Integer.parseInt(billingGoods.getAwardId());
            PromoteAwardPackage awardPackage = promoteAwardPackageService.getAwardPackageById(awardId);
            List<GoodsItem> goodsItems = new ArrayList<>();
            if (awardPackage != null) {
                goodsItems = JSON.parseArray(awardPackage.getItemsPackage(), GoodsItem.class);
            } else {
                awardPackage = new PromoteAwardPackage();
                awardPackage.setPackageGreet("首充返利,请查收");
            }
            //首充送一倍宝石
            if (billingGoods.getShopType().getId() == 1) {
                logger.info("正常首充送宝石");
                GoodsItem goodsItem = new GoodsItem();
                DicItem dicItem = new DicItem();
                dicItem.setValue("2");
                goodsItem.setItemType(dicItem);
                goodsItem.setValue(billingGoods.getGoodsCount());
                goodsItems.add(goodsItem);
            }
            goodsItemPackageFirst.getGoodsItems().addAll(goodsItems);
            operationDispatchService.sendPackage(gameCode, billingOrder.getAreaId(), "首充奖励",
                    awardPackage.getPackageGreet(), goodsItemPackageFirst, billingOrder.getUserId(), roleId, roleIds, OperationType.MULTI_SEND_ITEM);
            if (billingGoods.getShopType().getId() == 0) {
                logger.info("购买游戏内活动商品");
                GoodsItemPackage activityItemPackage = new GoodsItemPackage();
                GoodsItem acticeItem = new GoodsItem();
                acticeItem.setItemId(Integer.parseInt(billingGoods.getGoodsCode()));
                acticeItem.setValue(0);
                activityItemPackage.setValue(billingGoods.getGoodsAmount());
                activityItemPackage.getGoodsItems().add(acticeItem);
                operationDispatchService.deliverPayPackage(gameCode, billingOrder.getRoleId(), billingOrder.getAreaId(), activityItemPackage, orderId, 2, billingGoods.getGoodsCode());
            }

            //  }
        } else {
            String goodsitems = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_GIFTBAG + billingGoods.getGameId(),
                    billingGoods.getGoodsId() + "", String.class);
            List<GoodsItem> goodsItemList = JSON.parseArray(goodsitems, GoodsItem.class);
            if (!CollectionUtils.isEmpty(goodsItemList) && goodsItemList.get(0).getValue() != 0) {
                logger.info("付费返还开启");
                count = goodsItemList.get(0).getValue();
                GoodsItemPackage goodsItemPackages = new GoodsItemPackage();
                goodsItemPackages.getGoodsItems().addAll(goodsItemList);
                operationDispatchService.sendPackage(gameCode, billingOrder.getAreaId(), "付费返还通知",
                        "付费返还,请查收", goodsItemPackages, billingOrder.getUserId(), roleId, roleIds, OperationType.MULTI_SEND_ITEM);
            }
        }
        if (billingGoods.getShopType().getId() == 1) {
            goodsCountItem.setValue(billingGoods.getGoodsCount());
            goodsCountItem.setItemId(Integer.parseInt(billingGoods.getGoodsCode()));
            //人民币
            goodsItemPackage.setValue(billingGoods.getGoodsAmount());
            //商品数量
            goodsItemPackage.getGoodsItems().add(goodsCountItem);
            //充值发货
            operationDispatchService.deliverPayPackage(gameCode, billingOrder.getRoleId(), billingOrder.getAreaId(), goodsItemPackage, orderId, 1, billingGoods.getGoodsCode());
        }
        //改变首充状态,该用户以及该笔商品已消费首充
        cacheService.hPut(IcachedConstant.BILLING_IS_FIRSTBUY + gameId + siteId + billingGoods.getGoodsCode(), roleId + "", "pay");
        //如果发货成功，则修改订单状态，已发货
        billingOrder.setOrderStatus(OrderStatus.DELIVERED);
        billingOrderMapper.updateByOrderIdSelective(billingOrder);
    }

    @Override
    public void repairOrder(String orderId, String repairReason, long userId, int gameId, int siteId)
            throws ServiceException {


    }

    @Override
    public PageResult getOrders(Integer roleId, Integer siteId, Integer gameId, String roleName, Page curPage, String
            from, String to) throws Exception {
        if (curPage.getOrder() == null) {
            curPage.setOrder("id");
        }
        if (curPage.getSort() == null) {
            curPage.setSort("asc");
        }

        if (gameId == -1) {
            gameId = null;
        }

        if (siteId == -1) {
            siteId = null;
        }

        GameRole gameRole = null;
        if (roleName != null && roleName != "") {
            gameRole = gameRoleService.getRoleByRoleName(GameCode.getById(gameId), roleName);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate;
        Date toDate;
        if (from == null) {
            from = "1900-01-01 00:00:00";
            fromDate = sdf.parse(from);
        } else {
            fromDate = sdf.parse(from);
        }

        if (to == null) {
            toDate = new Date();
        } else {
            toDate = sdf.parse(to);
        }

        int startNum = (curPage.getPageIndex() - 1) * curPage.getPageSize();
        Map<String, Object> paramMap = new HashMap<>();
        if (roleId != null) {
            paramMap.put("roleId", roleId);
        } else {
            if (gameRole != null) {
                paramMap.put("roleId", gameRole.getRoleId());
            } else {
                paramMap.put("roleId", roleId);
            }
        }

        paramMap.put("siteId", siteId);
        paramMap.put("gameId", gameId);
        paramMap.put("startNum", startNum);
        paramMap.put("endNum", curPage.getPageSize());
        paramMap.put("order", curPage.getOrder());
        paramMap.put("sort", curPage.getSort());
        paramMap.put("from", fromDate);
        paramMap.put("to", toDate);

        List<BillingOrder> list = this.billingOrderMapper.getOrdersbyselective(paramMap);

        for (int i = 0; i < list.size(); i++) {
            GameArea gameArea = gameAreaService.getGameAreaById(list.get(i).getAreaId());
            GameRole role = gameRoleService.getRoleById(GameCode.getById(list.get(i).getGameId()), list.get(i).getRoleId());

            List<UserAccount> userAccountList = accountService.getAccountsByUid(list.get(i).getUserId());
            UserAccount userAccount = null;
            if (!CollectionUtils.isEmpty(userAccountList)) {
                 userAccount = userAccountList.get(0);
            }
            if (userAccount != null) {
                list.get(i).setAccountName(userAccount.getAccountName());
            } else {
                list.get(i).setAccountName("无账号ID");
            }
            if (role != null) {
                list.get(i).setRoleName(role.getRoleName());
            } else {
                list.get(i).setRoleName("无角色信息");
            }
            if (gameArea != null) {
                list.get(i).setAreaName(gameArea.getAreaName());
            } else {
                list.get(i).setAreaName("无区服信息");
            }

        }

        int totalCount = this.billingOrderMapper.totalCount(paramMap);
        curPage.setTotalCount(totalCount);
        PageResult<BillingOrder> pageResult = new PageResult<>();
        pageResult.setPage(curPage);
        pageResult.setRows(list);
        return pageResult;
    }

    @Override
    public List<BillingOrder> getOrders(BillingOrderExample billingOrderExample) throws ServiceException {
        return billingOrderMapper.selectByExample(billingOrderExample);
    }

    @Override
    public BillingOrder getOrders(String orderId) throws ServiceException {

        return billingOrderMapper.selectByOrderId(orderId);
    }

    @Override
    public void createRepairOrder(String orderId, String repairReason, long userId, int siteId, int gameId)
            throws ServiceException {
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setGameId(gameId);
        repairOrder.setSiteId(siteId);
        repairOrder.setOrderId(orderId);
        repairOrder.setRepairReason(repairReason);
        repairOrder.setUserId(userId);
        repairOrder.setRepairTime(new Date());
        repairOrderMapper.insertSelective(repairOrder);
    }
}

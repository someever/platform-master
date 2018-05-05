package com.fanfandou.platform.web.billing.service;

import com.alibaba.fastjson.JSON;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.billing.entity.BillingGoods;
import com.fanfandou.platform.api.billing.entity.BillingOrder;
import com.fanfandou.platform.api.billing.entity.Currency;
import com.fanfandou.platform.api.billing.entity.FirstBuyPolicy;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.OrderParam;
import com.fanfandou.platform.api.billing.service.BillingService;
import com.fanfandou.platform.api.billing.service.GoodsService;
import com.fanfandou.platform.api.billing.service.OrderService;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.api.user.entity.AccountType;
import com.fanfandou.platform.api.user.entity.UserAccount;
import com.fanfandou.platform.api.user.service.AccountService;
import com.fanfandou.platform.web.billing.vo.GoodsItemVo;
import com.fanfandou.platform.web.billing.vo.GoodsVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wudi.
 * Descreption:支付服务实现调用.
 * Date:2016/5/18
 */
@Service
public class BillingServiceClient extends BaseLogger {

    @Autowired
    private BillingService billingService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private OperationDispatchService operationDispatchService;


    /**
     * 创建订单ID
     *
     * @param orderParam 生成ID所需参数实体.
     * @return 返回订单号
     */
    public String createOrderId(OrderParam orderParam) throws ServiceException {
        String orderNo = orderService.createOrderId(orderParam);
        if (orderParam.getSiteId() == 34) {
            lytPrePaid(orderParam, orderNo);
        }
        prepaid(orderParam, orderNo);
        return orderNo;
    }


    /**
     * 充值埋点(下单未支付).
     */
    public void prepaid(OrderParam orderParam, String orderNo) {
        //根据角色id查询角色信息
        try {
            GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(orderParam.getGameId()), orderParam.getRoleId());
            int roleLevel = 0;
            String roleName = orderParam.getRoleId() + "";
            if (gameRole != null) {
                roleLevel = gameRole.getRoleLevel();
                roleName = gameRole.getRoleName();
            }
            String time = DateUtil.getFormatedDateStringForLogConetent(new Date());
            String roleCreateTime = DateUtil.getFormatedDateStringForLogConetent(gameRole.getCreateTime());
            StringBuilder message = new StringBuilder(time).append("|").append(orderParam.getIpAddress()).append("|")
                    .append(orderParam.getDeviceType() == 1 ? "android" : "ios").append("|").append(orderParam.getSystemVersion()).append("|")
                    .append(orderParam.getResolution()).append("|").append(orderParam.getMacAddress()).append("|").append(orderParam.getDeviceSerial())
                    .append("|").append(orderParam.getAppVersion()).append("|").append(orderParam.getConnectType())
                    .append("|").append(orderParam.getChannel()).append("|").append(orderParam.getPackageName()).append("|")
                    .append(orderParam.getAreaCode()).append("|").append(orderParam.getUserId()).append("|").append(orderParam.getRoleId())
                    .append("|").append(roleName).append("|").append(roleLevel).append("|").append(orderParam.getChannel()).append("|")
                    .append(orderParam.getGoodsCount()).append("|").append(0).append("|").append(orderParam.getPayAmount())
                    .append("|").append("CNY").append("|").append(0).append("|").append(0).append("|").append(time).append("|").append(orderNo).append("|")
                    .append(roleCreateTime).append("|").append(gameRole.getTotalPayTimes()).append("|").append(gameRole.getTotalPayAmount())
                    .append("|").append(0).append("|").append(0);
            playerLogger.prepaid(message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void lytPrePaid(OrderParam orderParam, String orderNo) {
        try {
            String lytAppId = "20000009";

            String lytAppKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";

            String lytCheckUrl = "http://swglxh2.game.75wan.cn:30064/payment";

            GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(orderParam.getGameId()), orderParam.getRoleId());

            List<UserAccount> accountUser = accountService.getAccountsByUid(gameRole.getUserId());

            String accountId = "1";
            if (CollectionUtils.isEmpty(accountUser)) {
                accountId = orderParam.getUserId() + "";
            } else {
                UserAccount userAccount = accountUser.get(0);
                accountId = "a_" + userAccount.getAccountName();
            }
            int roleLevel = 0;
            String roleName = orderParam.getRoleId() + "";
            if (gameRole != null) {
                roleLevel = gameRole.getRoleLevel();
                roleName = gameRole.getRoleName();
            }
            long chargeTime = new Date().getTime();
            String serverId = orderParam.getAreaCode();
            String currencyAmount = orderParam.getPayAmount() + "";
            String virtualCurrencyAmount = orderParam.getGoodsCount() + "";
            String status = "request";
            String strParams = new StringBuilder(accountId).append("#").append(lytAppId).append("#").append(chargeTime).append("#").append(currencyAmount)
                    .append("#").append("CNY").append("#").append(0).append("#").append(roleLevel).append("#").append(orderNo).append("#").append("android").append("#").append("应用宝")
                    .append("#").append(orderParam.getRoleId()).append("#").append(roleName).append("#").append(serverId).append("#").append(status).append("#").append(virtualCurrencyAmount).append("#")
                    .append(lytAppKey).toString();
            //accountId, appId, chargeTime, currencyAmount, currencyType, iapId, level,orderId ,os , partner,roleId, roleName, serverId, status, virtualCurrencyAmount
            logger.info("pay strParams = " + strParams);
            String sign = CipherUtils.initMd5().encrypt(strParams);

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("accountId", accountId + ""));
            params.add(new BasicNameValuePair("roleId", gameRole.getRoleId() + ""));
            params.add(new BasicNameValuePair("roleName", roleName));
            params.add(new BasicNameValuePair("level", roleLevel + ""));
            params.add(new BasicNameValuePair("serverId", serverId));
            params.add(new BasicNameValuePair("appId", lytAppId));
            params.add(new BasicNameValuePair("partner", "应用宝"));
            params.add(new BasicNameValuePair("os", "android"));
            params.add(new BasicNameValuePair("orderId", orderNo));
            params.add(new BasicNameValuePair("chargeTime", chargeTime + ""));
            params.add(new BasicNameValuePair("currencyType", "CNY"));
            params.add(new BasicNameValuePair("currencyAmount", currencyAmount + ""));
            params.add(new BasicNameValuePair("virtualCurrencyAmount", virtualCurrencyAmount + ""));
            params.add(new BasicNameValuePair("iapId", "0"));
            params.add(new BasicNameValuePair("status", status));
            params.add(new BasicNameValuePair("sign", sign));
            String resultLyt = HttpUtils.doPost(lytCheckUrl, params, "utf-8", "");
            logger.info("pay recorder = " + resultLyt);
        } catch (Exception e) {
            logger.info("lytskd recoder error");
        }

    }

    /**
     * 三方支付.
     *
     * @param orderId   我们自己的订单号.
     * @param reOrderId 三方订单号.
     * @param currency  币种ID.
     * @param amount    玩家支付金额(分).
     * @throws ServiceException ServiceException.
     */
    public void thirdCharge(String orderId, String reOrderId, Currency currency, int amount) throws ServiceException {
        prePaid(orderId);
        lytPaid(orderId);
        billingService.charge(orderId, reOrderId, currency, amount);
    }

    /**
     * 三方支付.
     *
     * @param orderId   我们自己的订单号.
     * @param reOrderId 三方订单号.
     * @param currency  币种ID.
     * @param amount    玩家支付金额(分).
     * @throws ServiceException ServiceException.
     */
    public void moniCharge(String orderId, String reOrderId, Currency currency, int amount) throws ServiceException {
        prePaid(orderId);
        lytPaid(orderId);
        billingService.pressCharge(orderId, reOrderId, currency, amount);
    }

    /**
     * 腾讯补单.
     */
    public String tencentSupportCharge(OrderParam orderParam) throws ServiceException {
        int amount = orderParam.getPayAmount();
        int goodsCode = 0;
        if (amount >= 6 && amount < 25) {
            goodsCode = 2;
        }
        if (amount >= 25 && amount < 50) {
            goodsCode = 3;
        }
        if (amount >= 50 && amount < 98) {
            goodsCode = 4;
        }
        if (amount >= 98 && amount < 128) {
            goodsCode = 5;
        }
        if (amount >= 128 && amount < 198) {
            goodsCode = 6;
        }
        if (amount >= 198 && amount < 328) {
            goodsCode = 7;
        }
        if (amount >= 328 && amount < 648) {
            goodsCode = 8;
        }
        if (amount >= 648) {
            goodsCode = 9;
        }
        if (orderParam.getAreaCode() == null) {
            GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(27), orderParam.getRoleId());
            GameArea gameArea = gameAreaService.getGameAreaById(gameRole.getAreaId());
            orderParam.setAreaCode(gameArea.getAreaCode());
        }
        orderParam.setPayType(2);
        orderParam.setGoodsCode(goodsCode + "");
        String orderId = createOrderId(orderParam);
        thirdCharge(orderId, orderId, Currency.CNY, orderParam.getPayAmount());
        return orderId;
    }

    public void lytPaid(String orderId) {
        try {
            String lytAppId = "20000009";

            String lytAppKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";

            String lytCheckUrl = "http://swglxh2.game.75wan.cn:30064/payment";
            BillingOrder billingOrder = cacheService
                    .get(IcachedConstant.BILLING_ORDER_PARAMS + orderId, BillingOrder.class);
            List<UserAccount> accountUser = accountService.getAccountsByUid(billingOrder.getUserId());
            String accountId = "1";
            if (CollectionUtils.isEmpty(accountUser)) {
                accountId = billingOrder.getUserId() + "";
            } else {
                UserAccount userAccount = accountUser.get(0);
                accountId = "a_" + userAccount.getAccountName();
            }
            if (billingOrder != null && billingOrder.getSiteId() == 34) {
                GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(billingOrder.getGameId()), billingOrder.getRoleId());
                int roleLevel = 0;
                String roleName = billingOrder.getRoleId() + "";
                if (gameRole != null) {
                    roleLevel = gameRole.getRoleLevel();
                    roleName = gameRole.getRoleName();
                }
                long chargeTime = new Date().getTime();
                String serverId = billingOrder.getAreaCode();
                if (StringUtils.isEmpty(serverId)) {
                    serverId = billingOrder.getAreaId() + "";
                }
                String currencyAmount = billingOrder.getPayAmount() + "";
                String virtualCurrencyAmount = billingOrder.getGoodsCount() + "";
                String status = "success";
                String strParams = new StringBuilder(accountId).append("#").append(lytAppId).append("#").append(chargeTime).append("#").append(currencyAmount)
                        .append("#").append("CNY").append("#").append(0).append("#").append(roleLevel).append("#").append(orderId).append("#").append("android").append("#").append("应用宝")
                        .append("#").append(billingOrder.getRoleId()).append("#").append(roleName).append("#").append(serverId).append("#").append(status).append("#").append(virtualCurrencyAmount).append("#")
                        .append(lytAppKey).toString();
                logger.info("pay strParams = " + strParams);
                String sign = CipherUtils.initMd5().encrypt(strParams);

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("accountId", accountId + ""));
                params.add(new BasicNameValuePair("roleId", gameRole.getRoleId() + ""));
                params.add(new BasicNameValuePair("roleName", roleName));
                params.add(new BasicNameValuePair("level", roleLevel + ""));
                params.add(new BasicNameValuePair("serverId", serverId));
                params.add(new BasicNameValuePair("appId", lytAppId));
                params.add(new BasicNameValuePair("partner", "应用宝"));
                params.add(new BasicNameValuePair("os", "android"));
                params.add(new BasicNameValuePair("orderId", orderId));
                params.add(new BasicNameValuePair("chargeTime", chargeTime + ""));
                params.add(new BasicNameValuePair("currencyType", "CNY"));
                params.add(new BasicNameValuePair("currencyAmount", billingOrder.getPayAmount() + ""));
                params.add(new BasicNameValuePair("virtualCurrencyAmount", billingOrder.getGoodsCount() + ""));
                params.add(new BasicNameValuePair("iapId", "0"));
                params.add(new BasicNameValuePair("status", status));
                params.add(new BasicNameValuePair("sign", sign));
                String resultLyt = HttpUtils.doPost(lytCheckUrl, params, "utf-8", "");
                logger.info("pay recorder = " + resultLyt);
            }
        } catch (Exception e) {
            logger.info("充值记录失败");
        }
    }


    public void prePaid(String orderId) {
        try {
            BillingOrder billingOrder = cacheService
                    .get(IcachedConstant.BILLING_ORDER_PARAMS + orderId, BillingOrder.class);
            GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(billingOrder.getGameId()), billingOrder.getRoleId());
            int roleLevel = 0;
            String roleName = billingOrder.getRoleId() + "";
            if (gameRole != null) {
                roleLevel = gameRole.getRoleLevel();
                roleName = gameRole.getRoleName();
            }
            String time = DateUtil.getFormatedDateStringForLogConetent(new Date());
            String roleCreateTime = DateUtil.getFormatedDateStringForLogConetent(gameRole.getCreateTime());
            StringBuilder message = new StringBuilder(time).append("|").append(billingOrder.getIpAddress()).append("|")
                    .append(billingOrder.getDeviceType() == 1 ? "android" : "android").append("|").append(billingOrder.getSystemVersion()).append("|")
                    .append(billingOrder.getResolution()).append("|").append(billingOrder.getMacAddress()).append("|").append(billingOrder.getDeviceSerial())
                    .append("|").append(billingOrder.getAppVersion()).append("|").append(billingOrder.getConnectType())
                    .append("|").append(billingOrder.getChannel()).append("|").append(billingOrder.getPackageName()).append("|")
                    .append(billingOrder.getAreaCode()).append("|").append(billingOrder.getUserId()).append("|").append(billingOrder.getRoleId())
                    .append("|").append(roleName).append("|").append(roleLevel).append("|").append(billingOrder.getChannel()).append("|")
                    .append(billingOrder.getGoodsCount()).append("|").append(0).append("|").append(billingOrder.getPayAmount())
                    .append("|").append("CNY").append("|").append(0).append("|").append(0).append("|").append(time).append("|").append(orderId).append("|")
                    .append(roleCreateTime).append("|").append(gameRole.getTotalPayTimes() + 1).append("|").append(gameRole.getTotalPayAmount() + billingOrder.getPayAmount())
                    .append("|").append(0).append("|").append(1);
            playerLogger.prepaid(message.toString());
        } catch (Exception e) {
            logger.info("prePaid 充值记录失败");
        }
    }

    /**
     * 钱包支付.
     *
     * @param orderId 我们自己的订单号.
     * @throws ServiceException ServiceException.
     */
    public void walletCharge(String orderId) throws ServiceException {
        billingService.walletPay(orderId);
    }

    /**
     * 客户端查询商品列表.
     *
     * @throws ServiceException ServiceException
     */
    public List<GoodsVo> queryGoods(int gameId, int siteId, String areaCode, long roleId, int shopType) throws ServiceException {
        List<BillingGoods> billingGoodsList = goodsService.queryGoods(gameId, siteId, areaCode, roleId, shopType);
        List<GoodsVo> goodsVos = new ArrayList<GoodsVo>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BillingGoods billingGoods : billingGoodsList) {
            GoodsVo goodsVo = new GoodsVo();
            goodsVo.setGameId(billingGoods.getGameId());
            goodsVo.setGoodsId(billingGoods.getGoodsId());
            goodsVo.setDiscount(billingGoods.getDisaccount());
            goodsVo.setGoodsAmount(billingGoods.getGoodsAmount());
            goodsVo.setGoodsCode(billingGoods.getGoodsCode());
            goodsVo.setGoodsDesc(billingGoods.getGoodsDesc());
            goodsVo.setGoodsMarkTime(simpleDateFormat.format(billingGoods.getGoodsMarkTime()));
            goodsVo.setGoodsName(billingGoods.getGoodsName());
            goodsVo.setGoodsCount(billingGoods.getGoodsCount());
            goodsVo.setRelatedGoodsId(billingGoods.getRelatedGoodsId());
            goodsVo.setShopType(billingGoods.getShopType().getId());
            goodsVo.setGoodsPic(billingGoods.getGoodsPic());
            goodsVo.setChargeExtra(billingGoods.getChargeExtra());

            // List<GoodsItem> goodsItems = JSON.parseArray(billingGoods.getFirstBuyPolicy().getItemPackage(), GoodsItem.class);
            /*if (!CollectionUtils.isEmpty(goodsItems)) {
                List<GoodsItemVo> goodsItemVos = new ArrayList<>();
                for (GoodsItem goodsItem : goodsItems) {
                    GoodsItemVo goodsItemVo = new GoodsItemVo();
                    goodsItemVo.setTypeId(goodsItem.getItemType().getValue());
                    goodsItemVo.setItemId(goodsItem.getItemId());
                    goodsItemVo.setValue(goodsItem.getValue());
                    goodsItemVos.add(goodsItemVo);
                }
                goodsVo.setGoodsItems(goodsItemVos);*/
            //} else {
            goodsVo.setGoodsItems(new ArrayList<GoodsItemVo>());
            //}
            if (billingGoods.getFirstBuyPolicy() == null) {
                billingGoods.setFirstBuyPolicy(new FirstBuyPolicy());
            }
            goodsVo.setFirstPay(billingGoods.getFirstBuy());

            goodsVos.add(goodsVo);
        }
        return goodsVos;
    }

    /**
     * 通过宝石购买商品.
     *
     * @param gameId   游戏ID
     * @param areaCode 区服CODE
     * @param userId   用户ID
     * @param money    money
     * @param goodsId  道具itemId
     */
    public void gemPurchase(int gameId, String areaCode, long userId, int money, int goodsId) throws ServiceException {
        //获取areaId
        int areaId = gameAreaService.getGameAreaByCode(gameId, areaCode).getId();
        billingService.purchaseByGem(goodsId, money, GameCode.getById(gameId), areaId, userId);
    }


    /**
     * 查询单个商品.
     *
     * @param goodsId 商品ID.
     */
    public GoodsVo getGoods(int goodsId) throws ServiceException {
        BillingGoods billingGoods = goodsService.queryGoodsById(goodsId);
        return GoodsVo.convertGoodsVo(billingGoods);
    }

    /**
     * 发送玩具激活码给游戏服.
     */
    public void sendToycode(int gameId, String areaCode, String userId, int itemId, String code) throws ServiceException {
        int areaId = gameAreaService.getGameAreaByCode(gameId, areaCode).getId();
        UserAccount userAccount = accountService.getAccount(AccountType.THIRD_OAUTH, userId);
        billingService.sendToycode(gameId, areaId, userAccount.getUserId(), itemId, code);
    }

}

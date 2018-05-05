package com.fanfandou.platform.serv.billing.service;

import com.alibaba.fastjson.JSON;
import com.fanfandou.admin.api.system.entity.DataDictionary;
import com.fanfandou.admin.api.system.service.DataDictionaryService;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.constant.DicItemKeyConstant;
import com.fanfandou.common.constant.DicKeyConstant;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.ActStatus;
import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.resource.DicItem;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.resource.SourceCodeFactory;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.platform.api.billing.entity.BillingGoods;
import com.fanfandou.platform.api.billing.entity.BillingGoodsExample;
import com.fanfandou.platform.api.billing.entity.FirstBuyPolicy;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.billing.entity.GoodsType;
import com.fanfandou.platform.api.billing.exception.BillingException;
import com.fanfandou.platform.api.billing.service.GoodsService;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.serv.billing.dao.BillingGoodsMapper;
import com.fanfandou.platform.serv.billing.dao.GoodsTypeMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wudi.
 * Descreption:商品实现类.
 * Date:2016/5/4
 */
@Service("goodsService")
public class BillingGoodsImpl extends BaseLogger implements GoodsService {

    @Autowired
    private BillingGoodsMapper billingGoodsMapper;

    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;

    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private OperationDispatchService operationDispatchService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Override
    public void addCreateGoods(BillingGoods billingGoods) throws ServiceException {
        if (billingGoods.getGameId() == null || billingGoods.getGoodsAmount() == null
                || billingGoods.getGoodsCount() == null || billingGoods.getGoodsName() == null
                || billingGoods.getGoodsType() == 0 || billingGoods.getGoodsCode() == null) {
            throw new BillingException(BillingException.GOODS_LACK_PARAMS, "创建商品参数缺失");
        }
        billingGoods.setGoodsId((int) tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_BILLING_GOODS));
        billingGoods.setCreateTime(new Date());
        billingGoodsMapper.insertSelective(billingGoods);
        updateCacheBillingGoods(billingGoods);
        //updateCacheFirstBuy(billingGoods);//首充策略
        updateCacheGoodsItem(billingGoods);//商品物品
        DicItem dicItem = dataDictionaryService.getDicItemByConstant(DicKeyConstant.PLATFORM_PUSHINFO_PUSHTYPE,
                DicItemKeyConstant.PLATFORM_PUSHINFO_SHELVE_UPDATE);
        String areaIds[] = billingGoods.getAreaIds().split(",");
        if (billingGoods.getGameId() == 1) {
            for (String areaId : areaIds) {
                operationDispatchService.pushMsgToClient(GameCode.getById(billingGoods.getGameId()),
                        Integer.parseInt(areaId), 0, dicItem, billingGoods.getGoodsId(), "");
            }
        }


    }

    /**
     * 首充策略
     *
     * @param billingGoods 商品对象
     */
    public void updateCacheFirstBuy(BillingGoods billingGoods) {
        cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_FIRSTBUY + billingGoods.getGameId(),
                billingGoods.getGoodsId() + "", billingGoods.getFirstBuyPolicy());
    }

    /**
     * 商品物品
     *
     * @param billingGoods 商品对象
     */
    public void updateCacheGoodsItem(BillingGoods billingGoods) {
        cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_GIFTBAG + billingGoods.getGameId(),
                billingGoods.getGoodsId() + "", billingGoods.getItemJson());
    }

    @Override
    public void updUpdateGoodsById(BillingGoods billingGoods) throws ServiceException {
        if (billingGoods.getGameId() == null || billingGoods.getGoodsAmount() == null
                || billingGoods.getGoodsCount() == null || billingGoods.getGoodsName() == null
                || billingGoods.getGoodsType() == 0 || billingGoods.getGoodsCode() == null) {
            throw new BillingException(BillingException.GOODS_LACK_PARAMS, "修改商品参数缺失");
        }
        billingGoodsMapper.updateByPrimaryKeySelective(billingGoods);
        updateCacheBillingGoods(billingGoods);
//        if (billingGoods.getFirstBuyPolicy() != null) {
//            updateCacheFirstBuy(billingGoods);//首充策略
//        }
        if (billingGoods.getItemJson() != null) {
            updateCacheGoodsItem(billingGoods);//商品物品
        }
        DicItem dicItem = dataDictionaryService.getDicItemByConstant(DicKeyConstant.PLATFORM_PUSHINFO_PUSHTYPE,
                DicItemKeyConstant.PLATFORM_PUSHINFO_PRICE_UPDATE);
        String areaIds[] = billingGoods.getAreaIds().split(",");
        if (billingGoods.getGameId() == 1) {
            for (String areaId : areaIds) {
                operationDispatchService.pushMsgToClient(GameCode.getById(billingGoods.getGameId()),
                        Integer.parseInt(areaId), 0, dicItem, billingGoods.getGoodsId(), "");
            }
        }
    }

    @Override
    public List<BillingGoods> queryGoodsByGameId(int gameId) throws ServiceException {
        List<BillingGoods> billingGoodsList = cacheService.hGetValues(PublicCachedKeyConstant.BILLING_GOODS_LIST + gameId,
                BillingGoods.class);
        if (billingGoodsList == null || billingGoodsList.size() == 0) {
            billingGoodsList = billingGoodsMapper.getGoodsByGameId(gameId);
            for (BillingGoods billingGoods : billingGoodsList) {
                cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_LIST + gameId, billingGoods.getGoodsId() + "", billingGoods);
            }
        }
        return billingGoodsList;
    }

    @Override
    public BillingGoods queryGoodsByCode(int gameId, int goodsId) throws ServiceException {

        BillingGoods billingGoods = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_LIST + gameId, goodsId + "",
                BillingGoods.class);
        if (billingGoods == null) {
            billingGoods = billingGoodsMapper.getGoodsByCode(gameId, goodsId);
            cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_LIST + gameId, billingGoods.getGoodsId() + "", billingGoods);
        }
        return billingGoods;
    }

    @Override
    public void refreshBillingGoodsCache() {
        Map<Integer, GameCode> games = SourceCodeFactory.getGames();
        for (Map.Entry<Integer, GameCode> entry : games.entrySet()) {
            int gameId = entry.getValue().getId();
            List<BillingGoods> billingGoodsList = billingGoodsMapper.getGoodsByGameId(gameId);
            for (BillingGoods billingGoods : billingGoodsList) {
                cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_LIST + gameId, billingGoods.getGoodsId() + "", billingGoods);
            }
        }
    }

    public void updateCacheBillingGoods(BillingGoods billingGoods) {
        cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_LIST + billingGoods.getGameId(),
                billingGoods.getGoodsId() + "", billingGoods);
    }

    @Override
    public List<BillingGoods> queryGoods(int gameId, int siteId, String areaCode, long roleId, int shopType) throws
            ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode);
        int areaId = gameArea.getId();
        List<BillingGoods> goodsList = queryGoodsByGameId(gameId);
        List<BillingGoods> goodsListEnd = new ArrayList<>();
        List<BillingGoods> firstBuyGoodsList = new ArrayList<>();
        logger.info("queryGoods >>>> " + goodsList.size());
        //1，首充策略，策略关联gameId。 2，通过gameId拿到策略
        FirstBuyPolicy firstBuyPolicy = new FirstBuyPolicy();

        if (CollectionUtils.isEmpty(goodsList)) {
            throw new BillingException(BillingException.GOODS_NOT_EXIST, "没有商品");
        }
        logger.info("当前区服ID " + areaId);
        boolean isContainAllSite = false;
        if (siteId == 108 || siteId == 109 || siteId == 110) {
            siteId = 108;
        }

        if (siteId == 64 || siteId == 111 || siteId == 112) {
            siteId = 63;
        }

        //从数据库中拿到每一个配方
        for (BillingGoods billingGoods : goodsList) {
            //判断该配方是否为该渠道

            if (!billingGoods.getValidStatus().equals(ValidStatus.VALID)) {
                continue;
            }
            logger.info("当前goodsID " + billingGoods.getGoodsId());
            if ((billingGoods.getSiteId() != 0 && !billingGoods.getSiteId().equals(siteId))) {
                continue;
            }
            boolean isContainSite = false;
            if (siteId == billingGoods.getSiteId()) {
                isContainSite = true;
                isContainAllSite = true;
            }

            //如果打开了，则开始计算首充，开始判断玩家是否是首充
            String firstPay = cacheService
                    .hGet(IcachedConstant.BILLING_IS_FIRSTBUY + gameId + siteId + billingGoods.getGoodsCode(), roleId + "", String.class);
            logger.info("该商品code =  " + billingGoods.getGoodsCode());
            if (StringUtils.isEmpty(firstPay)) {
                //则改用户未首充
                logger.info("该玩家未首充 = " + firstPay);
                firstBuyPolicy.setFirstPay(0);
                billingGoods.setFirstBuy(0);
            } else {
                logger.info("该玩家已首充 = " + firstPay);
                firstBuyPolicy.setFirstPay(1);
                billingGoods.setFirstBuy(1);
            }

            String goodsitems = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_GIFTBAG + billingGoods.getGameId(),
                    billingGoods.getGoodsId() + "", String.class);
            List<GoodsItem> goodsItemList = JSON.parseArray(goodsitems, GoodsItem.class);
            if (!CollectionUtils.isEmpty(goodsItemList) && goodsItemList.get(0).getValue() != 0) {
                logger.info("充值返利有效");
                billingGoods.setChargeExtra(goodsItemList.get(0).getValue());
            }

            billingGoods.setFirstBuyPolicy(firstBuyPolicy);
            boolean ifContainArea = true;
            if (ifContainArea && isContainSite) {
                goodsListEnd.add(billingGoods);
            }
            if (ifContainArea) {
                firstBuyGoodsList.add(billingGoods);
            }
        }
        if (isContainAllSite) {
            return goodsListEnd;
        }
        return firstBuyGoodsList;
    }

    @Override
    public PageResult queryGoods(Integer gameId, Integer siteId, String goodsName, Page page) throws ServiceException {

        if (page.getOrder() == null) {
            page.setOrder("goodsId");
        }
        if (page.getSort() == null) {
            page.setSort("asc");
        }
        int startNum = (page.getPageIndex() - 1) * page.getPageSize();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gameId", gameId);
        paramMap.put("siteId", siteId);
        paramMap.put("goodsName", goodsName);
        paramMap.put("startNum", startNum);
        paramMap.put("endNum", page.getPageSize());
        paramMap.put("order", page.getOrder());
        paramMap.put("sort", page.getSort());

        List<BillingGoods> billingGoodses = billingGoodsMapper.getGoodsBySelective(paramMap);
        int totalCount = this.billingGoodsMapper.totalCount(paramMap);
        page.setTotalCount(totalCount);
        PageResult<BillingGoods> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(billingGoodses);
        return pageResult;
    }

    @Override
    public List<BillingGoods> queryGoods(BillingGoodsExample billingGoodsExample) throws ServiceException {
        return billingGoodsMapper.selectByExample(billingGoodsExample);
    }

    @Override
    public BillingGoods queryGoods(String goodsCode) throws ServiceException {
        return billingGoodsMapper.selectByGoodsCode(goodsCode);
    }

    @Override
    public BillingGoods queryGoodsById(int goodsId) throws ServiceException {
        BillingGoods billingGoods = billingGoodsMapper.selectByPrimaryKey(goodsId);
        if (billingGoods == null) {
            throw new BillingException(BillingException.GOODS_NOT_EXIST);
        }
        FirstBuyPolicy firstBuyPolicy = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_FIRSTBUY + billingGoods.getGameId(),
                billingGoods.getGoodsId() + "", FirstBuyPolicy.class);
        billingGoods.setFirstBuyPolicy(firstBuyPolicy);

        String itemJson = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_GIFTBAG + billingGoods.getGameId(),
                billingGoods.getGoodsId() + "", String.class);
        billingGoods.setItemJson(itemJson);
        return billingGoods;
    }

    @Override
    public void delGoods(int goodsId) throws ServiceException {
        BillingGoods billingGoods = billingGoodsMapper.selectByPrimaryKey(goodsId);
        billingGoodsMapper.deleteByPrimaryKey(goodsId);
        //updateCacheBillingGoods(billingGoods);
        cacheService.hDel(PublicCachedKeyConstant.BILLING_GOODS_FIRSTBUY + billingGoods.getGameId(),
                billingGoods.getGoodsId() + "");//删除首充策略
        cacheService.hDel(PublicCachedKeyConstant.BILLING_GOODS_GIFTBAG + billingGoods.getGameId(), billingGoods.getGoodsId() + "");//删除道具集合
        //cacheService.hPut(PublicCachedKeyConstant.BILLING_GOODS_LIST + gameId, billingGoods.getGoodsId() + "", billingGoods);
        cacheService.hDel(PublicCachedKeyConstant.BILLING_GOODS_LIST + billingGoods.getGameId(), billingGoods.getGoodsId() + "");
        DicItem dicItem = dataDictionaryService.getDicItemByConstant(DicKeyConstant.PLATFORM_PUSHINFO_PUSHTYPE,
                DicItemKeyConstant.PLATFORM_PUSHINFO_OFF_SHELVE_UPDATE);
        String areaIds[] = billingGoods.getAreaIds().split(",");
        if (billingGoods.getGameId() == 1) {
            for (String areaId : areaIds) {
                operationDispatchService.pushMsgToClient(GameCode.getById(billingGoods.getGameId()),
                        Integer.parseInt(areaId), 0, dicItem, billingGoods.getGoodsId(), "");
            }
        }
    }

    @Override
    public void createGoodsType(String code) throws ServiceException {
        //生成主键
        long seqValue = tableSequenceGenerator.generate(TableSequenceConstant
                .PLATFORM_BILLING_GOODS_TYPE);
        GoodsType goodsType = new GoodsType();
        goodsType.setGoodsTypeId((int) seqValue);
        goodsType.setCode(code);
        goodsTypeMapper.insertSelective(goodsType);
    }

    @Override
    public void updateGoodsType(int id, String code) throws ServiceException {
        GoodsType goodsType = new GoodsType();
        goodsType.setGoodsTypeId(id);
        goodsType.setCode(code);
        goodsTypeMapper.updateByPrimaryKeySelective(goodsType);
    }

    @Override
    public List<GoodsType> queryGoodsType() throws ServiceException {
        return goodsTypeMapper.selectAll();
    }

    @Override
    public void deleteGoodsType(int id) throws ServiceException {
        goodsTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void awardPackageGenerate(List<Integer> goodsIds, List<Integer> itemIds, int packageType, String desc,
                                     int value) throws ServiceException {
        if (goodsIds.size() != 0) {
            if (itemIds == null) {
                for (Integer goodsId : goodsIds) {

                    //组装一个GoodsItemPackage
                    GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
                    goodsItemPackage.setAwardPackageId(IcachedConstant.BILLING_AWARD_PACKAGE_ID + goodsId);
                    //goodsItemPackage.setGoodsItems();
                    goodsItemPackage.setPackageDesc(desc);
                    goodsItemPackage.setValue(value);

                    BillingGoods billingGoods = new BillingGoods();
                    //生成物品奖励包ID Key = 常量 + goodsId
                    String packageId = IcachedConstant.BILLING_AWARD_PACKAGE_ID + goodsId;
                    billingGoods.setGoodsId(goodsId);
                    billingGoods.setAwardId(packageId);
                    billingGoodsMapper.updateByPrimaryKeySelective(billingGoods);
                    cacheService.put(packageId, goodsItemPackage);
                }
            }
        }
    }

    @Override
    public void firstBuyGenerate(List<Integer> goodsIds, String firstOperation, int operateCount, GoodsItemPackage goodsItemPackage)
            throws ServiceException {
        if (goodsIds.size() != 0) {
            for (Integer goodsId : goodsIds) {
                FirstBuyPolicy firstBuyPolicy = new FirstBuyPolicy();
                firstBuyPolicy.setFirstBuyId(IcachedConstant.BILLING_FIRSTBUY_POLICY_ID + goodsId);
                firstBuyPolicy.setFirstPay(0);
                firstBuyPolicy.setOperateCount(operateCount);
                //firstBuyPolicy.setGoodsItemPackage(goodsItemPackage);

                cacheService.put(IcachedConstant.BILLING_FIRSTBUY_POLICY + goodsId, firstBuyPolicy);
            }
        }
    }

    @Override
    public void firstBuyPolicy(int gameId, int operateCount, Boolean isFirstPay, String goodsItemPackageJson) throws ServiceException {
        FirstBuyPolicy firstBuyPolicy = new FirstBuyPolicy();
        firstBuyPolicy.setFirstPay(0);
        firstBuyPolicy.setOperateCount(operateCount);
        firstBuyPolicy.setItemPackage(goodsItemPackageJson);
        cacheService.put(IcachedConstant.BILLING_FIRSTBUY_POLICY + gameId, firstBuyPolicy);
    }

    @Override
    public FirstBuyPolicy getFirstBuyPolicy(int gameId) throws ServiceException {
        FirstBuyPolicy firstBuyPolicy = cacheService.get(IcachedConstant.BILLING_FIRSTBUY_POLICY + gameId, FirstBuyPolicy.class);
        return firstBuyPolicy;
    }

    @Override
    public void keyUpdate(String id, int value, String state) throws ServiceException {
        String[] idsList = id.split(",");
        if (state.equals("frame")) {
            for (int i = 0; i < idsList.length; i++) {
                BillingGoods billingGoods = billingGoodsMapper.selectByPrimaryKey(Integer.parseInt(idsList[i]));
                billingGoods.setValidStatus(ValidStatus.valueOf(value));
                updUpdateGoodsById(billingGoods);
            }
        } else {
            for (int i = 0; i < idsList.length; i++) {
                BillingGoods billingGoods = billingGoodsMapper.selectByPrimaryKey(Integer.parseInt(idsList[i]));
                FirstBuyPolicy firstBuyPolicy = cacheService.hGet(PublicCachedKeyConstant.BILLING_GOODS_FIRSTBUY + billingGoods.getGameId(),
                        billingGoods.getGoodsId() + "", FirstBuyPolicy.class);
                firstBuyPolicy.setFirstPay(value);
                billingGoods.setFirstBuyPolicy(firstBuyPolicy);
//                updateCacheFirstBuy(billingGoods);
            }
        }
    }
}

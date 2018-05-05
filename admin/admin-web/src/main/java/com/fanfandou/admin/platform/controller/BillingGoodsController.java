package com.fanfandou.admin.platform.controller;


import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.billing.entity.BillingGoods;
import com.fanfandou.platform.api.billing.entity.FirstBuyPolicy;
import com.fanfandou.platform.api.billing.entity.ShopType;
import com.fanfandou.platform.api.billing.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * author zhongliang.
 * Description:商品配方管理操作.
 */
@RestController
@RequestMapping(value = "/platform/billingGoods")
public class BillingGoodsController {


    @Autowired
    private GoodsService goodsService;

    /**
     * 跳转到商品列表页面
     *
     * @return 列表页面
     */
    @RequestMapping(value = "/billingGoodsInit")
    public ModelAndView toListMenu() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/BillingGoodsList");
        return mav;
    }

    /**
     * 跳转到首充策略页面
     *
     * @return 首充页面
     */
    @RequestMapping(value = "/firstBuyPolicyInit")
    public ModelAndView firstBuyPolicyList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/FirstBuyPolicyPage");
        return mav;
    }

    /**
     * 跳转到首充策略页面
     *
     * @return 首充页面
     */
    @RequestMapping(value = "/awardPackageInit")
    public ModelAndView awardPackage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/AwardPackage");
        return mav;
    }

    /**
     * 跳转到商品添加页面
     *
     * @return 添加页面
     */
    @RequestMapping(value = "/billingGoodsAddInit")
    public ModelAndView toBillingAdd() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/BillingGoodsAdd");
        return mav;
    }


    /**
     * 跳转到商品修改页面
     *
     * @return 修改页面
     */
    @RequestMapping(value = "/billingGoodsUpdateInit")
    public ModelAndView toBillingUpdate() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/BillingGoodsUpdate");
        return mav;
    }

    /**
     * 跳转到商品编辑页面
     *
     * @return 修改页面
     */
    @RequestMapping(value = "/billingGoodsEditInit")
    public ModelAndView toBillingEdit() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/BillingGoodsEdit");
        return mav;
    }


    /**
     * 分页查询
     *
     * @param gameId 游戏id
     * @param siteId 渠道id
     * @param page   page对象
     * @return 分页对象集合
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping("/pageList")
    public PageResult<BillingGoods> getPageList(Integer gameId, Integer siteId, Page page, String goodsName) throws ServiceException {

        return this.goodsService.queryGoods(gameId, siteId, goodsName, page);
    }

    /**
     * 根据id删除.
     *
     * @param goodIds 物品id
     */
    @ResponseBody
    @RequestMapping(value = "/goodsDelete")
    public void goodsDelete(String goodIds) throws ServiceException {
        String[] idsList = goodIds.split(",");
        for (int i = 0; i < idsList.length; i++) {
            this.goodsService.delGoods(Integer.parseInt(idsList[i]));
        }

    }

    /**
     * 根据id删除.
     *
     * @param code code
     */
    @ResponseBody
    @RequestMapping(value = "/checkCode")
    public int checkCode(String code) throws ServiceException {
        BillingGoods billingGoods = this.goodsService.queryGoods(code);
        if (billingGoods == null) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * 根据id查.
     *
     * @param id id
     */
    @ResponseBody
    @RequestMapping(value = "/getGoods/{id}")
    public BillingGoods edit(@PathVariable(value = "id") int id) throws ServiceException {
        BillingGoods billingGoods = goodsService.queryGoodsById(id);
        return billingGoods;
    }

    /**
     * 商品添加
     *
     * @param billingGoods 商品对象
     * @param valid        状态
     * @param shopTypeId   商品类型id
     * @throws ServiceException
     */
    @RequestMapping(value = "/insert")
    public void insert(BillingGoods billingGoods, int valid, int shopTypeId, String itemPackage, Integer packageId) throws ServiceException {
        if (billingGoods.getSiteId() == null) {
            billingGoods.setSiteId(0);
        }
//        if (operateCount == null) {
//            operateCount = 0;
//        }
//        if (isFirstPay == null) {
//            isFirstPay = 0;
//        }
        if (packageId == null) {
            packageId = 0;
        }
        billingGoods.setValidStatus(ValidStatus.valueOf(valid));
        billingGoods.setShopType(ShopType.valueOf(shopTypeId));
//        FirstBuyPolicy firstBuyPolicy = new FirstBuyPolicy();
//        firstBuyPolicy.setOperateCount(operateCount);
//        firstBuyPolicy.setFirstPay(isFirstPay);
//        firstBuyPolicy.setPackageId(packageId);
        billingGoods.setItemJson(itemPackage);
        billingGoods.setAwardId(packageId.toString());
//        billingGoods.setFirstBuyPolicy(firstBuyPolicy);
        this.goodsService.addCreateGoods(billingGoods);
    }

    /**
     * 商品修改
     *
     * @param billingGoods 商品对象
     * @param valid        状态
     * @param shopTypeId   商品类型id
     * @throws ServiceException
     */
    @RequestMapping(value = "/update")
    public void update(BillingGoods billingGoods, int valid, int shopTypeId, String itemPackage, Integer packageId) throws ServiceException {
        if (billingGoods.getSiteId() == null) {
            billingGoods.setSiteId(0);
        }
        billingGoods.setValidStatus(ValidStatus.valueOf(valid));
        billingGoods.setShopType(ShopType.valueOf(shopTypeId));
        billingGoods.setItemJson(itemPackage);
        billingGoods.setAwardId(packageId.toString());
        this.goodsService.updUpdateGoodsById(billingGoods);
    }

    /**
     * 商品修改
     *
     * @param id    商品id.
     * @param value 值.
     * @param state 状态.
     * @throws ServiceException
     */
    @RequestMapping(value = "/keyUpdate")
    public void keyUpdate(String id, Integer value, String state) throws ServiceException {
        this.goodsService.keyUpdate(id, value, state);
    }

    /**
     * 首充策略修改
     *
     * @param gameId 游戏id
     * @throws ServiceException
     */
    @RequestMapping(value = "/firstBuyPolicyEdit")
    public void firstBuyPolicyEdit(int gameId, int operateCount, Boolean isFirstPay, String itemPackage) throws ServiceException {
        try {
            itemPackage = URLDecoder.decode(itemPackage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.goodsService.firstBuyPolicy(gameId, operateCount, isFirstPay, itemPackage);
    }

    /**
     * 商品刷新缓存
     *
     * @throws ServiceException
     */
    @RequestMapping(value = "/refreshBillingGoodsCache")
    public void refreshBillingGoodsCache() throws ServiceException {
        this.goodsService.refreshBillingGoodsCache();
    }

    /**
     * 获取首充策略
     *
     * @param gameId 游戏id
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/getFirstBuyPolicy")
    public FirstBuyPolicy getFirstBuyPolicy(int gameId) throws ServiceException {
        return this.goodsService.getFirstBuyPolicy(gameId);
    }
//    @RequestMapping(value = "/firstBuyPolicyAdd")
//    public void firstBuyPolicy(String operation, int operateCount, String goodsIds) throws ServiceException {
//        List<Integer> list = new ArrayList<Integer>();
//        String[] idsList = goodsIds.split(",");
//        for (int i = 0; i < idsList.length; i++) {
//            list.add(Integer.parseInt(idsList[i]));
//        }
//        this.goodsService.firstBuyGenerate(list, operation, operateCount);
//    }
//
//    @RequestMapping(value = "/packageAdd")
//    public void packageAdd(int packageType, String packageDesc, int value, String goodsItemsIds, String goodsIds) throws ServiceException {
//        List<Integer> list = new ArrayList<Integer>();
//        String[] idsList = goodsIds.split(",");
//        for (int i = 0; i < idsList.length; i++) {
//            list.add(Integer.parseInt(idsList[i]));
//        }
//
//        List<Integer> goodsItemsIdlist = new ArrayList<Integer>();
//        String[] goodsItems = goodsIds.split(",");
//        for (int i = 0; i < goodsItems.length; i++) {
//            goodsItemsIdlist.add(Integer.parseInt(goodsItems[i]));
//        }
//
//        this.goodsService.awardPackageGenerate(list, goodsItemsIdlist, packageType, packageDesc, value);
//        System.out.print(packageDesc);
//    }
}

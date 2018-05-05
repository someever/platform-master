package com.fanfandou.admin.platform.controller;


import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.billing.entity.*;
import com.fanfandou.platform.api.billing.service.BillingService;
import com.fanfandou.platform.api.billing.service.GoodsService;
import com.fanfandou.platform.api.billing.service.OrderService;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.service.GameRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;


/**
 * author zhongliang.
 * Description:订单查询管理操作.
 */
@RestController
@RequestMapping(value = "/platform/billingOrder")
public class BillingOrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private BillingService billingService;


    /**
     * 跳转到商品列表页面
     *
     * @return 列表页面
     */
    @RequestMapping(value = "/billingOrderInit")
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/BillingOrderList");
        return mav;
    }

    /**
     * 跳转到商品列表页面
     *
     * @return 列表页面
     */
    @RequestMapping(value = "/orderParamEdit")
    public ModelAndView toOrderParamEdit() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/OrderParamEdit");
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
    public PageResult<BillingOrder> getPageList(Integer roleId, Integer siteId, Integer gameId, String roleName, Page page, String from, String to) throws Exception {

        return this.orderService.getOrders(roleId, siteId, gameId, roleName, page, from, to);
    }


    /**
     * 分页查询
     *
     * @param gameId   游戏id
     * @param roleId   角色id
     * @param roleName 角色名称
     */
    @ResponseBody
    @RequestMapping("/checkRole")
    public GameRole getPageList(Integer roleId, Integer gameId, String roleName) throws Exception {
        GameRole gameRole = null;
        if (roleId == null) {
            gameRole = gameRoleService.getRoleByRoleName(GameCode.getById(gameId), roleName);
            return gameRole;
        } else {
            gameRole = gameRoleService.getRoleById(GameCode.getById(gameId), roleId);
            return gameRole;
        }

    }

    /**
     * 添加
     *
     * @param orderParam 订单对象
     */
    @ResponseBody
    @RequestMapping("/orderParamAdd")
    public void orderParamAdd(OrderParam orderParam) throws Exception {
        this.billingService.moniCharge(orderParam);
    }
}

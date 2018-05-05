
package com.fanfandou.admin.toyquery.controller;

import com.fanfandou.admin.operation.service.MailOrderService;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.entity.GameRoleDispose;
import com.fanfandou.platform.api.game.service.GameClosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * author zhongliang.
 * Description:玩家封停管理操作.
 */


@RestController
@RequestMapping(value = "/toyQuery/gameClosure")
public class GameClosureController {

    @Autowired
    GameClosureService gameClosureService;


    /**
     * 跳转到封停List页面
     *
     * @return 分类页面
     */


    @RequestMapping(value = "/gameClosureInit")
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/toyquery/GameClosureList");
        return mav;
    }


    @ResponseBody
    @RequestMapping("/pageList")
    public PageResult<GameRoleDispose> getPageList(Integer gameId, Integer siteId, Page page, Integer roleId, Integer roleStatus, String roleName) throws Exception {
        return this.gameClosureService.getGameClosureList(page, gameId, siteId, roleId, roleStatus, roleName);
    }


    /**
     * 添加封停，禁言
     *
     * @param playersData 封停禁言
     * @throws ServiceException
     */
    @RequestMapping(value = "/insert")
    public void insert(GameRoleDispose playersData) throws ServiceException {
            this.gameClosureService.insertGameClosure(playersData);
    }

    /**
     * 玩具批量添加
     *
     * @param id     封停禁言id
     * @param status 封停禁言状态
     * @throws ServiceException
     */
    @RequestMapping(value = "/roleStatusUpdate")
    public void insert(int id, int status) throws ServiceException {
        this.gameClosureService.updateGameClosure(id, status);
    }
}

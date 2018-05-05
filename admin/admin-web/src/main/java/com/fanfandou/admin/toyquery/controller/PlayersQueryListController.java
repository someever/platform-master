/*

package com.fanfandou.admin.toyquery.controller;

import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.platform.api.game.entity.GameRoleDispose;
import com.fanfandou.platform.api.game.service.GameClosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


*/
/**
 * author zhongliang.
 * Description:玩家查询列表操作.
 *//*


@RestController
@RequestMapping(value = "/toyQuery/playersQueryList")
public class PlayersQueryListController {

    @Autowired
    GameClosureService gameClosureService;


    */
/**
     * 跳转到封停List页面
     *
     * @return 分类页面
     *//*


    @RequestMapping(value = "/gameClosureInit")
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/toyquery/GameClosureList");
        return mav;
    }


    @ResponseBody
    @RequestMapping("/pageList")
    public PageResult<GameRoleDispose> getPageList(Integer gameId, Integer areaId, Integer siteId, Page page, String from, String to) throws Exception {
        return this.gameClosureService.getGameClosureList(page, gameId, areaId, siteId, from, to);
    }

}

*/

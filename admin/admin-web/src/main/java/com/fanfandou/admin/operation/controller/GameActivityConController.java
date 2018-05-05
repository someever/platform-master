package com.fanfandou.admin.operation.controller;


import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.activity.entity.GameActivity;
import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.activity.service.GameActivityConService;
import com.fanfandou.platform.api.activity.service.GameActivityService;
import com.fanfandou.platform.api.game.entity.GameArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * author zhongliang.
 * Description:活动开关管理操作.
 */
@RestController
@RequestMapping(value = "/operation/gameActivityCon")
public class GameActivityConController {


    @Autowired
    private GameActivityConService gameActivityConService;

    /**
     * 跳转到公告List页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/gameActivityConInit")
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/GameActivityCon");
        return mav;
    }

    /**
     * 跳转到公告List页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/gameActivityConEdit")
    public ModelAndView toEdit() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/GameActivityConEdit");
        return mav;
    }

    /**
     * 分页查询
     *
     * @param activityName .
     * @param gameId       .
     * @param page         .
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/activityConPageList")
    public PageResult getPageList(String activityName, Integer gameId, Page page, String from, String to) throws Exception {
        return this.gameActivityConService.gameActivityConPage(gameId, activityName, page, from, to);
    }

    /**
     * 根据id查询公告
     *
     * @param activityId 活动id
     * @return 公告对象
     * @throws ServiceException
     */
    @RequestMapping(value = "/gameActivityConGetById")
    public GameActivityCon getActivityConById(Integer activityId) throws ServiceException {
        return this.gameActivityConService.getGameActivityConById(activityId);
    }

    /**
     * 修改
     *
     * @param gameActivityCon 活动对象
     * @throws ServiceException
     */
    @RequestMapping(value = "/gameActivityConUpdate")
    public void update(GameActivityCon gameActivityCon, String taskJson) throws ServiceException {
        gameActivityCon.setActivityJson(taskJson);
        this.gameActivityConService.updateGameActivityCon(gameActivityCon);
    }

    /**
     * 添加
     *
     * @param gameActivityCon 活动对象
     * @throws ServiceException
     */
    @RequestMapping(value = "/gameActivityConAdd")
    public void add(GameActivityCon gameActivityCon, String taskJson, Integer lastTaskId) throws ServiceException {
        gameActivityCon.setActivityJson(taskJson);
        if (gameActivityCon.getType() == 1) {
            this.gameActivityConService.updateTaskId(lastTaskId);
        }
        this.gameActivityConService.insertGameActivityCon(gameActivityCon);
    }

    /**
     * 批量删除 （活动）
     *
     * @param ids .
     * @throws Exception
     */
    @RequestMapping(value = "/gameActivityConDelete")
    public void deleteGameActivityCon(String ids) throws Exception {
        this.gameActivityConService.deleteGameActivityCon(ids);
    }

    /**
     * 批量删除（区服）
     *
     * @throws Exception
     */
    @RequestMapping(value = "/gameActivityConDeleteArea")
    public void deleteArea(String ids, String areaIds) throws Exception {
        this.gameActivityConService.deleteGameActivityConArea(ids, areaIds);
    }

    /**
     * 添加模板
     *
     * @param gameActivityCon 活动对象
     * @throws ServiceException
     */
    @RequestMapping(value = "/templateEdit")
    public void templateAdd(GameActivityCon gameActivityCon, String taskJson) throws ServiceException {
        gameActivityCon.setActivityJson(taskJson);
        this.gameActivityConService.templateEdit(gameActivityCon);
    }

    /**
     * 更新活动任务id
     *
     * @throws Exception
     */
    @RequestMapping(value = "/updateTaskId")
    public void updateTaskId(Integer id) throws Exception {
        this.gameActivityConService.updateTaskId(id);
    }


    /**
     * 获取活动模板
     *
     * @throws Exception
     */
    @RequestMapping(value = "/getGameActivityTemplate")
    public GameActivityCon getGameActivityTemplate() throws Exception {
        return this.gameActivityConService.getGameActivityTemplate();
    }

    /**
     * 更新活动任务id
     *
     * @throws Exception
     */
    @RequestMapping(value = "/getTaskId")
    public String getTaskId() throws Exception {
        String id = this.gameActivityConService.getTaskId();
        return id;
    }

    /**
     * 获取区服集合（整合）
     *
     * @throws Exception
     */
    @RequestMapping(value = "/getAreaListByActivityId")
    public List<GameArea> getAreaListByActivityId(String ids, Integer gameId) throws Exception {
        return this.gameActivityConService.getAreaByActivityConId(ids, gameId);
    }
}

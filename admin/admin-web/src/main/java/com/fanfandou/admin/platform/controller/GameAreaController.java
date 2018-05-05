package com.fanfandou.admin.platform.controller;


import com.fanfandou.common.entity.ActStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.entity.AreaGroup;
import com.fanfandou.platform.api.game.entity.GameArea;

import com.fanfandou.platform.api.game.entity.MaintenanceStatus;
import com.fanfandou.platform.api.game.entity.WhiteList;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * author zhongliang.
 * Description:区服管理操作.
 */
@RestController
@RequestMapping(value = "/platform/gameArea")
public class GameAreaController {
    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private OperationDispatchService operationDispatchService;

    /**
     * * 跳转到分类页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/gameAreaInit")
    public ModelAndView toListMenu() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/GameAreaList");
        return mav;
    }

    /**
     * 跳转到添加页面
     *
     * @return 添加页面
     */
    @RequestMapping(value = "/gameAreaAddInit")
    public ModelAndView togameAreaAdd() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/GameAreaAdd");
        return mav;
    }


    /**
     * 跳转到添加页面
     *
     * @return 添加页面
     */
    @RequestMapping(value = "/gameAreaEditInit")
    public ModelAndView toGameAreaEdit() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/GameAreaEdit");
        return mav;
    }

    /**
     * 跳转到copy页面
     *
     * @return 添加页面
     */
    @RequestMapping(value = "/gameAreaCopyInit")
    public ModelAndView togameAreaCopy() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/GameAreaCopy");
        return mav;
    }

    /**
     * 跳转到修改页面
     *
     * @return 添加页面
     */
    @RequestMapping(value = "/gameAreaUpdateInit")
    public ModelAndView togameAreaUpdate() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/GameAreaUpdate");
        return mav;
    }

    /**
     * 分页查询
     *
     * @param gameArea 区服对象
     * @param siteId   渠道id
     * @param page     page对象
     * @return 分页对象集合
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping("/pageList")
    public PageResult<GameArea> getPageList(GameArea gameArea, Integer siteId, Page page) throws ServiceException {
        return this.gameAreaService.getGameAreasForPage(gameArea.getGameId(), siteId, page);
    }

    /**
     * 区服添加
     *
     * @param gameArea    区服对象
     * @param maintenance 服务器状态
     * @param areaGroupId 大区id
     * @throws ServiceException
     */
    @RequestMapping(value = "/insert")
    public String insert(GameArea gameArea, int maintenance, int areaGroupId, String whiteListText, boolean whiteListCheck) throws ServiceException {
        gameArea.setMaintenanceStatus(MaintenanceStatus.valueOf(maintenance));
        AreaGroup areaGroup = new AreaGroup();
        areaGroup.setId(areaGroupId);
        gameArea.setAreaGroup(areaGroup);

        WhiteList whiteList = new WhiteList();
        whiteList.setTurnOff(whiteListCheck);
        whiteList.setWhiteContent(whiteListText);


        try {
            this.gameAreaService.addCreateGameArea(gameArea, whiteList);
        } catch (ServiceException e) {
            e.printStackTrace();
            return "load.region" + e.getId();
        }
        return null;
    }

    /**
     * 批量修改
     *
     * @param batchMaintenanceStatus 服务器状态
     * @param areaTag                服务器标记
     * @param supportVersionMin      游戏版本号下限
     * @param supportVersionMax      游戏版本号上限
     * @param ids                    id集合
     * @throws ServiceException
     */
    @RequestMapping(value = "/batchUpdate")
    public void batchUpdate(int batchMaintenanceStatus, Integer areaTag, String supportVersionMin, String supportVersionMax, String ids, String whiteListText, boolean whiteListCheck, String maintenanceDesc) throws ServiceException {
        GameArea gameArea = new GameArea();
        gameArea.setMaintenanceStatus(MaintenanceStatus.valueOf(batchMaintenanceStatus));
        gameArea.setAreaTag(areaTag);
        gameArea.setSupportVersionMin(supportVersionMin);
        gameArea.setSupportVersionMax(supportVersionMax);
        gameArea.setMaintenanceDesc(maintenanceDesc);
        WhiteList whiteList = new WhiteList();
        whiteList.setWhiteContent(whiteListText);
        whiteList.setTurnOff(whiteListCheck);
        String[] idsList = ids.split(",");
        if (whiteListText != null) {
            for (int i = 0; i < idsList.length; i++) {
                gameArea.setId(Integer.parseInt(idsList[i]));
                this.gameAreaService.updUpdateGameAreaById(gameArea, whiteList, false, 2);
            }

        } else {
            for (int i = 0; i < idsList.length; i++) {
                gameArea.setId(Integer.parseInt(idsList[i]));
                this.gameAreaService.updUpdateGameAreaById(gameArea, whiteList, true, 2);
            }
        }


    }

    /**
     * 区服修改
     *
     * @param gameArea    区服对象
     * @param maintenance 服务器状态
     * @param valueChange 是否有更新
     * @throws ServiceException
     */
    @RequestMapping(value = "/update")
    public String update(GameArea gameArea,Integer valid, int maintenance, Boolean valueChange, String whiteListText, boolean whiteListCheck) throws ServiceException {
        gameArea.setMaintenanceStatus(MaintenanceStatus.valueOf(maintenance));
        gameArea.setValidStatus(ActStatus.valueOf(valid));
        WhiteList whiteList = new WhiteList();
        whiteList.setTurnOff(whiteListCheck);
        whiteList.setWhiteContent(whiteListText);

        if (valueChange) {
            try {
                this.gameAreaService.updUpdateGameAreaById(gameArea, whiteList, false, 1);
            } catch (ServiceException e) {
                e.printStackTrace();
                return "load.region" + e.getId();
            }
        }
        return null;
    }

    /**
     * 根据id查.
     *
     * @param id id
     */
    @ResponseBody
    @RequestMapping(value = "/getGameArea/{id}")
    public GameArea getGameAreaById(@PathVariable(value = "id") int id) throws ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaById(id);
        return gameArea;
    }

    /**
     * 根据游戏id查.
     *
     * @param gameId 游戏id
     */
    @ResponseBody
    @RequestMapping(value = "/getGameAreasById")
    public List<GameArea> getGameAreasByGameId(int gameId) throws ServiceException {
        List<GameArea> gameAreas = gameAreaService.getAreasListByGameId(gameId);
        return gameAreas;
    }

    /**
     * 根据游戏id查.
     *
     * @param gameId 游戏id
     */
    @ResponseBody
    @RequestMapping(value = "/getGameAreasBySiteType")
    public List<GameArea> getGameAreasByGameId(int gameId, Integer siteType) throws ServiceException {
        List<GameArea> gameAreas = gameAreaService.getAreasByGameIdSite(gameId, siteType);
        return gameAreas;
    }

    /**
     * 根据id删除.
     *
     * @param areaIds
     */
    @ResponseBody
    @RequestMapping(value = "/areaDelete")
    public void areaDelete(String areaIds) throws ServiceException {
        String[] idsList = areaIds.split(",");
        for (int i = 0; i < idsList.length; i++) {
            this.gameAreaService.delDeleteGameAreaById(Integer.parseInt(idsList[i]));
        }

    }

    /**
     * 根据id集合修改状态
     *
     * @param ids   id集合
     * @param state 状态码
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/updateState")
    public void itemState(String ids, int state) throws ServiceException {
        WhiteList whiteList = new WhiteList();
        String[] idsList = ids.split(",");
        for (int i = 0; i < idsList.length; i++) {
            GameArea gameArea = this.gameAreaService.getGameAreaById(Integer.parseInt(idsList[i]));
            gameArea.setId(Integer.parseInt(idsList[i]));
            gameArea.setValidStatus(ActStatus.valueOf(state));
//            this.operationDispatchService.shutdownServer(GameCode.getById(gameArea.getGameId()), gameArea.getId());
            this.gameAreaService.updUpdateGameAreaById(gameArea, whiteList, true, 3);

        }
    }


    /**
     * 根据id集合开服关服
     *
     * @param ids   id集合
     * @param state 状态码
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/updateServerStatus")
    public void updateServerStatus(String ids, int state) throws ServiceException {
        WhiteList whiteList = new WhiteList();
        String[] idsList = ids.split(",");
        for (int i = 0; i < idsList.length; i++) {
            GameArea gameArea = this.gameAreaService.getGameAreaById(Integer.parseInt(idsList[i]));
            gameArea.setId(Integer.parseInt(idsList[i]));
            gameArea.setMaintenanceStatus(MaintenanceStatus.valueOf(state));
//            if (state == 2) {
//                this.operationDispatchService.shutdownServer(GameCode.getById(gameArea.getGameId()), gameArea.getId());
//            }
            this.gameAreaService.updUpdateGameAreaById(gameArea, whiteList, true, 4);

        }
    }


    /**
     * 刷新区服缓存
     *
     * @throws Exception
     */
    @RequestMapping(value = "/refreshGameAreaCache")
    public void refreshGameAreaCache() throws Exception {
        this.gameAreaService.refreshGameAreaCache();
    }


    /**
     * 推送版本更新
     *
     * @throws Exception
     */
    @RequestMapping(value = "/pushGameVersion")
    public void pushGameVersion(int gameId, int gameAreaId, String resId) throws Exception {
        this.operationDispatchService.pushGameVersion(GameCode.getById(gameId), gameAreaId, resId);
    }
}

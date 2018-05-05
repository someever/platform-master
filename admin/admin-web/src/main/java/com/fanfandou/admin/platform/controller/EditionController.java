package com.fanfandou.admin.platform.controller;

import com.fanfandou.admin.api.operation.entity.DeviceType;
import com.fanfandou.admin.api.operation.entity.GamePatch;
import com.fanfandou.admin.api.operation.entity.WhiteListOp;
import com.fanfandou.admin.api.operation.service.PatchService;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * author zhongliang.
 * Description:版本补丁.
 */
@RestController
@RequestMapping(value = "/platform/edition")
public class EditionController extends BaseLogger {

    @Autowired
    private PatchService patchService;
    @Autowired
    private OperationDispatchService operationDispatchService;

    /**
     * 跳转到edition List页面
     *
     * @return list页面
     */
    @RequestMapping(value = "/editionInit")
    public ModelAndView toEditionList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/EditionList");
        return mav;
    }

    /**
     * 跳转到edition List页面
     *
     * @return list页面
     */
    @RequestMapping(value = "/editionTotalInit")
    public ModelAndView toEditionTotalList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/EditionTotalList");
        return mav;
    }

    /**
     * 跳转到edition List页面
     *
     * @return list页面
     */
    @RequestMapping(value = "/editionEditInit")
    public ModelAndView toArticleList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/platform/EditionEdit");
        return mav;
    }


    /**
     * 版本补丁添加
     *
     * @param gamePatch       补丁对象
     * @param deviceTypeView  设备
     * @param whiteListCheck  是否开启白名单
     * @param validStatusView 是否生效
     * @throws ServiceException
     */
    @RequestMapping(value = "/insert")
    public String insert(GamePatch gamePatch, int deviceTypeView, int validStatusView, int whiteListCheck) throws ServiceException {
        gamePatch.setValidStatus(ValidStatus.valueOf(validStatusView));
        gamePatch.setDeviceType(DeviceType.valueOf(deviceTypeView));
        gamePatch.setWhiteStatus(ValidStatus.valueOf(whiteListCheck));
        this.patchService.addCreateGamePatch(gamePatch);
        return null;
    }

    /**
     * 分页查询
     *
     * @param gameId      游戏id
     * @param siteId      渠道id
     * @param versionType 类型
     * @param page        page对象
     * @return 分页对象集合
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/pageList")
    public PageResult<GamePatch> getPageList(int gameId, int siteId, int versionType, Page page) throws Exception {
        return this.patchService.getPageList(gameId, siteId, versionType, page);
    }


    /**
     * 根据id删除.
     *
     * @param editionIds id集合
     */
    @ResponseBody
    @RequestMapping(value = "/editionDelete")
    public void delete(String editionIds) throws ServiceException {
        this.patchService.delPatch(editionIds);
    }


    /**
     * 根据id查询版本补丁
     *
     * @param id id.
     * @return 版本对象
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/editionSelectById")
    public GamePatch selectById(int id) throws ServiceException {
        return this.patchService.selectById(id);
    }

    /**
     * 版本补丁修改
     *
     * @param gamePatch       补丁对象
     * @param deviceTypeView  设备
     * @param whiteListCheck  是否开启白名单
     * @param validStatusView 是否生效
     * @throws ServiceException
     */
    @RequestMapping(value = "/update")
    public void update(GamePatch gamePatch, int deviceTypeView, int validStatusView, int whiteListCheck) throws ServiceException {
        gamePatch.setValidStatus(ValidStatus.valueOf(validStatusView));
        gamePatch.setDeviceType(DeviceType.valueOf(deviceTypeView));
        gamePatch.setWhiteStatus(ValidStatus.valueOf(whiteListCheck));

        this.patchService.updUpdateGamePatch(gamePatch);
    }

    /**
     * 缓存更新
     *
     * @throws ServiceException
     */
    @RequestMapping(value = "/refreshCacheAll")
    public void refreshCacheAll() throws ServiceException {
        this.patchService.refreshCacheAll();
    }

    /**
     * 推送版本号
     *
     * @throws ServiceException
     */
    @RequestMapping(value = "/pushGameVersion")
    public void pushGameVersion(int gameId, int areaId, String resId) throws ServiceException {
        this.operationDispatchService.pushGameVersion(GameCode.getById(gameId), areaId, resId);
    }
}

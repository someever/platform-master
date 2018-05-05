package com.fanfandou.admin.server.operation.serviceImpl;

import com.fanfandou.admin.api.operation.entity.*;
import com.fanfandou.admin.api.operation.service.PatchService;
import com.fanfandou.admin.server.operation.dao.GamePatchMapper;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.platform.api.game.service.GameAreaService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wudi.
 * Descreption:补丁相关方法实现.
 * Date:2017/2/23
 */
@Service("patchService")
public class PatchServiceImpl extends BaseLogger implements PatchService {

    @Autowired
    private GamePatchMapper gamePatchMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;

    @Autowired
    private GameAreaService gameAreaService;

    @Override
    public GameVersionCheck checkGameVersion(DeviceType deviceType, GameCode gameCode, int siteId, int appVersion,
                                             int resourceVersion, String ip) throws ServiceException {
        GameVersionCheck gameVersionCheck = new GameVersionCheck();
        GameVersionCheck gameVersionCheck1 = new GameVersionCheck();
        //先区分游戏和渠道，以及白名单IP
        //1，根据游戏和渠道，设备号获取所有补丁信息
        List<GamePatch> gamePatches = getPatchList(deviceType, gameCode, siteId, resourceVersion);

        if (!CollectionUtils.isEmpty(gamePatches)) {
            //2,过滤白名单.
            for (GamePatch gamePatch : gamePatches) {
                boolean returnValue = false;
                if (gamePatch.getWhiteStatus().equals(ValidStatus.VALID)) {
                    if (StringUtils.isEmpty(gamePatch.getWhiteContent()) || gamePatch.getWhiteContent().contains(ip)) {
                        returnValue = true;
                    }
                } else {
                    returnValue = true;
                }
                if (returnValue) {

                    if (gamePatch.getVersionType() == 1 && gamePatch.getPatchVersion() > appVersion) {
                        logger.info("大版本需要更新" + appVersion);
                        GameUpdateConfig gameUpdateConfig = new GameUpdateConfig();
                        gameUpdateConfig.setUpdateFileName(gamePatch.getPatchName());
                        gameUpdateConfig.setUpdatePackageUrl(gamePatch.getPatchUrl());
                        gameUpdateConfig.setUpdatePackageSize(gamePatch.getPatchSize());
                        gameVersionCheck.setAppNeedUpdate(true);
                        gameVersionCheck.setAppVerCode(gamePatch.getPatchVersion());
                        gameVersionCheck.setAppVerName(gamePatch.getPatchName());
                        gameVersionCheck.setAppVerDesc(gamePatch.getPatchDesc());
                        gameVersionCheck.setAppUpdateConfig(gameUpdateConfig);
                    }

                    if (gamePatch.getVersionType() == 2 && gamePatch.getPatchVersion() > resourceVersion) {
                        if (appVersion <= 107 && gamePatch.getPatchVersion() >= 1300) {
                            if ( siteId == 63 || siteId == 64 ) {
                                continue;
                            }
                        }
                        if (siteId == 112 || siteId == 111) {
                            continue;
                        }
                        logger.info("资源版本需要更新" + resourceVersion);
                        GameUpdateConfig gameUpdateConfig = new GameUpdateConfig();

                        gameUpdateConfig.setUpdateFileName(gamePatch.getPatchName());
                        gameUpdateConfig.setUpdatePackageUrl(gamePatch.getPatchUrl());
                        gameUpdateConfig.setUpdatePackageSize(gamePatch.getPatchSize());
                        gameVersionCheck.setResourceNeedUpdate(true);
                        gameVersionCheck.setResourceVerName(gamePatch.getPatchName());
                        gameVersionCheck.getResourceUpdateConfigs().add(gameUpdateConfig);
                    }
                }
            }
        }
        return gameVersionCheck;
    }


    /**
     * 刷新缓存.
     */
    public void refreshCache(DeviceType deviceType, GameCode gameCode, int siteId,
                             GamePatch gamePatch) throws ServiceException {
        cacheService.hPut(PublicCachedKeyConstant.GAME_PATCH_UPDATE + gameCode.getId() + siteId
                + deviceType.getId(), gamePatch.getId() + "", gamePatch);
    }

    /**
     * 清除缓存.
     */
    public void cleanCache(DeviceType deviceType, GameCode gameCode, int siteId, int patchId) {
        cacheService.hDel(PublicCachedKeyConstant.GAME_PATCH_UPDATE + gameCode.getId() + siteId
                + deviceType.getId(), patchId + "");
    }

    /**
     * 获取需更新的补丁信息.
     */
    @SuppressWarnings("unchecked")
    public List<GamePatch> getPatchList(DeviceType deviceType, GameCode gameCode, int siteId,
                                        int resourceVersion) {
        /*List<GamePatch> gamePatches = cacheService.hGetValues(PublicCachedKeyConstant.GAME_PATCH_UPDATE
                + gameCode.getId() + siteId + deviceType.getId(), GamePatch.class);
        if (CollectionUtils.isEmpty(gamePatches)) {
            gamePatches = cacheService.hGetValues(PublicCachedKeyConstant.GAME_PATCH_UPDATE
                    + gameCode.getId() + 0 + deviceType.getId(), GamePatch.class);
        }*/
        List<GamePatch> clientPatches = new ArrayList<>();
        List<GamePatch> gamePatches = null;
        if (CollectionUtils.isEmpty(gamePatches)) {
            logger.info("从数据库中获取补丁数据");
            Map map = new HashMap();
            map.put("deviceType", deviceType.getId());
            gamePatches = gamePatchMapper.selectByDeviceType(map);
            for (GamePatch gamePatch : gamePatches) {
                if (gamePatch.getSiteId() == 0 || gamePatch.getSiteId() == siteId) {
                    cacheService.hPut(PublicCachedKeyConstant.GAME_PATCH_UPDATE + gameCode.getId() + siteId
                            + deviceType.getId(), gamePatch.getId() + "", gamePatch);
                    clientPatches.add(gamePatch);
                }
            }
        } else {
            logger.info("从缓存中获取补丁数据");
            clientPatches.addAll(gamePatches);
        }
        return clientPatches;
    }


    //更新白名单
    public void updateWhiteList(long id, WhiteListOp whiteListOp) throws ServiceException {
        cacheService.put(PublicCachedKeyConstant.GAME_PATCH_WHITE_LIST + id, whiteListOp);
    }

    //获取白名单
    public WhiteListOp getWhiteList(long id) throws ServiceException {
        return cacheService.get(PublicCachedKeyConstant.GAME_PATCH_WHITE_LIST + id, WhiteListOp.class);
    }


    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    public void addCreateGamePatch(GamePatch gamePatch) throws ServiceException {
        gamePatch.setId(tableSequenceGenerator.generate(PublicCachedKeyConstant.PLATFORM_GAME_PATCH));
        if (gamePatch.getSiteId() == null) {
            gamePatch.setSiteId(0);
        }
//        gamePatch.setWhiteContent(whiteListOp.getWhiteContent());
        gamePatchMapper.insert(gamePatch);
        //刷新缓存
        refreshCache(gamePatch.getDeviceType(), GameCode.getById(gamePatch.getGameId()), gamePatch.getSiteId(), gamePatch);

//        //更新白名单
//        updateWhiteList(gamePatch.getId(), whiteListOp);
    }


    public PageResult<GamePatch> getPageList(int gameId, int siteId, int versionType, Page page) throws Exception {
        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }

        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("gameId", gameId);
        paramMap.put("siteId", siteId);
        paramMap.put("versionType", versionType);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        List<GamePatch> gamePatchList = this.gamePatchMapper.pageList(paramMap);
        Map map = new HashMap();
        map.put("gameId", gameId);
        map.put("siteId", siteId);
        map.put("versionType", versionType);
        int totalCount = this.gamePatchMapper.totalCount(map);
        page.setTotalCount(totalCount);
        PageResult<GamePatch> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(gamePatchList);
        return pageResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void delPatch(String patchIds) throws ServiceException {
        String[] patchs = patchIds.split(",");
        List<Integer> idList = new ArrayList<Integer>();
        for (int i = 0; i < patchs.length; i++) {
            idList.add(Integer.parseInt(patchs[i]));
        }
        for (int i = 0; i < idList.size(); i++) {
            int id = idList.get(i);
            GamePatch gamePatch = this.gamePatchMapper.selectById(id);
            //清除缓存
            cleanCache(gamePatch.getDeviceType(), GameCode.getById(gamePatch.getGameId()), gamePatch.getSiteId(), id);
            this.gamePatchMapper.delete(id);
            WhiteListOp whiteListOp = new WhiteListOp();
            //更新白名单
            updateWhiteList(gamePatch.getId(), whiteListOp);
        }

    }

    public GamePatch selectById(int id) throws ServiceException {
        GamePatch gamePatch = this.gamePatchMapper.selectById(id);
        long idl = (long) id;
        WhiteListOp whiteListOp = getWhiteList(idl);
        gamePatch.setWhiteListOp(whiteListOp);
        return gamePatch;
    }

    @Override
    public void updUpdateGamePatch(GamePatch gamePatch) throws ServiceException {
        if (gamePatch.getSiteId() == null) {
            gamePatch.setSiteId(0);
        }
        this.gamePatchMapper.updateByPrimaryKeySelective(gamePatch);
        //刷新缓存
        refreshCache(gamePatch.getDeviceType(), GameCode.getById(gamePatch.getGameId()), gamePatch.getSiteId(), gamePatch);
//        //更新白名单
//        updateWhiteList(gamePatch.getId(), whiteListOp);
    }

    @Override
    public Integer getPatchVersion(int gameId) throws ServiceException {
        Integer version = gamePatchMapper.getGameVersion(gameId);
        logger.info("当前推送的版本号为 : " + version);
        return version;
    }

    @Override
    public void refreshCacheAll() throws ServiceException {
        List<GamePatch> gamePatchList = this.gamePatchMapper.getPatchAll();
        for (int i = 0; i < gamePatchList.size(); i++) {
            refreshCache(gamePatchList.get(i).getDeviceType(), GameCode.getById(gamePatchList.get(i).getGameId()), gamePatchList.get(i).getSiteId(), gamePatchList.get(i));
        }
    }
}

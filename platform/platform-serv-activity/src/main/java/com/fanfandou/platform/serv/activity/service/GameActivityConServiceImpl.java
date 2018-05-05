package com.fanfandou.platform.serv.activity.service;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.activity.service.GameActivityConService;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.serv.activity.dao.GameActivityConMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhongliang.
 * Descreption:游戏活动实现.
 * Date:2016/11/22
 */
@Service("gameActivityConService")
public class GameActivityConServiceImpl extends BaseLogger implements GameActivityConService {
    @Autowired
    GameActivityConMapper gameActivityConMapper;
    @Autowired
    OperationDispatchService operationDispatchService;
    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;
    @Autowired
    private GameAreaService gameAreaService;
    @Autowired
    private CacheService cacheService;

    //更新渠道类型
    public void updateActivityConChannelType(int conId, int typeId) throws ServiceException {
        cacheService.put(PublicCachedKeyConstant.GAME_ACTIVITY_CON_CHANNELTYPE + conId, typeId + "");
    }

    //获取渠道类型
    public Integer getActivityConChannelType(int conId) throws ServiceException {
        String id = cacheService.get(PublicCachedKeyConstant.GAME_ACTIVITY_CON_CHANNELTYPE + conId, String.class);
        return Integer.parseInt(id);
    }


    @Override
    public void insertGameActivityCon(GameActivityCon gameActivityCon) throws ServiceException {
        gameActivityCon.setCreateTime(new Date());
        gameActivityCon.setId((int) tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_ACTIVITYCON) + 10000);
        this.gameActivityConMapper.insertSelective(gameActivityCon);
        if (gameActivityCon.getChannelType() == null) {
            gameActivityCon.setChannelType(-1);
        }
        updateActivityConChannelType(gameActivityCon.getId(), gameActivityCon.getChannelType());//更新渠道类型
        String[] idsList = gameActivityCon.getAreaId().split(",");
        for (int i = 0; i < idsList.length; i++) {
            if (idsList[i] != null && !idsList[i].equals("")) {
                operationDispatchService.gameActivityController(GameCode.getById(gameActivityCon.getGameId()), Integer.parseInt(idsList[i]), gameActivityCon);
            }

        }

    }

    @Override
    public void templateEdit(GameActivityCon gameActivityCon) throws ServiceException {
        gameActivityCon.setCreateTime(new Date());
        gameActivityCon.setId((int) tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_ACTIVITYCON) + 10000);
        if (gameActivityCon.getChannelType() == null) {
            gameActivityCon.setChannelType(-1);
        }

        cacheService.put(PublicCachedKeyConstant.GAME_ACTIVITY_CON_CHANNELTYPE + PublicCachedKeyConstant.GAME_ACTIVITY_CON_TEMPLATE_CHANNELTYPE, gameActivityCon.getChannelType() + "");
        cacheService.put(PublicCachedKeyConstant.GAME_ACTIVITY_CON_TEMPLATE, gameActivityCon);
    }

    @Override
    public GameActivityCon getGameActivityTemplate() throws ServiceException {
        GameActivityCon gameActivityCon = cacheService.get(PublicCachedKeyConstant.GAME_ACTIVITY_CON_TEMPLATE, GameActivityCon.class);
        gameActivityCon.setChannelType(Integer.parseInt(cacheService.get(PublicCachedKeyConstant.GAME_ACTIVITY_CON_CHANNELTYPE + PublicCachedKeyConstant.GAME_ACTIVITY_CON_TEMPLATE_CHANNELTYPE, String.class)));
        return gameActivityCon;
    }

    @Override
    public void updateGameActivityCon(GameActivityCon gameActivityCon) throws ServiceException {
        this.gameActivityConMapper.updateByPrimaryKey(gameActivityCon);
        if (gameActivityCon.getChannelType() == null) {
            gameActivityCon.setChannelType(-1);
        }
        updateActivityConChannelType(gameActivityCon.getId(), gameActivityCon.getChannelType());//更新渠道类型
        String[] idsList = gameActivityCon.getAreaId().split(",");
        for (int i = 0; i < idsList.length; i++) {
            operationDispatchService.gameActivityController(GameCode.getById(gameActivityCon.getGameId()), Integer.parseInt(idsList[i]), gameActivityCon);
        }
    }

    @Override
    public void deleteGameActivityCon(String ids) throws ServiceException {
        String[] idsList = ids.split(",");
        for (int i = 0; i < idsList.length; i++) {
            GameActivityCon gameActivityCon = this.gameActivityConMapper.selectByPrimaryKey(Integer.parseInt(idsList[i]));
            String[] areaList = gameActivityCon.getAreaId().split(",");
            for (int j = 0; j < areaList.length; j++) {
                operationDispatchService.delGameActivityController(GameCode.getById(gameActivityCon.getGameId()), Integer.parseInt(areaList[j]), gameActivityCon.getId(), gameActivityCon.getType());
            }
            this.gameActivityConMapper.deleteByPrimaryKey(Integer.parseInt(idsList[i]));
            cacheService.del(PublicCachedKeyConstant.GAME_ACTIVITY_CON_CHANNELTYPE + Integer.parseInt(idsList[i]));//删除渠道类型
        }
    }

    @Override
    public void deleteGameActivityConArea(String ids, String areaIds) throws ServiceException {
        String[] idsList = ids.split(",");
        String[] areaList = areaIds.split(",");
        for (int i = 0; i < idsList.length; i++) {
            GameActivityCon gameActivityCon = this.gameActivityConMapper.selectByPrimaryKey(Integer.parseInt(idsList[i]));
            for (int j = 0; j < areaList.length; j++) {
                String pdArea = "," + gameActivityCon.getAreaId();
                String theArea = "," + areaList[j] + ",";
                if (pdArea.contains(theArea)) {
                    operationDispatchService.delGameActivityController(GameCode.getById(gameActivityCon.getGameId()), Integer.parseInt(areaList[j]), gameActivityCon.getId(), gameActivityCon.getType());
                    String area = pdArea.replace(theArea, ",");
                    gameActivityCon.setAreaId(area.substring(1, area.length()));
                    this.gameActivityConMapper.updateByPrimaryKey(gameActivityCon);
                }
            }
        }
    }

    @Override
    public PageResult gameActivityConPage(Integer gameId, String activityName, Page page, String from, String to) throws Exception {
        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (gameId == -1) {
            gameId = null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = null;
        Date toDate = null;
        if (from == null) {
            from = "1900-01-01 00:00:00";
            fromDate = sdf.parse(from);
        } else {
            fromDate = sdf.parse(from);
        }

        if (to == null) {
            toDate = new Date();
        } else {
            toDate = sdf.parse(to);
        }

        String codeNameStr = activityName;
        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("activityName", codeNameStr);
        paramMap.put("gameId", gameId);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        paramMap.put("from", fromDate);
        paramMap.put("to", toDate);
        List<GameActivityCon> gameActivityConList = this.gameActivityConMapper.selectBySelective(paramMap);

        Map map = new HashMap();
        map.put("activityName", codeNameStr);
        map.put("gameId", gameId);
        map.put("from", fromDate);
        map.put("to", toDate);
        int totalCount = this.gameActivityConMapper.totalCount(map);

        page.setTotalCount(totalCount);
        PageResult<GameActivityCon> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(gameActivityConList);
        return pageResult;
    }

    @Override
    public GameActivityCon getGameActivityConById(int id) throws ServiceException {
        GameActivityCon gameActivityCon = this.gameActivityConMapper.selectByPrimaryKey(id);
        gameActivityCon.setChannelType(getActivityConChannelType(gameActivityCon.getId()));
        return gameActivityCon;
    }

    @Override
    public void updateTaskId(Integer id) throws ServiceException {
        cacheService.put(PublicCachedKeyConstant.GAME_ACTIVITY_CON_TASKID, id.toString());
    }

    @Override
    public String getTaskId() throws ServiceException {
        String taskId = cacheService.get(PublicCachedKeyConstant.GAME_ACTIVITY_CON_TASKID, String.class);
        if (taskId == null) {
            taskId = "100000";
        }
        return taskId;
    }

    @Override
    public List<GameArea> getAreaByActivityConId(String ids, Integer gameId) throws ServiceException {
        List<GameArea> areaList = new ArrayList<>();
        List<String> areaIds = new ArrayList<>();
        String[] idsList = ids.split(",");
        for (int i = 0; i < idsList.length; i++) {
            GameActivityCon gameActivityCon = this.gameActivityConMapper.selectByPrimaryKey(Integer.parseInt(idsList[i]));
            String[] areas = gameActivityCon.getAreaId().split(",");
            for (int j = 0; j < areas.length; j++) {
                areaIds.add(areas[j]);
            }
        }
        logger.info("areaIds >>> areaIds = " + areaIds);
        Set set = new HashSet(areaIds);
        String[] aId = (String[]) set.toArray(new String[0]);
        List<GameArea> gameAreas = gameAreaService.getAreasListByGameId(gameId);
        logger.info("aId >>> aId = " + aId);
        logger.info("gameAreas >>> gameAreas = " + gameAreas);
        for (int i = 0; i < gameAreas.size(); i++) {
            for (int j = 0; j < aId.length; j++) {
                Integer thisId = 0;
                if (!StringUtils.isEmpty(aId[j])) {
                    thisId = Integer.parseInt(aId[j]);
                }
                logger.info("thisId >>> thisId = " + thisId + ", gameAreaId = " + gameAreas.get(i).getId());
                if (gameAreas.get(i).getId().equals(thisId) ) {
                    areaList.add(gameAreas.get(i));
                }
            }
        }
        logger.info("areaList >>> areaList = " + areaList.size());
        return areaList;
    }
}

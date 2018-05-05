package com.fanfandou.platform.serv.game.service;

import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameClosureService;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.platform.api.game.entity.GameRoleDispose;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.serv.game.dao.GameRoleDisposeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhongliang on 2016/3/15.
 * Description service实现类
 */


@Service("gameClosureService")
public class GameClosureServiceImpl extends BaseLogger implements GameClosureService {

    @Autowired
    private GameRoleDisposeMapper gameRoleDisposeMapper;
    @Autowired
    private GameRoleService gameRoleService;
    @Autowired
    private OperationDispatchService operationDispatchService;
    @Autowired
    private GameAreaService gameAreaService;


    @Override
    public PageResult<GameRoleDispose> getGameClosureList(Page page, Integer gameId, Integer siteId, Integer roleId, Integer roleStatus, String roleName) throws Exception {
        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (roleId != null && roleId == 0) {
            roleId = null;
        }
        if (roleId == null && roleName != null && !roleName.equals("")) {
            GameRole gameRole = gameRoleService.getRoleByRoleName(GameCode.getById(gameId), roleName);
            Long id = gameRole.getRoleId();
            roleId = new Long(id).intValue();
        }
        int num1 = (page.getPageIndex() - 1) * page.getPageSize();
        Map paramMap = new HashMap();
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        paramMap.put("gameId", gameId);
        paramMap.put("siteId", siteId);
        paramMap.put("roleId", roleId);
        paramMap.put("roleStatus", roleStatus);
        List<GameRoleDispose> pageList = this.gameRoleDisposeMapper.closurePageList(paramMap);

        for (int i = 0; i < pageList.size(); i++) {
            GameArea gameArea = gameAreaService.getGameAreaById(pageList.get(i).getAreaId());
            if (gameArea != null) {
                pageList.get(i).setAreaName(gameArea.getAreaName());
            } else {
                pageList.get(i).setAreaName("无区服信息");
            }

        }

        Map map = new HashMap();
        map.put("str1", page.getOrder());
        map.put("str2", page.getSort());
        map.put("num1", num1);
        map.put("num2", page.getPageSize());
        map.put("gameId", gameId);
        map.put("siteId", siteId);
        map.put("roleId", roleId);
        map.put("roleStatus", roleStatus);
        int totalCount = this.gameRoleDisposeMapper.closureCount(map);

        page.setTotalCount(totalCount);
        PageResult<GameRoleDispose> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(pageList);
        return pageResult;
    }

    @Override
    public void insertGameClosure(GameRoleDispose gameRoleDispose) throws ServiceException {
        GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(gameRoleDispose.getGameId()), gameRoleDispose.getRoleId());
        gameRoleDispose.setUserId(gameRole.getUserId());
        gameRoleDispose.setSiteId(gameRole.getSiteId());
        gameRoleDispose.setAreaId(gameRole.getAreaId());
        gameRoleDispose.setCreateTime(new Date());
        gameRoleDispose.setDisablesendmsgTime(new Date());
        operationDispatchService.banAccount(GameCode.getById(gameRoleDispose.getGameId()), gameRoleDispose.getAreaId(), gameRoleDispose.getRoleId(), gameRoleDispose.getClosureTime(), gameRoleDispose.getClosureReason(), gameRoleDispose.getRoleStatus(), 1);
        this.gameRoleDisposeMapper.insert(gameRoleDispose);
    }

    @Override
    public void updateGameClosure(int gameClosureId, int roleStatus) throws ServiceException {
        GameRoleDispose gameRoleDispose = gameRoleDisposeMapper.selectByPrimaryKey((long) gameClosureId);
        int status = gameRoleDispose.getRoleStatus();
        gameRoleDispose.setRoleStatus(roleStatus);
        gameRoleDisposeMapper.updateByPrimaryKey(gameRoleDispose);
        operationDispatchService.banAccount(GameCode.getById(gameRoleDispose.getGameId()), gameRoleDispose.getAreaId(), gameRoleDispose.getRoleId(), gameRoleDispose.getClosureTime(), gameRoleDispose.getClosureReason(), status, 2);
    }
}

package com.fanfandou.platform.api.game.service;

import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.entity.GameRoleInfoView;
import com.fanfandou.platform.api.game.entity.GameRoleInfos;

import java.util.Date;
import java.util.List;

/**
 * Description: 提供游戏角色相关操作.
 * <p/>
 * Date: 2016-08-10 14:59.
 * author: Arvin.
 */
public interface GameRoleService {

    GameRole createRole(GameRole gameRole, int siteId, GameCode gameCode) throws ServiceException;

    void updateRole(GameRole gameRole);

    GameRole getRoleById(GameCode gameCode, long roleId);

    GameRole getRoleByUserId(GameCode gameCode, int areaId, long userId);

    GameRole getRoleByUserId(GameCode gameCode, long userId);

    GameRole getRoleByName(GameCode gameCode, int areaId, String roleName);

    List<GameRole> getRoleList(GameCode gameCode, long userId);

    GameRole getGameRole(int gameId, int areaId, int userId, int roleId, String roleName);

    List<GameRole> getGameRoleByArea(int gameId, int areaId) throws ServiceException;

    GameRoleInfoView getGameRoleView(GameCode gameCode, int areaId, int roleId, int infoType, int value) throws ServiceException;

    GameRole getRoleByRoleName(GameCode gameCode, String roleName);

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return 对象
     * @throws ServiceException
     */
    PageResult gameRolePage(Page page, Integer gameId, Integer siteId, Integer roleId, String roleName,Integer areaId) throws Exception;

    GameRole gameRoleById(int id) throws ServiceException;

    /**
     * 封测返利.
     */
    void returnDoubleMoney(int areaId, long roleId) throws ServiceException;

}

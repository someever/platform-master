

package com.fanfandou.platform.api.game.service;

import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.entity.GameRoleDispose;


/**
 * Created by zhongliang on 2016/3/15.
 * Description 玩家封停service接口
 */


public interface GameClosureService {


    /**
     * @param gameId 游戏id
     * @param siteId 渠道id
     * @return 集合
     * @throws Exception
     */


    PageResult<GameRoleDispose> getGameClosureList(Page page, Integer gameId, Integer siteId, Integer roleId, Integer roleStatus,String roleName) throws Exception;

    /**
     * 修改
     *
     * @param gameRoleDispose 对象
     */
    void insertGameClosure(GameRoleDispose gameRoleDispose) throws ServiceException;

    void updateGameClosure(int gameClosureId, int roleStatus) throws ServiceException;
}




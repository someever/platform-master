package com.fanfandou.platform.api.activity.service;

import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.game.entity.GameArea;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by zhongliang.
 * Descreption:游戏活动服务.
 * Date:2016/11/22
 */
public interface GameActivityConService {

    /**
     * 新增活动.
     *
     * @param gameActivityCon 活动实体.
     */
    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    void insertGameActivityCon(GameActivityCon gameActivityCon) throws ServiceException;

    /**
     * 活动模板编辑
     *
     * @param gameActivityCon 活动
     * @throws ServiceException
     */
    void templateEdit(GameActivityCon gameActivityCon) throws ServiceException;

    /**
     * 活动模板获取
     *
     * @param id id
     * @return 活动模板
     * @throws ServiceException
     */
    GameActivityCon getGameActivityTemplate() throws ServiceException;

    /**
     * 修改活动.
     *
     * @param gameActivityCon 活动.
     * @throws ServiceException
     */
    void updateGameActivityCon(GameActivityCon gameActivityCon) throws ServiceException;

    /**
     * 删除活动
     *
     * @param ids 活动id集合.
     * @throws ServiceException
     */
    void deleteGameActivityCon(String ids) throws ServiceException;

    /**
     * 删除活动区服
     *
     * @param ids     活动id集合.
     * @param areaIds 区服id集合.
     * @throws ServiceException
     */
    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    void deleteGameActivityConArea(String ids, String areaIds) throws ServiceException;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return 对象
     * @throws ServiceException
     */
    PageResult gameActivityConPage(Integer gameId, String activityName, Page page, String from, String to) throws Exception;

    /**
     * 根据id获取活动
     *
     * @param id id
     * @return 活动
     * @throws ServiceException
     */
    GameActivityCon getGameActivityConById(int id) throws ServiceException;

    /**
     * 更新活动任务最新id
     *
     * @param id
     * @throws ServiceException
     */
    void updateTaskId(Integer id) throws ServiceException;

    /**
     * 获取活动最新任务id
     *
     * @return id
     */
    String getTaskId() throws ServiceException;

    List<GameArea> getAreaByActivityConId(String ids,Integer gameId) throws ServiceException;
}

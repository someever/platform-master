package com.fanfandou.platform.serv.game.dao;

import java.util.List;
import java.util.Map;

import com.fanfandou.platform.api.game.entity.GameRoleDispose;
import com.fanfandou.platform.api.game.entity.GameRoleDisposeExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoleDisposeMapper {
    int countByExample(GameRoleDisposeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GameRoleDispose record);

    int insertSelective(GameRoleDispose record);

    List<GameRoleDispose> selectByExample(GameRoleDisposeExample example);

    GameRoleDispose selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GameRoleDispose record, @Param("example") GameRoleDisposeExample example);

    int updateByExample(@Param("record") GameRoleDispose record, @Param("example") GameRoleDisposeExample example);

    int updateByPrimaryKeySelective(GameRoleDispose record);

    int updateByPrimaryKey(GameRoleDispose record);

    List<GameRoleDispose> closurePageList(Map paramMap);


/**
     * 分页总数.
     */

    int closureCount(Map map);
}

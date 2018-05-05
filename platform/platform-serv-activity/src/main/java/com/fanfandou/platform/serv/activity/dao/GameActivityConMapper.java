package com.fanfandou.platform.serv.activity.dao;


import com.fanfandou.platform.api.activity.entity.GameActivityCon;
import com.fanfandou.platform.api.activity.entity.GameActivityConExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GameActivityConMapper {
    int countByExample(GameActivityConExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GameActivityCon record);

    int insertSelective(GameActivityCon record);

    List<GameActivityCon> selectByExampleWithBLOBs(GameActivityConExample example);

    List<GameActivityCon> selectByExample(GameActivityConExample example);

    GameActivityCon selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GameActivityCon record, @Param("example") GameActivityConExample example);

    int updateByExampleWithBLOBs(@Param("record") GameActivityCon record, @Param("example") GameActivityConExample example);

    int updateByExample(@Param("record") GameActivityCon record, @Param("example") GameActivityConExample example);

    int updateByPrimaryKeySelective(GameActivityCon record);

    int updateByPrimaryKeyWithBLOBs(GameActivityCon record);

    int updateByPrimaryKey(GameActivityCon record);

    int totalCount(Map map);

    /**
     * 订单分页.
     */
    List<GameActivityCon> selectBySelective (Map paramMap);


}
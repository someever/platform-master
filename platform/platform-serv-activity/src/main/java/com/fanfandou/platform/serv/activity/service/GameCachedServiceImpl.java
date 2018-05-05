
package com.fanfandou.platform.serv.activity.service;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.common.util.SpringUtils;
import com.fanfandou.platform.api.activity.service.GameCachedService;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.service.GameRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by wudi.
 * Descreption:处理缓存实现.
 * Date:2017/8/3
 */

@Service("gameCachedService")
public class GameCachedServiceImpl extends BaseLogger implements GameCachedService, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private ThreadPoolTaskExecutor cacheExecutor;

    private static final long PINT_PERIOD = 10L;

    public GameCachedServiceImpl() {
        logger.info("构造方法开始加载缓存数据到数据库");
        if (cacheExecutor == null) {
            cacheExecutor = (ThreadPoolTaskExecutor) SpringUtils.getBean("cacheExecutor");
        }
        dealGameRoleCache();
    }

    @Override
    public void dealGameRoleCache() {

        cacheExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int exeSize = 1000;

                    List<GameRole> gameRoleList = cacheService.hGetValues(IcachedConstant.GAME_ROLE_LIST + 27, GameRole.class);
                    if (gameRoleList.size() < 1000) {
                        exeSize = gameRoleList.size();
                    }
                    long roleIdss = 0;
                            logger.info("开始执行缓存插入数据库操作，还剩余数据 ：{}", gameRoleList.size());
                    try {
                        Thread.sleep(10000);
                        if (!CollectionUtils.isEmpty(gameRoleList)) {
                            for (int i = 0; i < exeSize; i++) {
                                GameRole gameRole = gameRoleList.get(i);
                                if (gameRole == null) {
                                    continue;
                                }
                                roleIdss = gameRole.getRoleId();
                                gameRoleService.updateRole(gameRole);
                                cacheService.hDel(IcachedConstant.GAME_ROLE_LIST + 27, gameRole.getRoleId() + "");
                            }
                        } else {
                            logger.info("暂时没有缓存数据获取");
                        }
                    } catch (Exception e) {
                        cacheService.hDel(IcachedConstant.GAME_ROLE_LIST + 27, roleIdss + "");
                        logger.info("插入失败啦，终止循环: " + e.getMessage());
                    }
                }
            }
        });



        ScheduledExecutorService pingSchedule = Executors
                .newScheduledThreadPool(2);
        pingSchedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        }, PINT_PERIOD, PINT_PERIOD, TimeUnit.SECONDS);

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("开始加载缓存数据到数据库");
        dealGameRoleCache();
    }
}


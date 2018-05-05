package com.fanfandou.platform.web.activity.service;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.resource.SiteCode;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.activity.entity.GameActivity;
import com.fanfandou.platform.api.activity.service.GameActivityService;
import com.fanfandou.platform.api.activity.service.PromoteCodeBatchService;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.user.entity.UserAccount;
import com.fanfandou.platform.api.user.service.AccountService;
import com.fanfandou.platform.web.activity.vo.GameActivityVo;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wudi.
 * Descreption:游戏活动.
 * Date:2016/11/23
 */
@Service
public class GameActivityServiceClient extends BaseLogger {

    @Autowired
    private GameActivityService gameActivityService;

    @Autowired
    private PromoteCodeBatchService promoteCodeBatchService;

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private AccountService accountService;

    /**
     * 获取活动列表.
     */
    public List<GameActivityVo> getGameActivityList(int gameId, int siteId, String areaCode) throws ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode);
        List<GameActivity> gameActivities = gameActivityService.getGameActivityList(gameId);
        List<GameActivityVo> gameActivityVos = new ArrayList<>();
        for (GameActivity gameActivity : gameActivities) {
                if (gameActivity.getValidStatus().getId() == ValidStatus.REMOVED.getId()) {
                    continue;
                }
            if ((gameActivity.getSiteIds().contains("," + siteId + ",") || SiteCode.getById(siteId)
                    .isParent(SiteCode.getById(siteId)) || StringUtils.isEmpty(gameActivity.getSiteIds()))
                    && (gameActivity.getAreaIds().contains("," + gameArea.getId() + ","))) {
                GameActivityVo gameActivityVo = new GameActivityVo();
                gameActivityVo.setActivityContent(gameActivity.getActivityContent());
                gameActivityVo.setActivityId(gameActivity.getActivityId());
                gameActivityVo.setStartTime(transDate(gameActivity.getStartTime()));
                gameActivityVo.setEndTime(transDate(gameActivity.getEndTime()));
                gameActivityVo.setId(gameActivity.getId().intValue());
                gameActivityVo.setValidStatus(gameActivity.getValidStatus().getId());
                gameActivityVos.add(gameActivityVo);
            }
        }
        return gameActivityVos;
    }

    public String transDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取单个活动内容.
     */
    public GameActivityVo getGameActivity(int gameId, int id) throws ServiceException {
        GameActivity gameActivity = gameActivityService.getGameActivityContent(gameId, id);
        GameActivityVo gameActivityVo = new GameActivityVo();
        gameActivityVo.setActivityContent(gameActivity.getActivityContent());
        gameActivityVo.setActivityId(gameActivity.getActivityId());
        gameActivityVo.setEndTime(transDate(gameActivity.getEndTime()));
        gameActivityVo.setStartTime(transDate(gameActivity.getStartTime()));
        gameActivityVo.setId(gameActivity.getId().intValue());
        gameActivityVo.setValidStatus(gameActivity.getValidStatus().getId());
        return gameActivityVo;
    }

    /**
     * 通知游戏服活动信息.
     */
    public void sendActivityTask(int gameId, int siteId, long userId, String areaCode, int id)
            throws ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode);
        gameActivityService.sendActivityTask(gameId, siteId, userId, gameArea.getId(),Integer.parseInt(areaCode)
                , id);
    }

    /**
     * 通知游戏服活动结算.
     */
    public void settleActivityTask(int gameId, int siteId, long userId, String areaCode, int id, int taskId)
            throws ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode);
        gameActivityService.settleActivityTask(gameId, siteId, userId, gameArea.getId(),Integer.parseInt(areaCode), id, taskId);
    }

    /**
     * 激活码激活.
     */
    public String checkActiveCode(int gameId, int siteId, String areaCode, int roleId, long userId, String code, String channel) throws ServiceException {
        return promoteCodeBatchService.checkPromoteCode(gameId, siteId, areaCode, roleId,userId, code, channel);
    }

    public void lytRecoder(int gameId, String areaCode, int roleId, String code, String channel) {
        try {
            String appId = "20000009";
            String appKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";
            String checkUrl = "http://swglxh2.game.75wan.cn:30064/gift";
            GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(gameId), roleId);
            List<NameValuePair> lytParams = new ArrayList<NameValuePair>();
            long userId = gameRole.getUserId();
            long time = new Date().getTime();
            List<UserAccount> accountEntity = accountService.getAccountsByUid(userId);
            String account  = roleId + "";
            String gifgName = "渠道礼包";
            String level = "1";
            if (!CollectionUtils.isEmpty(accountEntity)) {
                logger.error("乐盈统计查到改用户");
                account = accountEntity.get(0).getAccountName();
            }

            String signStr = new StringBuilder(account).append("#").append(appId).append("#").append(code).append("#")
                    .append(gifgName).append("#").append(level).append("#").append(channel).append("#").append(channel)
                    .append("#").append(roleId).append("#").append(areaCode).append("#").append(time).append("#")
                    .append(appKey).toString();

            logger.info("recoder gift = " + signStr);
            String sign = CipherUtils.initMd5().encrypt(signStr);

            lytParams.add(new BasicNameValuePair("account", account));
            lytParams.add(new BasicNameValuePair("appId", appId));
            lytParams.add(new BasicNameValuePair("role_id", roleId + ""));
            lytParams.add(new BasicNameValuePair("gift_code", code));
            lytParams.add(new BasicNameValuePair("gift_name", gifgName));
            lytParams.add(new BasicNameValuePair("level", "1"));
            lytParams.add(new BasicNameValuePair("time", time + ""));
            lytParams.add(new BasicNameValuePair("server_id",areaCode + "" ));
            lytParams.add(new BasicNameValuePair("partner", channel));
            lytParams.add(new BasicNameValuePair("promotion", channel));
            lytParams.add(new BasicNameValuePair("sign", sign));

            String resultLyt = null;
            try {
                resultLyt = HttpUtils.doPost(checkUrl, lytParams, "utf-8", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        logger.info("resultLyt = " + resultLyt);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验用户是否激活.
     */
    public void checkUserActive(int gameId, int siteId, long userId) throws ServiceException {
        promoteCodeBatchService.checkUserActive(gameId, siteId, (int)userId);
    }
}

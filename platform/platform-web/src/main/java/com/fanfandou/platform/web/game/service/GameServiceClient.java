package com.fanfandou.platform.web.game.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fanfandou.admin.api.operation.entity.Notice;
import com.fanfandou.admin.api.operation.service.NoticeService;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.base.BaseVo;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.ActStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.resource.SiteCode;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.entity.MaintenanceStatus;
import com.fanfandou.platform.api.game.entity.WhiteList;
import com.fanfandou.platform.api.game.exception.GameException;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.user.exception.UserException;
import com.fanfandou.platform.web.game.vo.GameAreaVo;
import com.fanfandou.platform.web.game.vo.RoleInfoVo;
import com.fanfandou.platform.web.game.vo.RoleVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wudi.
 * Descreption:游戏交互服务层.
 * Date:2016/6/8
 */
@Service("gameServiceClient")
public class GameServiceClient extends BaseLogger {

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CacheService cacheService;


    /**
     * 客户端获取的区服信息.
     *
     * @param gameId      游戏ID
     * @param siteId      渠道ID
     * @param gameVersion 游戏当前版本号
     * @return 区服列表信息.
     */
    @SuppressWarnings("unchecked")
    public List<GameAreaVo> queryGameArea(int gameId, int siteId, int gameVersion, String whiteContent) throws ServiceException {
        List<GameArea> gameAreaList = gameAreaService.getAreasByGameId(gameId);

        List<GameAreaVo> returnAreaList = new ArrayList<GameAreaVo>();
        for (GameArea gameArea : gameAreaList) {
            WhiteList whiteList = cacheService.get(PublicCachedKeyConstant.AREA_WHITE_LIST + gameArea.getId(), WhiteList.class);
            if (whiteList == null) {
                whiteList = new WhiteList();
                whiteList.setTurnOff(false);
                whiteList.setWhiteContent("");
            }

            if (whiteList.isTurnOff() && (whiteList.isTurnOff() && whiteList.getWhiteContent().contains(whiteContent))) {
                gameArea.setValidStatus(ActStatus.ACTED);
                gameArea.setMaintenanceStatus(MaintenanceStatus.NORMAL);
            }

            //校验区服有效性
            if (gameArea.getValidStatus() == ActStatus.ACTED) {
                int minVersion = Integer.parseInt(gameArea.getSupportVersionMin());
                int maxVersion = Integer.parseInt(gameArea.getSupportVersionMax());
                //校验是否在版本信息内
                if (gameVersion >= minVersion && gameVersion <= maxVersion) {
                    //白名单检测
                   // WhiteList whiteList = cacheService.get(PublicCachedKeyConstant.AREA_WHITE_LIST + gameArea.getId(), WhiteList.class);
                    if (gameArea.getAvailableTime().getTime() > new Date().getTime()) {
                        continue;
                    }
                    //校验是否在渠道范围内
                    boolean flag = false;
                        String sites[] = gameArea.getSiteIds().split(",");
                        for (String site : sites) {
                            int pSiteId = Integer.parseInt(site);
                            if (pSiteId == siteId || StringUtils.isEmpty(site)) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            String availableTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(gameArea.getAvailableTime());
                            GameAreaVo gameAreaVo = new GameAreaVo();
                            gameAreaVo.setAreaCode(gameArea.getAreaCode());
                            gameAreaVo.setAreaDesc(gameArea.getAreaDesc());
                            gameAreaVo.setAreaName(gameArea.getAreaName());
                            gameAreaVo.setAreaTag(gameArea.getAreaTag());
                            gameAreaVo.setAvailableTime(availableTime);
                            gameAreaVo.setDisplayOrder(gameArea.getDisplayOrder());
                            gameAreaVo.setMaintenanceDesc(gameArea.getMaintenanceDesc());
                            gameAreaVo.setClientEnterAddr(JSON.toJSONString(gameArea.getClientEnterAddrList(), SerializerFeature.WriteEnumUsingToString));
                            gameAreaVo.setGameId(gameArea.getGameId());
                            gameAreaVo.setLoadStatus(gameArea.getLoadStatus());
                            gameAreaVo.setMaintenanceStatus(gameArea.getMaintenanceStatus().getId());
                            returnAreaList.add(gameAreaVo);
                        }
                }
            }
        }
        return returnAreaList;
    }


    /**
     * 获取游戏公告.
     */
    public String getAnnounce(int gameId, int siteId, int areaCode, int noticType) throws ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode + "");
        logger.info("GameServiceClient >>> getAnnounce areaId = " + gameArea.getId());
        Notice notice = noticeService.findByGameSiteAreaId(gameId, siteId, gameArea.getId(), noticType);
        StringBuilder stringBuilder = new StringBuilder();
        if (notice == null) {
            return stringBuilder.append("没有公告哟").toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("<!DOCTYPE html>\n<html>\n<head>\n");
        stringBuilder.append("    <meta charset=\"utf-8\">\n");
        stringBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        stringBuilder.append("    <title>游戏公告</title>\n");
        stringBuilder.append("</head>\n<body>");
        if (notice != null) {
            stringBuilder.append(notice.getNoticeContent());
        } else {
            stringBuilder.append("There are no announce");
        }
        stringBuilder.append("</body>\n</html>");
        return stringBuilder.toString();
    }

    /**
     * 获取游戏公告.
     */
    public String getAnnounceContent(int gameId, int siteId, String areaCode, int noticType) throws ServiceException {
        int areaId = 0;
        if (!areaCode.equals("0")) {
            GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode);
            areaId = gameArea.getId();
        }
        Notice notice = noticeService.findByGameSiteAreaId(gameId, siteId, areaId, noticType);
        if (notice == null) {
            notice = new Notice();
            notice.setNoticeJson("[{\"noticeLittleType\":\"1\",\"noticeLittleTitle\":\"欢迎\",\"noticeLittleText\":\"欢迎来到十万个冷笑话2\"}]");
        }
        String noticeContent = notice.getNoticeJson();

        if (siteId != 63 && siteId != 64 && siteId != 111 && siteId != 112 && siteId != 113 && siteId != 114) {
            noticeContent = noticeContent.replace("\\n", "");
        }

        return noticeContent.replace("%", "%25");
    }

    /**
     * 获取所有区服玩家角色列表.
     */
    public List<RoleVo> getRoleList(int gameId, long userId) throws ServiceException {
        List<GameRole> gameRoleList = gameRoleService.getRoleList(GameCode.getById(gameId), userId);
        List<RoleVo> roleVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(gameRoleList)) {
            for (GameRole gameRole : gameRoleList) {
                if (gameRole.getAreaId() != null) {
                    RoleVo roleVo = new RoleVo();
                    GameArea gameArea = gameAreaService.getGameAreaById(gameRole.getAreaId());
                    if (gameArea != null) {
                        roleVo.setAreaCode(gameArea.getAreaCode());
                        roleVo.setRoleHead(gameRole.getRoleHeadIcon());
                        roleVo.setRoleId(gameRole.getRoleId());
                        roleVo.setRoleLevel(gameRole.getRoleLevel());
                        roleVo.setRoleName(gameRole.getRoleName());
                        roleVoList.add(roleVo);
                    }
                }
            }
        }
        return roleVoList;
    }

    /**
     * 获取角色信息(如果不存在则临时创建).
     */
    public GameRole getRole(RoleInfoVo roleInfoVo) throws ServiceException {
        String ipCache = cacheService.get(IcachedConstant.CACHE_USER_IP + roleInfoVo.getIpAddress() + roleInfoVo.getUserId() + "getRole", String.class);
        if (ipCache != null) {
            throw new UserException(UserException.USER_REQUEST_QUICK, "用户请求过于频繁，请稍后再试");
        }
        cacheService.put(IcachedConstant.CACHE_USER_IP + roleInfoVo.getIpAddress() + roleInfoVo.getUserId() + "getRole", "getRole", 10);

        GameArea gameArea = gameAreaService.getGameAreaByCode(roleInfoVo.getGameId(), roleInfoVo.getAreaCode() + "");
        GameRole gameRole = gameRoleService.getRoleByUserId(GameCode.getById(roleInfoVo.getGameId()), gameArea.getId(),
                Long.valueOf(roleInfoVo.getUserId()));
        String roleName = "";
        try {
            if (gameRole == null) {
                gameRole = new GameRole();
                if (gameRole.getRoleName() != null) {
                    roleName = URLEncoder.encode(gameRole.getRoleName(), "utf-8");
                }
                gameRole.setAreaCode(roleInfoVo.getAreaCode());
                gameRole.setGameId(roleInfoVo.getGameId());
                gameRole.setUserId(Long.valueOf(roleInfoVo.getUserId()));
                gameRole.setSiteId(roleInfoVo.getSiteId());
                gameRole.setRoleName(roleName);
                gameRole.setAreaId(gameArea.getId());
                gameRole = gameRoleService.createRole(gameRole, roleInfoVo.getSiteId(),GameCode.getById(roleInfoVo.getGameId()));
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return gameRole;
    }

    /**
     * 检查角色名(没有有临时创建)，这里包括角色名修改记录.
     */
    public void checkRoleName(int gameId, int areaCode, int roleId, String roleName) throws ServiceException {
        //GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode + "");
        //先检查角色名是否有已存在.
        //GameRole gameRole = gameRoleService.getRoleByName(GameCode.getById(gameId), gameArea.getId(), roleName);
        String cachRoleName = cacheService.hGet(IcachedConstant.GAME_ROLE_NAME, roleName + "", String.class);
        logger.debug("cachRoleName = " + cachRoleName);
        if (StringUtils.isEmpty(cachRoleName) || cachRoleName.equals(roleId + "")) {
            //角色名可用，开始添加角色名称
            cacheService.hPut(IcachedConstant.GAME_ROLE_NAME, roleName + "", roleName);
            GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(gameId), roleId);
            gameRole.setRoleName(roleName);
            //创建或者修改角色名
            cacheService.hPut(IcachedConstant.GAME_ROLE_LIST + gameId, roleId + "", gameRole);
            //gameRoleService.updateRole(gameRole);
        } else {
            if (!roleName.contains(String.valueOf(roleId))) {
                throw new GameException(GameException.GAMEROLE_EXIST, "角色名已存在");
            } else {
                logger.info("角色校验特殊处理");
            }
        }
    }

    /**
     * 修改角色头像.
     */
    public void changeRoleHead(int gameId, int roleId, String roleHead) throws ServiceException {
        logger.debug("roleHead = " + roleHead);
        GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(gameId), roleId);
        gameRole.setRoleHeadIcon(roleHead);
        gameRoleService.updateRole(gameRole);
    }

    /**
     * 修改角色头像.
     */
    public void recordRoleLevel(int gameId, int roleId, int roleLevel) throws ServiceException {
        GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(gameId), roleId);
        if (gameRole == null) {
            throw new ServiceException(GameException.GAMEROLE_UNEXSIT, "角色ID不存在");
        }
        gameRole.setRoleLevel(roleLevel);
        cacheService.hPut(IcachedConstant.GAME_ROLE_LIST + gameId, roleId + "", gameRole);
        //gameRoleService.updateRole(gameRole);
    }

    /**
     * record player enter game.
     */
    public void enterGame(RoleInfoVo roleInfoVo) {
        logger.info("enterGameServoce>>>>>>>");
        if (roleInfoVo.getSiteId() == 15 || (roleInfoVo.getSiteId() >= 29 && roleInfoVo.getSiteId() <= 120)) {
            leYingInfo(roleInfoVo, 1);
        }
    }

    /**
     * logout infos.
     */
    public void logoutInfo(RoleInfoVo roleInfoVo) {
        logger.info("logoutInfoService>>>>>>>");
        if (roleInfoVo.getSiteId() == 15 || (roleInfoVo.getSiteId() >= 29 && roleInfoVo.getSiteId() <= 120)) {//roleInfoVo.getSiteId() == 15 || (roleInfoVo.getSiteId() >= 29 && roleInfoVo.getSiteId() <= 65)
            try {
                GameRole gameRole = gameRoleService.getRoleById(GameCode.getById(27), Long.valueOf(roleInfoVo.getRoleId()));
                if (gameRole == null) {
                    throw new ServiceException(GameException.GAMEROLE_UNEXSIT, "角色ID不存在");
                }
                gameRole.setRoleLevel(roleInfoVo.getLevel());
                cacheService.hPut(IcachedConstant.GAME_ROLE_LIST + 27, roleInfoVo.getRoleId() + "", gameRole);
            } catch (ServiceException e) {
                logger.info("等级记录失败");
            }
            leYingInfo(roleInfoVo, 2);
        }
    }

    /**
     * leYing infos.
     */
    public void leYingInfo(RoleInfoVo roleInfoVo, int type) {

        String appId = "20000009";
        String appKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";
        String checkUrl = "http://swglxh2.game.75wan.cn:30064/role_info";
        List<NameValuePair> mainObject = new ArrayList<NameValuePair>();
        mainObject.add(new BasicNameValuePair("appId", appId));

        JSONArray jsonArray = new JSONArray();

        JSONObject roleObject = new JSONObject();
        roleObject.put("account", roleInfoVo.getUserId());
        roleObject.put("role_id", roleInfoVo.getRoleId());
        roleObject.put("role_name", roleInfoVo.getRoleName());
        roleObject.put("level", roleInfoVo.getLevel());
        roleObject.put("gold", roleInfoVo.getGold());
        roleObject.put("money", roleInfoVo.getMoney());
        roleObject.put("type", type);
        roleObject.put("server_id", roleInfoVo.getAreaCode() + "");

        logger.info("leYingInfo roleObject = " + roleObject.toJSONString());

        jsonArray.add(0, roleObject);

        mainObject.add(new BasicNameValuePair("role_list", jsonArray.toJSONString()));

        String signStr = appId + "#" + jsonArray.toString() + "#" + appKey;
        String sign = CipherUtils.initMd5().encrypt(signStr);
        mainObject.add(new BasicNameValuePair("sign", sign));
        String resultParams = null;
        try {
            resultParams = HttpUtils.doPost(checkUrl, mainObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("role_info return : " + resultParams);
    }

    /**
     * 公测返利.
     */
    public void obDoubleMoney(int gameId, int areaCode, int roleId) throws ServiceException {
        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode + "");
        gameRoleService.returnDoubleMoney(gameArea.getId(), roleId);
    }

    /**
     * 获取拼接好的公共信息.
     */
    public StringBuilder getCommonInfos(BaseVo baseVo, String time) {

        StringBuilder message = new StringBuilder(time).append("|").append(baseVo.getIpAddress()).append("|")
                .append(baseVo.getDeviceType() == 1 ? "android" : "ios").append("|").append(baseVo.getSystemVersion()).append("|")
                .append(baseVo.getResolution()).append("|").append(baseVo.getMacAddress()).append("|").append(baseVo.getDeviceSerial())
                .append("|").append(baseVo.getAppVersion()).append("|").append(baseVo.getConnectType())
                .append("|").append(baseVo.getChannel()).append("|").append(baseVo.getPackageName());
        logger.debug(message.toString());
        return message;
    }
}

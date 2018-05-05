package com.fanfandou.platform.web.game.controller;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.base.BaseVo;
import com.fanfandou.common.entity.result.JsonResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.common.util.ErrorValidate;
import com.fanfandou.common.util.HttpAddrUtil;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.user.exception.UserException;
import com.fanfandou.platform.web.game.service.GameServiceClient;
import com.fanfandou.platform.web.game.vo.GameAreaVo;
import com.fanfandou.platform.web.game.vo.GameRoleVo;
import com.fanfandou.platform.web.game.vo.RoleInfoVo;
import com.fanfandou.platform.web.game.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by wudi.
 * Descreption:游戏内容控制类.
 * Date:2016/6/8
 */
@RequestMapping("/game")
@RestController
@ErrorValidate
public class GameController extends BaseLogger {

    @Autowired
    private GameServiceClient gameServiceClient;

    /**
     * 获取区服信息.
     *
     * @param gameId      游戏ID
     * @param siteId      渠道ID
     * @param gameVersion 游戏版本号
     */
    @RequestMapping("/getGameAreas")
    public JsonResult queryGameAreas(int gameId, int siteId, int gameVersion)
            throws ServiceException {
        if (gameId == 0 || siteId == 0) {
            throw new UserException(UserException.USER_INVALID_SITE, "请求信息缺失");
        }
        //获取IP地址
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);
        List<GameAreaVo> gameAreaList = gameServiceClient.queryGameArea(gameId, siteId, gameVersion, ipAddress);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(gameAreaList);
        jsonResult.setMessage(JsonResult.SUCCESS_MSG);
        jsonResult.setStatus(JsonResult.SUCCESS);
        return jsonResult;
    }

    /**
     * 游戏公告,选择区服后，返回页面..
     */
    @RequestMapping("/getAnnounce")
    public void getAnnounce(int gameId, int siteId, int areaCode, int notifyType) throws Exception {
        String announce = gameServiceClient.getAnnounce(gameId, siteId, areaCode, notifyType);
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        PrintWriter out = response.getWriter();
        out.print(announce);
    }

    /**
     * 游戏公告内容,选服前(只获取文字内容).
     */
    @RequestMapping("/getAnnounceContent")
    public void getAnnounceContent(int gameId, int siteId, String areaCode, int notifyType) throws Exception {
        String noticeContent = gameServiceClient.getAnnounceContent(gameId, siteId, areaCode, notifyType);
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        out.print(noticeContent);
    }

    /**
     * 获取玩家所有区服角色列表.
     */
    @RequestMapping("/getRoleList")
    public JsonResult getRoleList(int gameId, long userId) throws ServiceException {
        List<RoleVo> roleVoList = gameServiceClient.getRoleList(gameId, userId);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(roleVoList);
        jsonResult.setMessage(JsonResult.SUCCESS_MSG);
        jsonResult.setStatus(JsonResult.SUCCESS);
        return jsonResult;
    }

    /**
     * 获取用户角色信息(包括角色分配和获取).
     */
    @RequestMapping("/getRole")
    public JsonResult getRole(RoleInfoVo roleInfoVo) throws ServiceException {
        GameRole gameRole = gameServiceClient.getRole(roleInfoVo);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(gameRole);
        jsonResult.setMessage(JsonResult.SUCCESS_MSG);
        jsonResult.setStatus(JsonResult.SUCCESS);
        return jsonResult;
    }

    /**
     * 检查和创建角色名.
     */
    @RequestMapping("/checkRoleName")
    public JsonResult checkRoleName(int gameId, int areaCode, int roleId, String roleName) throws ServiceException {
        logger.info("getRole gameId = {}, areaCode = {}", gameId, areaCode);
        try {
            roleName = URLDecoder.decode(roleName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        gameServiceClient.checkRoleName(gameId, areaCode, roleId, roleName);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 公测返利.
     */
    @RequestMapping("/obDoubleMoney")
    public JsonResult obDoubleMoney(int gameId, int areaCode, int roleId) throws ServiceException {
        gameServiceClient.obDoubleMoney(gameId, areaCode, roleId);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 更换角色头像.
     */
    @RequestMapping("/changeRoleHead")
    public JsonResult changeRoleHead() throws ServiceException {

        return JsonResult.RESULT_SUCCESS;
    }



}

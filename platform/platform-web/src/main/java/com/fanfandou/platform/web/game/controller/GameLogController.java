package com.fanfandou.platform.web.game.controller;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.base.BaseVo;
import com.fanfandou.common.entity.result.JsonResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.common.util.ErrorValidate;
import com.fanfandou.common.util.HttpAddrUtil;
import com.fanfandou.platform.web.game.service.GameServiceClient;
import com.fanfandou.platform.web.game.vo.RoleInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wudi.
 * Descreption:游戏相关日志埋点.
 * Date:2017/7/13
 */
@RequestMapping("/game")
@RestController
@ErrorValidate
public class GameLogController extends BaseLogger {

    @Autowired
    private GameServiceClient gameServiceClient;


    /**
     * 记录玩家角色等级.
     */
    @RequestMapping("/recordRoleLevel")
    public JsonResult recordRoleLevel(int gameId, int roleId, int roleLevel) throws ServiceException {
        gameServiceClient.recordRoleLevel(gameId, roleId, roleLevel);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * user enter game.
     *
     */
    @RequestMapping("/enterGame")
    public JsonResult enterGame(RoleInfoVo roleInfoVo) {
        gameServiceClient.enterGame(roleInfoVo);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * user logout.
     */
    @RequestMapping("/logout")
    public JsonResult logout(RoleInfoVo roleInfoVo) {
        gameServiceClient.logoutInfo(roleInfoVo);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 设备激活日志记录.
     */
    @RequestMapping("/public/activeDevice")
    public JsonResult activeDevice(BaseVo baseVo) {
        //获取IP地址
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);
        baseVo.setIpAddress(ipAddress);
        String nowDate = DateUtil.getFormatedDateStringForLogConetent(new Date());
        String message = gameServiceClient.getCommonInfos(baseVo,nowDate).toString().trim();
        playerLogger.activation(message);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 游戏更新日志记录.
     */
    @RequestMapping("/public/updateClient")
    public JsonResult updateClient(BaseVo baseVo) {
        //获取IP地址
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);
        baseVo.setIpAddress(ipAddress);
        String nowDate = DateUtil.getFormatedDateStringForLogConetent(new Date());
        String message = gameServiceClient.getCommonInfos(baseVo, nowDate).append("|").append("0").append("|")
                .append(baseVo.getUpdateStatus()).append("|").append(nowDate).toString();
        playerLogger.update(message);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 到达登录UI界面日志记录.
     */
    @RequestMapping("/public/loginUI")
    public JsonResult loginUI(BaseVo baseVo) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);
        baseVo.setIpAddress(ipAddress);
        String nowDate = DateUtil.getFormatedDateStringForLogConetent(new Date());
        String message = gameServiceClient.getCommonInfos(baseVo,nowDate).append("|").append(baseVo.getCostTime()).toString();
        playerLogger.loginUi(message);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 到达游戏界面日志记录.
     */
    @RequestMapping("/public/loadGame")
    public JsonResult loadGame(BaseVo baseVo, String accountName) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);
        baseVo.setIpAddress(ipAddress);
        String costTime = baseVo.getCostTime();
        if (costTime.equals("0")) {
            BigDecimal db = new BigDecimal(Math.random() * (5 - 3.0f) + 3.0f);
            costTime = db.setScale(3, BigDecimal.ROUND_HALF_UP)// 保留3位小数并四舍五入
                    .toString();
        }
        String nowDate = DateUtil.getFormatedDateStringForLogConetent(new Date());
        String message = gameServiceClient.getCommonInfos(baseVo,nowDate).append("|").append(baseVo.getTime())
                .append("|").append(accountName).append("|").append(costTime).toString().trim();
        playerLogger.enterGame(message);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 到达游戏界面日志记录.
     */
    @RequestMapping("/public/cgRecord")
    public JsonResult cgRecord(BaseVo baseVo, String step) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);
        baseVo.setIpAddress(ipAddress);
        String nowDate = DateUtil.getFormatedDateStringForLogConetent(new Date());
        String message = gameServiceClient.getCommonInfos(baseVo,nowDate).append("|").append(step).toString();
        playerLogger.cgRecord(message);
        return JsonResult.RESULT_SUCCESS;
    }
}

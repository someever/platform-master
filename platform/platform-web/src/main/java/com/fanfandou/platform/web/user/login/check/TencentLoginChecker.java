package com.fanfandou.platform.web.user.login.check;

import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.HttpAddrUtil;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.user.exception.UserException;
import com.fanfandou.platform.web.user.vo.AccountVo;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by wudi.
 * Descreption:应用宝登录校验.
 * Date:2017/6/1
 */
public class TencentLoginChecker extends BaseLogger {

    private static TencentLoginChecker instance = new TencentLoginChecker();

    private TencentLoginChecker() {
    }

    public static TencentLoginChecker getInstance() {
        return instance;
    }

    /**
     * 三方方登录入口.
     */
    public AccountVo login(AccountVo accountVo) throws ServiceException {

        String qqAppId = "1106058391";

        String qqAppKey = "ZHz22ZZ4FnLa8I6N";

        String wxAppId = "wxc00b1f42185cdfc0";

        String wxAppKey = "17f6ee80a995ba5767abe222f1060d7c";

        String qqCheckUrl = "http://ysdk.qq.com/auth/qq_check_token";

        String wxCheckUrl = "http://ysdk.qq.com/auth/wx_check_token";

        String lytAppId = "20000009";

        String lytAppKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";

        String lytCheckUrl = "http://swglxh2.game.75wan.cn:30064/account";

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        //获取IP地址
        String ipAddress = HttpAddrUtil.getRemoteAddr(httpServletRequest);

        String appId = accountVo.getAppId();
        String openId = accountVo.getAccountName();
        String openKey = accountVo.getToken();
        long timeStamp = new Date().getTime() / 1000;

        String checkUrl = qqCheckUrl;
        String appKey = qqAppKey;
        String openKeyParam = "openkey";
        if (wxAppId.equals(appId)) {
            checkUrl = wxCheckUrl;
            appKey = wxAppKey;
            openKeyParam = "access_token";
        }

        logger.info("appKey = {}, ip = {}", appKey, ipAddress);

        String sig = CipherUtils.initMd5().encrypt(appKey + timeStamp);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            params.add(new BasicNameValuePair("timestamp", URLEncoder.encode(timeStamp + "", "utf-8")));
            params.add(new BasicNameValuePair("appid", URLEncoder.encode(appId, "utf-8")));
            params.add(new BasicNameValuePair("sig", URLEncoder.encode(sig, "utf-8")));
            params.add(new BasicNameValuePair("openid", URLEncoder.encode(openId, "utf-8")));
            if (wxAppId.equals(appId)) {
                params.add(new BasicNameValuePair("access_token", URLEncoder.encode(openKey, "utf-8")));
            }
            params.add(new BasicNameValuePair("openkey", URLEncoder.encode(openKey, "utf-8")));
            params.add(new BasicNameValuePair("userip", URLEncoder.encode(ipAddress, "utf-8")));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*params.add(new BasicNameValuePair("timestamp", timeStamp + ""));
        params.add(new BasicNameValuePair("appid", appId));
        params.add(new BasicNameValuePair("sig", sig));
        params.add(new BasicNameValuePair("openid", openId));
        params.add(new BasicNameValuePair("openkey", openKey));
        params.add(new BasicNameValuePair("userip", ipAddress));*/
        String resultParams = null;
        try {
            /*if (wxAppId.equals(appId)) {
                resultParams = HttpUtils.doGet(checkUrl, params);
            } else {
                resultParams = HttpUtils.doPost(checkUrl, params);
            }*/
            resultParams = HttpUtils.doGet(checkUrl, params,"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("TencentLoginChecker>>>>>>>>>>>>>>>>>resultParams = " + resultParams);

        JSONObject paramObject = JSONObject.parseObject(resultParams);

        int status = paramObject.getIntValue("ret");
        if (status != 0) {
            throw new UserException(UserException.USER_LOGIN_INVALIED, paramObject.getString("msg"));
        }

        accountVo.setAccountName(openId);
        accountVo.setChannel("sdk_yingyongba");
        accountVo.setToken(openKey);

        String lytAccount = openId;

        lytAccount = "a_" + openId;

        //乐盈的统计接口(必接！)
        int appVersion = accountVo.getAppVersion();
        String partner = "应用宝";
        String promotion = partner;
        String deviceId = accountVo.getDeviceSerial();
        String ip = ipAddress;
        String netWork = accountVo.getConnectType();
        String operator = accountVo.getOperator();
        String osVersion = accountVo.getSystemVersion();
        String brandModel = accountVo.getModel();
        String signStr = new StringBuilder(lytAccount).append("#").append(lytAppId).append("#").append(appVersion)
                .append("#").append(brandModel).append("#").append(deviceId).append("#").append(ip).append("#")
                .append(netWork).append("#").append(operator).append("#").append(osVersion).append("#").append(partner)
                .append("#").append(promotion).append("#").append(lytAppKey).toString();

        logger.info("lyt req " + signStr);

        String lytSign = CipherUtils.initMd5().encrypt(signStr);

        List<NameValuePair> lytParams = new ArrayList<NameValuePair>();

        lytParams.add(new BasicNameValuePair("accountId", lytAccount));
        lytParams.add(new BasicNameValuePair("appId", lytAppId));
        lytParams.add(new BasicNameValuePair("appVersion", appVersion + ""));
        lytParams.add(new BasicNameValuePair("partner", partner));
        lytParams.add(new BasicNameValuePair("promotion", promotion));
        lytParams.add(new BasicNameValuePair("deviceId", deviceId));
        lytParams.add(new BasicNameValuePair("ip", ip));
        lytParams.add(new BasicNameValuePair("network", netWork));
        lytParams.add(new BasicNameValuePair("operator", operator));
        lytParams.add(new BasicNameValuePair("osVersion", osVersion));
        lytParams.add(new BasicNameValuePair("brandModel", brandModel));

        String lytStr = getOrderParams(lytParams, lytAppKey);

        logger.info("lytStr = " + lytStr);

        //String sign = CipherUtils.initMd5().encrypt(signStr);

        lytParams.add(new BasicNameValuePair("sign", lytSign));

        String resultLyt = null;
        try {
            resultLyt = HttpUtils.doPost(lytCheckUrl, lytParams, "utf-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("TencentLoginChecker >>>> resultLyt = " + resultLyt);


        return accountVo;
    }

    public static String getOrderParams(List<NameValuePair> values, String appKey) {


        SortedSet<String> allParams = Sets.newTreeSet();

        for (NameValuePair pair : values) {
            if (pair.getName().equals("sign")) {
                continue;
            }
            try {
                allParams.add(pair.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Joiner.on("#").join(allParams) + "#" + appKey;
    }
}

package com.fanfandou.platform.web.user.login.check;


import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.crypto.Md5Cipher;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.DateUtil;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.user.exception.UserException;
import com.fanfandou.platform.web.user.login.SiteNote;
import com.fanfandou.platform.web.user.vo.AccountVo;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhongliang.
 * Descreption:乐视登录接口验证.
 * Date:2016/5/16
 */
@SiteNote(name = "mobile.android.lyt")
public class LytLoginChecker extends BaseLogger {

    private String appId = "20000009";

    private String appKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";

    private String address = "http://swglxh2.game.75wan.cn:30064/check_token";

    private static LytLoginChecker instance = new LytLoginChecker();

    private LytLoginChecker() {
    }

    public static LytLoginChecker getInstance() {
        return instance;
    }

    /**
     * 三方方登录入口.
     */
    public AccountVo login(AccountVo accountVo) throws ServiceException {
        String clientId = accountVo.getAppId();

        String token = accountVo.getToken();

        String param = clientId + "#" + token + "#" + appKey;

        String sign = CipherUtils.initMd5().encrypt(param);

        logger.info("LytLoginChecker>>>>>>>>>>>>>>>>>decode thirdOauth= token = " + token);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appId", appId));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sign", sign));

        String resultParams = null;
        try {
            resultParams = HttpUtils.doPost(address, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("LytLoginChecker>>>>>>>>>>>>>>>>>resultParams = " + resultParams);

        JSONObject paramObject = JSONObject.parseObject(resultParams);

        if (paramObject.getIntValue("code") != 100) {
            throw new UserException(UserException.USER_LOGIN_INVALIED, "登录验证无效");
        }
        String userId = paramObject.getString("account");
        String partner = paramObject.getString("partner");

        String nowDate = DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss");

        String message = nowDate + "|" + accountVo.getIpAddress() + "|"
                + (accountVo.getDeviceType() == 1 ? "android" : "ios") + "|" + accountVo.getSystemVersion() + "|"
                + accountVo.getResolution() + "|" + accountVo.getMacAddress() + "|" + accountVo.getDeviceSerial() + "|"
                + accountVo.getAppVersion() + "-" + accountVo.getGameVersion() + "|" + accountVo.getConnectType() + "|"
                + partner + "|" + accountVo.getPackageName() + "|" + nowDate + "|" + userId ;

        playerLogger.identification(message);

        accountVo.setAccountName(userId);
        accountVo.setAccountId(userId);
        accountVo.setUserId(userId);
        accountVo.setChannel(partner);
        String mode = "hilink";
        if (userId.contains("i_") || userId.contains("a_")) {
            mode = "lyt";
        }
        accountVo.setModel(mode);
        accountVo.setSignCode(paramObject.getString("sign"));
        return accountVo;
    }
}

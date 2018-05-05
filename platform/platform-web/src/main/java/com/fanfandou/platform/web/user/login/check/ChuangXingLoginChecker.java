package com.fanfandou.platform.web.user.login.check;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.user.exception.UserException;
import com.fanfandou.platform.web.user.login.SiteNote;
import com.fanfandou.platform.web.user.vo.AccountVo;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wudi.
 * Descreption:乐盈appstore登录.
 * Date:2017/7/20
 */
@SiteNote(name = "mobile.ios.appstore")
public class ChuangXingLoginChecker extends BaseLogger {

    private String appId = "526";
    private String loginKey = "db8ba29e96978a0901f199cdccbb7704";
    private String checkUrl = "https://api.31you.com/sdkapi.php";

    private static ChuangXingLoginChecker instance = new ChuangXingLoginChecker();

    private ChuangXingLoginChecker() {
    }

    public static ChuangXingLoginChecker getInstance() {
        return instance;
    }

    /**
     * 三方方登录入口.
     */
    public AccountVo login(AccountVo accountVo) throws ServiceException {
        String ac = "check";
        String sdkVersion = "2.1.7";
        String sessionId = accountVo.getAccessToken();
        String userId = accountVo.getUserId();
        int time = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
        appId = accountVo.getAppId();
        if (appId .equals("548")) {
            loginKey = "b5ed98aba3946908c4e8ded114c61ce8";
        } else if (appId .equals("547")) {
            loginKey = "5f61bfac37bd4e890853c41de69e1d1e";
        } else if (appId.equals("526")) {
            loginKey = "db8ba29e96978a0901f199cdccbb7704";
        }

        String str = "ac=" + ac + "&appid=" + appId + "&sdkversion=" + sdkVersion + "&sessionid="
                + sessionId + "&time=" + time + loginKey;

        String sign = CipherUtils.initMd5().encrypt(str);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ac", ac));
        params.add(new BasicNameValuePair("appid", appId));
        params.add(new BasicNameValuePair("sdkversion", sdkVersion));
        params.add(new BasicNameValuePair("sessionid", sessionId));
        params.add(new BasicNameValuePair("time", time + ""));
        params.add(new BasicNameValuePair("sign", sign));

        String resultParams = null;
        try {
            resultParams = HttpUtils.doPost(checkUrl, params);
            logger.info("LytLoginChecker>>>>>>>>>>>>>>>>>resultParams = " + resultParams);
            JSONObject paramObject = JSONObject.parseObject(resultParams);
            int code = paramObject.getIntValue("code");
            //if (code == 1) {
                //JSONArray jsonArray = paramObject.getJSONArray("userInfo");
                //JSONObject object = jsonArray.getJSONObject(0);
                accountVo.setAccountName(userId);//object.getString("uid")
                accountVo.setUserId(userId);
                return accountVo;
            /*} else {
                throw new UserException(UserException.USER_LOGIN_INVALIED, "登录验证无效");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.fanfandou.platform.web.user.login.check;

import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.user.exception.UserException;
import com.fanfandou.platform.web.user.vo.AccountVo;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi.
 * Descreption:quick sdk 登录校验.
 * Date:2017/7/29
 */
public class QuickLoginChecker extends BaseLogger {

    private String appId = "20000009";

    private static QuickLoginChecker instance = new QuickLoginChecker();

    private QuickLoginChecker() {
    }

    public static QuickLoginChecker getInstance() {
        return instance;
    }

    /**
     * 三方方登录入口.
     */
    public AccountVo login(AccountVo accountVo) throws ServiceException {
        String productCode = "McabJXdExQGdP7f5iFG3ad56xrapMitF";

        String address = "http://checkuser.sdk.quicksdk.net/v2/checkUserInfo";

        String userId = accountVo.getUserId();

        String token = accountVo.getToken();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("uid", userId));
        params.add(new BasicNameValuePair("product_code", productCode));

        String resultParams = null;
        try {
            resultParams = HttpUtils.doPost(address, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("LytLoginChecker>>>>>>>>>>>>>>>>>resultParams = " + resultParams);

        if (resultParams != null && !resultParams.equals("1")) {
            throw new UserException(UserException.USER_LOGIN_INVALIED, "登录验证无效");
        }
        accountVo.setAccountName(userId + accountVo.getSiteId());
        accountVo.setUserId(userId + accountVo.getSiteId());

        return accountVo;
    }
}

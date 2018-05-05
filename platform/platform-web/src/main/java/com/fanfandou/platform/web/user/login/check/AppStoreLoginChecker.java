package com.fanfandou.platform.web.user.login.check;

import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.web.user.vo.AccountVo;

/**
 * Created by wudi.
 * Descreption:appStore登录.
 * Date:2017/7/26
 */
public class AppStoreLoginChecker {

    private static AppStoreLoginChecker instance = new AppStoreLoginChecker();

    private AppStoreLoginChecker() {
    }

    public static AppStoreLoginChecker getInstance() {
        return instance;
    }

    /**
     * 三方方登录入口.
     */
    public AccountVo login(AccountVo accountVo) throws ServiceException {
        accountVo.setAccountName(accountVo.getUserId());
        return accountVo;
    }

}

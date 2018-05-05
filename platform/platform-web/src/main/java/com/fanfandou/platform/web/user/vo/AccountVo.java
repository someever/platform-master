package com.fanfandou.platform.web.user.vo;

import com.fanfandou.common.base.BaseVo;
import com.fanfandou.common.entity.SendStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.resource.SiteCode;
import com.fanfandou.platform.api.user.entity.AccountType;
import com.fanfandou.platform.api.user.entity.AuthTokenType;
import com.fanfandou.platform.api.user.entity.UserAccount;
import com.fanfandou.platform.web.user.servcie.LoginType;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;


/**
 * Created by wudi.
 * Descreption:账号VO.
 * Date:2016/3/31
 */
public class AccountVo extends BaseVo implements Serializable {

    public AccountVo() {
    }

    /**
     * accountVo 转化 userAccount.
     */
    public static UserAccount convertAccount(AccountVo accountVo) {
        UserAccount userAccount = new UserAccount();
        userAccount.setSiteId(accountVo.getSiteId());
        userAccount.setChannel(accountVo.getChannel());
        userAccount.setGameId(accountVo.getGameId());
        userAccount.setAccountName(accountVo.getAccountName());
        userAccount.setAccountType(accountVo.getAccountType());
        userAccount.setCreateIp(accountVo.getIpAddress());
        userAccount.setCreateDeviceSerial(accountVo.getDeviceSerial());
        userAccount.setCreateTime(new Date());
        return userAccount;
    }

    private String accountId;

    private String userId;

    @NotNull(message = "渠道不能为空")
    private Integer siteId;

    @NotNull(message = "游戏编号不能为空")
    private Integer gameId;

    private Integer areaCode;

    private Integer areaId;

    private String appId;

    private String extraDate;

    private String accountName;

    private int accountType = AccountType.DEVICE.getId();

    private int loginType = LoginType.ACCOUNT.getId();

    private SendStatus sendStatus = SendStatus.REGISTER;

    private String password;

    private String newPassword;

    private String encryptSeed;

    private String accessToken;

    private String refreshToken;

    private String statusExtdata;

    private String token;

    private String signCode;

    private AuthTokenType tokenType;

    private Date createTime;

    private String thirdOauth;

    private String serverKey;

    private String extraData;

    private String appid;

    private String operator;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtraDate() {
        return extraDate;
    }

    public void setExtraDate(String extraDate) {
        this.extraDate = extraDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountType getAccountType() {
        return AccountType.valueOf(accountType);
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEncryptSeed() {
        return encryptSeed;
    }

    public void setEncryptSeed(String encryptSeed) {
        this.encryptSeed = encryptSeed;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getStatusExtdata() {
        return statusExtdata;
    }

    public void setStatusExtdata(String statusExtdata) {
        this.statusExtdata = statusExtdata;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public AuthTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(AuthTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getThirdOauth() {
        return thirdOauth;
    }

    public void setThirdOauth(String thirdOauth) {
        this.thirdOauth = thirdOauth;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}

package com.fanfandou.platform.web.billing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.result.JsonResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.ErrorValidate;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.billing.entity.BillingOrder;
import com.fanfandou.platform.api.billing.entity.Currency;
import com.fanfandou.platform.api.billing.entity.OrderParam;
import com.fanfandou.platform.api.billing.exception.BillingException;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.web.billing.callback.Utils.GooglePlay.Security;
import com.fanfandou.platform.web.billing.callback.Utils.ReorderParams;
import com.fanfandou.platform.web.billing.callback.Utils.TencentUtils.GenSign;
import com.fanfandou.platform.web.billing.callback.Utils.apple.OrderValiderApple;
import com.fanfandou.platform.web.billing.callback.Utils.quick.QuickData;
import com.fanfandou.platform.web.billing.callback.Utils.quick.QuickParseUtils;
import com.fanfandou.platform.web.billing.service.BillingServiceClient;
import com.fanfandou.platform.web.billing.vo.AppStoreVo;
import com.fanfandou.platform.web.billing.vo.LytReqVo;
import com.fanfandou.platform.web.billing.vo.MsdkVo;
import com.sun.crypto.provider.HmacSHA1;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wudi.
 * Descreption:渠道支付回调地址.
 * Date:2016/8/1
 */
@RequestMapping("/billing")
@RestController
public class BillingCallback extends BaseLogger {

    @Autowired
    private BillingServiceClient billingServiceClient;

    @Autowired
    private CacheService cacheService;

    /**
     * 乐视支付回调.
     */
    @RequestMapping("/callback/letvPayCallback")
    public String letvPayCallback() {
        String result = "fail";
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("letvPayCallback >> " + httpServletRequest.getRequestURI() + ",params = "
                + JSON.toJSONString(httpServletRequest.getParameterMap()));
        try {
            //String prodCallback = "http://api.platform.fanfandou.com:8888/billing/callback/letvPayCallback";
            String urlCallback = "http://106.75.33.45:8888/platform/billing/callback/letvPayCallback";
            String secretKey = "82f4d3ae4ced4178bff0cf711402a27e";
            String toyType = "ttl-toys";
            String appKey = httpServletRequest.getParameter("appKey");
            if (appKey.equals("276242")) {
                secretKey = "346a5556cc1648d4a0db92d066c93bbb";
                toyType = "mouse";
            }
            //beta参数
            // String codeAppkey = "234dd0f1782f4cf0a85881bcb4c54d77";
            //String codeSecret = "346a5556cc1648d4a0db92d066c93bbb";

            String params = httpServletRequest.getParameter("params");
            String pxNumber = httpServletRequest.getParameter("pxNumber");
            String price = httpServletRequest.getParameter("price");
            String userName = httpServletRequest.getParameter("userName");
            String currencyCode = httpServletRequest.getParameter("currencyCode");
            String products = httpServletRequest.getParameter("products");
            String sign = httpServletRequest.getParameter("sign");
            String extraData[] = params.split("\\*");
            String orderNo = extraData[0];
            String codeType = extraData[1];
            int gameId = Integer.parseInt(extraData[2]);
            String areaCode = extraData[3];
            int goodsId = Integer.parseInt(extraData[4]);

            logger.info("letvPayCallback >> orderNo = " + orderNo);

            //重新排序，生成新的字符串
            String strParams = urlCallback + ReorderParams.getOrderParams(httpServletRequest, secretKey);
            logger.info("letvPayCallback >> strParams = " + strParams);
            strParams = URLEncoder.encode(strParams, "utf-8");
            logger.info("letvPayCallback >> strParamsEncoder = " + strParams);
            //加密
            String msign = CipherUtils.initMd5().encrypt(strParams);
            logger.info("letvPayCallback >> msign = " + msign);
            if (msign.equals(sign)) {

                String timeStamp = new Date().getTime() + "";//
                if (codeType.contains("ttl-toys")) {
                    //如果有类型，则走乐视0元购流程
                    String reqUrl = "http://hades.hdtv.letv.com/hades/proxy/api/cdkey/";

                    String reqStrParams = "accType=" + toyType + "appKey=" + appKey + "appPackage=" + "com.fanfandou.ttol.letv"
                            + "pxNumber=" + pxNumber + "timestamp=" + timeStamp + secretKey;
                    String reqStrAll = "POST" + reqUrl + reqStrParams;
                    logger.info("0gou >> reqStrAll = " + reqStrAll);
                    reqStrAll = URLEncoder.encode(reqStrAll, "utf-8");
                    String codeSign = CipherUtils.initMd5().encrypt(reqStrAll);
                    List<NameValuePair> paramsPair = new ArrayList<NameValuePair>();
                    paramsPair.add(new BasicNameValuePair("pxNumber", pxNumber));
                    paramsPair.add(new BasicNameValuePair("appPackage", "com.fanfandou.ttol.letv"));
                    paramsPair.add(new BasicNameValuePair("accType", toyType));
                    paramsPair.add(new BasicNameValuePair("sign", codeSign));
                    paramsPair.add(new BasicNameValuePair("timestamp", timeStamp));
                    paramsPair.add(new BasicNameValuePair("appKey", appKey));
                    logger.info("0gou >> codeSign = " + codeSign);
                    JSONObject jsonObject = JSONObject.parseObject(HttpUtils.doPost(reqUrl, paramsPair, "utf-8", ""));
                    logger.info("0gou >> jsonObject = " + jsonObject);
                    if (!StringUtils.isEmpty(jsonObject.getString("cdkey"))) {
                        logger.info("开始零元购发货");
                        String retCode = jsonObject.getString("cdkey");
                        billingServiceClient.sendToycode(gameId, areaCode, userName, goodsId, retCode);
                    }
                }
                logger.info("正常充值发货");
                billingServiceClient.thirdCharge(orderNo, pxNumber, Currency.CNY, (int) (Double.parseDouble(price) * 100));
                result = "SUCCESS";
            }
        } catch (Exception e) {
            logger.error("letvPayCallback", e);
            result = "FAIL";
        }
        logger.info("正常返回状态 result = " + result);
        return result;
    }

    /**
     * 谷歌支付回调.
     *
     * @return 支付结果.
     */
    @RequestMapping("/googlePlayCallback")
    public JsonResult googlePlayCallback() {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMessage(JsonResult.FAIL_MSG);
        jsonResult.setStatus(JsonResult.FAIL);
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("letvPayCallback >> " + request.getRequestURI() + ",params = "
                + JSON.toJSONString(request.getParameterMap()));
        String signData = request.getParameter("signData");
        String signature = request.getParameter("signSignature");
        String orderNo = request.getParameter("orderNO");
        String numberNo = request.getParameter("numberNo");
        String money = request.getParameter("money");
        logger.info("googlePlayCallback >>>> signData = {}, signSignature = {}, orderNo = {}, money = {}",
                signData, signature, orderNo, money);
        if (!StringUtils.contains(numberNo, '.')) {  //订单号不符合规则

            logger.info("googlePlayCallback -> numberNO invalid. numberNO : " + numberNo);
            return jsonResult;
        }
        try {
            boolean flag = Security.verifyPurchase(signData, signature);
            if (flag) {
                billingServiceClient.thirdCharge(orderNo, numberNo, Currency.CNY, Integer.parseInt(money));
                jsonResult.setMessage(JsonResult.SUCCESS_MSG);
                jsonResult.setStatus(JsonResult.SUCCESS);
            }
        } catch (ServiceException e) {
            logger.error("googlePlayCallback >", e);
        }
        logger.info("result = " + jsonResult);

        return jsonResult;
    }

    /**
     * 乐赢互动支付回调.
     *
     * @return 支付结果.
     */
    @RequestMapping("/callback/leYingPayCallback")
    @ResponseBody
    public void lytPlayCallback(@RequestBody LytReqVo lytReqVo) {
        String returnValue = "fail";
        String appKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";/*
        if (lytReqVo.getAppId().equals("20000009")) {
             appKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";
        }*/
        playerLogger.ordering(lytReqVo.getCp_order_id());
        String lytStrParams = new StringBuilder(lytReqVo.getAccount()).append("#").append(lytReqVo.getAppId()).append("#").append(lytReqVo.getCode())
                .append("#").append(lytReqVo.getCommodity_id()).append("#").append(lytReqVo.getCp_extension()).append("#").append(lytReqVo.getCp_order_id())
                .append("#").append(lytReqVo.getPrice()).append("#").append(lytReqVo.getRole_id()).append("#").append(lytReqVo.getServer_id()).append("#")
                .append(lytReqVo.getTrade_id()).append("#").append(appKey).toString();

        //a_11220000009102100000154 # 20000009 # 100 # 60,6,20170735199 # 8 # 20170735199 # 6 # 27954 # 2 # 20000009ddb34c59e2f3288ead72274783eb22ac # McabJXdExQGdP7f5iFG3ad56xrapMitF

        logger.info("lytStrParams = " + lytStrParams + ", sign = " + lytReqVo.getSign());
        String cpSign = CipherUtils.initMd5().encrypt(lytStrParams);
        logger.info("mSign = " + cpSign);
        try {
            if (cpSign.equals(lytReqVo.getSign())) {
                returnValue = "success";
                billingServiceClient.thirdCharge(lytReqVo.getCp_order_id(), lytReqVo.getTrade_id(), Currency.CNY, lytReqVo.getPrice());
            }
        } catch (ServiceException e) {
            if (e.getId() == BillingException.PAY_ORDER_INVALID) {
                returnValue = "fail";
            }
            e.printStackTrace();
        }
        logger.info("支付结果 = " + returnValue);
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException i) {
            i.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        out.print(returnValue);
    }

    public JsonResult oldTencentPay(MsdkVo msdkVo) throws ServiceException {
        logger.info("老版本支付开启。。。。。。。");
        //余额查询
        String balanceUrl = "https://ysdk.qq.com/mpay/get_balance_m";

        //扣除游戏币接口
        String payUrl = "https://ysdk.qq.com/mpay/pay_m";

        //取消支付接口
        String cancelPayUrl = "https://ysdk.qq.com/mpay/cancel_pay_m";

        //直接赠送接口
        String presentUrl = "https://ysdk.qq.com/mpay/present_m";

        String appKey = "IDgh0hvCztOlGZl1SrPpJVVeEoSHAzgJ";

        String returnValue = "fail";

        String sessionId = "openid";
        String sessionType = "kp_actoken";

        if (msdkVo.getMsdkType() == 2) {
            sessionId = "hy_gameid";
            sessionType = "wc_actoken";
        }

        try {
            //#############################################先查询余额##################################################
            //首先组装cookies
            String cookies = "session_id=" + sessionId + ";session_type=" + sessionType + ";org_loc=" + URLEncoder.encode("/mpay/get_balance_m", "utf-8");
            long timeStamps = new Date().getTime() / 1000;
            //组装签名

            List<NameValuePair> balanceParams = new ArrayList<NameValuePair>();
            balanceParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("ts", timeStamps + ""));

            balanceParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("pfkey",URLEncoder.encode(msdkVo.getPfkey(),"utf-8") ));
            balanceParams.add(new BasicNameValuePair("zoneid", msdkVo.getAreaId()));
            String banlaceSig = GenSign.generateSign("/v3/r/mpay/get_balance_m",balanceParams  ,appKey);
            balanceParams.add(new BasicNameValuePair("sig", URLEncoder.encode(banlaceSig, "utf-8")));

            String resultBalance = HttpUtils.doGet(balanceUrl, balanceParams, cookies);

            logger.info("余额查询回调：" + resultBalance);
            JSONObject balanceResult = JSONObject.parseObject(resultBalance);
            int balanceRet = balanceResult.getIntValue("ret");
            if (balanceRet == 0) {
                //订单中查询价格
                BillingOrder billingOrder = cacheService
                        .get(IcachedConstant.BILLING_ORDER_PARAMS + msdkVo.getOrderId(), BillingOrder.class);
                int realMoney = billingOrder.getPayAmount() * 10;
                //查询余额成功,首先查询余额
                int balance = balanceResult.getIntValue("balance");
                logger.info("充值RMB = " + realMoney);
                int balanceAmt = balance - realMoney;
                logger.info("余额查询成功");
                if (balanceAmt < 0) {
                    if (msdkVo.getMoney() != billingOrder.getPayAmount()) {
                        throw new BillingException(BillingException.GOODS_NOT_EXIST);
                    }

                } else {
                    //补单

                }
                //#################################扣除游戏币##########################################
                //String billno = "2017" + new Date().getTime() / 100;
                List<NameValuePair> payParams = new ArrayList<NameValuePair>();
                payParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
                payParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
                payParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
                payParams.add(new BasicNameValuePair("ts", timeStamps + ""));

                payParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
                payParams.add(new BasicNameValuePair("pfkey",URLEncoder.encode(msdkVo.getPfkey(),"utf-8") ));
                payParams.add(new BasicNameValuePair("zoneid", msdkVo.getAreaId()));
                payParams.add(new BasicNameValuePair("amt", balance + ""));
                payParams.add(new BasicNameValuePair("billno", msdkVo.getOrderId()));
                String paySig = GenSign.generateSign("/v3/r/mpay/pay_m",payParams  ,appKey);
                payParams.add(new BasicNameValuePair("sig", URLEncoder.encode(paySig,"utf-8")));
                String resultPay = HttpUtils.doGet(payUrl, payParams,  cookies);
                logger.info("扣除游戏币回调：" + resultPay);
                JSONObject payResult = JSONObject.parseObject(resultPay);
                int payRet = payResult.getIntValue("ret");
                if (payRet == 0) {
                    //扣费成功
                    logger.info("扣费成功");
                    String txbillno = payResult.getString("billno");
                    //人民币单位分
                    billingServiceClient.thirdCharge(msdkVo.getOrderId(), txbillno, Currency.CNY, msdkVo.getMoney() / 10);
                    returnValue = "success";
                } else {
                    //余额不足
                    //#################################取消支付接口###########################################
                    List<NameValuePair> cancerPayParams = new ArrayList<NameValuePair>();
                    cancerPayParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("ts", timeStamps + ""));

                    cancerPayParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("pfkey",URLEncoder.encode(msdkVo.getPfkey(),"utf-8") ));
                    cancerPayParams.add(new BasicNameValuePair("zoneid", msdkVo.getAreaId()));
                    cancerPayParams.add(new BasicNameValuePair("amt", realMoney + ""));
                    cancerPayParams.add(new BasicNameValuePair("billno", msdkVo.getOrderId()));
                    String canerPaySig = GenSign.generateSign("/v3/r/mpay/cancel_pay_m",cancerPayParams  ,appKey);
                    cancerPayParams.add(new BasicNameValuePair("sig", URLEncoder.encode(canerPaySig, "utf-8")));
                    String resultcancer = HttpUtils.doGet(cancelPayUrl, cancerPayParams, cookies);
                    logger.info("取消扣除游戏币回调：" + resultcancer);
                    JSONObject cancerPayResult = JSONObject.parseObject(resultcancer);
                    int cancerPayRet = cancerPayResult.getIntValue("ret");
                    if (cancerPayRet == 0) {
                        logger.info("取消支付成功");
                    } else {
                        logger.info("取消支付失败");
                    }
                }


            } else {
                logger.info("余额不足，再见！");
                return JsonResult.RESULT_FAIL;

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(returnValue);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 应用宝支付相关.
     */
    @RequestMapping("/callback/tencentPayCallback")
    public JsonResult tencentPayCallback(MsdkVo msdkVo) throws ServiceException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.info("睡眠失败？");
        }

        if (StringUtils.isEmpty(msdkVo.getPayType())) {
            return oldTencentPay(msdkVo);
        }

        //余额查询
        String balanceUrl = "https://ysdk.qq.com/mpay/get_balance_m";

        //扣除游戏币接口
        String payUrl = "https://ysdk.qq.com/mpay/pay_m";

        //取消支付接口
        String cancelPayUrl = "https://ysdk.qq.com/mpay/cancel_pay_m";

        //直接赠送接口
        String presentUrl = "https://ysdk.qq.com/mpay/present_m";

        String appKey = "IDgh0hvCztOlGZl1SrPpJVVeEoSHAzgJ";

        String returnValue = "fail";

        String sessionId = "openid";
        String sessionType = "kp_actoken";

        if (msdkVo.getMode().equals("beta")) {
            appKey = "6bi2LT54VGjTUUQQ5dJDNqbfGAyjSpeJ";
            balanceUrl = "https://ysdktest.qq.com/mpay/get_balance_m";
            payUrl = "https://ysdktest.qq.com/mpay/pay_m";
        }

        if (msdkVo.getMsdkType() == 2) {
            sessionId = "hy_gameid";
            sessionType = "wc_actoken";
        }

        try {
            //#############################################先查询余额##################################################
            //首先组装cookies
            String cookies = "session_id=" + sessionId + ";session_type=" + sessionType + ";org_loc=" + URLEncoder.encode("/mpay/get_balance_m", "utf-8");
            long timeStamps = new Date().getTime() / 1000;
            //组装签名

            List<NameValuePair> balanceParams = new ArrayList<NameValuePair>();
            balanceParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("ts", timeStamps + ""));

            balanceParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("pfkey", URLEncoder.encode(msdkVo.getPfkey(), "utf-8")));
            balanceParams.add(new BasicNameValuePair("zoneid", "1"));
            String banlaceSig = GenSign.generateSign("/v3/r/mpay/get_balance_m", balanceParams, appKey);
            balanceParams.add(new BasicNameValuePair("sig", URLEncoder.encode(banlaceSig, "utf-8")));

            String resultBalance = HttpUtils.doGet(balanceUrl, balanceParams, cookies);

            logger.info("余额查询回调：" + resultBalance);
            JSONObject balanceResult = JSONObject.parseObject(resultBalance);
            int balanceRet = balanceResult.getIntValue("ret");
            int realMoney = 0;
            if (balanceRet == 0) {
                //查询余额成功,首先查询余额
                int balance = balanceResult.getIntValue("balance");
                if (balance <= 0) {
                    return JsonResult.RESULT_FAIL;
                }
                //首先确认是否是正常充值回调
                if ("2".equals(msdkVo.getPayType())) {
                    OrderParam orderParam = new OrderParam();
                    orderParam.setPayAmount(balance / 10);
                    orderParam.setAreaCode(msdkVo.getAreaId());
                    orderParam.setGameId(27);
                    orderParam.setSiteId(34);
                    orderParam.setRoleId(msdkVo.getRoleId());
                    orderParam.setGoodsCount(balance);
                    String orderId = billingServiceClient.tencentSupportCharge(orderParam);
                    msdkVo.setOrderId(orderId);
                    realMoney = balance;
                } else {
                    realMoney = msdkVo.getMoney();
                    logger.info("充值RMB = " + realMoney);
                    int balanceAmt = balance - realMoney;
                    logger.info("余额查询成功");
                    if (balanceAmt < 0) {//有刷单嫌疑，抛出异常
                        throw new BillingException(BillingException.GOODS_NOT_EXIST);
                    }
                }
                //#################################扣除游戏币##########################################
                //String billno = "2017" + new Date().getTime() / 100;
                List<NameValuePair> payParams = new ArrayList<NameValuePair>();
                payParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
                payParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
                payParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
                payParams.add(new BasicNameValuePair("ts", timeStamps + ""));

                payParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
                payParams.add(new BasicNameValuePair("pfkey", URLEncoder.encode(msdkVo.getPfkey(), "utf-8")));
                payParams.add(new BasicNameValuePair("zoneid", "1"));
                payParams.add(new BasicNameValuePair("amt", realMoney + ""));
                payParams.add(new BasicNameValuePair("billno", msdkVo.getOrderId()));
                String paySig = GenSign.generateSign("/v3/r/mpay/pay_m", payParams, appKey);
                payParams.add(new BasicNameValuePair("sig", URLEncoder.encode(paySig, "utf-8")));
                String resultPay = HttpUtils.doGet(payUrl, payParams, cookies);
                logger.info("扣除游戏币回调：" + resultPay);
                JSONObject payResult = JSONObject.parseObject(resultPay);
                int payRet = payResult.getIntValue("ret");
                if (payRet == 0) {
                    //扣费成功
                    logger.info("扣费成功");
                    String txbillno = payResult.getString("billno");
                    //人民币单位分
                    billingServiceClient.thirdCharge(msdkVo.getOrderId(), txbillno, Currency.CNY, msdkVo.getMoney() / 10);
                    returnValue = "success";
                } else {
                    //余额不足
                    //#################################取消支付接口###########################################
                    List<NameValuePair> cancerPayParams = new ArrayList<NameValuePair>();
                    cancerPayParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("ts", timeStamps + ""));

                    cancerPayParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("pfkey", URLEncoder.encode(msdkVo.getPfkey(), "utf-8")));
                    cancerPayParams.add(new BasicNameValuePair("zoneid", "1"));
                    cancerPayParams.add(new BasicNameValuePair("amt", realMoney + ""));
                    cancerPayParams.add(new BasicNameValuePair("billno", msdkVo.getOrderId()));
                    String canerPaySig = GenSign.generateSign("/v3/r/mpay/cancel_pay_m", cancerPayParams, appKey);
                    cancerPayParams.add(new BasicNameValuePair("sig", URLEncoder.encode(canerPaySig, "utf-8")));
                    String resultcancer = HttpUtils.doGet(cancelPayUrl, cancerPayParams, cookies);
                    logger.info("取消扣除游戏币回调：" + resultcancer);
                    JSONObject cancerPayResult = JSONObject.parseObject(resultcancer);
                    int cancerPayRet = cancerPayResult.getIntValue("ret");
                    if (cancerPayRet == 0) {
                        logger.info("取消支付成功");
                    } else {
                        logger.info("取消支付失败");
                    }
                }

                //#################################直接赠送接口###########################################
            /*List<NameValuePair> presentPayParams = new ArrayList<NameValuePair>();
            presentPayParams.add(new BasicNameValuePair("openid", URLEncoder.encode(msdkVo.getOpenid(), "utf-8")));
            presentPayParams.add(new BasicNameValuePair("openkey", URLEncoder.encode(msdkVo.getOpenkey(), "utf-8")));
            presentPayParams.add(new BasicNameValuePair("appid", URLEncoder.encode(msdkVo.getAppid(), "utf-8")));
            presentPayParams.add(new BasicNameValuePair("ts", timeStamps + ""));

            presentPayParams.add(new BasicNameValuePair("pf", URLEncoder.encode(msdkVo.getPf(), "utf-8")));
            presentPayParams.add(new BasicNameValuePair("pfkey",URLEncoder.encode(msdkVo.getPfkey(),"utf-8") ));
            presentPayParams.add(new BasicNameValuePair("zoneid", msdkVo.getAreaId()));
            presentPayParams.add(new BasicNameValuePair("presenttimes", msdkVo.getMoney() + ""));
            presentPayParams.add(new BasicNameValuePair("billno", billno + 1));
            String presentPaySig = GenSign.generateSign("/v3/r/mpay/present_m",presentPayParams  ,appKey);
            presentPayParams.add(new BasicNameValuePair("sig", URLEncoder.encode(presentPaySig, "utf-8")));
            String resultpresent = HttpUtils.doGet(presentUrl, presentPayParams, cookies);
            logger.info("直接赠送接口回调：" + resultpresent);
            JSONObject presentPayResult = JSONObject.parseObject(resultpresent);
            int presentPayRet = presentPayResult.getIntValue("ret");
            if (presentPayRet == 0) {
                logger.info("发送成功");
            } else {
                logger.info("发送失败");
            }*/

            } else {
                logger.info("余额不足，再见！");
                return JsonResult.RESULT_FAIL;

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(returnValue);
        return JsonResult.RESULT_SUCCESS;
    }

    /**
     * 苹果appStore支付校验.
     */
    @RequestMapping("/callback/appStorePayCallback")
    public String appStorePayCallback(AppStoreVo appStoreVo) throws ServiceException {
        String receiptData = appStoreVo.getReceiptData();
        try {
            receiptData = URLDecoder.decode(receiptData, "utf-8");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("receiptData >>> " + receiptData);
        String numberNO = OrderValiderApple.instance().valid(receiptData, appStoreVo.getOrderId(), false);
        billingServiceClient.thirdCharge(appStoreVo.getOrderId(), numberNO, Currency.CNY, appStoreVo.getPayAmount());
        return "success";
    }


    /**
     * 中手游IOS支付回调接口.
     */
    @RequestMapping("/callback/chuangXingCallback")
    public void chuangXingCallback() {
        String returnValue = "ERROR";
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String amount = request.getParameter("amount");
        String appid = request.getParameter("appid");
        String charid = request.getParameter("charid");
        String cporderid = request.getParameter("cporderid");
        String extinfo = request.getParameter("extinfo");
        String gold = request.getParameter("gold");
        String orderid = request.getParameter("orderid");
        String serverid = request.getParameter("serverid");
        String time = request.getParameter("time");
        String uid = request.getParameter("uid");
        String sign = request.getParameter("sign");
        String payKey = "f9e1959d4bdc4c4be408bb6f9a487938";

        if (appid.equals("548")) {
            payKey = "6c4b58345b8193a5e79e64a0bfd7036f";
        } else if (appid.equals("547")) {
            payKey = "f85124e888e8823a9875fd35fb3e9722";
        } else if (appid.equals("526")) {
            payKey = "f9e1959d4bdc4c4be408bb6f9a487938";
        }

        String strParams = ReorderParams.getOrderParams(request, payKey);
        logger.info("strParams = " + strParams);
        String cpSign = CipherUtils.initMd5().encrypt(strParams);

        if (cpSign.equals(sign)) {
            try {
                billingServiceClient.thirdCharge(cporderid, orderid, Currency.CNY, (int) Double.parseDouble(amount));
                returnValue = "SUCCESS";
            } catch (ServiceException e) {
                logger.info(e.getDetail());
            }
        }

        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException i) {
            i.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        out.print(returnValue);
        logger.info("chuangXingCallback 支付结果 ：" + returnValue);
    }

    /**
     * quick sdk 支付回调处理.
     */
    @RequestMapping("/callback/quickPaycCallback")
    public String quickPaycCallback() {
        String returnValue = "ERROR";
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ntData = request.getParameter("nt_data");
        String sign = request.getParameter("sign");
        String md5Sign = request.getParameter("md5Sign");
        String md5Key = "";
        String mSign = CipherUtils.initMd5().encrypt(ntData + sign + md5Key);
        String datas = "";
        if (mSign.equals(md5Sign)) {
            datas = QuickParseUtils.decode(ntData, md5Key);
        }
        logger.info("datas = {}, msign = {}", datas, mSign);
        QuickData quickData = QuickParseUtils.parseQuickData(datas);
        String myOrder = quickData.getGameOrder();
        String cpOrder = quickData.getOrderNo();
        String amount = quickData.getAmount();
        String status = quickData.getStatus();

        if ("0".equals(status)) {
            try {
                billingServiceClient.thirdCharge(myOrder, cpOrder, Currency.CNY, Integer.parseInt(amount));
                returnValue = "SUCCESS";
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        logger.info("result = " + returnValue);

        return returnValue;
    }

    /**
     * 压测支付接口.
     */
    @RequestMapping("/callback/pressTestCharge")
    public JsonResult pressTestCharge(String orderId, String amount) throws ServiceException {
        billingServiceClient.moniCharge(orderId, orderId, Currency.CNY, Integer.parseInt(amount));
        return JsonResult.RESULT_SUCCESS;
    }

}

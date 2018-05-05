package com.fanfandou.platform.web.billing.callback.Utils.TencentUtils;

import com.fanfandou.common.base.BaseLogger;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by wudi.
 * Descreption:应用宝SDK参数组装.
 * Date:2017/6/7
 */
public class GenSign extends BaseLogger {
    protected static Logger logger = LoggerFactory.getLogger(GenSign.class);

    // 编码方式
    private static final String CONTENT_CHARSET = "UTF-8";

    // HMAC算法
    private static final String HMAC_ALGORITHM = "HmacSHA1";


    public static String generateSign(String url, List<NameValuePair> values, String secret) throws UnsupportedEncodingException {
        //step1
        String requestType = "GET";
        String uri = URLEncoder.encode(url, "utf-8");
        //step2 , 3
        String strSig = URLEncoder.encode(getOrderParams(values), "utf-8");
        logger.info("unsig = " + getOrderParams(values));
        logger.info("sig = " + strSig);
        //step4
        String finalStr = requestType + "&" + uri + "&" + strSig;
        logger.info("finalStr = " + finalStr);
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);

            SecretKeySpec secretKey = new SecretKeySpec((secret + "&").getBytes(CONTENT_CHARSET), mac.getAlgorithm());

            mac.init(secretKey);

            byte[] hash = mac.doFinal(finalStr.getBytes(CONTENT_CHARSET));

            String finalSig = encodeUrl(hash);

            logger.info("finalSig = " + finalSig);

            return finalSig;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";

    }


    public static String encodeUrl(byte[] str) {
        return new String(Base64.encodeBase64(str));
    }


    //

    /**
     * 二行制转字符串.
     */
    public static String byte2hex(String s) {
        byte[] b = s.getBytes();
        String retStr = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            retStr = "%" + hex.toUpperCase();
        }
        return retStr;
    }

    /**
     * 为请求参数重新排序
     *
     * @return 排序后的值.
     */
    public static String getOrderParams(List<NameValuePair> values) {


        SortedSet<String> allParams = Sets.newTreeSet();

        for (NameValuePair pair : values) {
            if (pair.getName().equals("sig")) {
                continue;
            }
            try {
                logger.info(pair.getName() + "=" + pair.getValue());
                allParams.add(pair.getName() + "=" + pair.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Joiner.on("&").join(allParams);
    }

}

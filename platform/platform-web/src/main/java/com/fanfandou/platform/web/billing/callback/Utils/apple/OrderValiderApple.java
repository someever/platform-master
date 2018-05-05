package com.fanfandou.platform.web.billing.callback.Utils.apple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.billing.exception.BillingException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: zhangjianbing
 * Date: 13-2-17
 * Time: 下午7:29
 * To change this template use File | Settings | File Templates.
 */
public class OrderValiderApple extends BaseLogger {

    private static OrderValiderApple instance = new OrderValiderApple();

    private OrderValiderApple() {

    }

    public static OrderValiderApple instance() {
        return instance;
    }

    public String valid(String receipt, String orderNO, boolean isSanbox) throws ServiceException {
        try {
            logger.info("OrderValiderApple begin valid. ");

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new HaLinTrustManager()}, new java.security.SecureRandom());
            String url;
            if (isSanbox) { //如果是沙盒测试
                url = "https://sandbox.itunes.apple.com/verifyReceipt";
            } else { // 正式环境
                url = "https://buy.itunes.apple.com/verifyReceipt";
            }
            logger.info("OrderValiderApple verify url: " + url);
            URL console = new URL(url);

            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());

            String str = String.format(Locale.CHINA, "{\"receipt-data\":\"" + receipt + "\"}");
            hurlBufOus.write(str.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            logger.info("OrderValiderApple verifyReceipt response: " + sb.toString());


            JSONObject json = JSON.parseObject(sb.toString());//new JSONObject(sb.toString());
            JSONObject props = null;
            String status = json.getString("status");

            if (!isSanbox && !"0".equals(status)) {     //正式验证失败，尝试沙盒验证
                return valid(receipt, orderNO, true);
            }  else if (!"0".equals(status)) {
                throw new BillingException(BillingException.GOODS_LACK_PARAMS);
            }
            props = json.getJSONObject("receipt");
            String numberNO = props.getString("transaction_id");//json.getString(props, "transaction_id");
             return numberNO;
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw (ServiceException)e;
            }
            e.printStackTrace();
            throw new ServiceException(ServiceException.GENERIC);
        }
    }
}

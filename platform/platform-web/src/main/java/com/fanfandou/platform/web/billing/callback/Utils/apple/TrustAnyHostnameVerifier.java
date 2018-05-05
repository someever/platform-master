package com.fanfandou.platform.web.billing.callback.Utils.apple;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author <a href="mailto:stevezhang@simlink.me">Steve</a>
 *         Date: 12-12-19  下午7:20
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

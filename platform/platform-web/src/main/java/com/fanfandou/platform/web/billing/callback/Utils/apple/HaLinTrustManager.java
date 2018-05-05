package com.fanfandou.platform.web.billing.callback.Utils.apple;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author <a href="mailto:stevezhang@simlink.me">Steve</a>
 *         Date: 13-1-5  下午8:44
 */
public class HaLinTrustManager implements X509TrustManager {
   public HaLinTrustManager() {

    }

    // check client trust status
    public void checkClientTrusted(X509Certificate chain[], String authType)
            throws CertificateException {
        System.out.println("check client trust status");
    }

    // check Server trust status
    public void checkServerTrusted(X509Certificate chain[], String authType)
            throws CertificateException {
        System.out.println("check Server trust status");
    }

    //get those accepted Issuers
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

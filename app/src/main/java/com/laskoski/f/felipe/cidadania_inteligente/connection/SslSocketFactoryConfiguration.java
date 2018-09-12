package com.laskoski.f.felipe.cidadania_inteligente.connection;

import android.content.Context;

import com.laskoski.f.felipe.cidadania_inteligente.R;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Felipe on 9/11/2018.
 */

public class SslSocketFactoryConfiguration {
    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    SSLSocketFactory sslSocketFactory;
    private static final String KEYSTORE_PASSWORD = "smartcitzenUSP";

    public SslSocketFactoryConfiguration(Context context) {
        try {
            // Get an instance of the RSA format
            // (JKS) Sun implementation
            // (BKS) - Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("pkcs12");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = context.getResources().openRawResource(R.raw.keystore);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Provide the password of the keystore
                trusted.load(in, KEYSTORE_PASSWORD.toCharArray());
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}

package com.zsteven44.android.myrxjavaproject.services;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static String API_BASE_URL = "https://api.imgur.com/";

    private static OkHttpClient.Builder okhttpclient = createUnsafeHttpClient();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());


    private static Retrofit retrofit = retrofitBuilder.build();

    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    /*
        Highly recommended that you do not use this method often or for serious production
        vs development
     */
    public static void changeApiBaseUrl(String newBaseUrl) {
        API_BASE_URL = newBaseUrl;
        retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_BASE_URL);
    }

    public static <S> S createService(Class<S> serviceClass) {

        if (!okhttpclient.interceptors().contains(httpLoggingInterceptor)){
            okhttpclient.addInterceptor(httpLoggingInterceptor);
            retrofitBuilder.client(okhttpclient.build());
            retrofit = retrofitBuilder.build();
        }

        return retrofit.create(serviceClass);
    }

    private static OkHttpClient.Builder createUnsafeHttpClient(){
        /*
        TODO implement custom trustManager and avoid allowing unsafe SSL handshakes, using this link:
        https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/CustomTrust.java
         */

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okhttpclient = new OkHttpClient.Builder();
            okhttpclient.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            okhttpclient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return okhttpclient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}

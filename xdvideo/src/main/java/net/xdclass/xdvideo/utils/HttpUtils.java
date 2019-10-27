package net.xdclass.xdvideo.utils;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http请求封装
 */
public class HttpUtils {

    @Value("${http.connectionTimeout}")
    private static int connectionTimeout;

    @Value("${http.requestTimeout}")
    private static int requestTimeout;

    @Value("${http.socketTimeout}")
    private static int socketTimeout;

    private static final Gson gson = new Gson();

    /**
     * 发送get请求
     *
     * @param url
     * @return
     */
    public static Map<String, Object> doGet(String url) {
        Map<String, Object> map = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(HttpUtils.getRequestConfig());

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResult = EntityUtils.toString(httpResponse.getEntity());
                map = gson.fromJson(jsonResult, map.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 发送post请求
     * @param url
     * @param data
     * @return
     */
    public static String doPost(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //设置超时时间
        httpPost.setConfig(HttpUtils.getRequestConfig());
        //请求微信平台需要设置content-Type
        httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");
        if (data != null && data instanceof String) {
            StringEntity stringEntity = new StringEntity(data.toString(),"UTF-8");
            httpPost.setEntity(stringEntity);
        }
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                String jsonResult = EntityUtils.toString(httpEntity);
                return jsonResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static RequestConfig getRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(requestTimeout) //请求超时
                .setSocketTimeout(socketTimeout)
                .setRedirectsEnabled(true)  //允许自动重定向
                .build();
        return requestConfig;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

}

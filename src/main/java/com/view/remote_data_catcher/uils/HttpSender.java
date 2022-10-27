package com.view.remote_data_catcher.uils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/*
<dependency>
<groupId>org.apache.httpcomponents</groupId>
<artifactId>httpclient</artifactId>
<version>4.5.10</version>
</dependency>
*/

public class HttpSender {
    private static CloseableHttpClient httpclient = HttpClients.createDefault();


    public static byte[] downloadFile(String urlStr) {
        try {
            URL url = new URL(urlStr);
            byte[] bytes = IOUtils.toByteArray(url);
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException("download fail " + e);
        }
    }

    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    public static String doGet(String url, HashMap<String, String> headerMap, HashMap<String, String> queryMap) {

        url = urlMix(url, queryMap);


        HttpGet httpGet = new HttpGet(url);

        if (null != headerMap && headerMap.size() > 0) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> m : entries) {
                httpGet.setHeader(m.getKey(), m.getValue());
            }
        }

        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            HttpEntity entity1 = response1.getEntity();
            InputStream content = entity1.getContent();
            String s = IOUtils.toString(content, Charset.forName("utf-8"));
            content.close();
            return s;

        } catch (IOException e) {
            throw new RuntimeException("do get fail cause:" + e.getMessage());
        }


    }

    public static String doJSonPost(String url, String jsonStr) {
        return doJSonPost(url, null, null, jsonStr);
    }

    public static String doJSonPost(String url, HashMap<String, String> headerMap, HashMap<String, String> queryMap, String jsonStr) {


        url = urlMix(url, queryMap);

        HttpPost httpPost = new HttpPost(url);


        StringEntity entity = null;
        entity = new StringEntity(jsonStr, Charset.forName("UTF-8"));


        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        if (null != headerMap && headerMap.size() > 0) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> m : entries) {
                httpPost.setHeader(m.getKey(), m.getValue());
            }
        }

        try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
            HttpEntity entity2 = response2.getEntity();
            InputStream content = entity2.getContent();
            String s = IOUtils.toString(content, Charset.forName("utf-8"));
            content.close();
            return s;
        } catch (IOException e) {
            throw new RuntimeException("do post fail cause:" + e.getMessage());
        }

    }


    private static String urlMix(String url, HashMap<String, String> queryMap) {
        if (url == null) {
            throw new NullPointerException("url can not be null");
        }
        if (null != queryMap && queryMap.size() > 0) {
            url += "?1=1&";
            Set<Map.Entry<String, String>> entries = queryMap.entrySet();
            for (Map.Entry<String, String> m : entries) {
                url += (m.getKey() + "=" + m.getValue() + "&");
            }
        }
        return url;
    }


    /**
     * 表单请求
     *
     * @param url
     * @param headerMap
     * @param data
     * @return
     */
    public static String doFormData(String url, HashMap<String, String> headerMap, HttpEntity data) {
        HttpPost httpPost = new HttpPost(url);


        if (null != headerMap && headerMap.size() > 0) {
            Set<Map.Entry<String, String>> entries = headerMap.entrySet();
            for (Map.Entry<String, String> m : entries) {
                httpPost.setHeader(m.getKey(), m.getValue());
            }
        }

        httpPost.setEntity(data);

        try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
            HttpEntity entity2 = response2.getEntity();
            InputStream content = entity2.getContent();
            String s = IOUtils.toString(content, "utf-8");
            content.close();
            return s;
        } catch (IOException e) {
            throw new RuntimeException("do post fail cause:" + e.getMessage());
        }
    }
}

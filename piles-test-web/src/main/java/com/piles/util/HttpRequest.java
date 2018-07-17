package com.piles.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpRequest {


    public static boolean httpPostWithJson(Map<String, JSONObject> map, String url) {
        boolean isSuccess = false;
        log.info("请求信息:" + url + JSON.toJSONString(map));

        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            // 设置超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String s : map.keySet()) {
                nvps.add(new BasicNameValuePair(s, map.get(s).toJSONString()));

            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8"));
            post.setEntity(urlEncodedFormEntity);

            HttpResponse response = httpClient.execute(post);
            HttpEntity entityResponse = response.getEntity();
            //EntityUtils中的toString()方法转换服务器的响应数据
            String str = EntityUtils.toString(entityResponse, "utf-8");
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                isSuccess = false;
                log.error("请求信息:" + map + " 返回结果" + str);
            } else {

                log.info("请求信息:" + map + " 返回结果" + str);
                return true;
            }
        } catch (Exception e) {
            log.error("请求信息:" + url + JSON.toJSONString(map), e);
            isSuccess = false;
        } finally {
            if (post != null) {
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error("http请求关闭异常:{}",e);
                }
            }
        }
        return isSuccess;
    }

    public static boolean httpPost(Map<String, String> map, String url) {
        boolean isSuccess = false;
        log.info("请求信息:" + url + JSON.toJSONString(map));

        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            // 设置超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String s : map.keySet()) {
                nvps.add(new BasicNameValuePair(s, String.valueOf(map.get(s))));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8"));
            post.setEntity(urlEncodedFormEntity);

            HttpResponse response = httpClient.execute(post);
            HttpEntity entityResponse = response.getEntity();
            //EntityUtils中的toString()方法转换服务器的响应数据
            String str = EntityUtils.toString(entityResponse, "utf-8");
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                isSuccess = false;
                log.error("请求信息:" + map + " 返回结果" + str);
            } else {

                log.info("请求信息:" + map + " 返回结果" + str);
                return true;
            }
        } catch (Exception e) {
            log.error("请求信息:" + url + JSON.toJSONString(map), e);
            isSuccess = false;
        } finally {
            if (post != null) {
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error("http请求关闭异常:{}",e);
                }
            }
        }
        return isSuccess;
    }

    public static String httpPostForStrWithJson(Map<String, JSONObject> map, String url) {
        String result = null;
        log.info("请求信息:" + url + JSON.toJSONString(map));

        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            // 设置超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String s : map.keySet()) {
                nvps.add(new BasicNameValuePair(s, map.get(s).toJSONString()));

            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8"));
            post.setEntity(urlEncodedFormEntity);

            HttpResponse response = httpClient.execute(post);
            HttpEntity entityResponse = response.getEntity();
            //EntityUtils中的toString()方法转换服务器的响应数据
            String str = EntityUtils.toString(entityResponse, "utf-8");
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error("请求信息:" + map + " 返回结果" + str);
            } else {
                //状态为成功的时候赋值
                result = str;
                log.info("请求信息:" + map + " 返回结果" + str);
            }
        } catch (Exception e) {
            log.error("请求信息:" + url + JSON.toJSONString(map), e);
        } finally {
            if (post != null) {
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error("http请求关闭异常:{}",e);
                }
            }
        }
        return result;
    }


}
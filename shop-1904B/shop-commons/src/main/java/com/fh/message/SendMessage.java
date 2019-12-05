package com.fh.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMessage {

    private static final String SERVER_URL = "https://api.netease.im/sms/sendcode.action";//请求的URL
    private static final String APP_KEY = "15ef9cee59de13a5d123317980b8ba1b";//网易云分配的账号
    private static final String APP_SECRET = "370da2cd8221";//密码
    // private static final String MOULD_ID="3057527";//模板ID
    private static final String NONCE = "456231";//随机数
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN = "6";

    public static JSONObject sendMessage(String phone) throws IOException {
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpPost httpPost=new HttpPost(SERVER_URL);
        String curTime = String.valueOf((new Date().getTime() / 1000L));
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

//设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

//设置请求参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mobile", phone));
        nameValuePairs.add(new BasicNameValuePair("codeLen", CODELEN));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

//执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseEntity = EntityUtils.toString(response.getEntity(),"utf-8");

        return JSON.parseObject(responseEntity);
    }
}

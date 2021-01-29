package com.jt;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestHttpClient {
    /*
     * 1.需要实例化HttpClient对象
     * 2.确定URL地址  http://xxxxxx
     * 3.定义请求类型   GET/POST/PUT/DELETE
     * 4.发起远程请求 获取响应的结果 response对象
     * 5.判断状态码 是否为200 表示请求结果正确
     * 6.获取远程服务返回的数据   html代码片段/JSON串/javaScript数据
     */

    @Test
    public void testGet() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://www.baidu.com";//可以访问任意的IP地址
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        int statue = httpResponse.getStatusLine().getStatusCode();
        if (statue == 200)
        {
            //获取返回值结果
            String result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
            System.out.println(result);
            // 可以针对result进行二次的开发  return xxxx
        }
    }
}

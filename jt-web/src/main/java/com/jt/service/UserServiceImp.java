package com.jt.service;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class UserServiceImp implements UserService {
    @Override
    public List<User> findUserList() {
        List<User> userList = new ArrayList<>();
        HttpClient httpClient = HttpClients.createDefault();
        String url = "http://sso.jt.com/user/findUserList";
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200)
            {
                //证明请求有效
                String result = EntityUtils.toString(response.getEntity(),"utf-8");
                //将远程获取的数据转化为对象
                userList = ObjectMapperUtil.toObj(result,userList.getClass());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return userList;
    }
}

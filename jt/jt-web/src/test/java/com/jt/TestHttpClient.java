package com.jt;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

public class TestHttpClient {

	@Test
	public void testGet() throws ClientProtocolException, IOException {
		String url = "http://manage.jt.com/web/testCors";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		//判断响应码是否为200
		//400提交参数异常,404文件找不到 ,406接受参数异常,500服务器异常,504超时,200正常
		int status = response.getStatusLine().getStatusCode();
		if(status == 200) {
			//获取返回值的实体对象
			HttpEntity httpEntity =response.getEntity();
			//将远程服务器返回的信息,转为字符串,方便调用 1.json 2.html代码片段
			String result = EntityUtils.toString(httpEntity,"utf-8");
			User user = ObjectMapperUtil.toObject(result, User.class);
			System.out.println(user);	
		}
	}
}

package com.gq.common.utils;

import java.io.IOException;  
  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.HttpException;  
import org.apache.commons.httpclient.HttpMethod;  
import org.apache.commons.httpclient.methods.GetMethod;  
import org.apache.commons.lang.StringUtils; 
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gq.common.log.LogUtil;


/**
 * 封装类
 * 对HttpClient进行的简单封装，主要优点是，静态调用，自动识别网页字符集，伪装火狐/IE浏览器
 *
 */
public class HttpClientUtil {  
  
  public static HttpClient getClient() {  
    HttpClient client = new HttpClient();  
    return client;  
  }  
  
  public static String getHtml(String url) throws HttpException, IOException {  
    return getHtml(url, 80, null, null, 0, null);  
  }  
  
  public static String getHtml(String url, String cookie) throws HttpException, IOException {  
    return getHtml(url, 80, null, null, 0, cookie);  
  }  
  
  public static String getHtml(String url, int port, String cookie) throws HttpException, IOException {  
    return getHtml(url, port, null, null, 0, cookie);  
  }  
  
  public static String getHtml(String url, int port, String encoding, String proxyHost, int proxyPort, String cookie)  
      throws HttpException, IOException {  
    HttpClient httpClient = getClient();  
    String rest = null;  
    if(proxyHost != null && proxyPort != 0) httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);  
    HttpMethod method = new GetMethod(url);  
    if(!StringUtils.isBlank(cookie)) {  
      method.addRequestHeader("Cookie", cookie);  
    }  
    method.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727");  
    //Mozilla/5.0 (Windows NT 6.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1  
    httpClient.executeMethod(method);  
  
    //根据http头解析正确的字符集  
    String header = method.getResponseHeader("Content-Type").getValue();  
    if(header.contains("charset=")) {  
      encoding = header.substring(header.indexOf("charset=") + "charset=".length(), header.length());  
    }  
    if(encoding == null) encoding = "GBK";  
  
    rest = new String(method.getResponseBody(), encoding);  
    method.releaseConnection();  
    return rest;  
  }  
  
  public static void main(String[] args) throws HttpException, IOException {  
    String url = "http://www.ccb.com";  
    System.out.println(getHtml(url));  
  }
  public static String httpPost(String url, Map paramMap, String code) {
		String content = null;
		if (url == null || url.trim().length() == 0 || paramMap == null
				|| paramMap.isEmpty())
			return null;
		HttpClient httpClient = new HttpClient();
		//设置header
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");//
		
		//代理设置
		//httpClient.getHostConfiguration().setProxy("128.128.176.74", 808);
		
		PostMethod method = new PostMethod(url);
		Iterator it = paramMap.keySet().iterator();
		

		while (it.hasNext()) {
			String key = it.next() + "";
			Object o = paramMap.get(key);
			if (o != null && o instanceof String) {
				method.addParameter(new NameValuePair(key, o.toString()));
			}
			if (o != null && o instanceof String[]) {
				String[] s = (String[]) o;
				if (s != null)
					for (int i = 0; i < s.length; i++) {
						method.addParameter(new NameValuePair(key, s[i]));
					}
			}
		}
		try {
			LogUtil.log("执行的method ：：",method.toString());
			int statusCode = httpClient.executeMethod(method);
			
			System.out.println("httpClientUtils::statusCode="+statusCode);
			
			content = new String(method.getResponseBody(), code);
			LogUtil.log("结果，httpClientUtils::statusCode=",content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(method!=null)method.releaseConnection();
			method = null;
			httpClient = null;
		}
		return content;

	}
  	/*public static String httpPost(String url, Map paramMap) {
		//编码：GB2312
		return HttpClientUtil.httpPost(url, paramMap, "GB2312");
	}*/
}  
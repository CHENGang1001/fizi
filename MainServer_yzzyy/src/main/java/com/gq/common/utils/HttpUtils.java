package com.gq.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 
 * http工具类 Detailed description
 * 
 * @author Administrator
 * @version [version, 2016年1月7日]
 * @see [relevant class/method]
 * @since [product/module version]
 */
public class HttpUtils {
	/**
	 * 响应json数据
	 * 
	 * @param response
	 * @param returncode
	 */
	// public static <T> void responseJson(HttpServletResponse response,
	// RetunCode returncode) {
	// response.setContentType("text/json;charset=UTF-8");
	// CommonReponse cr = new CommonReponse();
	// cr.getHeader().setResultCode(returncode.getCode());
	// cr.getHeader().setResultMsg(returncode.getMsg());
	// String returnXML = JsonUtils.toJson(cr);
	//
	// try {
	// response.getWriter().write(returnXML);
	// response.flushBuffer();
	// } catch (IOException e) {
	//
	// }
	// }

	/**
	 * post 请求
	 */
	public String doPost(String url, String body) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		// CloseableHttpResponse response = httpclient.execute(httpget);
		return body;

	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("content-type", "application/json");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
//			out = new PrintWriter(conn.getOutputStream());
//			// 发送请求参数
//			out.print(param);
//			// flush输出流的缓冲
//			out.flush();
			OutputStream os = conn.getOutputStream();
			
			OutputStreamWriter osw = new OutputStreamWriter(os, CharEncoding.UTF_8);
			
			osw.write(param);
			osw.close();
			
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	//用来实现首页新闻资讯的接口获取数据
	public static String sendGet(String url) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("content-type", "application/json");
			conn.setRequestProperty("content-type", "text/html");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
			conn.setConnectTimeout(2000);			
			// 发送POST请求必须设置如下两行
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"ISO-8859-1"));
			// 发送请求参数
			//out.print(param);
			//System.out.println(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO-8859-1"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
}

package com.gq.common.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.PropertiesUtil;

/**
 * 发送短信客户端
 *
 * @author zhixinchen
 * @ClassName: SmsClient
 * @Description: 发送短信客户端
 * @date: Dec 31, 2013 3:27:43 PM
 */
public class SmsClient {

	/**
	 * 私有的构造函数
	 */
	private SmsClient() {

	}

	/**
	 * 请求编码
	 */
	private static String requestEncoding = "UTF-8";

	/**
	 * DEBUG日志
	 */
	private static Logger log = MHLogManager.getDebugLog();

	private static final int TEN = 10;
	private static final int BYTE_SIZE = 1024;
	private static final int SUCCESS_CODE = 200;

	/**
	 * 请求短信平台
	 *
	 * @param reqUrl
	 * <br />
	 * @param parameters
	 * <br />
	 */
	private static void doPost(String reqUrl, Map<String, String> parameters) {
		long startTime = System.currentTimeMillis();
		HttpURLConnection httpURLConnection = null;
		String responseContent = null;
		String vchartset = SmsClient.requestEncoding;
		StringBuffer params = new StringBuffer();
		int level = Level.INFO;
		try {
			for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
				Entry<?, ?> element = (Entry<?, ?>) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(), vchartset));
				params.append("&");
			}
			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Host", "api.sms.ronghub.com");
			httpURLConnection.setRequestProperty("App-Key", PropertiesUtil.getMessageValueByKey("RCAPPKEY"));
            Random random = new Random();
			httpURLConnection.setRequestProperty("Nonce", random.toString());
			Timestamp time = new Timestamp(System.currentTimeMillis());  
			httpURLConnection.setRequestProperty("Timestamp", time.toString());
			httpURLConnection.setRequestProperty("Signature", sha1(PropertiesUtil.getMessageValueByKey("RCAppSecret") + random.toString() + time.toString()));
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setConnectTimeout(Integer.parseInt(PropertiesUtil.getMessageValueByKey("smsConnectTimeOut")));
			httpURLConnection.setReadTimeout(Integer.parseInt(PropertiesUtil.getMessageValueByKey("smsReadTimeOut")));
			httpURLConnection.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			httpURLConnection.getOutputStream().write(b, 0, b.length);
			httpURLConnection.getOutputStream().flush();
			httpURLConnection.getOutputStream().close();

			InputStream in = httpURLConnection.getInputStream();
			byte[] echo = new byte[TEN * BYTE_SIZE];
			int len = in.read(echo);
			responseContent = (new String(echo, 0, len)).trim();
			int code = httpURLConnection.getResponseCode();
			if (code != SUCCESS_CODE) {
				level = Level.ERROR;
				responseContent = responseContent + "code:" + code;
				log.error("url:{}", reqUrl);
				log.error("returnCode:{}", code);
			}
		} catch (Exception e) {
			level = Level.ERROR;
			log.error("Exception", e);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			// 接口日志
			MHLogManager.interfaceLog(level, "MHSERVER", "SMS", "sendSMS", params.toString(), responseContent,
					System.currentTimeMillis() - startTime);
		}
	}
	
	 public static String sha1(String data) throws NoSuchAlgorithmException {  
	        MessageDigest md = MessageDigest.getInstance("SHA1");  
	        md.update(data.getBytes());  
	        StringBuffer buf = new StringBuffer();  
	        byte[] bits = md.digest();  
	        for(int i=0;i<bits.length;i++){  
	            int a = bits[i];  
	            if(a<0) a+=256;  
	            if(a<16) buf.append("0");  
	            buf.append(Integer.toHexString(a));  
	        }  
	        return buf.toString();  
	    }  

	/**
	 * 发送短信客户端类
	 *
	 * @param msisdn
	 * <br />
	 * @param verify
	 * <br />
	 */
	public static void sendSms(String msisdn, String verify) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		// 发送的手机号
		map.put("mobile", msisdn);
		// 填写短信内容
		String content = "@1@=" + verify;
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid3"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}

	/**
	 * 发送短信客户端类
	 *
	 * @param userName
	 * <br />
	 * @param msisdn
	 * <br />
	 * @param verify
	 * <br />
	 */
	public static void sendSms(String msisdn, String verify, String userName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		// 发送的手机号
		map.put("mobile", msisdn);
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + verify;
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid3"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送取消预约短信
	 */
	public static void sendUnregisterPaySms(String msisdn, String bespeakDate,String userName, String doctorName,String patientname) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + bespeakDate+ ",@3@=" + doctorName+ ",@4@=" + patientname;
		map.put("content", content);
		
		//设置小心类型
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		
		//消息编码
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid1"));
		
		//发送短信
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送预约挂号短信
	 */
	public static void sendAppointmentSms(String msisdn ,String userName, String doctorName, String orderNumber, String billNo, String bespeakDate, String time,String patientname) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + doctorName + ",@3@=" + orderNumber + ",@4@=" + billNo + ",@5@=" + bespeakDate+ ",@6@=" + time+ ",@7@=" + patientname;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid2"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送预约挂号不支付短信
	 */
	public static void sendAppointmentNopaySms(String msisdn ,String userName, String doctorName, String orderNumber, String billNo, String bespeakDate, String time,String patientname) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + doctorName + ",@3@=" + orderNumber + ",@4@=" + billNo + ",@5@=" + bespeakDate+ ",@6@=" + time+ ",@7@=" + patientname;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid8"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送当日挂号短信
	 */
	public static void sendTodayAppointmentSms(String msisdn, String userName, String doctorName, String orderNumber, String billNo, String bespeakDate,String patientName) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + doctorName + ",@3@=" + orderNumber + ",@4@=" + billNo + ",@5@=" + bespeakDate + ",@6@=" + patientName;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid5"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送业务失败退费短信
	 */
	public static void sendRefundSms(String msisdn, String userName, String payFor) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + payFor;
		map.put("content", content);
		
		//设置小心类型
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		
		//消息编码
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid4"));
		
		//发送短信
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	/**
	 * 发送缴费成功短信
	 */
	public static void sendToPaySms(String msisdn, String userName, String payfor, String billCost) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + payfor + ",@3@=" + billCost;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid6"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送停诊通知短信（已支付）
	 */
	public static void sendStopScheduleSms(String phonenum, String patientName, String date, String doctorName) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", phonenum);
		
		// 填写短信内容
		String content = "@1@=" + patientName + ",@2@=" + date + ",@3@=" + doctorName;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名（在 global.properties 中配置）
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid7"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
	
	/**
	 * 发送停诊通知短信(窗口支付)
	 */
	public static void sendStopScheduleSms2(String phonenum, String patientName, String date, String doctorName) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", phonenum);
		
		// 填写短信内容
		String content = "@1@=" + patientName + ",@2@=" + date + ",@3@=" + doctorName;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名（在 global.properties 中配置）
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid11"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}

	/**
	 * 发送门诊缴费成功短信
	 */
	public static void sendToSms(String msisdn, String userName, String payfor, String billCost,String drugWinNo,String billNo) {
		Map<String, String> map = new HashMap<String, String>();
		
		//方法
		map.put("method", PropertiesUtil.getMessageValueByKey("method"));
		
		// 用户账户号
		map.put("username", PropertiesUtil.getMessageValueByKey("username"));
		
		// 用户密码
		map.put("password", PropertiesUtil.getMessageValueByKey("password"));
		
		//校验码
		map.put("veryCode", PropertiesUtil.getMessageValueByKey("veryCode"));
		
		// 发送的手机号
		map.put("mobile", msisdn);
		
		// 填写短信内容
		String content = "@1@=" + userName + ",@2@=" + payfor + ",@3@=" + billCost + ",@4@=" + drugWinNo+ ",@5@=" + billNo;
		
		map.put("content", content);
		map.put("msgtype", PropertiesUtil.getMessageValueByKey("msgtype"));
		map.put("code", "utf-8");
				
		// 模板名
		map.put("tempid", PropertiesUtil.getMessageValueByKey("tempid10"));
		SmsClient.doPost(PropertiesUtil.getMessageValueByKey("url"), map);
	}
}

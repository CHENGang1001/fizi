package com.gq.common.utils.payment.wx;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.gq.business.payment.service.impl.PaymentServiceImpl;
import com.gq.common.utils.HttpUtils;
import com.gq.common.utils.StringUtils;
import com.gq.common.utils.payment.WxpaySearch;
import com.gq.common.utils.payment.WxpaySearchList;

public class WxPayment {
	// 日志测试文件
	private static Logger log = Logger.getLogger(WxPayment.class);

	/**
	 * 生成微信支付订单号
	 * @return 微信支付订单号
	 */
	public static String getOrderID() {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(sdf.format(new Date()));
		sb.append("wx");
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			sb.append(random.nextInt(10));
		}

		return sb.toString();
	}
	
	/**
	 * 封装微信支付参数
	 * @param param
	 * @return 微信支付所需参数
	 */
	public static Map<String, String> getWxParam(Map<String, Object> param) {
		Map<String, String> wxParam = new HashMap<String, String>();
		//应用id
		wxParam.put("appid", WxConstants.APPID);
		//商户号
		wxParam.put("mch_id", WxConstants.MCH_ID);
		//随机字符串
		wxParam.put("nonce_str", genNonceStr());
		//后台回调地址
		wxParam.put("notify_url", WxConstants.NOTIFY_URL);
		//订单号
		if(param.containsKey("orderId")) {
			wxParam.put("out_trade_no", param.get("orderId").toString());
		}
		//终端ip
		wxParam.put("spbill_create_ip", WxConstants.SPBILL_CREATE_IP);
		//支付金额 单位为分(前端传递过来的单位为元)
		if(param.containsKey("amount")) {
			wxParam.put("total_fee", String.format("%.0f", Float.parseFloat(param.get("amount").toString()) * 100));
		}
		//交易类型
		wxParam.put("trade_type", WxConstants.TRADE_TYPE);
		if(param.containsKey("payFor")) {
			String type = null;
			if (param.get("payFor").equals("1")) {
				type = "扬州市中医院预约缴费";
			} else if (param.get("payFor").equals("2")) {
				type = "扬州市中医院当日挂号缴费";
			} else if (param.get("payFor").equals("3")) {
				type = "扬州市中医院预约挂号缴费";
			} else if (param.get("payFor").equals("4")) {
				type = "扬州市中医院门诊缴费";
			} else if (param.get("payFor").equals("5")) {
				type = "扬州市中医院住院预交缴费";
			} else {
				type = "扬州市中医院其它操作";
			}
			wxParam.put("body", type);
		}
		return wxParam;
	}
	
	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String genNonceStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 生成微信支付所需签名
	 * @param params
	 * @param flag 
	 * @return 验签结果
	 */
	public static String createWeiSign(Map<String, String> params, int flag) {
		StringBuilder buf = new StringBuilder((params.size() + 1) * 100);
		buildPayParams(buf, params, false);
		if(flag==1){
			buf.append("&key=").append(WxConstants.KEY);
			System.out.println("wei key: " + WxConstants.KEY);
		}else{
			buf.append("&key=").append(WXPublicConstants.WX_PUBLIC_KEY);
			System.out.println("wei key: " + WXPublicConstants.WX_PUBLIC_KEY);
		}
		String preStr = buf.toString();
		String sign = "";
		// 获得签名验证结果
		System.out.println("wei preStr: " + preStr);
		try {
			sign = toMD5(preStr).toUpperCase();
		} catch (Exception e) {
			sign = toMD5(preStr).toUpperCase();
		}
		System.out.println("wei sign: " + sign);
		return sign;
	}
	
	/**
	 * 将参数按照参数名ASCII码从小到大排序
     * @param payParams 微信支付参数
     * @return
     */
    private static void buildPayParams(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            if(encoding){
                sb.append(urlEncode(payParams.get(key)));
            }else{
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }
    
    private static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        } 
    }
    
    /**
     * MD5验签算法
     * @param inStr 待验签字符串
     * @return 验签结果
     */
    private static String toMD5(String inStr) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inStr.getBytes("utf-8"));
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }
    
    /**
     * 将微信支付map格式数据转换为xml格式
     * @param paramMap
     * @return 带有CDATA标签的xml数据
     */
	@SuppressWarnings("unchecked")
	public static String toXml(Map<String, String> paramMap)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Object localObject;
      List list = new ArrayList(paramMap.keySet());
      Collections.sort(list);
      localObject = list;
      localStringBuilder.append("<xml>");
      Iterator localIterator = ((List)localObject).iterator();
      while (localIterator.hasNext())
      {
        localObject = (String)localIterator.next();
        localStringBuilder.append("<").append((String)localObject).append(">");
//        localStringBuilder.append("<![CDATA[").append((String)paramMap.get(localObject)).append("]]>");
        localStringBuilder.append((String)paramMap.get(localObject));
        localStringBuilder.append("</").append((String)localObject).append(">\n");
      }
      localStringBuilder.append("</xml>");
      return localStringBuilder.toString();
    }

	/**
	 * 获取微信对账URL
	 * @param i 
	 * @throws Exception 
	 * 
	 */
	public static WxpaySearchList queryOrderInfoByWeXin(String date, int i) throws Exception {
		WxpaySearchList list = new WxpaySearchList();
		String url = WxConstants.API_URL+WxConstants.DOWNLOADBILL_URL+"?";
		list = weixinPost(url,date,i); 
		return list;
	}
	
	/**
     * post发送请求数据
     * @param urlStr
	 * @return 
	 * @throws Exception 
     */
    public static  WxpaySearchList weixinPost(String urlStr,String date,int flag) throws Exception { 
    	 WxpaySearchList list = new WxpaySearchList();
         boolean isOK = false;
         Map<String, String> wxParam = new HashMap<String, String>();
       //生成签名
         if(flag==1){
        	 wxParam.put("appid", WxConstants.APPID);
        	 wxParam.put("mch_id", WxConstants.MCH_ID);
         }else if(flag==2){
        	 wxParam.put("appid", WXPublicConstants.WX_PUBLIC_APPID);
        	 wxParam.put("mch_id", WXPublicConstants.WX_PUBLIC_MCH_ID);
         }
        
        wxParam.put("nonce_str", genNonceStr());
        wxParam.put("bill_date", date);
        wxParam.put("bill_type", "ALL");
		String sign = createWeiSign(wxParam,flag);
		wxParam.put("sign", sign);
		//将map类型数据转换为xml格式
		String entity = toXml(wxParam);
		//向微信发送请求
		String content = HttpUtils.sendPost(WxConstants.API_URL + WxConstants.DOWNLOADBILL_URL, entity);
		//将返回的xml结果转换为map格式
		//wxParam = XmlUtils.readStringXmlOut(content);
		if(content!=null){
			if(content.indexOf("FAIL")!=-1||content.indexOf("No Bill Exist")!=-1){
				return null;
			}
			list= analyze(content);
		}else{
			return null;
		}
		return list;
       /*  Map<String, String> wxParam = new HashMap<String, String>();
 		wxParam.put("appid", WxConstants.APPID);
 		wxParam.put("mch_id", WxConstants.MCH_ID);
 		wxParam.put("nonce_str", genNonceStr());
 		wxParam.put("notify_url", WxConstants.NOTIFY_URL);
        String A = "appid="+WxConstants.APPID+
        			"&bill_date="+date+
        			"&mch_id="+WxConstants.MCH_ID+
        			"&nonce_str="+genNonceStr()+
        			"key="+WxConstants.KEY;
        String sign = toMD5(A).toUpperCase(); ;
        String xmlInfo = GetpayXmlRequest(WxConstants.APPID,WxConstants.MCH_ID,genNonceStr(),sign,date,"ALL"); 
        
        System.out.println("urlStr=" + urlStr); 
        System.out.println("xmlInfo=" + xmlInfo); 
        
        String result = HttpUtils.sendPost(urlStr,xmlInfo);
        list= readTxt(result);
        
        out.write(new String(xmlInfo.getBytes("UTF-8")));
        out.flush(); 
        // out.close(); 
        //读取服务器的响应内容并显示 定义BufferedReader输入流来读取URL的响应
        br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8")); 
       String line = ""; 
       StringBuffer buffer = new StringBuffer();
       while ((line = br.readLine()) != null){  
	        buffer.append(line); // 将读到的内容添加到 buffer 中
	        buffer.append("\n"); // 添加换行符
	        System.out.println(line);
       }  */
	} 
    
    private static WxpaySearchList analyze(String content) {
		if(StringUtils.isEmpty(content)) {
			return null;
		}
		WxpaySearchList result = new WxpaySearchList();
		List<WxpaySearch> exlist = new ArrayList<WxpaySearch>();
		//String[] array = content.split("\n");
		//String newStr = content.replaceAll(",`", " "); // 去空格
		String newStr = content.replaceAll(",", " ");
	    String[] tempStr = newStr.split("`"); // 数据分组
	    String[] title = tempStr[0].split(" ");// 分组标题
		//int size = length - 3;//对账数据条数，去除第一行标题行和末尾两行汇总信息行
		int size = tempStr.length / title.length; // 计算循环次数
		int count = 1;
		
		String[] detail = null;
		while (size -- > 0)
		{
			Map<String, String> map = new HashMap<String, String>();
			for(int index = 0; index < title.length; index++) {
				detail = tempStr[count].split("`");
				if(index == title.length - 1) {
					title[index] = title[index].replace("\r", "");
					detail[0] = detail[0].replace("\r", "");
				}
				if(index==0){
					title[index]="交易时间";
					map.put(title[index], detail[0].trim());
				}else{
					map.put(title[index], detail[0].replace(" ", ""));
				}
				count++;
			}
			exlist.add(getWxpaySearch(map));
			
		}
		System.out.println("analyze::<" + exlist + ">>>>>>>>>>>>>>>>>>>end>>>>>>>>");
		result.setList(exlist);
		Map<String, String> map1 = new HashMap<String, String>();
		int length1 = tempStr.length-6;
		String[] title1 = tempStr[length1].split(" ");
		String[] detail1 = null;
		title1[0] = title1[0].replace("0.00%", "");
		title1[0] = title1[0].replace("\r", "");
		for(int index = 0; index < title1.length; index++) {
			detail1 = tempStr[count].split("`");
			if(index == title1.length - 1) {
				detail1[0] = detail1[0].replace("\r", "");
			}
			map1.put(title1[index], detail1[0].replace(" ", ""));
			count++;
		}
		//总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额
		String af = map1.get("手续费总金额");
		Double afe =Double.parseDouble(af);
		result.setAllfee(afe);
		String apa = map1.get("总交易额");
		Double apam =Double.parseDouble(apa);
		result.setAllpayamount(apam);
		String ara = map1.get("总企业红包退款金额");
		Double aram =Double.parseDouble(ara);
		result.setAllredMoney(aram);
		String arfa = map1.get("总退款金额");
		Double arfam =Double.parseDouble(arfa);
		result.setAllrefundamount(arfam);
		result.setAlltotal(map1.get("总交易单数"));
		
		return result;
	}
    
    private static WxpaySearch getWxpaySearch(Map<String, String> map) {
    	WxpaySearch bean = new WxpaySearch();
    	String ot = map.get("交易时间");
		bean.setOrderTime(ot);
		bean.setAppId(map.get("公众账号ID"));
		bean.setMchId(map.get("商户号"));
		bean.setKidMchId(map.get("子商户号"));
		bean.setPosId(map.get("设备号"));
		bean.setWxOrderId(map.get("微信订单号"));
		bean.setOrderId(map.get("商户订单号"));
		bean.setOpenId(map.get("用户标识"));
		bean.setPayType(map.get("交易类型"));
		bean.setPayStatus(map.get("交易状态"));
		bean.setPayBank(map.get("付款银行"));
		bean.setMoneyType(map.get("货币种类"));
		String am= map.get("总金额");
		Double allm =Double.parseDouble(am);
		bean.setAllMoney(allm);
		String rm= map.get("企业红包金额");
		Double redm =Double.parseDouble(rm);
		bean.setRedMoney(redm);
		bean.setWxRefundId(map.get("微信退款单号"));
		bean.setRefundId(map.get("商户退款单号"));
		String outcome = map.get("退款金额");
		Double outc =Double.parseDouble(outcome);
		bean.setOutcome(outc);
		bean.setRedRefundMoney(map.get("企业红包退款金额"));
		bean.setRefundType(map.get("退款类型"));
		bean.setRefundStatus(map.get("退款状态"));
		bean.setBody(map.get("商品名称"));
		bean.setAttach(map.get("商户数据包"));
		bean.setFee(map.get("手续费"));
		bean.setRates(map.get("费率"));
		
		if("".equals(outcome) || "0".equals(outcome) || "0.00".equals(outcome)) {
			//充值
			bean.setState("1");
		} else {
			//退费
			bean.setState("0");
		}
		return bean;
	}
    
	/**
	 * 时间戳
	 * @return 自1970年1月1日 0点0分0秒以来的秒数
	 */
	public static String getTimeStamp() {
		long time = System.currentTimeMillis() / 1000;
		return String.valueOf(time);
	}

	 //微信退款
	@SuppressWarnings("deprecation")
	public static String doRefund(String url, String data) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(
				new File(WxConstants.BOOK_URL));// P12文件目录
		try {
			keyStore.load(instream, WxConstants.MCH_ID.toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WxConstants.MCH_ID.toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpost = new HttpPost(url); // 设置响应头信息
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
	
	//微信退费
	public static int wxRefundOrder(String amount, String orderId, String refundAccount) {
		int result ;
		Map<String, String> wxpayParam = new HashMap<String, String>();
		// appid
		wxpayParam.put("appid", WxConstants.APPID);
		// 商户号
		wxpayParam.put("mch_id", WxConstants.MCH_ID);
		//微信退款来源
		wxpayParam.put("refund_account", refundAccount);
		// 商户订单号
		wxpayParam.put("out_trade_no", orderId);
		// 商户退款单号，确保唯一性
		wxpayParam.put("out_refund_no", orderId + "Refund");
		// 总金额，单位为分
		String totalFee = String.valueOf(Double.parseDouble(amount) * 100);
		wxpayParam.put("total_fee", totalFee.substring(0, totalFee.indexOf(".")));
		// 退款金额，单位为分
		String refundFee = String.valueOf(Double.parseDouble(amount) * 100);
		wxpayParam.put("refund_fee", refundFee.substring(0, refundFee.indexOf(".")));
		// 操作员，默认为商户号
		wxpayParam.put("op_user_id", WxConstants.MCH_ID);
		// 随机字符串
		wxpayParam.put("nonce_str", WxPayment.genNonceStr());
		// 签名
		String sign = WxPayment.createWeiSign(wxpayParam,1);
		wxpayParam.put("sign", sign);

		result = refund(wxpayParam);
		return result;
	}
	
	private static int refund(Map<String, String> param) {
		// 0、退款失败 1、退款成功 2、未结算资金不足，退款失败
		int isOk = 0;
		Map<String, String> contentMap = new HashMap<String, String>();
		if (null == param.get("out_trade_no") && null == param.get("trade_no")) {
			return 0;
		}
		if (null == param.get("refund_fee")) {
			return 0;
		}
		// appid
		if (null != param.get("appid")) {
			contentMap.put("appid", param.get("appid").toString());
		}
		// 商户号
		if (null != param.get("mch_id")) {
			contentMap.put("mch_id", param.get("mch_id").toString());
		}
		//退款资金来源
		if (null != param.get("refund_account")) {
			contentMap.put("refund_account", param.get("refund_account").toString());
		}
		// 商户订单号
		if (null != param.get("out_trade_no")) {
			contentMap.put("out_trade_no", param.get("out_trade_no").toString());
		}
		// 微信订单号
		if (null != param.get("transaction_id")) {
			contentMap.put("transaction_id", param.get("transaction_id").toString());
		}
		// 商户退款单号
		if (null != param.get("out_refund_no")) {
			contentMap.put("out_refund_no", param.get("out_refund_no").toString());
		}
		// 总金额
		if (null != param.get("total_fee")) {
			contentMap.put("total_fee", param.get("total_fee").toString());
		}
		// 退款金额
		if (null != param.get("refund_fee")) {
			contentMap.put("refund_fee", param.get("refund_fee").toString());
		}
		// 操作员，默认商户号
		if (null != param.get("op_user_id")) {
			contentMap.put("op_user_id", param.get("op_user_id").toString());
		}
		// 随机字符串
		if (null != param.get("nonce_str")) {
			contentMap.put("nonce_str", param.get("nonce_str").toString());
		}
		// 签名
		if (null != param.get("sign")) {
			contentMap.put("sign", param.get("sign").toString());
		}
		String xml = "<xml>" 
				+ "<appid>" + contentMap.get("appid") +"</appid>"
				+ "<refund_account>" + contentMap.get("refund_account") + "</refund_account>" 
				+ "<mch_id>" + contentMap.get("mch_id")+ "</mch_id>" 
				+ "<nonce_str>" + contentMap.get("nonce_str") + "</nonce_str>" 
				+ "<sign><![CDATA["+ contentMap.get("sign") + "]]></sign>" 
				+ "<out_trade_no>" + contentMap.get("out_trade_no")+ "</out_trade_no>" 
				+ "<out_refund_no>" + contentMap.get("out_refund_no") + "</out_refund_no>"
				+ "<total_fee>" + contentMap.get("total_fee") + "</total_fee>" 
				+ "<refund_fee>"+ contentMap.get("refund_fee") + "</refund_fee>" 
				+ "<op_user_id>" + contentMap.get("op_user_id")+ "</op_user_id>" 
				+ "</xml>";
		String url = WxConstants.req_url;
		log.info("商户订单号:"+contentMap.get("out_trade_no")+" 商户退款单号:"+contentMap.get("out_refund_no")+"总金额:"+contentMap.get("total_fee")+"退款金额:"+contentMap.get("refund_fee")+"退款资金来源："+contentMap.get("refund_account")+"appid"+" 微信退款url=====》"+url);
		try {
			String content = WxPayment.doRefund(url, xml);
			Map<String, String> map = XmlUtils.readStringXmlOut(content);
			log.info("商户订单号:"+contentMap.get("out_trade_no")+"微信退款返回信息=====》"+map.get("return_code"));
			if ("SUCCESS".equals(map.get("return_code"))) {
				if ("SUCCESS".equals(map.get("result_code"))) {
					// 退款成功
					isOk = 1;
					log.info("订单号:"+contentMap.get("out_trade_no")+" 微信退款成功resultcode=====》"+map.get("result_code"));
				}else if ("FAIL".equals(map.get("result_code")) && "NOTENOUGH".equals(map.get("err_code"))) {
					//未结算资金不足，退款失败
					isOk = 2;
					log.info("订单号:"+contentMap.get("out_trade_no")+" 微信退款失败resultcode=====>"+map.get("result_code")+",退款失败第三方代码："+map.get("err_code"));
				}else{
				    log.info("--退款失败其余原因(1-return:SUCCESS 2-return_code:FAIL)--");
				    if("SUCCESS".equals(map.get("return_code"))){
				    	log.info("订单号："+contentMap.get("out_trade_no")+"退款失败：业务代码："+map.get("err_code")+" | 错误代码描述："+map.get("err_code_des"));
			            
				    	if("SYSTEMERROR".equals(map.get("err_code"))){
				    	  Thread.sleep(1000); 
					      log.info("再次退款---订单号："+contentMap.get("out_trade_no")+"进行再次退款");
						  String content1 = WxPayment.doRefund(url, xml);
						  Map<String, String> map1 = XmlUtils.readStringXmlOut(content1);
						if("SUCCESS".equals(map1.get("return_code"))){
							if ("SUCCESS".equals(map1.get("result_code"))) {
								// 退款成功
								isOk = 1;
								log.info("再次退款---订单号:"+contentMap.get("out_trade_no")+" 微信退款成功resultcode=====》"+map1.get("result_code"));
							}else if ("FAIL".equals(map1.get("result_code")) && "NOTENOUGH".equals(map1.get("err_code"))) {
								//未结算资金不足，退款失败
								isOk = 2;
								log.info("再次退款---订单号:"+contentMap.get("out_trade_no")+" 微信退款失败resultcode=====>"+map1.get("result_code")+",退款失败第三方代码："+map1.get("err_code"));
							}else{
							    log.info("再次退款---订单号:"+contentMap.get("out_trade_no")+"--退款失败其余原因(1-return:SUCCESS 2-return_code:FAIL)--");
							}
						 }
			            }	
				    	//log.info("业务代码："+map.get("err_code")+" | 错误代码描述："+map.get("err_code_des"));
				    }else{
				    	log.info("订单号："+contentMap.get("out_trade_no")+"return_code:"+map.get("return_code")+" | return_msg:"+map.get("return_msg"));
				    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOk;
	}
}

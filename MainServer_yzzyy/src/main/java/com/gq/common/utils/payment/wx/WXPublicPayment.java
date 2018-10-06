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

import com.gq.business.payment.service.impl.WXPaymentServiceImpl;

public class WXPublicPayment {
	// 日志测试文件
		private static Logger log = Logger.getLogger(WXPaymentServiceImpl.class);

	/**
	 * 封装微信公众号支付参数
	 * 
	 * @param param
	 * @return 微信公众号支付所需参数
	 */
	public static Map<String, String> getWXPublicParam(Map<String, Object> param) {
		Map<String, String> wxParam = new HashMap<String, String>();
		// 应用id
		wxParam.put("appid", WXPublicConstants.WX_PUBLIC_APPID);
		// 商户号
		wxParam.put("mch_id", WXPublicConstants.WX_PUBLIC_MCH_ID);
		// 随机字符串
		wxParam.put("nonce_str", genNonceStr());
		// 商品描述
		if (param.containsKey("payFor")) {
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
		// 订单号
		if (param.containsKey("orderId")) {
			wxParam.put("out_trade_no", param.get("orderId").toString());
		}
		// 支付金额 单位为分(前端传递过来的单位为元)
		if (param.containsKey("amount")) {
			//String totalFee = String.valueOf(Float.parseFloat(param.get("amount").toString().trim()) * 100);
			//wxParam.put("total_fee", totalFee.substring(0, totalFee.indexOf(".")));
			wxParam.put("total_fee", String.format("%.0f", Float.parseFloat(param.get("amount").toString()) * 100));
		}
		// 用户端ip
		wxParam.put("spbill_create_ip", param.get("spbill_create_ip").toString());
		// 后台回调地址
		wxParam.put("notify_url", WXPublicConstants.WX_PUBLIC_NOTIFY_URL);
		// 交易类型
		wxParam.put("trade_type", WXPublicConstants.WX_PUBLIC_TRADE_TYPE);

		// openid trade_type=JSAPI时（即公众号支付），此参数必传
		if (param.containsKey("openid")) {
			wxParam.put("openid", param.get("openid").toString());
		}

		return wxParam;
	}

	/**
	 * 获取随机字符串
	 * 
	 * @return
	 */
	public static String genNonceStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 生成微信支付订单号
	 * @return 微信支付订单号
	 */
	public static String getOrderID() {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(sdf.format(new Date()));
		sb.append("wxp");
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			sb.append(random.nextInt(10));
		}

		return sb.toString();
	}

	/**
	 * 微信公众号退款
	 * @param amount
	 * @param orderId
	 * @param refundAccount
	 * @return
	 */
	public static int wxRefundOrder(String amount, String orderId) {
		int result;
		Map<String, String> wxpayParam = new HashMap<String, String>();
		// appid
		wxpayParam.put("appid", WXPublicConstants.WX_PUBLIC_APPID);
		// 商户号
		wxpayParam.put("mch_id", WXPublicConstants.WX_PUBLIC_MCH_ID);
		// 微信退款来源
		// wxpayParam.put("refund_account", refundAccount);
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
		wxpayParam.put("op_user_id", WXPublicConstants.WX_PUBLIC_MCH_ID);
		// 随机字符串
		wxpayParam.put("nonce_str", WXPublicPayment.genNonceStr());
		// 签名
		String sign = WXPublicPayment.createWeiSign(wxpayParam);
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
		// 退款资金来源
		/*if (null != param.get("refund_account")) {
			contentMap.put("refund_account", param.get("refund_account").toString());
		}*/
		// 商户订单号
		if (null != param.get("out_trade_no")) {
			contentMap.put("out_trade_no", param.get("out_trade_no").toString());
		}
		// 微信订单号
		/*if (null != param.get("transaction_id")) {
			contentMap.put("transaction_id", param.get("transaction_id").toString());
		}*/
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
		String xml = "<xml>" + "<appid>" + contentMap.get("appid") + "</appid>" + "<mch_id>" + contentMap.get("mch_id")
				+ "</mch_id>" + "<nonce_str>" + contentMap.get("nonce_str") + "</nonce_str>" + "<sign><![CDATA["
				+ contentMap.get("sign") + "]]></sign>" + "<out_trade_no>" + contentMap.get("out_trade_no")
				+ "</out_trade_no>" + "<out_refund_no>" + contentMap.get("out_refund_no") + "</out_refund_no>"
				+ "<total_fee>" + contentMap.get("total_fee") + "</total_fee>" + "<refund_fee>"
				+ contentMap.get("refund_fee") + "</refund_fee>" + "<op_user_id>" + contentMap.get("op_user_id")
				+ "</op_user_id>" + "</xml>";
		String url = WXPublicConstants.WX_PUBLIC_req_url;
		try {
			String content = WXPublicPayment.doRefund(url, xml);
			Map<String, String> map = XmlUtils.readStringXmlOut(content);
			log.info("商户订单号："+contentMap.get("out_trade_no")+" return_code:"+map.get("return_code")+" 商户退款单号："+contentMap.get("out_refund_no")+" 总金额："+contentMap.get("total_fee")+" 退款金额："+contentMap.get("refund_fee"));
			if ("SUCCESS".equals(map.get("return_code"))) {
				if ("SUCCESS".equals(map.get("result_code"))) {
					log.info("商户订单号："+contentMap.get("out_trade_no")+"--退款返回结果result_code:"+map.get("result_code"));
					// 退款成功
					isOk = 1;
				} else if ("FAIL".equals(map.get("result_code")) && "NOTENOUGH".equals(map.get("err_code"))) {
					log.info("商户订单号："+contentMap.get("out_trade_no")+"--退款返回结果result_code:"+map.get("result_code")+"--err_code:"+map.get("err_code")+"--未结算资金不足，退款失败");
					// 未结算资金不足，退款失败
					isOk = 2;
				}else{
				    log.info("--退款失败其余原因(1-return:SUCCESS 2-return_code:FAIL)--");
				    if("SUCCESS".equals(map.get("return_code"))){
				    	log.info(map.get("业务代码："+"err_code")+" | 错误代码描述："+map.get("err_code_des"));
				    }else{
				    	log.info("return_code:"+map.get("return_code")+" | return_msg:"+map.get("return_msg"));
				    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

	// 微信退款
	@SuppressWarnings("deprecation")
	public static String doRefund(String url, String data) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(WXPublicConstants.WX_PUBLIC_BOOK_URL));// P12文件目录
		try {
			keyStore.load(instream, WXPublicConstants.WX_PUBLIC_MCH_ID.toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, WXPublicConstants.WX_PUBLIC_MCH_ID.toCharArray()).build();
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

	/**
	 * 生成微信支付所需签名
	 * @param params
	 * @return 验签结果
	 */
	public static String createWeiSign(Map<String, String> params) {
		StringBuilder buf = new StringBuilder((params.size() + 1) * 100);
		buildPayParams(buf, params, false);
		buf.append("&key=").append(WXPublicConstants.WX_PUBLIC_KEY);
		System.out.println("wei key: " + WXPublicConstants.WX_PUBLIC_KEY);
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

    /**
     * urlEncode编码
     * @param str
     * @return
     */
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
	 * 时间戳
	 * @return 自1970年1月1日 0点0分0秒以来的秒数
	 */
	public static String getTimeStamp() {
		long time = System.currentTimeMillis() / 1000;
		return String.valueOf(time);
	}
}

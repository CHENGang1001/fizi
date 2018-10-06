package com.gq.common.utils.payment.CCB;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gq.business.payment.model.TradingRecordBean;
import com.gq.common.utils.payment.CcbSearch;
import com.gq.common.utils.payment.CcbSearchList;
import com.gq.common.utils.payment.wx.XmlUtils;

public class CCBPayment {
	private static Logger logger = Logger.getLogger(CCBPayment.class);
	/**
	 * 网上支付数字签名验证示例
	 * 
	 * @param strSrc
	 * @param strSign
	 * @param strPubKey
	 * @param request
	 */
	public void virify(String strSrc, String strSign, String strPubKey, HttpServletRequest request) {
		boolean bRet;
		RSASig rsa = new RSASig();
		String strRet;
		String path = "";
		String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		if (strSrc == null) {
			strSrc = "";
		}
		if (strSign == null) {
			strSign = "";
		}
		if (strPubKey == null) {
			strPubKey = "";
		}
		rsa.setPublicKey(strPubKey);
		bRet = rsa.verifySigature(strSign, strSrc);
		if (bRet) {
			strRet = "Y";
		} else {
			strRet = "N";
		}
	}
	
	public String initPayment() {
		String result = null;
		
		return result;
	}
	
	public static String getOrderID() {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sb.append(sdf.format(new Date()));
		Random random = new Random();
		for (int i = 0; i < 2; i++) {
			sb.append(random.nextInt(10));
		}

		return sb.toString();
	}
	
	public static String getOrderID(int num) {
		StringBuilder sb = new StringBuilder(3);
		sb.append(System.currentTimeMillis());
		Random random = new Random();
		for (int i = 0; i < num; i++) {
			sb.append(random.nextInt(10));
		}

		return sb.toString();
	}
	
	public static String generateOrder(TradingRecordBean trb, String gateway)

	{
		String res = "";
		String subject = "";
		String body = "";
		String totalFee = trb.getAmount();
		// 1：预约支付、2：当日挂号支付、3：预约挂号支付、4：门诊缴费、5：住院预交
		if (trb.getPayFor().equals("1")) {
			subject = "扬州市中医院预约缴费";
			body = "扬州市中医院预约缴费";
		} else if (trb.getPayFor().equals("2")) {
			subject = "扬州市中医院当日挂号缴费";
			body = "扬州市中医院当日挂号缴费";
		} else if (trb.getPayFor().equals("3")) {
			subject = "扬州市中医院预约挂号缴费";
			body = "扬州市中医院预约挂号缴费";
		} else if (trb.getPayFor().equals("4")) {
			subject = "扬州市中医院门诊缴费";
			body = "扬州市中医院门诊缴费";
		} else if (trb.getPayFor().equals("5")) {
			subject = "扬州市中医院住院预交缴费";
			body = "扬州市中医院住院预交缴费";
		} else {
			subject = "扬州市中医院其它操作";
			body = "扬州市中医院其它操作";
		}
		if ("1".equals(trb.getPayType())) {
			res = generateCcbOrder(trb.getOrderId(), body, totalFee, gateway);
		}
		return res;

	}
	
	/**
	 * 拼接建行支付URL
	 * @param inTradeNo 内部流水号
	 * @param body 商品信息 
	 * @param totalFee 费用
	 * @return 支付URL
	 */
	private static String generateCcbOrder(String inTradeNo, String body, String totalFee, String gateway) {
		String res = "";
		if(StringUtils.isEmpty(totalFee) || StringUtils.isEmpty(inTradeNo)) {
			return null;
		}
		//商户代码
		res += "MERCHANTID=" + CCBConstants.MERCHANTID + "&";
		//商户柜台代码
		res += "POSID=" + CCBConstants.POSID + "&";
		//分行代码
		res += "BRANCHID=" + CCBConstants.BRANCHID + "&";
		//内部流水号
		res += "ORDERID=" + inTradeNo + "&";
		//付款金额
		res += "PAYMENT=" + totalFee + "&";
		//币种
		res += "CURCODE=" + CCBConstants.CURCODE + "&";
		//交易码
		res += "TXCODE=" + CCBConstants.TXCODE + "&";
		//备注1
		res += "REMARK1=" + "&";
		//备注2
		res += "REMARK2=" + "&";
		//接口类型 1防钓鱼 0非钓鱼
		res += "TYPE=" + CCBConstants.TYPE + "&";
		//公钥后30位
		String tmp = res;
		tmp += "PUB=" + CCBConstants.PUB + "&";
		//网关类型
		res += "GATEWAY=" + gateway + "&";
		tmp += "GATEWAY=" + gateway + "&";
		//客户端ip
		res += "CLIENTIP=" + "&";
		tmp += "CLIENTIP=" + "&";
		//商户注册信息
		res += "REGINFO=" + escape(CCBConstants.REGINFO) + "&";
		tmp += "REGINFO=" + escape(CCBConstants.REGINFO) + "&";
		//商品信息
		res += "PROINFO=" + escape(body) + "&";
		tmp += "PROINFO=" + escape(body) + "&";
		//商户域名
		//商户域名
		res += "REFERER=" + CCBConstants.REFERER;
		tmp += "REFERER=" + CCBConstants.REFERER;
		//MAC校验域
		res += "&MAC=" + toMD5(tmp);
		//建行支付网址
		res = CCBConstants.URL + "?" + res;
		
		return res;
	}
	
	/**
	 * 获取建行支付URL
	 * @param inTradeNo 内部流水号
	 * @param body 商品信息 
	 * @param totalFee 费用
	 * @return 支付URL
	 */
	public static String getOrder(TradingRecordBean trb) {
		String res = "";
		String subject = "";
		String body = "";
		String totalFee = trb.getAmount();
		// 1：预约支付、2：当日挂号支付、3：预约挂号支付、4：门诊缴费、5：住院预交
			subject = "扬州中医院预约退费";
			body = "扬州中医院预约退费";
		if ("1".equals(trb.getPayType())) {
			res = getCcbOrder(trb.getOrderId(), body, totalFee);
		}
		return res;
	}
	
	/**
	 * 获取建行支付URL
	 * @param inTradeNo 内部流水号
	 * @param body 商品信息 
	 * @param totalFee 费用
	 * @return 支付URL
	 */
	private static String getCcbOrder(String inTradeNo, String body, String totalFee) {
		String res = "";
		if(StringUtils.isEmpty(totalFee) || StringUtils.isEmpty(inTradeNo)) {
			return null;
		}
		//商户代码
		res += "CUST_ID=" + inTradeNo + "&";
		//商户柜台代码
		res += "POSID=" + CCBConstants.POSID + "&";
		//分行代码
		res += "BRANCHID=" + CCBConstants.BRANCHID + "&";
		//内部流水号
		res += "ORDER=" + inTradeNo + "&";
		//退款金额
		res += "MONEY=" + totalFee + "&";
		//币种
		res += "CURCODE=" + CCBConstants.CURCODE + "&";
		//交易码
		res += "TXCODE=" + CCBConstants.TXCODE + "&";
		//备注1
		res += "REMARK1=" + "&";
		//备注2
		res += "REMARK2=" + "&";
		//接口类型 1防钓鱼 0非钓鱼
		res += "TYPE=" + CCBConstants.TYPE + "&";
		//公钥后30位
		String tmp = res;
		tmp += "PUB=" + CCBConstants.PUB + "&";
		//网关类型
		res += "GATEWAY=" + "&";
		tmp += "GATEWAY=" + "&";
		//客户端ip
		res += "CLIENTIP=" + "&";
		tmp += "CLIENTIP=" + "&";
		//商户注册信息
		res += "REGINFO=" + escape(CCBConstants.REGINFO) + "&";
		tmp += "REGINFO=" + escape(CCBConstants.REGINFO) + "&";
		//商品信息
		res += "PROINFO=" + escape(body) + "&";
		tmp += "PROINFO=" + escape(body) + "&";
		//商户域名
		res += "REFERER=";
		tmp += "REFERER=";
		//MAC校验域
		res += "&MAC=" + toMD5(tmp);
		//建行支付网址
		res = CCBConstants.URL + "?" + res;
		
		return res;
	}
	
	public static String toMD5(String inStr) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inStr.getBytes());
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

	
	//escape编码
	public static String escape(String src) {  
        int i;  
        char j;  
        StringBuffer tmp = new StringBuffer();  
        tmp.ensureCapacity(src.length() * 6);  
        for (i = 0; i < src.length(); i++) {  
            j = src.charAt(i);  
            if (Character.isDigit(j) || Character.isLowerCase(j)  
                    || Character.isUpperCase(j))  
                tmp.append(j);  
            else if (j < 256) {  
                tmp.append("%");  
                if (j < 16)  
                    tmp.append("0");  
                tmp.append(Integer.toString(j, 16));  
            } else {  
                tmp.append("%u");  
                tmp.append(Integer.toString(j, 16));  
            }  
        }  
        return tmp.toString();  
    }  
 
	//escape反编码
    public static String unescape(String src) {  
        StringBuffer tmp = new StringBuffer();  
        tmp.ensureCapacity(src.length());  
        int lastPos = 0, pos = 0;  
        char ch;  
        while (lastPos < src.length()) {  
            pos = src.indexOf("%", lastPos);  
            if (pos == lastPos) {  
                if (src.charAt(pos + 1) == 'u') {  
                    ch = (char) Integer.parseInt(src  
                            .substring(pos + 2, pos + 6), 16);  
                    tmp.append(ch);  
                    lastPos = pos + 6;  
                } else {  
                    ch = (char) Integer.parseInt(src  
                            .substring(pos + 1, pos + 3), 16);  
                    tmp.append(ch);  
                    lastPos = pos + 3;  
                }  
            } else {  
                if (pos == -1) {  
                    tmp.append(src.substring(lastPos));  
                    lastPos = src.length();  
                } else {  
                    tmp.append(src.substring(lastPos, pos));  
                    lastPos = pos;  
                }  
            }  
        }  
        return tmp.toString();  
    }

    /**
	 * 获取建行对账URL
	 * @param inTradeNo 内部流水号
	 * @param body 商品信息 
	 * @param totalFee 费用
	 * @return 支付URL
     * @throws Exception 
     * @throws IOException 
	 */
	public static CcbSearchList queryOrderInfoByCcb(String orderDate,String type) throws Exception,IOException { 
		logger.setLevel(Level.DEBUG);
    	CcbSearchList list = new CcbSearchList();  
        String baseUrl = CCBConstants.dz_URL + "?";  
        String MERCHANTID = CCBConstants.MERCHANTID;  
        String BRANCHID = CCBConstants.BRANCHID;  
        String POSID = CCBConstants.POSID;  //柜台号
        String ORDERDATE = orderDate.replaceAll("-","");  //定单日期  ;"20190830";//
        String BEGORDERTIME = "00:00:00";  //定单开始时间
        String ENDORDERTIME = "23:59:59";  //定单截止时间
        String TXCODE = "410408"; 
        String SEL_TYPE = "3";//查询方式,1页面形式  2文件返回形式 (提供TXT和XML格式文件的下载)   3 XML页面形式
        String ORDERID = "";
        String BEGORDERID="";
        String ENDORDERID="";
        String QUPWD = "111111";  // 查询密码
        String TYPE = type;//0支付流水  1退款流水
        String KIND = "1";//0 未结算流水  1 已结算流水
        String STATUS = "1";//0失败  1成功  2不确定  3全部（已结算流水查询不支持全部）
       
        String PAGE = "1";      //  String BEGORDERID = this.getBankPwd();  
        String OPERATOR  = "";// 预留字段 
        String CHANNEL = ""; //  预留字段
        
        String url1 = "MERCHANTID=" + MERCHANTID + "&BRANCHID="+BRANCHID +"&POSID="+POSID +"&ORDERDATE="+ORDERDATE
    			+ "&BEGORDERTIME="+BEGORDERTIME +"&ENDORDERTIME="+ENDORDERTIME +"&ORDERID=" + "&QUPWD="+QUPWD + "&TXCODE="+TXCODE
    			+ "&TYPE="+TYPE + "&KIND="+KIND +"&STATUS="+STATUS +"&SEL_TYPE="+SEL_TYPE +"&PAGE=" + PAGE +"&OPERATOR="+OPERATOR+"&CHANNEL=" ;
        
        String param ="MERCHANTID=" + MERCHANTID+ "&BRANCHID=" + BRANCHID+ "&POSID=" + POSID 
   			 + "&ORDERDATE=" + ORDERDATE+ "&BEGORDERTIME=" + BEGORDERTIME+ "&ENDORDERTIME=" + ENDORDERTIME 
   			 + "&ORDERID=" + ORDERID+ "&QUPWD=&TXCODE=" + TXCODE + "&TYPE=" + TYPE
   			 + "&KIND=" + KIND+ "&STATUS=" + STATUS+ "&SEL_TYPE=" + SEL_TYPE  
   			 + "&PAGE=" + PAGE + "&OPERATOR=" + OPERATOR + "&CHANNEL=" + CHANNEL ;
        
		String MAC = MD5.md5Str(param);
		String url = baseUrl  + url1 + "&MAC="+MAC;
		System.out.println("url========="+url);
		int count = getCountOrderByUrl(url);//查询一次,得到总记录数
		System.out.println("查询一次,得到总记录数:count========="+count);
		logger.info("查询一次,得到总记录数:count========="+count);
		if(count !=-1){
			//因为银行返回的xml文件最大200条,所以要进行分页来获取
			int page = 1;
			if (count % 200 == 0){
				page = count / 200;
			}else{
				page = count / 200 + 1;
			}
			//如果小于200条,直接发送url;如果大于200,循环多次发送请求
			for(int i=1; i<=page; i++){
				String tempUrl = "MERCHANTID=" + MERCHANTID + "&BRANCHID="+BRANCHID +"&POSID="+POSID +"&ORDERDATE="+ORDERDATE
					+ "&BEGORDERTIME="+BEGORDERTIME +"&ENDORDERTIME="+ENDORDERTIME +"&ORDERID=" + "&QUPWD="+QUPWD + "&TXCODE="+TXCODE
					+ "&TYPE="+TYPE + "&KIND="+KIND +"&STATUS="+STATUS +"&SEL_TYPE="+SEL_TYPE +"&PAGE=" + i +"&OPERATOR="+OPERATOR+"&CHANNEL=" ;
				
				String param1 ="MERCHANTID=" + MERCHANTID+ "&BRANCHID=" + BRANCHID+ "&POSID=" + POSID 
			   			 + "&ORDERDATE=" + ORDERDATE+ "&BEGORDERTIME=" + BEGORDERTIME+ "&ENDORDERTIME=" + ENDORDERTIME 
			   			 + "&ORDERID=" + ORDERID+ "&QUPWD=&TXCODE=" + TXCODE + "&TYPE=" + TYPE
			   			 + "&KIND=" + KIND+ "&STATUS=" + STATUS+ "&SEL_TYPE=" + SEL_TYPE  
			   			 + "&PAGE=" + i+ "&OPERATOR=" + OPERATOR + "&CHANNEL=" + CHANNEL ;
				String MAC1 = MD5.md5Str(param1);
				String url3 = baseUrl+ tempUrl + "&MAC="+MAC1;
				System.out.println("url3========="+url3);
				logger.info("向银行请求所生成的url:"+url3);
				list = sendTOUrl(url3);
				logger.info("list============"+list);
			}
			 return list;  
		}else{
			System.out.println("获取总记录数异常（没有数据 ）!");
			logger.error("获取总记录数异常（没有数据 ）!");
			return null;
		}
   }
	

	public static CcbSearchList readXML(String str){
		CcbSearchList result = new CcbSearchList();
		List<CcbSearch> exlist = new ArrayList<CcbSearch>();
		// 采用 Document方式解析XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// 通过解析器工厂创建解析 器
			DocumentBuilder db = dbf.newDocumentBuilder();
			// 将该字符串转为InputStream流
			//InputStream iStream = new ByteArrayInputStream(str.getBytes("GB2312"));
			InputStream iStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
			// 获取xml Document
			Document dm = db.parse(iStream);
			
			if ("000000".equals(dm.getElementsByTagName("RETURN_CODE").item(0).getFirstChild().getNodeValue())) {
				// 得到所有Route节点
				NodeList routes = dm.getElementsByTagName("QUERYORDER");
				if (routes.getLength() > 0) {
					for (int i = 0; i < routes.getLength(); i++) {
						Element element = (Element) routes.item(i);
						// 获取Route节点的 具体描述 属性
						//商户代码
						String merchantid = element.getElementsByTagName("MERCHANTID").item(0).getTextContent();
						//商户所在分行
						String branchid = element.getElementsByTagName("BRANCHID").item(0).getTextContent();
						//商户的POS号
						String posid = element.getElementsByTagName("POSID").item(0).getTextContent();
						//订单号
						String orderid = element.getElementsByTagName("ORDERID").item(0).getTextContent();
						//支付/退款交易时间
						String orderdate = element.getElementsByTagName("ORDERDATE").item(0).getTextContent();
						//记账日期
						String accdate = element.getElementsByTagName("ACCDATE").item(0).getTextContent();
						//支付金额
						String amount = element.getElementsByTagName("AMOUNT").item(0).getTextContent();
						// 支付/退款状态码
						String statuscode = element.getElementsByTagName("STATUSCODE").item(0).getFirstChild().getNodeValue();
						// 支付/退款状态
						String status = element.getElementsByTagName("STATUS").item(0).getFirstChild().getNodeValue();
						// 退款金额
						String refund = element.getElementsByTagName("REFUND").item(0).getFirstChild().getNodeValue();
						// 签名串 
						String sign = element.getElementsByTagName("SIGN").item(0).getFirstChild().getNodeValue();

						CcbSearch e = new CcbSearch();
						e.setMerchantid(merchantid);
						e.setBranchid(branchid);
						e.setPosid(posid);
						e.setOrderid(orderid);
						
						StringBuilder sbDate = new StringBuilder();
						if (!orderdate.contains("-")) {
							sbDate.append(orderdate.substring(0, 4)).append("-").append(orderdate.substring(4, 6)).append("-")
									.append(orderdate.substring(6, 8));
						} else {
							sbDate.append(orderdate.toString());
						}
						e.setOrderdate(sbDate.toString());
						
						StringBuilder sDate = new StringBuilder();
						if (!accdate.contains("-")) {
							sDate.append(accdate.substring(0, 4)).append("-").append(accdate.substring(4, 6)).append("-")
									.append(accdate.substring(6, 8));
						} else {
							sDate.append(accdate.toString());
						}
						e.setAccdate(sDate.toString());

						double aa=Double.parseDouble(amount);
						e.setAmount(aa);
						
						e.setStatuscode(statuscode);
						e.setStatus(status);
						
						double bb=Double.parseDouble(refund);
						e.setRefund(bb);
						e.setSign(sign);

						exlist.add(e);
					}
					result.setList(exlist);
				}
				
				// 获取列表信息
				//总笔数
				NodeList total = dm.getElementsByTagName("TOTAL");
				double y = Double.parseDouble(total.item(0).getTextContent());
				result.setTotal(y);
				//当前页
				NodeList curpage = dm.getElementsByTagName("CURPAGE");
				result.setCurpage(curpage.item(0).getFirstChild().getNodeValue());
				//总页数
				NodeList pagecount = dm.getElementsByTagName("PAGECOUNT");
				int x =  Integer.valueOf(pagecount.item(0).getFirstChild().getNodeValue()).intValue();
				result.setPagecount(x);
				//支付总金额
				NodeList payamount = dm.getElementsByTagName("PAYAMOUNT");
				result.setPayamount(Double.parseDouble(payamount.item(0).getTextContent()));
				//退款总金额
				NodeList refundamount = dm.getElementsByTagName("REFUNDAMOUNT");
				result.setRefundamount(Double.parseDouble(refundamount.item(0).getTextContent()));

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("啊哦。e========>"+e);
		} finally {

		}
		// 返回支付宝的帐务明细列表
		return result;
	}
	
	
	//发送一次请求,得到一共多少条记录.
	public static int getCountOrderByUrl(String url)throws Exception, IOException {
		System.out.println("发送一次请求,得到一共多少条记录.");
		logger.info("发送一次请求,得到一共多少条记录.");
		int count = 1;
		String orderCount="";
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		
		method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727)");
		method.setRequestHeader("Content-Encoding","text/html");
		method.setRequestHeader("Content-Type","text/xml; charset=UTF-8");
		method.setRequestHeader("Accept-Language", "zh-cn");
		method.setRequestHeader("Connection","close");
		
		client.executeMethod(method);
		byte[] bytes = method.getResponseBody();
	    String xmlString = new String(bytes);
	    
	    String tempString = "";
		InputStream is = new ByteArrayInputStream(xmlString.getBytes()); 
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		for (String temp = br.readLine(); temp != null; 
				tempString += temp, temp = br.readLine()
		);
		 System.out.println("银行返回的xml字符串:"+tempString);
		 logger.info("银行返回的xml字符串:"+tempString);
		InputStream stream = new ByteArrayInputStream(tempString.getBytes());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(stream);

		if ("000000".equals(doc.getElementsByTagName("RETURN_CODE").item(0).getFirstChild().getNodeValue())) {
			NodeList total = doc.getElementsByTagName("TOTAL");
			orderCount = total.item(0).getTextContent();
			if(!"".equals(orderCount)){
				count = Integer.parseInt(orderCount);
			}
		}else{
			count = -1;
		}
		
	    return count;
	}
	
	//向银行发送查询请求
	public static CcbSearchList sendTOUrl(String url)throws Exception, IOException {
		CcbSearchList list = new CcbSearchList();
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		
		method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727)");
		method.setRequestHeader("Content-Encoding","text/html");
		method.setRequestHeader("Content-Type","text/xml; charset=UTF-8");
		method.setRequestHeader("Accept-Language", "zh-cn");
		method.setRequestHeader("Connection","close");
		
		client.executeMethod(method);
		byte[] bytes = method.getResponseBody();
	    String xmlString = new String(bytes);
	    list = readXML(xmlString.trim());
	    logger.info("sendTOUrl----list======:"+list);
	    return list;
	}
	
	//建行退费
	public static boolean sendToCCB(String amount, String orderId) {
		boolean isOk = false;

		String s = null;
		Socket mysocket;
		DataOutputStream out = null;
		DataInputStream in = null;
		int len = 0;
		int count = 0;
		byte[] b = new byte[1024];

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?>");
		buffer.append("<TX><REQUEST_SN>" + getReqNo() + "</REQUEST_SN>");
		buffer.append("<CUST_ID>" + CCBConstants.MERCHANTID + "</CUST_ID>");
		buffer.append("<USER_ID>" + CCBConstants.USER_ID + "</USER_ID>");
		buffer.append("<PASSWORD>" + CCBConstants.PASSWORD + "</PASSWORD>");
		buffer.append("<TX_CODE>" + CCBConstants.TX_CODE + "</TX_CODE>");
		buffer.append("<LANGUAGE>" + CCBConstants.LANGUAGE + "</LANGUAGE>");
		buffer.append("<TX_INFO><MONEY>" + amount + "</MONEY>");
		buffer.append("<ORDER>" + orderId + "</ORDER></TX_INFO>");
		buffer.append("<SIGN_INFO></SIGN_INFO><SIGNCERT></SIGNCERT></TX>");
		String str = buffer.toString();
		try {
			mysocket = new Socket(CCBConstants.IP, CCBConstants.PORT);
			in = new DataInputStream(mysocket.getInputStream());
			out = new DataOutputStream(mysocket.getOutputStream());
			byte[] array = str.getBytes();
			out.write(array);
			while (true) {
				if (count > 5) {
					break;
				}
				len = in.read(b);
				s = new String(b, "GB2312");
				if (len <= 0) {
					count++;
				}
			}
			s = s.split("</TX>")[0] + "</TX>";
			Map<String, String> map = XmlUtils.readStringXmlOut(s);
			logger.info("建行退费返回结果=====》"+map.get("RETURN_CODE"));
			if (map.get("RETURN_CODE").equals("000000")) {
				// 退款成功
				isOk = true;
				logger.info("建行退费成功=====》isOk="+isOk);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isOk;
	}
	
	private static String getReqNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		System.out.println("key="+key+"key(length)="+key.length());
		logger.info("退費生成請求序列號(yyyyMMddHHmmss)：getReqNo---key="+key+"key(length)="+key.length());
		
		Random r = new Random();
		//random.nextInt(n)+m;就返回m到m+n-1之间的随机数
		int x = r.nextInt(90)+10;
		key = key + x;
		key = key.substring(0, 16);
		System.out.println("key(16)="+key);
		logger.info("退費生成請求序列號(end)：key(16)="+key);
		return key;
		
	}
}

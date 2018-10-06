package com.gq.common.utils.payment.CCB;

public class CCBConstants {

	/**
	 * 建行支付网址
	 */
	public static final String URL = "https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain";

	/**
	 * 建行对账支付网址
	 */
	public static final String dz_URL = "https://ibsbjstar.ccb.com.cn/app/ccbMain";

	
	/**
	 * 商户代码，由建行统一分配
	 */
	public static final String MERCHANTID = "105321080990007";
	
	/**
	 * 商户柜台代码，由建行统一分配
	 */
	public static final String POSID = "531931781";
	
	/**
	 * 分行代码，由建行统一指定
	 */
	public static final String BRANCHID = "320000000";
	
	/**
	 * 币种 01:人民币
	 */
	public static final String CURCODE = "01";
	
	/**
	 * 交易码,由建行统一分配
	 */
	public static final String TXCODE = "520100";
	
	/**
	 * 接口类型
	 * 	0- 非钓鱼接口
	 * 	1- 防钓鱼接口
	 */
	public static final String TYPE = "1";
	
	/**
	 * 公钥后30位
	 */
	public static final String PUB = "aab34f47ffd6438ee99f52f7020111";
									  
	/**
	 * 注册信息
	 */
	public static final String REGINFO = "扬州中医院";
	
	/**
	 * 语言
	 */
	public static final String LANGUAGE = "CN";	
	
	/**
	 * 退费Ip地址
	 */
	public static final String IP = "127.0.0.1";
	
	/**
	 * 退费端口号
	 */
	public static final int PORT = 12346;
	
	/**
	 * 商品域名
	 */
	public static final String REFERER = "222.189.155.98:8699";
	
	/**
	 * 操作员号
	 */
	public static final String USER_ID = "46883042-1-112";
	
	/**
	 * 操作员密码
	 */
	public static final String PASSWORD = "yzzyy111111";
	
	/**
	 * 退款交易请求码
	 */
	public static final String TX_CODE = "5W1004";
	
}

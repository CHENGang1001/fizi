package com.gq.common.utils.payment.wx;

public class WXPublicConstants {

	/**
	 * 接口API URL前缀
	 */
	public static final String WX_PUBLIC_API_URL = "https://api.mch.weixin.qq.com";

	/**
	 * 微信退款url
	 */
	public static final String WX_PUBLIC_req_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/**
	 * 微信退款证书路径（本机apiclient_cert.p12文件路径）
	 */
	public static final String WX_PUBLIC_BOOK_URL = "D:/yzzyy_cert/apiclient_cert.p12";

	/**
	 * 下单地址URL
	 */
	public static final String WX_PUBLIC_UNIFIEDORDER_URL = "/pay/unifiedorder";

	/**
	 * 对账地址URL
	 */
	public static final String WX_PUBLIC_DOWNLOADBILL_URL = "/pay/downloadbill";

	/**
	 * appid
	 */
	public static final String WX_PUBLIC_APPID = "wx80ccfe11712fcab9"; // 公众账号ID(appid)

	/**
	 * 微信退款资金来源(未结算资金)
	 */
	public static final String WX_PUBLIC_REFUND_ACCOUNT_UNSETTLED = "REFUND_SOURCE_UNSETTLED_FUNDS";

	/**
	 * 微信退款资金来源(可用余额资金)
	 */
	public static final String WX_PUBLIC_REFUND_ACCOUNT_RECHARGE = "REFUND_SOURCE_RECHARGE_FUNDS";

	/**
	 * 商户号
	 */
	public static final String WX_PUBLIC_MCH_ID = "1345522401"; // 商户号(mch_id)

	/**
	 * 终端ip
	 */
	public static final String WX_PUBLIC_SPBILL_CREATE_IP = "127.0.0.1";

	/**
	 * 后台通知地址
	 */
	// public static final String NOTIFY_URL =
	// "http://chuckcoin.f3322.net:8891/payment/wxNotify.do";
	public static final String WX_PUBLIC_NOTIFY_URL = "http://222.189.155.98:8699/payment/wxPublicNotify.do";
			                                        //"http://222.189.155.98:8699/payment/wxPublicNotify.do";

	/**
	 * 微信公众号支付交易类型
	 */
	public static final String WX_PUBLIC_TRADE_TYPE = "JSAPI";

	/**
	 * API密钥
	 */
	public static final String WX_PUBLIC_KEY = "be53cd2e9f5ed65f92ab726a2ee32f50"; // API密钥

	/**
	 * 成功
	 */
	public static final String WX_PUBLIC_SUCCESS = "SUCCESS";

	/**
	 * 失败
	 */
	public static final String WX_PUBLIC_FAIL = "FAIL";

	/**
	 * 扩展字段,固定值
	 */
	public static final String WX_PUBLIC_PACKAGE = "Sign=WXPay";

}

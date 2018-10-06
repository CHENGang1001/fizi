package com.gq.common.utils.payment.wx;

public class WxConstants {

	/**
	 * 接口API URL前缀
	 */
    public static final String API_URL = "https://api.mch.weixin.qq.com";

    /**
     * 微信退款url
     */
    public static final String req_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 微信退款证书路径（本机apiclient_cert.p12文件路径）
     */
    public static final String BOOK_URL = "D:/yzzyy_refund/cert/apiclient_cert.p12";
    
    /**
     * 下单地址URL 
     */
    public static final String UNIFIEDORDER_URL = "/pay/unifiedorder";
    /**
     * 对账地址URL 
     */
    public static final String DOWNLOADBILL_URL = "/pay/downloadbill";
    
    /**
     * appid
     */
    public static final String APPID = "wx229ff5f5a1394113";
    /**
     * 通过APP支付的appid
     */
    public static final String app_APPID = "wx80ccfe11712fcab9";
    
    /**
     * 微信退款资金来源(未结算资金)
     */
	public static final String REFUND_ACCOUNT_UNSETTLED = "REFUND_SOURCE_UNSETTLED_FUNDS";
	/**
     * 微信退款资金来源(可用余额资金)
     */
	public static final String REFUND_ACCOUNT_RECHARGE  = "REFUND_SOURCE_RECHARGE_FUNDS";
    
    /**
     * 商户号
     */
    public static final String MCH_ID = "1370064002";
    /**
     * 通过公众号的商户号
     */
    public static final String app_MCH_ID = "1345522401";
    
    /**
     * 终端ip
     */
    public static final String SPBILL_CREATE_IP = "127.0.0.1";
    
    /**
     * 后台通知地址
     */
//    public static final String NOTIFY_URL = "http://chuckcoin.f3322.net:8891/payment/wxNotify.do";
    public static final String NOTIFY_URL = "http://222.189.155.98:8699/payment/wxNotify.do";
    
    /**
     * 交易类型
     */
    public static final String TRADE_TYPE = "APP";
    
    /**
     * API密钥
     */
    public static final String KEY = "9y5y63762z56z0407y5y952b7y5y952b";
    
    /**
     * 成功
     */
    public static final String SUCCESS = "SUCCESS";
    
    /**
     * 失败
     */
    public static final String FAIL = "FAIL";
    /**
     * 扩展字段,固定值
     */
    public static final String PACKAGE = "Sign=WXPay";

}

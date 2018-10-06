package com.gq.business.payment.service;

import java.util.Map;

public interface WXIPaymentService {

	/**
	 * 微信公众号退费接口
	 * 
	 * @param param
	 * @return
	 */
	boolean wxPublicRefundOrder(Map<String, Object> param);

	/**
	 * 微信发送模板消息接口
	 * 
	 * @param url
	 * @return
	 */
	void sendTemplateMsg(String openid, String wx_orderid, String wx_name, String wx_money, String wx_card);
}

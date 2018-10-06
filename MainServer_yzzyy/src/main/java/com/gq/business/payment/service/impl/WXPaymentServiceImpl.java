package com.gq.business.payment.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.appointment.controller.WXpublicAppointmentController;
import com.gq.business.payment.mappers.PaymentMapper;
import com.gq.business.payment.service.WXIPaymentService;
import com.gq.common.utils.HttpUtils;
import com.gq.common.utils.payment.CCB.CCBPayment;
import com.gq.common.utils.payment.wx.WXPublicPayment;

@Service
public class WXPaymentServiceImpl implements WXIPaymentService {
	// 日志测试文件
	private static Logger log = Logger.getLogger(WXPaymentServiceImpl.class);
	@Autowired
	private PaymentMapper paymentMapper;

	/**
	 * 微信退费接口
	 */
	@Override
	public boolean wxPublicRefundOrder(Map<String, Object> param) {
		int result;
		boolean isOk = false;

		// 订单号
		String payNumber = param.get("payNumber").toString();
		log.info("订单号："+payNumber+"--进入退款流程");
		// 订单金额
		String amount = param.get("amount").toString();
		log.info("订单号："+payNumber+"--订单金额："+amount);
		// 支付类型：1.建行支付 2.微信支付
		String payType = paymentMapper.loadRecord(payNumber);
		log.info("订单号："+payNumber+"--支付类型（1、建行支付 2、微信支付 4、微信公众号支付：)--"+payType);
		if (null != payType && !payType.equals("")) {
			if ("1".equals(payType)) {
				log.info("订单号："+payNumber+"--建行退费开始");
				isOk = CCBPayment.sendToCCB(amount, payNumber);
				log.info("订单号："+payNumber+"--建行退费返回结果--"+isOk);
			} else if ("4".equals(payType)||"2".equals(payType)) {
			 log.info("订单号："+payNumber+"--微信公众号退费1");
				result = WXPublicPayment.wxRefundOrder(amount, payNumber);
			 log.info("订单号："+payNumber+"--微信公众号退费1返回结果(0、退款失败 1、退款成功 2、未结算资金不足，退款失败)--"+result);
				if (result == 2) {
			 log.info("订单号："+payNumber+"--微信公众号退费2");	
					result = WXPublicPayment.wxRefundOrder(amount, payNumber);
			 log.info("订单号："+payNumber+"--微信公众号退费2返回结果(0、退款失败 1、退款成功 2、未结算资金不足，退款失败)--"+result);
				}
				if (result == 1) {
					isOk = true;
				}
			}
		}
		return isOk;
	}

	/**
	 * 微信发送模板消息接口
	 */
	@Override
	public void sendTemplateMsg(String openid, String wx_orderid, String wx_name, String wx_money, String wx_card) {
		// 调用扬州中医院微信服务接口
		String url = "http://app.yzszyy.com/wx_yzzyy/wx/sendOtherTemplateMsg?openid=" + openid + "&wx_orderid="
				+ wx_orderid + "&wx_name=" + wx_name + "&wx_money=" + wx_money + "&wx_card=" + wx_card;
		HttpUtils.sendGet(url);
	}
}

package com.gq.business.payment.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * 交易记录
 * 
 * @author Administrator
 * @version [version, 2016年8月30日]
 * @see [relevant class/method]
 * @since [product/module version]
 */
public class TradingRecordBean
{
	/*
	 * 来源：登录途径0：iOS 1：Android 2：微信 3：支付宝 4：其它
	 */
	private String approach;
	/*
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 第三方订单号
	 */
	private String transactionId;
	
	/*
	 * 支付链接
	 */
	private String paymentUrl;

	/*
	 * 支付状态：1、待支付；2、已支付；
	 */
	private String status;
	/*
	 * 支付金额
	 */
	private String amount;
	/*
	 * 支付用途：1：预约支付、2：当日挂号支付、3：预约挂号支付、4：门诊缴费、5：住院预交
	 */
	private String pay_for;
	/*
	 * 支付类型：1：建行支付 2:微信支付
	 */
	private String pay_type;
	/*
	 * 订单创建时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/*
	 * 订单超时日期
	 */
	private Date timeout_date;
	/*
	 * 订单结束日期
	 */
	private Date finish_date;
	/**
	 * 是否已经取消预约 1：没有取消 2：已经取消
	 */
	private String cancelregister;
	/**
	 * 预约取消时间
	 */
	private Date cancelregisterdate;
	
	/**
	 * 创建者id
	 */
	private String createrId;

	public TradingRecordBean() {
		super();
		this.orderId = "";
		this.transactionId = "";
		this.paymentUrl = "";
		this.status = "";
		this.amount = "";
		this.pay_for = "";
		this.createTime = null;
		this.timeout_date = null;
		this.finish_date = null;
		this.cancelregister = "";
		this.cancelregisterdate = null;
		this.createrId = "";
		this.approach = "";
	}

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentUrl() {
		return paymentUrl;
	}

	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPayFor() {
		return pay_for;
	}

	public void setPayFor(String payFor) {
		this.pay_for = payFor;
	}

	public String getPayType() {
		return pay_type;
	}

	public void setPayType(String payType) {
		this.pay_type = payType;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTimeoutDate() {
		return timeout_date;
	}

	public void setTimeoutDate(Date timeoutDate) {
		this.timeout_date = timeoutDate;
	}
	
	public Date getFinishDate() {
		return finish_date;
	}

	public void setFinishDate(Date finishDate) {
		this.finish_date = finishDate;
	}

	public String getCancelregister() {
		return cancelregister;
	}

	public void setCancelregister(String cancelregister) {
		this.cancelregister = cancelregister;
	}

	public Date getCancelregisterdate() {
		return cancelregisterdate;
	}

	public void setCancelregisterdate(Date cancelregisterdate) {
		this.cancelregisterdate = cancelregisterdate;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	
}

package com.gq.common.utils.payment;

public class WxpaySearch {
	/*交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,
	交易状态,付款银行,货币种类,总金额,代金券或立减优惠金额,微信退款单号,商户退款单号,退款金额,
	代金券或立减优惠退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率*/
	private String id;
	/**
	 * 状态：1充值、0退费
	 */
	private String orderTime;
	private String appId;
	private String mchId;
	private String kidMchId;
	private String posId;
	private String wxOrderId;
	private String orderId;
	private String openId;
	private String payType;
	private String payStatus;
	private String payBank;
	private String moneyType;
	private Double allMoney;
	private Double redMoney;
	private String wxRefundId;
	private String refundId;
	private Double outcome;
	private String redRefundMoney;
	private String refundType;
	private String refundStatus;
	private String body;
	private String attach;
	private String fee;
	private String rates;
	private String state;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getKidMchId() {
		return kidMchId;
	}
	public void setKidMchId(String kidMchId) {
		this.kidMchId = kidMchId;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getWxOrderId() {
		return wxOrderId;
	}
	public void setWxOrderId(String wxOrderId) {
		this.wxOrderId = wxOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPayBank() {
		return payBank;
	}
	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	
	public Double getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(Double allMoney) {
		this.allMoney = allMoney;
	}
	public Double getRedMoney() {
		return redMoney;
	}
	public void setRedMoney(Double redMoney) {
		this.redMoney = redMoney;
	}
	public String getWxRefundId() {
		return wxRefundId;
	}
	public void setWxRefundId(String wxRefundId) {
		this.wxRefundId = wxRefundId;
	}
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	public Double getOutcome() {
		return outcome;
	}
	public void setOutcome(Double outcome) {
		this.outcome = outcome;
	}
	public String getRedRefundMoney() {
		return redRefundMoney;
	}
	public void setRedRefundMoney(String redRefundMoney) {
		this.redRefundMoney = redRefundMoney;
	}
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getRates() {
		return rates;
	}
	public void setRates(String rates) {
		this.rates = rates;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}

package com.gq.business.payment.model;

public class LogBean {
	
	private String orderId;
	
	private String payType;
	
	private String amount;
	
	private String remark;
	
	private String resultCode;
	
	private String resultMsg;
	
	private String hisResultCode;
	
	private String hisResultMsg;
	
	private String refundhisResultMsg;
	
    private long startTime;
    
    private String into;
    
    private String callHisPay;
    
    
    
    
	public String getInto() {
		return into;
	}

	public void setInto(String into) {
		this.into = into;
	}

	public String getCallHisPay() {
		return callHisPay;
	}

	public void setCallHisPay(String callHisPay) {
		this.callHisPay = callHisPay;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getHisResultCode() {
		return hisResultCode;
	}

	public void setHisResultCode(String hisResultCode) {
		this.hisResultCode = hisResultCode;
	}

	public String getHisResultMsg() {
		return hisResultMsg;
	}

	public void setHisResultMsg(String hisResultMsg) {
		this.hisResultMsg = hisResultMsg;
	}

	public String getRefundhisResultMsg() {
		return refundhisResultMsg;
	}

	public void setRefundhisResultMsg(String refundhisResultMsg) {
		this.refundhisResultMsg = refundhisResultMsg;
	}
	
	
}

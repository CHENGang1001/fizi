package com.gq.common.utils.payment;

import java.util.List;

public class WxpaySearchList {

	private List<WxpaySearch> list;
	private String alltotal;
	private Double allpayamount;
	private Double allrefundamount;
	private Double allredMoney;
	private Double allfee;
	private String startid;
	private int id;
	public List<WxpaySearch> getList() {
		return list;
	}
	public void setList(List<WxpaySearch> list) {
		this.list = list;
	}
	
	public String getAlltotal() {
		return alltotal;
	}
	public void setAlltotal(String alltotal) {
		this.alltotal = alltotal;
	}
	
	public Double getAllpayamount() {
		return allpayamount;
	}
	public void setAllpayamount(Double allpayamount) {
		this.allpayamount = allpayamount;
	}
	public Double getAllrefundamount() {
		return allrefundamount;
	}
	public void setAllrefundamount(Double allrefundamount) {
		this.allrefundamount = allrefundamount;
	}
	public Double getAllredMoney() {
		return allredMoney;
	}
	public void setAllredMoney(Double allredMoney) {
		this.allredMoney = allredMoney;
	}
	public Double getAllfee() {
		return allfee;
	}
	public void setAllfee(Double allfee) {
		this.allfee = allfee;
	}
	public String getStartid() {
		return startid;
	}
	public void setStartid(String startid) {
		this.startid = startid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}

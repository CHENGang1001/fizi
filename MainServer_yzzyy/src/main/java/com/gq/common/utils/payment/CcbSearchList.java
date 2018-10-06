package com.gq.common.utils.payment;

import java.util.List;

public class CcbSearchList {

	private List<CcbSearch> list;
	private Double total;
	
	private String curpage;//当前页
	private int pagecount;//总页数
	private Double payamount;
	private Double refundamount;
	private String startid;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<CcbSearch> getList() {
		return list;
	}

	public void setList(List<CcbSearch> list) {
		this.list = list;
	}

	public String getCurpage() {
		return curpage;
	}

	public void setCurpage(String curpage) {
		this.curpage = curpage;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getPayamount() {
		return payamount;
	}

	public void setPayamount(Double payamount) {
		this.payamount = payamount;
	}

	public Double getRefundamount() {
		return refundamount;
	}

	public void setRefundamount(Double refundamount) {
		this.refundamount = refundamount;
	}

	public String getStartid() {
		return startid;
	}

	public void setStartid(String startid) {
		this.startid = startid;
	}

	

}

package com.gq.common.request;

public class RequestEntityEx<requstBean> {
	private requstBean content;
	
	public requstBean getContent(){
		return content;
	}
	
	public void setContent(requstBean content){
		this.content = content;
	}
}

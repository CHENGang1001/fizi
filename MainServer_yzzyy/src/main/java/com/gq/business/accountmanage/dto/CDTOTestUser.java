package com.gq.business.accountmanage.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CDTOTestUser implements Serializable{
	@JsonProperty(value = "Id")
	private int Id;
	
	@JsonProperty(value = "Name")
	private String Name;
	
	@JsonProperty(value = "Status")
	private boolean Status;
	
	public int getId(){
		return this.Id;
	}
	
	public void setId(int Id){
		this.Id = Id;
	}

	public String getName(){
		return this.Name;
	}
	
	public void setName(String Name){
		this.Name = Name;
	}

	public boolean getStatus(){
		return this.Status;
	}
	
	public void setStatus(boolean Status){
		this.Status = Status;
	}

}

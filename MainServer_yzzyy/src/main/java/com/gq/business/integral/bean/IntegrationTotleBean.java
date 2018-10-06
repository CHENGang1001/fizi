/**
 * Project Name:sbhospital_New
 * File Name:IntegrationTotleBean.java
 * Package Name:com.cienet.common.beans
 * Date:2017年2月23日下午5:09:36
 * Copyright (c) 2017, lvxz@600280.com All Rights Reserved.
 *
 */
package com.gq.business.integral.bean;

/**
 * TODO 积分总表bean类
 * date: 2017年2月23日 下午5:09:36 <br/>
 *
 * @author wangjie
 * @version 
 * @since JDK 1.8
 */
public class IntegrationTotleBean {
	/*
	ID
	用户名20（user_name）
	获取积分总值10（total_value）

	消耗积分10（exchange_value）
	积分获取次数int（obtain_count）
	积分消费次数int（exchange_count）
	最近一次获取时间date（last_ obtain _time）
	最近一次消耗时间date（last_ exchange _time）
	*/
	private String ID;
	private String total_value;
	private String exchange_value;
	private String obtain_count;
	private String exchange_count;
	private String last_obtain_time;
	private String last_exchange_time;
	/**
	 * iD.
	 *
	 * @return  the iD
	 * @since   JDK 1.8
	 */
	public String getID() {
		return ID;
	}
	/**
	 * iD.
	 *
	 * @param   iD    the iD to set
	 * @since   JDK 1.8
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * total_value.
	 *
	 * @return  the total_value
	 * @since   JDK 1.8
	 */
	public String getTotal_value() {
		return total_value;
	}
	/**
	 * total_value.
	 *
	 * @param   total_value    the total_value to set
	 * @since   JDK 1.8
	 */
	public void setTotal_value(String total_value) {
		this.total_value = total_value;
	}
	/**
	 * exchange_value.
	 *
	 * @return  the exchange_value
	 * @since   JDK 1.8
	 */
	public String getExchange_value() {
		return exchange_value;
	}
	/**
	 * exchange_value.
	 *
	 * @param   exchange_value    the exchange_value to set
	 * @since   JDK 1.8
	 */
	public void setExchange_value(String exchange_value) {
		this.exchange_value = exchange_value;
	}
	/**
	 * obtain_count.
	 *
	 * @return  the obtain_count
	 * @since   JDK 1.8
	 */
	public String getObtain_count() {
		return obtain_count;
	}
	/**
	 * obtain_count.
	 *
	 * @param   obtain_count    the obtain_count to set
	 * @since   JDK 1.8
	 */
	public void setObtain_count(String obtain_count) {
		this.obtain_count = obtain_count;
	}
	/**
	 * exchange_count.
	 *
	 * @return  the exchange_count
	 * @since   JDK 1.8
	 */
	public String getExchange_count() {
		return exchange_count;
	}
	/**
	 * exchange_count.
	 *
	 * @param   exchange_count    the exchange_count to set
	 * @since   JDK 1.8
	 */
	public void setExchange_count(String exchange_count) {
		this.exchange_count = exchange_count;
	}
	/**
	 * last_obtain_time.
	 *
	 * @return  the last_obtain_time
	 * @since   JDK 1.8
	 */
	public String getLast_obtain_time() {
		return last_obtain_time;
	}
	/**
	 * last_obtain_time.
	 *
	 * @param   last_obtain_time    the last_obtain_time to set
	 * @since   JDK 1.8
	 */
	public void setLast_obtain_time(String last_obtain_time) {
		this.last_obtain_time = last_obtain_time;
	}
	/**
	 * last_exchange_time.
	 *
	 * @return  the last_exchange_time
	 * @since   JDK 1.8
	 */
	public String getLast_exchange_time() {
		return last_exchange_time;
	}
	/**
	 * last_exchange_time.
	 *
	 * @param   last_exchange_time    the last_exchange_time to set
	 * @since   JDK 1.8
	 */
	public void setLast_exchange_time(String last_exchange_time) {
		this.last_exchange_time = last_exchange_time;
	}
	
}

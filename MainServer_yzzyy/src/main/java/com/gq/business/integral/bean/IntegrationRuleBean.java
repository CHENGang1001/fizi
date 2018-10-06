/**
 * Project Name:sbhospital_New
 * File Name:IntegeationRuleBean.java
 * Package Name:com.cienet.common.beans
 * Date:2017年2月23日上午10:30:48
 * Copyright (c) 2017, lvxz@600280.com All Rights Reserved.
 *
 */
package com.gq.business.integral.bean;

/**
 * TODO 积分规则Bean
 * date: 2017年2月23日 上午10:30:48 <br/>
 *
 * @author wangjie
 * @version 
 * @since JDK 1.8
 */
public class IntegrationRuleBean {
	/**
	 * 积分ID
	 */
	private String id;

	/**
	 * 积分类型
	 */
	private String integral_type;
	
	/**
	 * 类型名称
	 */
	private String type_name;
	
	/**
	 * 基数
	 */
	private String base_num;
	
	/**
	 * 积分数
	 */
	private String num;
	
	/**
	 * 使用状态  1-使用中 2-废止
	 */
	private String status;
	
	/**
	 * 上限次数 2（upper_limit）0-无上限 其余数字表示对应上限次数

	 */
	private String upper_limit;
	
	/**
	 * 开始时间
	 */
	private String start_time;
	
	/**
	 * 结束时间
	 */
	private String end_time;
	
	/**
	 * 创建时间
	 */
	private String create_time;
	
	/**
	 * integral_type.
	 *
	 * @return  the integral_type
	 * @since   JDK 1.8
	 */
	public String getIntegral_type() {
		return integral_type;
	}

	/**
	 * integral_type.
	 *
	 * @param   integral_type    the integral_type to set
	 * @since   JDK 1.8
	 */
	public void setIntegral_type(String integral_type) {
		this.integral_type = integral_type;
	}

	/**
	 * type_name.
	 *
	 * @return  the type_name
	 * @since   JDK 1.8
	 */
	public String getType_name() {
		return type_name;
	}

	/**
	 * type_name.
	 *
	 * @param   type_name    the type_name to set
	 * @since   JDK 1.8
	 */
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	/**
	 * base_num.
	 *
	 * @return  the base_num
	 * @since   JDK 1.8
	 */
	public String getBase_num() {
		return base_num;
	}

	/**
	 * base_num.
	 *
	 * @param   base_num    the base_num to set
	 * @since   JDK 1.8
	 */
	public void setBase_num(String base_num) {
		this.base_num = base_num;
	}

	/**
	 * num.
	 *
	 * @return  the num
	 * @since   JDK 1.8
	 */
	public String getNum() {
		return num;
	}

	/**
	 * num.
	 *
	 * @param   num    the num to set
	 * @since   JDK 1.8
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * status.
	 *
	 * @return  the status
	 * @since   JDK 1.8
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * status.
	 *
	 * @param   status    the status to set
	 * @since   JDK 1.8
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * upper_limit.
	 *
	 * @return  the upper_limit
	 * @since   JDK 1.8
	 */
	public String getUpper_limit() {
		return upper_limit;
	}

	/**
	 * upper_limit.
	 *
	 * @param   upper_limit    the upper_limit to set
	 * @since   JDK 1.8
	 */
	public void setUpper_limit(String upper_limit) {
		this.upper_limit = upper_limit;
	}

	/**
	 * start_time.
	 *
	 * @return  the start_time
	 * @since   JDK 1.8
	 */
	public String getStart_time() {
		return start_time;
	}

	/**
	 * start_time.
	 *
	 * @param   start_time    the start_time to set
	 * @since   JDK 1.8
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	/**
	 * end_time.
	 *
	 * @return  the end_time
	 * @since   JDK 1.8
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * end_time.
	 *
	 * @param   end_time    the end_time to set
	 * @since   JDK 1.8
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * create_time.
	 *
	 * @return  the create_time
	 * @since   JDK 1.8
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * create_time.
	 *
	 * @param   create_time    the create_time to set
	 * @since   JDK 1.8
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

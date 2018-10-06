package com.gq.business.integral.service;

import java.util.Map;

public interface IIntegrationRuleService {
	/**
	 * IntegrationRule:(积分兑换规则). <br/>
	 * @param type--兑换类型  username--用户名  paymoney--支付金额
	 * @author wangjie
	 * @date 2017年2月23日
	 */
	Map<String, Object> IntegrationRule(Map<String, String> param); 
}

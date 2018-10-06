/**
 * Project Name:sbhospital_New
 * File Name:IntegrationRuleMapper.java
 * Package Name:com.cienet.common.mapper
 * Date:2017年2月23日上午10:28:55
 * Copyright (c) 2017, lvxz@600280.com All Rights Reserved.
 *
 */
package com.gq.business.integral.mappers;

import java.util.List;
import java.util.Map;

import com.gq.business.integral.bean.IntegrationRuleBean;

/**
 * TODO 对数据库表 T_INTEGRAL_RULE_LIST 的操作
 * date: 2017年2月23日 上午10:28:55 <br/>
 *
 * @author wangjie
 * @version 
 * @since JDK 1.8
 */
public interface IntegrationRuleMapper {
	/**
	 * getIntegrationRule:(获取对应的积分规则). <br/>
	 * @param param
	 * @return
	 * @author wangjie
	 * @date 2017年2月23日
	 */
	IntegrationRuleBean getIntegrationRule(Map<String, String> param);
	
	/**
	 * selectAllIntegrationRule:(获取所有积分规则). <br/>
	 * @return
	 * @author wangjie
	 * @date 2017年2月24日
	 */
	List<IntegrationRuleBean> selectAllIntegrationRule(IntegrationRuleBean bean);
	
	/**
	 * insertIntegrationRule:(插入积分规则). <br/>
	 * @param bean
	 * @return
	 * @author wangjie
	 * @date 2017年2月24日
	 */
	int insertIntegrationRule(IntegrationRuleBean bean);
}

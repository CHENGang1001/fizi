package com.gq.business.integral.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.integral.bean.IntegrationRuleBean;
import com.gq.business.integral.mappers.IntegralMapper;
import com.gq.business.integral.mappers.IntegrationRuleMapper;
import com.gq.business.integral.service.IIntegrationRuleService;
import com.gq.common.log.LogManager;
import com.gq.config.ReturnCode;

@Service
public class IntegrationRuleServiceImpl implements IIntegrationRuleService{
	private Logger log = LogManager.getDebugLog();

	@Autowired
	private IntegrationRuleMapper integrationMapper;

	@Autowired
	private IntegralMapper integraMapper;

	@Override
	public Map<String, Object> IntegrationRule(Map<String, String> param) {
		// TODO Auto-generated method stub
		// @param type--兑换类型  username--用户名  paymoney--支付金额
		IntegrationRuleBean bean = new IntegrationRuleBean();
		Map<String, Object> ary = new HashMap<>();
		try {
			bean = integrationMapper.getIntegrationRule(param);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("数据库查询---------------------" + e.getMessage());
		}		
		if(null == bean){
			ary.put("resultCode", ReturnCode.INTEGRSTION_NOTFUND.getCode());
			ary.put("resultMsg", ReturnCode.INTEGRSTION_NOTFUND.getMsg());
		}
		else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间
			if(bean.getStart_time() != null && compare_date(date, bean.getStart_time()) == -1){
				ary.put("resultCode", ReturnCode.INTEGRSTION_TIMEOUT.getCode());
				ary.put("resultMsg", ReturnCode.INTEGRSTION_TIMEOUT.getMsg());
			}
			else{
				if(bean.getEnd_time() != null && compare_date(date, bean.getEnd_time()) == 1){
					ary.put("resultCode", ReturnCode.INTEGRSTION_TIMEOUT.getCode());
					ary.put("resultMsg", ReturnCode.INTEGRSTION_TIMEOUT.getMsg());
				}
				else{
					ary.put("resultCode", ReturnCode.OK.getCode());
					ary.put("resultMsg", ReturnCode.OK.getMsg());
					int value = integraValue(bean.getIntegral_type(), param.get("paymoney"), bean.getBase_num(), bean.getNum());
					ary.put("addIntegralValue", value);
					Map<String, Object> map = new HashMap<>();

					map.put("userName", param.get("username"));
					map.put("integraValue", value);
					map.put("currentTime", date);
					map.put("expiredTime", "");
					map.put("ruleId", bean.getIntegral_type());
					log.info("插入的积分详情数据---------------------" + map.toString());
					try {
						integraMapper.insertIntegrationDetail(map);
					} catch (Exception e) {
						log.info("积分详情数据插入失败---------------------" + e.getMessage());
					}
					//		#{userName}, #{total_value},#{exchange_value},
					//#{obtain_count}, #{exchange_count},
					//#{last_obtain_time},#{last_exchange_time})
					Map<String, Object> content = new HashMap<>();
					content.put("userName", param.get("username"));
					content.put("total_value", value);
					content.put("exchange_value", "0");
					content.put("obtain_count", "1");
					content.put("exchange_count", "0");
					content.put("last_obtain_time", date);
					content.put("last_exchange_time", date);
					log.info("插入的积分总表数据---------------------" + content.toString());
					try {
						integraMapper.updateIntegrationList(content);
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e.getMessage());
						log.info("积分总表数据插入失败---------------------" + e.getMessage());
					}
				}
			}
		}
		return ary;
	}

	public static int integraValue(String type, String payMoney, String base, String value){
		if("1".equals(type) || "4".equals(type)){
			return Integer.parseInt(value);
		}
		else if("2".equals(type) || "3".equals(type)){
			double price = Double.parseDouble(payMoney);
			double baseMoney = Double.parseDouble(base);
			return (int)Math.round(price/baseMoney);
		}
		else if("0".equals(base) && !"0".equals(value)){
			return Integer.parseInt(value);
		}
		else{
			double price = Double.parseDouble(payMoney);
			double baseMoney = Double.parseDouble(base);
			return (int)Math.round(price/baseMoney);
		}
	}

	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
}

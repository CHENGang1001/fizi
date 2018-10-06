package com.gq.business.integral.service;

import java.util.List;
import java.util.Map;

import com.gq.config.ReturnCode;

public interface IntegralService {
	/**
	 * 根据用户名查询用户积分汇总情况
	 * @param param
	 * @return 指定用户积分汇总情况
	 */
	Map<String, Object> getIntegralerInfo(Map<String, Object> param);
	
	/**
	 * 获得所有上架中的商品列表
	 * @return
	 */
	List<Map<String, Object>> getOnSaleGoodsList();
	
	/**
	 * 兑换商品
	 * @param param
	 * @return 成功兑换商品后返回结果
	 */
	Map<String, Object> exchangeUserGoods(Map<String, Object> param);
	
	/**
	 * 获得指定用户的积分流水记录
	 * @return
	 */
	List<Map<String, Object>> getIntegralRecordList(Map<String, Object> param);
	
	/**
	 * 获得指定类型的的积分排名
	 * @param type 1-日排名 2-周排名 3-月排名
	 * @return
	 */
	List<Map<String, Object>> getRankingList(Map<String, Object> param);
	
	/**
	 * 获得指定用户的兑换记录列表
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> getIntegralOrderList(Map<String, Object> param);
	
	/**
	 * 将已兑换的订单状态改为已删除
	 * @param param
	 * @return
	 */
	ReturnCode deleteIntegralOrder(Map<String, Object> param);
}

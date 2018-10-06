package com.gq.business.integral.mappers;

import java.util.List;
import java.util.Map;

public interface IntegralMapper {

	/**
	 * 根据用户名查询用户积分汇总情况
	 * @param param
	 * @return 指定用户积分汇总情况
	 */
	Map<String, Object> getIntegralerInfo(Map<String, Object> param);
	
	/**
	 * 获取所有上架中的商品列表
	 * @return 所有上架中的商品列表
	 */
	List<Map<String, Object>> getOnSaleGoodsList();
	
	/**
	 * 根据商品编码和销售类型获取指定日期商品的日汇总量
	 * @param GOODS_CODE 商品编码
	 * @param SELL_TYPE 销售类型  1-积分 2-现金
	 * @param TIME 销售时间 yyyy-MM-dd
	 * @return 指定商品的日汇总量
	 */
	String getGoodsSummary(Map<String, Object> param);
	
	/**
	 * 获得指定用户的积分流水记录
	 * @return
	 */
	List<Map<String, Object>> getIntegralRecordList(Map<String, Object> param);
	
	/**
	 * 根据积分规则ID获得积分规则名称
	 * @param ruleId 积分规则ID
	 * @return 积分规则名称
	 */
	String getRuleName(String ruleId);
	
	/**
	 * 获得指定时间段内的积分排名
	 * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss
	 * @param endTime 结束时间 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	List<Map<String, Object>> getRankingList(Map<String, Object> param);
	
	/**
	 * 获得指定用户的兑换记录列表
	 * @param userName 用户名
	 * @return 兑换记录列表
	 */
	List<Map<String, Object>> getIntegralOrderList(String userName);
	
	/**
	 * 查询指定用户的剩余积分是否足够兑换商品
	 * @param userName 用户名
	 * @param goodsCode 商品编号
	 * @param goodsNum 商品数量
	 * @return 用户剩余积分值减去商品所需积分的值
	 * 			大于等于0则表示足够  小于0则表示不够
	 */
	int isIntegralEnough(Map<String, Object> param);
	
	/**
	 * 查询指定商品当日最大可兑换量
	 * @param sellType 销售类型 1-积分 2-现金
	 * @param time 日期 yyyy-MM-dd
	 * @param goodsCode 商品编号
	 * @return 指定商品的当日最大兑换量
	 * 			大于0则表示具体的当日可兑换量  等于0表示库存不足或已达日兑换上限
	 */
	int getCanExchangedNum(Map<String, Object> param);
	
	/**
	 * 更新商品剩余库存量
	 * @param goodsCode 商品编号
	 * @param goodsNum 商品数量
	 */
	int updateGoodsInventory(Map<String, Object> param);
	
	/**
	 * 更新商品日汇总量
	 * @param goodsCode 商品编号
	 * @param goodsNum 商品数量
	 * @param sellType 销售类型 1-积分 2-现金
	 * @param time 日期 yyyy-MM-dd
	 */
	int updateGoodsDaySummary(Map<String, Object> param);
	
	/**
	 * 插入一条新的商品日汇总记录
	 * @param goodsCode 商品编号
	 * @param goodsNum 商品数量
	 * @param sellType 销售类型 1-积分 2-现金
	 * @param time 日期 yyyy-MM-dd
	 */
	int insertGoodsDaySummary(Map<String, Object> param);
	
	/**
	 * 更新积分总表消耗值
	 * @param goodsCode 商品编号
	 * @param goodsNum 商品数量
	 * @param userName 用户名
	 * @param currentTime 日期 yyyy-MM-dd HH:mm:ss
	 */
	int updateUserIntegralList(Map<String, Object> param);
	
	/**
	 * insertIntegrationDetail:(插入一条增加积分的记录). <br/>
	 * @param param
	 * @return 
	 * @author wangjie
	 * @date 2017年2月23日
	 */
	int insertIntegrationDetail(Map<String, Object> param);
	
	/**
	 * 获取指定商品编码的商品信息
	 * @param goodsCode 商品编号
	 * @return 商品信息
	 */
	Map<String, Object> getGoodsInfoByCode(String goodsCode);
	
	/**
	 * insertIntegralOrderList:(插入一条新的积分订单的记录). <br/>
	 * @param param
	 * @return 
	 * @author jinjing
	 * @date 2017年2月23日
	 */
	int insertIntegralOrderList(Map<String, Object> param);
	
	/**
	 * insertIntegralOrderDetails:(插入一条新的积分订单详情的记录). <br/>
	 * @param param
	 * @return 
	 * @author jinjing
	 * @date 2017年2月23日
	 */
	int insertIntegralOrderDetails(Map<String, Object> param);
	
	/**
	 * updateIntegrationList:(更新积分总表). <br/>
	 * @param total_value  obtain_count last_obtain_time
	 * @return
	 * @author wangjie
	 * @date 2017年2月23日
	 */
	int updateIntegrationList(Map<String,Object> param);
	
	/**
	 * insertIntegrationList:(向积分总表中插入一条数据). <br/>
	 * @param param
	 * @return
	 * @author wangjie
	 * @date 2017年2月23日
	 */
	int insertIntegrationList(Map<String,Object> param);
	
	/**
	 * selectIntegrationOneRow:(查询积分总表中对应的记录). <br/>
	 * @param param
	 * @return
	 * @author wangjie
	 * @date 2017年2月23日
	 */
//	IntegrationTotleBean selectIntegrationOneRow(Map<String,Object> param);

	/**
	 * 将已兑换的订单状态改为已删除
	 * @param orderNo 订单号
	 * @return
	 */
	int deleteIntegralOrder(String orderNo);
	
	/**
	 * 根据商品编码获取商品的资源图片存储地址
	 * @param goodsCode 商品编码
	 * @return 商品的资源图片存储地址
	 */
	String getIntegralGoodsPic(String goodsCode);

	Map<String, Object> selectAmount(Map<String, Object> map);
	
	/**
	 * 根据username查询该用户的推广人数
	 * @param map
	 * @return 数量
	 */
	int getCountForShareuser(Map<String, Object> map);
}

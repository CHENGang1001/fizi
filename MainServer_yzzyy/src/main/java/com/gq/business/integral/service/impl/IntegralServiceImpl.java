package com.gq.business.integral.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.integral.mappers.IntegralMapper;
import com.gq.business.integral.service.IntegralService;
import com.gq.common.log.LogManager;
import com.gq.config.ReturnCode;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

@Service
public class IntegralServiceImpl implements IntegralService{
	@Autowired
	private IntegralMapper integralMapper;

	/**
	 * 日志
	 */
	private Logger logger = LogManager.getDebugLog();

	/**
	 * 根据用户名查询用户积分汇总情况
	 * @param param
	 * @return 指定用户积分汇总情况
	 */
	@Override
	public Map<String, Object> getIntegralerInfo(Map<String, Object> param) {
		if(!param.containsKey("userName")) {
			return null;
		}
		Map<String, Object> userInfo = new HashMap<String, Object>();
		try {
			userInfo = integralMapper.getIntegralerInfo(param);
			Map<String, Object> integralerInfo = new HashMap<String, Object>();
			if(null == userInfo || userInfo.isEmpty()){
				return null;
			}
			int count = 0;
			try {
				count = integralMapper.getCountForShareuser(param);
			} catch (Exception e) {
				logger.error("数据库查询失败", e.getMessage());
			}
			//用户名
			integralerInfo.put("userName", userInfo.get("USER_NAME"));
			//用户当前剩余积分（历史获得总积分减去消耗总积分）
			int TOTAL_VALUE = Integer.valueOf(userInfo.get("TOTAL_VALUE").toString());
			int EXCHANGE_VALUE = 0;
			if(userInfo.containsKey("EXCHANGE_VALUE") && !"".equals(userInfo.get("EXCHANGE_VALUE").toString())){
				EXCHANGE_VALUE = Integer.valueOf(userInfo.get("EXCHANGE_VALUE").toString());
			}
			int currentValue = TOTAL_VALUE - EXCHANGE_VALUE;
			integralerInfo.put("currentValue", String.valueOf(currentValue));
			integralerInfo.put("shareCount", String.valueOf(count));

			return integralerInfo;
		} catch (Exception e) {
			//logger.error("数据库查询失败", userInfo.toString());
			return null;
		}


	}

	/**
	 * 获得所有上架中的商品列表
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOnSaleGoodsList() {
		
		//获得所有上架中的商品列表
		List<Map<String, Object>> goodsList = integralMapper.getOnSaleGoodsList();
		if(null == goodsList || goodsList.isEmpty()) {
			return null;
		}
		// 返回的商品列表
		List<Map<String, Object>> returnGoodsList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : goodsList) {
			//int residuesGoods = getGoodsDayNum(map.get("GOODS_CODE").toString());
			String residuesGoods = getGoodsNum(map.get("GOODS_CODE").toString());
			Map<String, Object> returnGoods = new HashMap<String, Object>();
			// 商品名
			returnGoods.put("goodsName", map.get("GOODS_NAME"));
			// 商品编码
			returnGoods.put("goodsCode", map.get("GOODS_CODE"));
			// 积分值
			returnGoods.put("integral", map.get("NEEDED_INTEGRAL"));
			// 限购量
			returnGoods.put("purchase", map.get("PURCHASE"));
			// 当前可兑换量
			returnGoods.put("currentNum", residuesGoods);
			// 商品描述
			returnGoods.put("goodsDescription", map.get("GOODS_DESCRIPTION"));
			// 商品图片地址
			returnGoods.put("picUrl", getGoodsPic(map.get("GOODS_CODE").toString()));

			returnGoodsList.add(returnGoods);
		}

		return returnGoodsList;
	}

	

	/**
	 * 根据商品编码获取商品的资源图片存储地址
	 * @param goodsCode 商品编码
	 * @return 商品的资源图片存储地址
	 */
	private String getGoodsPic(String goodsCode) {
		// 商品图片地址
		String url = integralMapper.getIntegralGoodsPic(goodsCode);
		if (TextUtils.isEmpty(url)) {
			url = "";
		} else {
			url = "http://resource.jssecco.com" + url;
		}
		return url;
	}

	/**
	 * 获得当日积分商品最大可兑换量
	 * 
	 * @param goodsCode
	 * @return 大于0则表示具体的当日可兑换量  等于0表示库存不足或已达日兑换上限
	 */
	private int getGoodsDayNum(String goodsCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 积分类型
		map.put("sellType", "1");
		// 当前日期 yyyy-MM-dd
		String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		map.put("time", currentDate);
		//商品编码
		map.put("goodsCode", goodsCode);
		// 商品兑换日汇总量
		return integralMapper.getCanExchangedNum(map);
	}

	/**
	 * 兑换商品
	 * @param param
	 * @return 成功兑换商品后返回结果
	 */
	@Override
	public Map<String, Object> exchangeUserGoods(Map<String, Object> param) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(!param.containsKey("userName")) {
			returnMap.put("returnCode", "-1");
			return returnMap;
		}
		if(!param.containsKey("goodsCode")) {
			returnMap.put("returnCode", "-1");
			return returnMap;
		}
		if(!param.containsKey("goodsNum")) {
			returnMap.put("returnCode", "-1");
			return returnMap;
		}
		// 商品编码
		String goodsCode = param.get("goodsCode").toString();
		// 用户名称
		String userName = param.get("userName").toString(); 
		// 兑换数量
		String goodsNum = param.get("goodsNum").toString();
		// 查询指定用户的剩余积分是否足够兑换商品，大于等于0则表示足够  小于0则表示不够
		int isEnough = integralMapper.isIntegralEnough(param);
		// 用户积分不足
		if(isEnough < 0) {
			returnMap.put("returnCode", "-2");
			return returnMap;
		}
		Interner<String> pool = Interners.newWeakInterner();
		// 对同一商品操作进行线程锁控制
		synchronized (pool.intern(goodsCode)) {
			// 积分商品剩余可兑换量
			String residues = getGoodsNum(goodsCode);
			int residuesGoods = Integer.valueOf(residues);
			// 库存不足或商品已达当日兑换上限
			if(residuesGoods <= 0) {
				returnMap.put("returnCode", "-3");
				return returnMap;
			}
			// 用于想要兑换的数量与商品剩余可兑换量进行比对
			if(residuesGoods < Integer.valueOf(goodsNum)) {
				// 剩余可兑换量不足
				returnMap.put("returnCode", "-4");
				return returnMap;
			}
			// 更新商品剩余库存量
			integralMapper.updateGoodsInventory(param);
			// 积分类型
			param.put("sellType", "1");
			// 当前日期 yyyy-MM-dd
			String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
			param.put("time", currentDate);
			// 更新商品日汇总量 返回值为0表示更新不成功，数据库不存在该数据
			if(0 == integralMapper.updateGoodsDaySummary(param)) {
				// 如果更新失败，说明数据不存在，所以插入一条新的数据
				integralMapper.insertGoodsDaySummary(param);
			}
		}
		// 当前时间 yyyy-MM-dd HH:mm:ss
		param.put("currentTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
		// 更新积分总表
		integralMapper.updateUserIntegralList(param);
		// 商品信息
		Map<String, Object> goodsInfo = integralMapper.getGoodsInfoByCode(goodsCode);
		// 本次消耗积分值
		String integraValue = String.valueOf(Integer.valueOf(goodsInfo.get("NEEDED_INTEGRAL").toString()) * Integer.valueOf(goodsNum));
		param.put("integraValue", "-" + integraValue);
		// 积分过期时间  为空代表不过期
		param.put("expiredTime", "");
		// 规则ID/交易ID
		param.put("ruleId", "");
		// 向积分明细表插入一条新的数据
		integralMapper.insertIntegrationDetail(param);
		// 商品总价 单位：分
		String totalValue = String.valueOf(Integer.valueOf(goodsInfo.get("UNIT_PRICE").toString()) * Integer.valueOf(goodsNum));
		// 订单号 
		String orderNo = "1" + (new SimpleDateFormat("yyMMdd")).format(new Date()) + "01" + getRandomNum(5);
		// 向订单表插入一条新的数据
		int num1 = insertIntegralOrderList(orderNo, userName, param.get("currentTime").toString(), totalValue, "1");
		// 向订单明细表插入一条新的数据
		int num2 = insertIntegralOrderDetails(orderNo, goodsCode, goodsNum, "0", totalValue, "0", "",integraValue);
		if(num1 == 1 && num2 == 1) {
			returnMap.put("returnCode", "0");
			returnMap.put("orderNo", orderNo);
			returnMap.put("currentTime", param.get("currentTime").toString());
		}

		return returnMap;
	}

	/**
	 * 向订单总表插入一条兑换数据
	 * 
	 * @param orderNo 订单号
	 * @param userName 用户名
	 * @param currentTime 当前时间 yyyy-MM-dd HH:mm:ss
	 * @param totalPrice 商品总价 单位：分
	 * @param status 支付状态 0-待支付 1-已支付 2-已兑换 3-已删除
	 * @return
	 */
	private int insertIntegralOrderList(String orderNo, String userName, String currentTime, String totalPrice, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 订单号
		map.put("orderNo", orderNo);
		// 用户名
		map.put("userName", userName);
		// 当前时间 yyyy-MM-dd HH:mm:ss
		map.put("currentTime", currentTime);
		// 商品总价
		map.put("totalPrice", totalPrice);
		// 支付状态 0-待支付 1-已支付 2-已兑换 3-已删除
		map.put("status", status);

		return integralMapper.insertIntegralOrderList(map);
	}

	/**
	 * 插入一条新的积分订单详情的记录
	 * 
	 * @param orderNo 订单号
	 * @param goodsCode 商品编码
	 * @param goodsNum 商品数量
	 * @param cash 现金支付金额
	 * @param integral 积分兑换金额
	 * @param coupon 优惠券金额
	 * @param reserved 其他预留优惠字段
	 * @return
	 */
	private int insertIntegralOrderDetails(String orderNo, String goodsCode, String goodsNum, String cash, String integral, String coupon, String reserved,String integraValue) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 订单号
		map.put("orderNo", orderNo);
		// 商品编码
		map.put("goodsCode", goodsCode);
		// 商品数量
		map.put("goodsNum", goodsNum);
		// 现金支付金额
		map.put("cash", cash);
		// 积分兑换金额
		map.put("integral", integral);
		// 优惠券金额
		map.put("coupon", coupon);
		// 其他预留优惠字段
		map.put("reserved", reserved);
		// 操作员编号
		map.put("operaterId", "");
		//本次所消耗的积分
		map.put("integraValue", integraValue);
		return integralMapper.insertIntegralOrderDetails(map);
	}

	/**
	 * 产生所需数量的随机数
	 * 
	 * @param num 需要产生的随机数数量
	 * @return 所需数量的随机数
	 */
	private String getRandomNum(int num) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < num; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	/**
	 * 获得指定用户的积分流水记录
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getIntegralRecordList(Map<String, Object> param) {
		if(!param.containsKey("userName")) {
			return null;
		}
		// 获得指定用户的积分流水记录列表
		List<Map<String, Object>> list = integralMapper.getIntegralRecordList(param);
		// 返回给前端的列表
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> map : list) {
			Map<String, Object> recordMap = new HashMap<String, Object>();
			String ruleName = "积分兑换";
			if(map.containsKey("RULE_ID")) {
				if (!"".equals(map.get("RULE_ID"))) {
					ruleName = integralMapper.getRuleName(map.get("RULE_ID").toString());
				}
			}
			String expiredTime = "";
			if(map.containsKey("EXPIRED_TIME")) {
				expiredTime = map.get("EXPIRED_TIME").toString();
			}
			recordMap.put("endTime", expiredTime);
			recordMap.put("startTime", map.get("CREATDATE").toString().subSequence(0, map.get("CREATDATE").toString().length()-2));
			recordMap.put("integral", map.get("CURRENT_INTEGRAL"));
			recordMap.put("operation", ruleName);
			returnList.add(recordMap);
		}
		return returnList;
	}

	/**
	 * 获得指定类型的的积分排名
	 * @param type 1-日排名 2-周排名 3-月排名
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getRankingList(Map<String, Object> param) {
		if(!param.containsKey("type")) {
			return null;
		}
		// 排名类型 1-日排名 2-周排名 3-月排名
		String type = param.get("type").toString();
		// 开始时间
		String startTime = " 00:00:00";
		// 结束时间
		String endTime = " 23:59:59";
		// 当前日期 yyyy-MM-dd
		String currentTime = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		// 排名搜索开始日期
		String startDate = null;
		if("1".equals(type)) {
			startDate = currentTime + startTime;
		} else if ("2".equals(type)) {
			Calendar lastDate = Calendar.getInstance();
			lastDate.roll(Calendar.DATE, -7);// 日期回滚7天
			startDate = new SimpleDateFormat("yyyy-MM-dd").format(lastDate.getTime()) + startTime;
		} else if ("3".equals(type)) {
			Calendar lastDate = Calendar.getInstance();
			lastDate.add(Calendar.MONTH, -1);//月份减一
			startDate = new SimpleDateFormat("yyyy-MM-dd").format(lastDate.getTime()) + startTime;
		}
		// 排名搜索结束日期
		String endDate = currentTime + endTime;
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("startTime", startDate);
		searchMap.put("endTime", endDate);
		return integralMapper.getRankingList(searchMap);
	}

	/**
	 * 获得指定用户的兑换记录列表
	 * @param 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getIntegralOrderList(Map<String, Object> param) {
		if(!param.containsKey("userName")) {
			return null;
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		// 用户名
		String userName = param.get("userName").toString();
		List<Map<String, Object>> list = integralMapper.getIntegralOrderList(userName);
		for(Map<String, Object> map : list) {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.putAll(map);
			returnMap.put("picUrl", getGoodsPic(map.get("GOODS_CODE").toString()));
			returnMap.put("ORDER_TIME",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(map.get("ORDER_TIME")));
			returnList.add(returnMap);
		}
		return returnList;
	}

	/**
	 * 将已兑换的订单状态改为已删除
	 * @param param
	 * @return
	 */
	@Override
	public ReturnCode deleteIntegralOrder(Map<String, Object> param) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		if(!param.containsKey("orderNo")) {
			returnCode = ReturnCode.ILLEGAL_ACCESS;
			return returnCode;
		}
		// 订单号
		String orderNo = param.get("orderNo").toString();
		// 将已兑换的订单状态改为已删除
		int updateStatus = integralMapper.deleteIntegralOrder(orderNo);
		if(0 == updateStatus) {
			// 更新失败
			returnCode = ReturnCode.CANNOT_DELETE_GOODS;
		} else if(1 == updateStatus) {
			// 更新成功
			returnCode = ReturnCode.OK;
		}
		return returnCode;
	}
	
	private String getGoodsNum(String goodsCode) {
		String residuesGoods = null;
		Map<String, Object> map = new HashMap<String, Object>();
		// 当前日期 yyyy-MM-dd
		String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		map.put("time", currentDate);
		map.put("goodsCode", goodsCode);
		Map<String, Object> residues = integralMapper.selectAmount(map);
		if ("0".equals(residues.get("PURCHASE"))) {
			residuesGoods = residues.get("CURRENT_AMOUNT").toString();
		} else {
			// 剩余数量
			int currentAmount = Integer.parseInt(residues.get("CURRENT_AMOUNT").toString());
			// 日限数量
			int purchase = Integer.parseInt(residues.get("PURCHASE").toString());
			// 该日已经销售数量
			int sellNum = 0;
			if (residues.containsKey("SELL_NUM")) {
				sellNum = Integer.parseInt(residues.get("SELL_NUM").toString());
			}
			if ((purchase - sellNum)  >= currentAmount) {
				residuesGoods = String.valueOf(currentAmount);
			} else {
				residuesGoods = String.valueOf(purchase - sellNum);
			}
			if (Integer.parseInt(residuesGoods) < 0) {
				residuesGoods = "0";
			}
		}
		return residuesGoods;
	}
}

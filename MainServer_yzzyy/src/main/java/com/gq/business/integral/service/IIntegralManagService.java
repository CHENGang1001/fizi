package com.gq.business.integral.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.gq.business.integral.bean.GoodsBean;
import com.gq.business.integral.bean.OrderGoodsBean;
import com.gq.common.exception.ServiceException;

public interface IIntegralManagService {
	/**
	 * @Description 商品兑换管理员登录
	 * @param param
	 * @return 登录者信息
	 */
	Map<String, Object> exchangeLogin(Map<String, Object> param) throws ServiceException;

	
	/**
	 * @Description 发布商品
	 * @param goods
	 * @param imageSourse 
	 * @return 是否成功
	 */
	int publishGoods(GoodsBean goods, String imageSourse) throws ServiceException;

	/**
	 * @Description 兑换商品
	 * @param orderNo
	 * @return 
	 */
	String exchangeGoods(String orderNo) throws ServiceException;

	/**
	 * @Description 更改商品状态
	 * @param goods
	 */
	void uploadGoodsStatus(GoodsBean goods) throws ServiceException;

	/**
	 * 获取指定类型的商品列表
	 * @param type 商品状态 0-上架中 1-仓库中
	 * @param pages 分页显示页数
	 * @param num 每页显示的个数
	 * @return 商品列表
	 */
	List<Map<String, Object>> integralGoodsList(Map<String, Object> param);

	/**
	 * 查询订单
	 * @param param
	 * @return 订单
	 */
	OrderGoodsBean getOrder(Map<String, Object> param) throws ParseException;


	int editGoods(GoodsBean goods);


	String getGoodsPic(String string);


	int editGoodsAll(GoodsBean goods, String imageSourse);


	int deleteGoods(String goodsCode);


	List<Map<String, Object>> findOrderByCode(String goodsCode);

}

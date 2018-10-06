package com.gq.business.integral.mappers;

import java.util.List;
import java.util.Map;

import com.gq.business.integral.bean.GoodsBean;
import com.gq.business.integral.bean.ImageBean;
import com.gq.business.integral.bean.OrderGoodsBean;

/**
 * 后台管理操作Service层
 *
 * @author liangyuli
 * @ClassName: AdminManageMapper
 * @Description: 提供商品兑换操作Mapper接口
 * @date: Feb 20, 2017 17:59:16 PM
 */
public interface IntegrationManageMapper {

	/**
	 * @Description 商品兑换管理员登录
	 * @param param
	 * @return 登录者信息
	 */
	Map<String, Object> checkAccount(Map<String, Object> param);

	/**
	 * @Description 发布商品
	 * @param goods
	 */
	int insertGoods(GoodsBean goods);

	/**
	 * @Description 兑换商品
	 * @param orderNo
	 */
	void updateOrder(String orderNo);
	
	/**
	 * @Description 上传商品图片
	 * @param imageBean
	 */
	void insertGoodsImage(ImageBean imageBean);

	/**
	 * @Description 更改商品状态
	 * @param goods
	 */
	void uploadGoodsStatus(GoodsBean goods);

	/**
	 * @Description 获取最新商品的商品代码
	 * @retrun 最新商品的商品代码
	 */
	String selectMaxGoodsCode();
	
	/**
	 * 获取指定类型的所有商品列表
	 * @param type 商品状态 0-上架中 1-仓库中
	 * @param pages 分页显示页数
	 * @param num 每页显示的个数
	 * @return 商品列表
	 */
	List<Map<String, Object>> integralGoodsListAll(Map<String, Object> param);
	/**
	 * 获取指定类型和搜索条件的商品列表
	 * @param type 商品状态 0-上架中 1-仓库中
	 * @param pages 分页显示页数
	 * @param num 每页显示的个数
	 * @param name 搜索关键字
	 * @return 商品列表
	 */
	List<Map<String, Object>> integralGoodsList(Map<String, Object> param);
	
	/**
	 * 获取指定类型商品总页数
	 * @param type 商品状态 0-上架中 1-仓库中
	 * @param num 每页显示的个数
	 * @return 商品总页数 
	 */
	Map<String, Object> integralGoodsListQtyAll(Map<String, Object> param);
	
	/**
	 * 获取指定类型和搜索条件的商品总页数
	 * @param type 商品状态 0-上架中 1-仓库中
	 * @param num 每页显示的个数
	 * @return 商品总页数 
	 */
	Map<String, Object> integralGoodsListQty(Map<String, Object> param);

	/**
	 * 获取订单信息
	 * @param orderNo
	 * @return 订单信息
	 */
	OrderGoodsBean getOrder(String orderNo);

	/**
	 * 查询订单状态
	 * @param orderNo
	 * @return 订单状态
	 */
	String queryOrderStatus(String orderNo);

	/**
	 * 查询发布商品时查询商品名称是否重复
	 * @param name
	 * @return 同名商品数量
	 */
	int queryNameIsRepeat(String name);

	/**
	 * 发布商品时查询最大商品ID
	 * @return 最大商品ID
	 */
	int selctMaxGoodsId();

	/**
	 * 判断是否有商品存在
	 * @return 0为空
	 */
	int queryIsHaveGoods();

	/**
	 * 编辑商品的库存
	 * @param goods
	 * @return
	 */
	int editGoods(GoodsBean goods);

	String getGoodsPic(String code);

	int findByCode(GoodsBean goods);

	int updateGoods(GoodsBean goods);

	int updateGoodsImage(ImageBean imageBean);

	int deleteGoods(String goodsCode);

	List<Map<String, Object>> findOrderByOrder(String goodsCode);
}

package com.gq.business.integral.bean;

import java.io.Serializable;
/**
 * 商品Bean
 *
 * @author liangyuli
 * @ClassName: GoodsBean
 * @Description: 商品Bean
 * @date: Feb 21, 10:29:27 PM
 */
/**
 * @author L
 *
 */
public class OrderGoodsBean implements Serializable {
	private static final long serialVersionUID = -8059861084491937485L;

	private String goods_name;//商品名称
	private String goods_code;//商品代码
	private String needed_integral;//商品所需积分
	private String order_no;//订单号
	private String user_name;//购买者账号
	private String order_time;//兑换时间
	private String total_price;//兑换价格
	private String status;//兑换状态 0-待支付 1-已支付 2已兑换
	
	public String getNeeded_integral() {
		return needed_integral;
	}
	public void setNeeded_integral(String needed_integral) {
		this.needed_integral = needed_integral;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}

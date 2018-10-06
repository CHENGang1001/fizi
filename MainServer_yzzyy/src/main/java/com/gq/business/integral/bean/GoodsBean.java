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
public class GoodsBean implements Serializable {
	private static final long serialVersionUID = -8059861084491937485L;

	private String id;
	private String goods_name;//商品名
	private String goods_code;//商品代码
	private String needed_integral;//所需积分值
	private String unit_price;//价格
	private String total_amount;//商品总量
	private String current_amount;//商品当前剩余数量
	private String create_time;//商品创建时间
	private String purchase;//商品日限购量
	private String goods_description;//商品描述
	private String goods_status;//商品状态 0-上架中 1-仓库中
	
	private String project;
	private String classify;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getNeeded_integral() {
		return needed_integral;
	}
	public void setNeeded_integral(String needed_integral) {
		this.needed_integral = needed_integral;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getCurrent_amount() {
		return current_amount;
	}
	public void setCurrent_amount(String current_amount) {
		this.current_amount = current_amount;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPurchase() {
		return purchase;
	}
	public void setPurchase(String purchase) {
		this.purchase = purchase;
	}
	public String getGoods_description() {
		return goods_description;
	}
	public void setGoods_description(String goods_description) {
		this.goods_description = goods_description;
	}
	public String getGoods_status() {
		return goods_status;
	}
	public void setGoods_status(String goods_status) {
		this.goods_status = goods_status;
	}
	
}

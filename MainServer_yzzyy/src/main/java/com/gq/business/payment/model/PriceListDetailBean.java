package com.gq.business.payment.model;

import java.io.Serializable;
import java.util.Date;

import com.gq.common.utils.StringUtils;

/**
 * 划价单详情Bean
 * 
 * @author zy
 * @version [version, 2016年2月23日]
 */
public class PriceListDetailBean implements Serializable {

	private static final long serialVersionUID = -2508025924507707290L;

	// 以下显示字段
	/** 名称 */
	private String name;
	/** 单位，示例：盒 */
	private String unit;
	/** 规则，示例：12片*3板 */
	private String specification;

	// 以下数据库字段
	/** db，划价单号 */
	private String priceListID;
	/** db，就诊卡卡号 */
	private String cardNo;
	/** db，项目类别（0=药品）（1=诊疗） */
	private String itemClass;
	/** db，项目代码 */
	private String itemCode;
	/** db，项目名称 */
	private String itemName;
	/** db，项目规格 */
	private String itemSpec;
	/** db，数量 */
	private Integer amount;
	/** db，单位名 */
	private String units;
	/** db，单价 */
	private double price;
	/** db，总价 */
	private double charges;
	/** db，处方单种类：0-西药处方；1-中药处方；2-检验医嘱；3-检查医嘱；9-混 合医嘱； */
	private String prescriptionType;
	/** db，创建时间 */
	private Date createDate;

	public String getName() {
		return StringUtils.isNullOrEmpty(name) ? itemName : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return StringUtils.isNullOrEmpty(unit) ? units : unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSpecification() {
		return StringUtils.isNullOrEmpty(specification) ? itemSpec : specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getPriceListID() {
		return priceListID;
	}

	public void setPriceListID(String priceListID) {
		this.priceListID = priceListID;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public String getPrescriptionType() {
		return prescriptionType;
	}

	public void setPrescriptionType(String prescriptionType) {
		this.prescriptionType = prescriptionType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}

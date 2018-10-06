package com.gq.business.payment.model;

import java.io.Serializable;

/**
 * 划价单Bean
 * 
 * @author zy
 * @version [version, 2016年2月22日]
 */
public class PriceListBean implements Serializable {

	private static final long serialVersionUID = 3830633571076537234L;

	// 以下数据库字段
	/** db，划价单号 */
	private String priceListID;
	/** db，科室代码 */
	private String deptCode;
	/** db，科室名称 */
	private String deptName;
	/** db，医生代码 */
	private String doctorCode;
	/** db，医生名称 */
	private String doctorName;
	/** db，诊断 */
	private String diagnose;
	/** db，执行地点 */
	private String windowNo;
	/** db，人员费用结算类别编码 */
	private String personnelType;

	public String getPriceListID() {
		return priceListID;
	}

	public void setPriceListID(String priceListID) {
		this.priceListID = priceListID;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public String getWindowNo() {
		return windowNo;
	}

	public void setWindowNo(String windowNo) {
		this.windowNo = windowNo;
	}

	public String getPersonnelType() {
		return personnelType;
	}

	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}

}

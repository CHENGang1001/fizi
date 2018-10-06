package com.gq.business.payment.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单Bean
 * 
 * @author zy
 * @version [version, 2016年2月24日]
 */
public class OrderBean implements Serializable {

	private static final long serialVersionUID = 1935547958899603876L;
	public static final String ORDER_TYPE_PRENOS = "0";
	public static final String ORDER_TYPE_PATIENTINFO = "1";
	public static final String ORDER_PAY_STATUS_WAITING = "0";
	public static final String ORDER_PAY_STATUS_SUCCESS = "1";
	public static final String ORDER_PAY_STATUS_FAIL = "2";

	// 以下显示字段
	/** 订单日期 */
	private Date orderDate;
	/** 订单类型，可选项：0预约挂号、1划价单 */
	private String orderType;
	/** 订单简述 */
	private String description;
	/** 科室编号 */
	private String departmentID;
	/** 科室名称 */
	private String departmentName;
	/** 挂号类型 */
	private String appointmentType;
	/** 预约日期，示例：2016-01-18 14 */
	private Date appointmentDate;
	/** 午别，可选项：AM上午、PM下午 */
	private String noonType;
	/** 专家编号 */
	private String doctorID;
	/** 专家名称 */
	private String doctorName;
	/** 专家职位 */
	private String doctorPosition;
	/** 疾病编号 */
	private String diseaseID;
	/** 疾病名称 */
	private String diseaseName;
	/** 号序 */
	private String appointmentNumber;
	/** 就诊人卡号 */
	private String patientCardNumber;
	/** 就诊人姓名 */
	private String patientName;
	/** 费用 */
	private double fee;
	/** 支付、银行卡类型 */
	private String type;
	/** 同步通知页面 */
	private String returnUrl;

	/** 划价单 */
	private PriceListBean preNos;
	/** 划价详情单 */
	private List<PriceListDetailBean> recordList;
	/** 预约 */
	private ReservationBean reservation;

	private Integer pageStart;
	private Integer pageEnd;
	private Integer pageNumber;
	private Integer pageSize;

	// 以下数据库字段
	/** 订单编号 */
	private String orderID;
	/** db，划价单号 */
	private String priceListID;
	/** db，创建日期 */
	private Date createDate;
	/** db，坑号 */
	private String reservateFlow;
	/** db，用户名 */
	private String userName;
	/** db，订单总价 */
	private double totalPrice;
	/** db，银联流水号 */
	private String unionPayID;
	/** db，生成银联流水号时间 */
	private Date unionPayIDTime;
	/** db，银联查询流水号 */
	private String qunionPayID;
	/** db，0，未支付 1，支付成功 2，支付失败 */
	private String payStatus;
	/** db，同步HIS状态 0，同步失败 1，同步成功 */
	private String synHisStatus;
	/** db，发票号码 */
	private String inVoiceNumber;
	/** db，支付方式 */
	private String payWay;
	/** db，支付账号 */
	private String payAccount;
	/** db，预约编号 */
	private String clinicCode;
	/** db，His通知失败后返回的错误消息 */
	private String synHisErrorMsg;
	/** db，银行返回码 */
	private String unionRespCode;
	/** db，His同步时间 */
	private Date synHisStatusDate;
	/** db，用户是否已经取消预约 1：没有取消 2：已经取消 */
	private String cancelRegister;
	/** db，预约取消时间 */
	private Date cancelRegisterDate;

	public PriceListBean getPreNos() {
		return preNos;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(String departmentID) {
		this.departmentID = departmentID;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getNoonType() {
		return noonType;
	}

	public void setNoonType(String noonType) {
		this.noonType = noonType;
	}

	public String getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorPosition() {
		return doctorPosition;
	}

	public void setDoctorPosition(String doctorPosition) {
		this.doctorPosition = doctorPosition;
	}

	public String getDiseaseID() {
		return diseaseID;
	}

	public void setDiseaseID(String diseaseID) {
		this.diseaseID = diseaseID;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getAppointmentNumber() {
		return appointmentNumber;
	}

	public void setAppointmentNumber(String appointmentNumber) {
		this.appointmentNumber = appointmentNumber;
	}

	public String getPatientCardNumber() {
		return patientCardNumber;
	}

	public void setPatientCardNumber(String patientCardNumber) {
		this.patientCardNumber = patientCardNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Integer getPageStart() {
		return pageStart;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}

	public Integer getPageEnd() {
		return pageEnd;
	}

	public void setPageEnd(Integer pageEnd) {
		this.pageEnd = pageEnd;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setPreNos(PriceListBean preNos) {
		this.preNos = preNos;
	}

	public List<PriceListDetailBean> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<PriceListDetailBean> recordList) {
		this.recordList = recordList;
	}

	public ReservationBean getReservation() {
		return reservation;
	}

	public void setReservation(ReservationBean reservation) {
		this.reservation = reservation;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getPriceListID() {
		return priceListID;
	}

	public void setPriceListID(String priceListID) {
		this.priceListID = priceListID;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getReservateFlow() {
		return reservateFlow;
	}

	public void setReservateFlow(String reservateFlow) {
		this.reservateFlow = reservateFlow;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getUnionPayID() {
		return unionPayID;
	}

	public void setUnionPayID(String unionPayID) {
		this.unionPayID = unionPayID;
	}

	public Date getUnionPayIDTime() {
		return unionPayIDTime;
	}

	public void setUnionPayIDTime(Date unionPayIDTime) {
		this.unionPayIDTime = unionPayIDTime;
	}

	public String getQunionPayID() {
		return qunionPayID;
	}

	public void setQunionPayID(String qunionPayID) {
		this.qunionPayID = qunionPayID;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getSynHisStatus() {
		return synHisStatus;
	}

	public void setSynHisStatus(String synHisStatus) {
		this.synHisStatus = synHisStatus;
	}

	public String getInVoiceNumber() {
		return inVoiceNumber;
	}

	public void setInVoiceNumber(String inVoiceNumber) {
		this.inVoiceNumber = inVoiceNumber;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getClinicCode() {
		return clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getSynHisErrorMsg() {
		return synHisErrorMsg;
	}

	public void setSynHisErrorMsg(String synHisErrorMsg) {
		this.synHisErrorMsg = synHisErrorMsg;
	}

	public String getUnionRespCode() {
		return unionRespCode;
	}

	public void setUnionRespCode(String unionRespCode) {
		this.unionRespCode = unionRespCode;
	}

	public Date getSynHisStatusDate() {
		return synHisStatusDate;
	}

	public void setSynHisStatusDate(Date synHisStatusDate) {
		this.synHisStatusDate = synHisStatusDate;
	}

	public String getCancelRegister() {
		return cancelRegister;
	}

	public void setCancelRegister(String cancelRegister) {
		this.cancelRegister = cancelRegister;
	}

	public Date getCancelRegisterDate() {
		return cancelRegisterDate;
	}

	public void setCancelRegisterDate(Date cancelRegisterDate) {
		this.cancelRegisterDate = cancelRegisterDate;
	}

}

package com.gq.business.payment.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约Bean
 * 
 * @author zy
 * @version [version, 2016年2月23日]
 */
public class ReservationBean implements Serializable {

	private static final long serialVersionUID = 5882748505091678475L;

	// 以下数据库字段
	/** db，ID */
	private String reservationID;
	/** db，就诊卡卡号 */
	private String cardNo;
	/** db，病人姓名 */
	private String patientName;
	/** db，病人手机 */
	private String patientMobile;
	/** db，预约日期 */
	private Date reservationDate;
	/** db，预约排班序号 */
	private String reservationFlow;
	/** db，可预约号序及时间段、流水号。（号序与时间段|号序 流水号，对应以“|”分割） */
	private String reservationNo;
	/** db，科室 ID */
	private String departmentID;
	/** db，科室名称 */
	private String departmentName;
	/** db，医生 ID */
	private String doctorID;
	/** db，医生姓名 */
	private String doctorName;
	/** db，专病名称 */
	private String speciDisea;
	/** db，坐诊时间 */
	private String seeTime;
	/** db，预约类型 */
	private String reservationClassID;
	/** db，挂号号别 ID */
	private String registerTypeID;
	/** db，挂号号别名称 */
	private String registerType;
	/** db，创建时间 */
	private Date createDate;
	/** db，预约编号 */
	private String outReservateFlow;
	/** db，可预约号序及时间段、流水号。（号序与时间段|号序流水号，对应以“|” 分割） 如 1|9:00|0001 */
	private String outYYNo;
	/** db，就诊地点 */
	private String address;
	/** db，预约挂号验证码 */
	private String verifyCode;
	/** db，预约挂号费用(包括挂号费和诊疗费) */
	private String fee;
	/** db，预约编号 */
	private String clinicCode;
	/** db，挂号费 */
	private String registerFee;
	/** db，诊疗费 */
	private String diagnoseFee;

	public String getReservationID() {
		return reservationID;
	}

	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientMobile() {
		return patientMobile;
	}

	public void setPatientMobile(String patientMobile) {
		this.patientMobile = patientMobile;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getReservationFlow() {
		return reservationFlow;
	}

	public void setReservationFlow(String reservationFlow) {
		this.reservationFlow = reservationFlow;
	}

	public String getReservationNo() {
		return reservationNo;
	}

	public void setReservationNo(String reservationNo) {
		this.reservationNo = reservationNo;
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

	public String getSpeciDisea() {
		return speciDisea;
	}

	public void setSpeciDisea(String speciDisea) {
		this.speciDisea = speciDisea;
	}

	public String getSeeTime() {
		return seeTime;
	}

	public void setSeeTime(String seeTime) {
		this.seeTime = seeTime;
	}

	public String getReservationClassID() {
		return reservationClassID;
	}

	public void setReservationClassID(String reservationClassID) {
		this.reservationClassID = reservationClassID;
	}

	public String getRegisterTypeID() {
		return registerTypeID;
	}

	public void setRegisterTypeID(String registerTypeID) {
		this.registerTypeID = registerTypeID;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOutReservateFlow() {
		return outReservateFlow;
	}

	public void setOutReservateFlow(String outReservateFlow) {
		this.outReservateFlow = outReservateFlow;
	}

	public String getOutYYNo() {
		return outYYNo;
	}

	public void setOutYYNo(String outYYNo) {
		this.outYYNo = outYYNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getClinicCode() {
		return clinicCode;
	}

	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}

	public String getRegisterFee() {
		return registerFee;
	}

	public void setRegisterFee(String registerFee) {
		this.registerFee = registerFee;
	}

	public String getDiagnoseFee() {
		return diagnoseFee;
	}

	public void setDiagnoseFee(String diagnoseFee) {
		this.diagnoseFee = diagnoseFee;
	}

}

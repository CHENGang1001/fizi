package com.gq.business.accountmanage.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

//公共用户实体bean
public class PublicUserBean implements Serializable {

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8048119300444156734L;

	/**
	 * 用户ID
	 */
	private String userID;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 手机号
	 */
	private String msisdn;

	/**
	 * 用户真实姓名
	 */
	private String userRealName;

	/**
	 * 就诊卡卡号
	 */
	private String patientCardID;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 绑定健康卡时，健康卡号
	 */
	private String patientHealthCardID;

	/**
	 * 手机号
	 */
	private String leavePhoneNumber;

	/**
	 * 病历号
	 */
	private String patientID;

	/**
	 * 
	 */
	private String home;

	/**
	 * 
	 */
	private String approach;
	/**
	 * 
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registertime;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 手机验证码
	 */
	private String verificationCode;

	/**
	 * 客户端类型
	 */
	private String clientType;

	/**
	 * sessionID
	 */
	private String sessionID;

	/**
	 * 新密码
	 */
	private String newPassword;

	/**
	 * 验证码类型
	 */
	private String verifyType;

	/**
	 * 微信ID
	 */
	private String weixinIdentify;

	/**
	 * 分页开始位置
	 */
	private int pageStart;

	/**
	 * 分页结束位置
	 */
	private int pageEnd;

	/**
	 * 绑定健康卡时，办理健康卡预留手机号
	 */
	private String phoneNumber;

	/**
	 * 支付宝用户ID
	 */
	private String toUserID;

	/**
	 * 用户名
	 */
	
	/**
	 * 绑定健康卡时，办理健康卡预留手机号
	 */
	private String patientName;
	
	/**
	 * 推广人手机号码
	 * */
	private String shareUser;
	/**
	 * 该密码方法
	 * @return
	 */
	private String changeType;
	
	/**
	 * 分享用户id
	 */
	private String shareUserId;
	
	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getPatientCardID() {
		return patientCardID;
	}

	public void setPatientCardID(String patientCardID) {
		this.patientCardID = patientCardID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPatientHealthCardID() {
		return patientHealthCardID;
	}

	public void setPatientHealthCardID(String patientHealthCardID) {
		this.patientHealthCardID = patientHealthCardID;
	}

	public String getLeavePhoneNumber() {
		return leavePhoneNumber;
	}

	public void setLeavePhoneNumber(String leavePhoneNumber) {
		this.leavePhoneNumber = leavePhoneNumber;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public Date getRegistertime() {
		return registertime;
	}

	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}

	public String getWeixinIdentify() {
		return weixinIdentify;
	}

	public void setWeixinIdentify(String weixinIdentify) {
		this.weixinIdentify = weixinIdentify;
	}

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageEnd() {
		return pageEnd;
	}

	public void setPageEnd(int pageEnd) {
		this.pageEnd = pageEnd;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getToUserID() {
		return toUserID;
	}

	public void setToUserID(String toUserID) {
		this.toUserID = toUserID;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getShareUser() {
		return shareUser;
	}

	public void setShareUser(String shareUser) {
		this.shareUser = shareUser;
	}

	public String getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	@Override
	public String toString() {
		return "PublicUserBean [userID=" + userID + ", userName=" + userName + ", password=" + password + ", msisdn="
				+ msisdn + ", userRealName=" + userRealName + ", patientCardID=" + patientCardID + ", address="
				+ address + ", patientHealthCardID=" + patientHealthCardID + ", leavePhoneNumber=" + leavePhoneNumber
				+ ", patientID=" + patientID + ", home=" + home + ", approach=" + approach + ", registertime="
				+ registertime + ", gender=" + gender + ", age=" + age + ", verificationCode=" + verificationCode
				+ ", clientType=" + clientType + ", sessionID=" + sessionID + ", newPassword=" + newPassword
				+ ", verifyType=" + verifyType + ", weixinIdentify=" + weixinIdentify + ", pageStart=" + pageStart
				+ ", pageEnd=" + pageEnd + ", phoneNumber=" + phoneNumber + ", toUserID=" + toUserID + ", patientName="
				+ patientName + ", shareUser=" + shareUser + ", changeType=" + changeType + ", shareUserId="
				+ shareUserId + "]";
	}

	
}

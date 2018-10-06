package com.gq.business.useraccount.model;

import java.io.Serializable;

public class UserInfoBean implements Serializable{
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
	
	//用户ID
	private String userId;
	
	//手机号码
	private String phoneNumber;
	
	//昵称
	private String userName;
	
	//微信三方登录授权码openId
	private String openId;
	
	//登录token
	private String token;
	
	//融云token
	private String rcToken;
	
	//短信验证sessionId
	private String sessionId;
	
	//融云的rcUserid
	private String rcUserId;
	
	//登录密码
	private String password;
	
	//头像
	private String headPic;
	
	//修改密码（新密码）
	private String newPassword;
	
	//验证码
	private String verifyCode;
	
	//验证码类型
	private String VerifyType;
	
	//客户端判断1：iOS；2：Android；
	private String client;
	
	//判断是否禁用：0：NO；1：YES
	private String isClose;
	
	//注册日期
	private String registdate;
	
	//最近一次登录的时间
	private String lastLoginDate;
	
	//七牛服务地址
	private String qiniuServeUrl;
	
	//上传图片时用到的凭证
	private String qiniuToken;
	
	//用户角色类型 1-家长 2-教师 3-机构
	private int userType;
	
	//被举报的次数
	private int reportNumber;
	
	//登录方式 1-手机号登录 2-微信授权登录 3-QQ授权登录 4-微博授权登录
	private int LoginType;
	
	//支付宝授权登录
	private String ailiAuthId;
	
	//qq授权登录
	private String qqopenId;

	public String getRcToken() {
		return rcToken;
	}

	public void setRcToken(String rcToken) {
		this.rcToken = rcToken;
	}

	public String getRcUserId() {
		return rcUserId;
	}

	public void setRcUserId(String rcUserid) {
		this.rcUserId = rcUserid;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegistdate() {
		return registdate;
	}

	public void setRegistdate(String registdate) {
		this.registdate = registdate;
	}	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getVerifyType() {
		return VerifyType;
	}

	public void setVerifyType(String verifyType) {
		VerifyType = verifyType;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getQiniuServeUrl() {
		return qiniuServeUrl;
	}

	public void setQiniuServeUrl(String qiniuServeUrl) {
		this.qiniuServeUrl = qiniuServeUrl;
	}

	public String getQiniuToken() {
		return qiniuToken;
	}

	public void setQiniuToken(String qiniuToken) {
		this.qiniuToken = qiniuToken;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(int reportNumber) {
		this.reportNumber = reportNumber;
	}

	public int getLoginType() {
		return LoginType;
	}

	public void setLoginType(int loginType) {
		LoginType = loginType;
	}

	public String getQqopenId() {
		return qqopenId;
	}

	public void setQqopenId(String qqopenId) {
		this.qqopenId = qqopenId;
	}

	public String getAiliAuthId() {
		return ailiAuthId;
	}

	public void setAiliAuthId(String ailiAuthId) {
		this.ailiAuthId = ailiAuthId;
	}
	
}

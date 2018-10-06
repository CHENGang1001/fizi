package com.gq.business.accountmanage.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 事务管理的bean
 */
public class TransactionBean implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8045413624499834105L;

    /**
     * 事务号
     */
    private String transID;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 创建日期
     */
    private Date timestamp;
    /***
     * 途径 
     */
    private String approach;
    /**
     * 标识---1：登录 0：登出
     */
    private String sign;
    /**
     * @return the transID
     */
    public String getTransID() {
        return transID;
    }

    /**
     * @param transID the transID to set
     */
    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	/**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
    
}

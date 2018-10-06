package com.gq.business.accountmanage.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机验证码bean
 * @ClassName: VeifycodeBean
 * @Description: 手机验证码bean
 * @date: Nov 22, 2013 12:33:09 PM
 */
public class VeifyCodeBean implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2751078200031985512L;

    /**
     * 手机号码
     */
    private String msisdn;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 生成日期
     */
    private Date timestamp;

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the verifyCode
     */
    public String getVerifyCode() {
        return verifyCode;
    }

    /**
     * @param verifyCode the verifyCode to set
     */
    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
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
}

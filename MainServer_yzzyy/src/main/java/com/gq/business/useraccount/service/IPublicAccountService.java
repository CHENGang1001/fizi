package com.gq.business.useraccount.service;

import com.gq.business.useraccount.model.UserInfoBean;
import com.gq.common.exception.ServiceException;

/**
 * 用户操作Service层
 */
public interface IPublicAccountService {
	
	/**
	 * 注册接口
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	void userRegister(UserInfoBean publicUserBean) throws ServiceException;
	
	/**
	 * 注册 -- 三方登录失败（首次用三方登录）需要绑定手机号码密码等信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	UserInfoBean userThirdRegister(UserInfoBean publicUserBean) throws ServiceException;

	/**
	 * 登陆接口
	 * 
	 * @param pUserInfoBean
	 * <br />
	 * @throws ServiceException
	 */
	UserInfoBean userLogin(UserInfoBean pUserInfoBean) throws ServiceException;
	
	/**
	 * 三方登陆接口
	 * 
	 * @param pUserInfoBean
	 * <br />
	 * @throws ServiceException
	 */
	UserInfoBean userThirdLogin(UserInfoBean pUserInfoBean) throws ServiceException;
	
	/**
	 * 获取手机验证码
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	String getVerifyCode(UserInfoBean publicUserBean) throws ServiceException;

	/**
	 * 找回密码接口
	 * 
	 * @param userInfo
	 * @throws ServiceException
	 */
	void forgotPwd(UserInfoBean userInfo) throws ServiceException;

	/**
	 * 修改密码接口
	 * @param userInfo
	 * @throws ServiceException
	 */
	void modifyPwd(UserInfoBean userInfo) throws ServiceException;
	
	/**
	 * 验证登录token是否失效
	 * @param token
	 * @throws ServiceException
	 */
	void parmVerifyToken(String token) throws ServiceException;
}

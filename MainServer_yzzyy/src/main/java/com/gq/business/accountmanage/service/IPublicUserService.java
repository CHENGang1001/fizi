package com.gq.business.accountmanage.service;

import java.util.Map;

import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.model.TransactionBean;
import com.gq.common.exception.ServiceException;

/**
 * 用户操作Service层
 */
public interface IPublicUserService {
	/**
	 * 登录接口
	 *
	 * @param params
	 * <br />
	 * @return <br />
	 * @throws ServiceException
	 */
	PublicUserBean loginAccount(Map<String, Object> params) throws ServiceException;

	/**
	 * 获取手机验证码
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	String getVerifyCode(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 注册接口
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	void userRegister(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 获取个人信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	PublicUserBean baseInfo(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 修改个人信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	PublicUserBean updateBaseInfo(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 修改密码
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	void changePassword(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 找回密码
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	void forgotPassword(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 退出
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	void logoutAccount(PublicUserBean publicUserBean) throws ServiceException;

	/**
	 * 用户鉴权
	 *
	 * @param transactionBean
	 * <br />
	 * @throws ServiceException
	 */
	void authentication(TransactionBean transactionBean) throws ServiceException;

	/**
	 * 用户鉴权
	 *
	 * @param transactionBean
	 * <br />
	 * @return <br />
	 */
	int authenticationForJ(TransactionBean transactionBean);

	//根据订单ID获取对应的创建者用户ID
	PublicUserBean getPublicUserByOrderID(String orderId);
}

package com.gq.business.useraccount.mappers;

import java.util.List;

import com.gq.business.useraccount.model.UserInfoBean;
import com.gq.common.exception.ServiceException;

public interface PublicAccountMapper {
	
	/**
	 * 查询用户信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	UserInfoBean checkAccount(UserInfoBean publicUserBean);
	
	/**
	 * 根据手机号查看用户信息
	 *
	 * @param msisdn
	 * <br />
	 * @return <br
	 *         /
	 */
	String getUserNameByMsisdn(String msisdn);
	
	/**
	 * 查看手机号码是否在库中是否存在
	 *
	 * @param publicUserBean
	 * <br />
	 */
	int userNoByMsisdn(UserInfoBean publicUserBean);
	
	/**
	 * 注册插入数据
	 * 
	 * @param publicUserBean
	 * @return
	 */
	int registerUserInfo(UserInfoBean publicUserBean);
	
	/**
	 *  获取当前用户信息
	 * @param bean
	 * @return
	 */
	UserInfoBean getUserInfo(UserInfoBean bean);
	
	
	/**
	 *  获取当前用户信息_微信三方登录用
	 * @param bean
	 * @return
	 */
	UserInfoBean getUserInforThirdLogin(UserInfoBean bean);

	/**
	 *  获取当前用户信息_QQ三方登录用
	 * @param bean
	 * @return
	 */
	UserInfoBean getUserInforQQLogin(UserInfoBean bean);

	/**
	 *  获取当前用户信息_支付宝三方登录用
	 * @param bean
	 * @return
	 */
	UserInfoBean getUserInforAiliLogin(UserInfoBean bean);

	
	/**
	 * 更新登陆用户信息
	 * @param bean
	 * @return
	 */
	int updateLoginUserInfo(UserInfoBean bean);
	
	/**
	 * 更新登陆用户openId
	 * @param bean
	 * @return
	 */
	int updateLoginUserOpenId(UserInfoBean bean);

	/**
	 * 更新登陆用户openId
	 * @param bean
	 * @return
	 */
	int updateLoginqqOpenId(UserInfoBean bean);

	/**
	 * 更新登陆用户openId
	 * @param bean
	 * @return
	 */
	int updateLoginAiliOpenId(UserInfoBean bean);

	/**
	 * 忘记密码
	 * 
	 * @param publicUserBean
	 * @return
	 */
	int forgotPwd(UserInfoBean publicUserBean);
	
	/**
	 *  根据token和password获取当前用户信息
	 * @param bean
	 * @return
	 */
	UserInfoBean getUserInfoToken(UserInfoBean bean);
	
	/**
	 * 修改用户密码
	 * @param bean
	 * @return
	 */
	int updateUserInfoPwd(UserInfoBean bean);

	/**
	 * 注册融云
	 * 
	 * @param publicUserBean
	 * @return
	 */
	int registerRCUserInfo(UserInfoBean bean);
	
	/**
	 * 验证登录token
	 * @param token
	 * @return
	 * @throws ServiceException
	 */
	UserInfoBean checkLoginToken(String token);

}

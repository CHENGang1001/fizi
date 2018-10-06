package com.gq.business.accountmanage.mappers;

import com.gq.business.accountmanage.model.*;

;

/**
 * 公共用户Mapper
 */
public interface PublicUserMapper {
	/**
	 * 根据ID检索
	 *
	 * @param publicUserBean
	 * <br />
	 */
	PublicUserBean getByID(PublicUserBean publicUserBean);

	/**
	 * 更新用户信息
	 *
	 * @param publicUserBean
	 * <br />
	 */
	int updateUser(PublicUserBean publicUserBean);

	/**
	 * 查看手机号码是否在库中是否存在
	 *
	 * @param publicUserBean
	 * <br />
	 */
	int userNoByMsisdn(PublicUserBean publicUserBean);

	/**
	 * 增加用户
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	int addUser(PublicUserBean publicUserBean);

	/**
	 * 查询用户信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	PublicUserBean checkAccount(PublicUserBean publicUserBean);

	/**
	 * 查询用户信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	PublicUserBean checkAccountByMsisdn(PublicUserBean publicUserBean);

	/**
	 * 更换密码
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	int changePwd(PublicUserBean publicUserBean);

	/**
	 * 绑定就诊卡号
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	int bindPatientCard(PublicUserBean publicUserBean);

	/**
	 * 检查次就诊卡号是否已被绑定
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	PublicUserBean queryPatientCard(PublicUserBean publicUserBean);

	/**
	 * 根据用户姓名获得用户信息
	 *
	 * @param userName
	 * <br />
	 * @return <br />
	 */
	PublicUserBean queryUserInfoByUserName(String userName);

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
	 * 更新健康卡信息
	 * 
	 * @param pBean
	 * @return
	 */
	int updateHealthCardInfo(PublicUserBean pBean);

	/**
	 * 更改健康卡信息
	 * 
	 * @param patientName
	 * @param outCardNo
	 * @return
	 */
	int updateHealthCardByUserName(String userName, String outCardNo);

	//根据订单ID获取对应的创建者用户ID
	PublicUserBean getPublicUserByOrderID(String orderId);

	/**
	 * 查出用户上一次的登录信息
	 * 
	 * @param patientName
	 * @param outCardNo
	 * @return
	 */
	
	LoginInfoBean selectId(LoginInfoBean logininfo);
	
	/**
	 * 修改上次的退出登录时间为本次的登录时间
	 * 
	 * @param patientName
	 * @param outCardNo
	 * @return
	 */

	void editloginInfo(LoginInfoBean logininfo);

	/**
	 * 添加用户的登录信息入库
	 * 
	 * @param patientName
	 * @param outCardNo
	 * @return
	 */
	void addloginInfo(LoginInfoBean logininfo);
}

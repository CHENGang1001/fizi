package com.gq.business.accountmanage.mappers;

import java.util.List;

import com.gq.business.accountmanage.model.VeifyCodeBean;

/**
 * 手机验证码DAO层
 *
 * @author zhixinchen
 * @ClassName: VerifyCodeMapper
 * @Description: 手机验证码DAO层
 * @date: Nov 22, 2013 12:34:09 PM
 */
public interface VerifyCodeMapper {
	/**
	 * 插入手机验证码
	 *
	 * @param veifyCodeBean
	 * <br />
	 * @return <br />
	 */
	int insert(VeifyCodeBean veifyCodeBean);

	/**
	 * 更新手机验证码
	 *
	 * @param veifyCodeBean
	 * <br />
	 * @return <br />
	 */
	int update(VeifyCodeBean veifyCodeBean);

	/**
	 * 删除手机验证码
	 *
	 * @param veifyCodeBean
	 * <br />
	 * @return <br />
	 */
	int destory(VeifyCodeBean veifyCodeBean);

	/**
	 * 校验验证码
	 *
	 * @param veifyCodeBean
	 * <br />
	 * @return <br />
	 */
	List<VeifyCodeBean> checkVerifyCode(VeifyCodeBean veifyCodeBean);

	/**
	 * 清除验证码表中所有数据
	 *
	 * @return <br />
	 */
	int truncateVerifyCode();

	/**
	 * 根据时间清除验证码
	 *
	 * @return <br />
	 */
	int deleteByTime();
}

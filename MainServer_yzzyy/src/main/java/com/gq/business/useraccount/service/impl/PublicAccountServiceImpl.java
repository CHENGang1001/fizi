package com.gq.business.useraccount.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.Nil;
import com.gq.business.accountmanage.mappers.VerifyCodeMapper;
import com.gq.business.accountmanage.model.LoginInfoBean;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.model.VeifyCodeBean;
import com.gq.business.useraccount.mappers.PublicAccountMapper;
import com.gq.business.useraccount.model.UserInfoBean;
import com.gq.business.useraccount.service.IPublicAccountService;
import com.gq.common.exception.ServiceException;
import com.gq.common.utils.MathUtils;
import com.gq.common.utils.PropertiesUtil;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;
import com.qiniu.util.Auth;

import io.rong.RongCloud;
import io.rong.methods.user.User;
import io.rong.models.SMSVerifyCodeResult;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

/**
 * 用户操作Service层
 *
 * @ClassName: IUserInterfaceService
 * @Description: 提供大众用户操作Service服务
 */
@Service
public class PublicAccountServiceImpl implements IPublicAccountService{

	/**
	 * 忘记密码时的短信验证码类型
	 */
	private static final String GETVERIFYCODEBYFORGETPWD = "1";
	/**
	 * 修改密码时的短信验证码类型
	 */
	private static final String GETVERIFYCODEBYCHANGEPWD = "2";
	/**
	 * 三方登录
	 */
	private static final String GETVERIFYCODEFORTHIRD = "3";

	//	@Autowired
	//	private VerifyCodeMapper verifyCodeMapper;

	@Autowired
	private PublicAccountMapper publicUserMapper;

	/**
	 * 注册
	 */
	@Override
	public void userRegister(UserInfoBean publicUserBean) throws ServiceException {
		// TODO Auto-generated method stub
		// 校验手机验证码
		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		veifyCodeBean.setMsisdn(publicUserBean.getPhoneNumber());
		veifyCodeBean.setVerifyCode(publicUserBean.getVerifyCode());
		SMSVerifyCodeResult sMSVerifyCodeResult = new SMSVerifyCodeResult(200, true, null);
		try {

			RongCloud rongCloud = RongCloud.getInstance(PropertiesUtil.getMessageValueByKey("RCAPPKEY"), PropertiesUtil.getMessageValueByKey("RCAppSecret"));

			//短信验证
			sMSVerifyCodeResult = rongCloud.sms.verifyCode(publicUserBean.getSessionId(), publicUserBean.getVerifyCode());

			if (!sMSVerifyCodeResult.getSuccess()) {
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}

			//自定义 api 地址方式
			// RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret,api);
			User User = rongCloud.user;

			/**
			 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/user/user.html#register
			 *
			 * 注册用户，生成用户在融云的唯一身份标识 Token
			 */
			String userName = publicUserBean.getUserName();
			if(userName == null || userName.isEmpty()) {	
				String str = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
				userName = "用户" + str;
			}
			publicUserBean.setUserName(userName);

			String headPic = publicUserBean.getHeadPic();
			if(headPic == null || headPic.isEmpty()) {
				headPic = "f11914798a7b7afc6d61d32433485852.png"; // 默认头像http://p70a2g4yj.bkt.clouddn.com/
			}
			publicUserBean.setHeadPic(headPic);

			//用户注册入库
			publicUserBean.setIsClose("0");
			publicUserMapper.registerUserInfo(publicUserBean);

			UserModel user = new UserModel()
					.setId(publicUserBean.getPhoneNumber())
					.setName(publicUserBean.getUserName())
					.setPortrait(PropertiesUtil.getMessageValueByKey("STATICQINIUSERVICE") + headPic);
			TokenResult result = User.register(user);
			System.out.println("getToken:  " + result.toString());

			//融云入库操作
			UserInfoBean bean = new UserInfoBean();
			bean.setHeadPic(headPic);
			bean.setUserName(publicUserBean.getUserName());
			bean.setRcToken(result.getToken().toString());
			bean.setRcUserId(publicUserBean.getPhoneNumber());
			publicUserMapper.registerRCUserInfo(bean);


		} catch (Exception e) {
			// TODO: handle exception
			switch (sMSVerifyCodeResult.getCode()) {
			case 200:{
				if (!sMSVerifyCodeResult.getSuccess()) {
					throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
				}
			}
			case 1014:{
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALIDAGAIN);
			}
			case 1015:{
				throw new ServiceException(ReturnCode.USER_ERIFYCODE_TIMEOVER);
			}
			default:
				break;
			}
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}
	}
	

	/**
	 * 注册 -- 三方登录失败（首次用三方登录）需要绑定手机号码密码等信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	@Override
	public UserInfoBean userThirdRegister(UserInfoBean publicUserBean) throws ServiceException {
		// TODO Auto-generated method stub
		// 校验手机验证码
		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		veifyCodeBean.setMsisdn(publicUserBean.getPhoneNumber());
		veifyCodeBean.setVerifyCode(publicUserBean.getVerifyCode());
		SMSVerifyCodeResult sMSVerifyCodeResult = new SMSVerifyCodeResult(200, true, null);
		RongCloud rongCloud = RongCloud.getInstance(PropertiesUtil.getMessageValueByKey("RCAPPKEY"), PropertiesUtil.getMessageValueByKey("RCAppSecret"));
		//短信验证
		try {
			sMSVerifyCodeResult = rongCloud.sms.verifyCode(publicUserBean.getSessionId(), publicUserBean.getVerifyCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}
		if (sMSVerifyCodeResult.getSuccess() == null || !sMSVerifyCodeResult.getSuccess()) {
			throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
		}

		try {
			UserInfoBean bean = publicUserMapper.checkAccount(publicUserBean);
			if (bean == null) {
				//无此用户信息，需要注册并绑定  返回bean
				User User = rongCloud.user;

				/**
				 * API 文档: http://rongcloud.github.io/server-sdk-nodejs/docs/v1/user/user.html#register
				 *
				 * 注册用户，生成用户在融云的唯一身份标识 Token
				 */
				String userName = publicUserBean.getUserName();
				if(userName == null || userName.isEmpty()) {	
					String str = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
					userName = "用户" + str;
				}
				publicUserBean.setUserName(userName);

				String headPic = publicUserBean.getHeadPic();
				if(headPic == null || headPic.isEmpty()) {
					headPic = "f11914798a7b7afc6d61d32433485852.png"; // 默认头像http://p70a2g4yj.bkt.clouddn.com/
				}
				publicUserBean.setHeadPic(headPic);

				//用户注册入库
				publicUserBean.setIsClose("0");
				publicUserMapper.registerUserInfo(publicUserBean);

				UserModel user = new UserModel()
						.setId(publicUserBean.getPhoneNumber())
						.setName(publicUserBean.getUserName())
						.setPortrait(PropertiesUtil.getMessageValueByKey("STATICQINIUSERVICE") + headPic);
				TokenResult result = new TokenResult(200, null, null, null);
				try {
					result = User.register(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new ServiceException(ReturnCode.SYSTEM_ERROR);
				}

				System.out.println("getToken:  " + result.toString());

				//融云入库操作
				UserInfoBean infoBean = new UserInfoBean();
				infoBean.setHeadPic(headPic);
				infoBean.setUserName(publicUserBean.getUserName());
				infoBean.setRcToken(result.getToken().toString());
				infoBean.setRcUserId(publicUserBean.getPhoneNumber());
				publicUserMapper.registerRCUserInfo(infoBean);
			}

			String openId = "";
			//如果openID没有值 则可直接绑定 只绑定openid 
			switch (publicUserBean.getLoginType()) {
			case 2:
			{
				openId = bean.getOpenId();
				if (openId.isEmpty()) {
					publicUserMapper.updateLoginUserOpenId(bean);
				}
			}
			break;
			case 3:
			{
				openId = bean.getQqopenId();
				if (openId.isEmpty()) {
					publicUserMapper.updateLoginqqOpenId(bean);
				}
			}
			break;
			case 4:
			{
				openId = bean.getAiliAuthId();
				if (openId.isEmpty()) {
					publicUserMapper.updateLoginAiliOpenId(bean);
				}
			}
			break;

			default:
				break;
			}

			if (!openId.isEmpty()) {
				throw new ServiceException(ReturnCode.USER_AUTHORIZEFAIL_EXIST);
			}


			return publicUserMapper.getUserInfo(bean);

		} catch (DuplicateKeyException e) {
			// TODO: handle exception
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 获取手机验证码
	 *
	 * @param publicUserBean
	 * <br />
	 */
	public String getVerifyCode(UserInfoBean publicUserBean) throws ServiceException {
		String userName = "";

		// 如果是因为忘记密码而获取验证码，需要检查该手机号是否注册
		if (GETVERIFYCODEBYFORGETPWD.equals(publicUserBean.getVerifyType())) {
			// 检查手机号码是否注册
			userName = publicUserMapper.getUserNameByMsisdn(publicUserBean.getPhoneNumber());

			// 如果手机号码未注册，不产生验证码，并返回异常
			if (StringUtils.isEmpty(userName)) {
				// need record log
				throw new ServiceException(ReturnCode.USER_PHONENUM_NOT_REGISTER);
			}
		}else if (GETVERIFYCODEBYCHANGEPWD.equals(publicUserBean.getVerifyType())) {
			// 检查手机号码是否注册
			userName = publicUserMapper.getUserNameByMsisdn(publicUserBean.getPhoneNumber());

			// 如果手机号码未注册，不产生验证码，并返回异常
			if (StringUtils.isEmpty(userName)) {
				// need record log
				throw new ServiceException(ReturnCode.USER_PHONENUM_NOT_REGISTER);
			}
		}
		else if (GETVERIFYCODEFORTHIRD.equals(publicUserBean.getVerifyType())){
		}
		else {
			// 判断手机号码是否注册
			if (publicUserMapper.userNoByMsisdn(publicUserBean) != 0) {
				// need record log
				throw new ServiceException(ReturnCode.USER_MSISDN_REGISTERED);
			}
		}

		// 生成手机验证码
		String verifyCode = MathUtils.getVerifyCode();

		// 手机验证码入库
		//		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		//		veifyCodeBean.setMsisdn(publicUserBean.getPhoneNumber());
		//		veifyCodeBean.setVerifyCode(verifyCode);
		//		try {
		//			verifyCodeMapper.insert(veifyCodeBean);
		//		} catch (DuplicateKeyException e) {
		//			verifyCodeMapper.update(veifyCodeBean);
		//		}
		return verifyCode;
		/*
		 * //发送手机验证码 if (StringUtils.isEmpty(userName)) {
		 * SmsClient.sendSms(publicUserBean.getMsisdn(), verifyCode); } else {
		 * SmsClient.sendSms(publicUserBean.getMsisdn(), verifyCode, userName);
		 * }
		 */
	}

	/**
	 * 登陆接口的实现
	 */
	@Override
	public UserInfoBean userLogin(UserInfoBean pUserInfoBean) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			UserInfoBean bean = new UserInfoBean();
			bean = publicUserMapper.getUserInfo(pUserInfoBean);
			if (bean == null) {
				bean = publicUserMapper.checkAccount(pUserInfoBean);
				if (bean == null) {
					throw new ServiceException(ReturnCode.USER_USERNAME_INVALID);
				}
				else {
					throw new ServiceException(ReturnCode.USER_PWD_INVALID);
				}
			}
			else {
				if (bean.getIsClose() == "1") {
					throw new ServiceException(ReturnCode.USER_ACCOUNTISCLOSE);
				}

				String token = UUID.randomUUID().toString().replace("-", "");
				bean.setToken(token);
				bean.setQiniuServeUrl(PropertiesUtil.getMessageValueByKey("STATICQINIUSERVICE"));

				Auth auth = Auth.create(PropertiesUtil.getMessageValueByKey("QINIUACCESSKEY"), PropertiesUtil.getMessageValueByKey("QINIUSECRETKEY"));
				bean.setQiniuToken(auth.uploadToken(PropertiesUtil.getMessageValueByKey("QINIUSPACENAME")));

				publicUserMapper.updateLoginUserInfo(bean);
			}

			return bean;
		} catch (DuplicateKeyException e) {
			// TODO handle exception
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}		
	}

	/**
	 * 三方登陆接口的实现
	 */
	@Override
	public UserInfoBean userThirdLogin(UserInfoBean pUserInfoBean) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			UserInfoBean bean = new UserInfoBean();
			switch (pUserInfoBean.getLoginType()) {
			case 2://微信授权登录
				bean = publicUserMapper.getUserInforThirdLogin(pUserInfoBean);
				break;
			case 3:{//QQ登录
				pUserInfoBean.setQqopenId(pUserInfoBean.getOpenId());
				bean = publicUserMapper.getUserInforQQLogin(pUserInfoBean);
			}
			break;
			case 4:{//支付宝
				pUserInfoBean.setAiliAuthId(pUserInfoBean.getOpenId());
				bean = publicUserMapper.getUserInforAiliLogin(pUserInfoBean);
			}
			break;
			default:
				break;
			}
			if (bean == null) {
				throw new ServiceException(ReturnCode.USER_AUTHORIZEFAIL_NOUSER);
			}
			else {
				if (bean.getIsClose() == "1") {
					throw new ServiceException(ReturnCode.USER_ACCOUNTISCLOSE);
				}

				String token = UUID.randomUUID().toString().replace("-", "");
				bean.setToken(token);
				bean.setQiniuServeUrl(PropertiesUtil.getMessageValueByKey("STATICQINIUSERVICE"));

				Auth auth = Auth.create(PropertiesUtil.getMessageValueByKey("QINIUACCESSKEY"), PropertiesUtil.getMessageValueByKey("QINIUSECRETKEY"));
				bean.setQiniuToken(auth.uploadToken(PropertiesUtil.getMessageValueByKey("QINIUSPACENAME")));

				publicUserMapper.updateLoginUserInfo(bean);
			}
			bean.setLoginType(pUserInfoBean.getLoginType());
			return bean;
		} catch (DuplicateKeyException e) {
			// TODO handle exception
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}		
	}

	@Override
	public void forgotPwd(UserInfoBean publicUserBean) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			//					publicUserBean.setIsClose("0");
			RongCloud rongCloud = RongCloud.getInstance(PropertiesUtil.getMessageValueByKey("RCAPPKEY"), PropertiesUtil.getMessageValueByKey("RCAppSecret"));

			//短信验证
			SMSVerifyCodeResult sMSVerifyCodeResult = rongCloud.sms.verifyCode(publicUserBean.getSessionId(), publicUserBean.getVerifyCode());

			if (!sMSVerifyCodeResult.getSuccess()) {
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}

			publicUserMapper.forgotPwd(publicUserBean);
		} catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}

	}

	/**
	 * 修改密码
	 */
	@Override
	public void modifyPwd(UserInfoBean userInfo) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			UserInfoBean bean = new UserInfoBean();
			parmVerifyToken(userInfo.getToken());
			bean = publicUserMapper.getUserInfoToken(userInfo);
			if (bean==null) {
				throw new  ServiceException(ReturnCode.SYSTEM_ERROR);
			}
			else {
				userInfo.setPassword(userInfo.getNewPassword());
				publicUserMapper.updateUserInfoPwd(userInfo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}
	}


	/**
	 * 验证登录token是否失效
	 * @param token
	 * @throws ServiceException
	 */
	public void parmVerifyToken(String token) throws ServiceException{
		if (StringUtils.isEmpty(token)) {
			throw new ServiceException(ReturnCode.USER_LOGININVALID);
		}
		UserInfoBean bean = new UserInfoBean(); 
		bean = publicUserMapper.checkLoginToken(token);
		if (bean == null) {
			throw new ServiceException(ReturnCode.USER_LOGININVALID);
		}
	}
}

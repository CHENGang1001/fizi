package com.gq.business.useraccount.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.ResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.rong.models.SMSSendCodeResult;
import io.rong.models.SMSVerifyCodeResult;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import io.rong.RongCloud;
import io.rong.methods.user.User;

import com.gq.base.BaseController;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.service.IPublicUserService;
import com.gq.business.useraccount.mappers.PublicAccountMapper;
import com.gq.business.useraccount.model.UserInfoBean;
import com.gq.business.useraccount.service.IPublicAccountService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntityEx;
import com.gq.common.response.ResponseEntity;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.PropertiesUtil;
import com.gq.common.utils.SmsClient;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;

/**
 * 用户操作接口层
 *
 * @author
 * @ClassName: UserAccountController
 * @Description: 实现用户操作的接口功能
 * @date:
 */
@Controller
public class UserAccountController extends BaseController{
	@Autowired
	private IPublicAccountService accountInterfaceService;

	@Autowired
	private IPublicUserService userInterfaceService;

	@Autowired
	private PublicAccountMapper publicUserMapper;

	/**
	 * 注册
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/register")
	public @ResponseBody ResponseEntity userRegister(@RequestBody RequestEntityEx<UserInfoBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取注册信息
			UserInfoBean userInfo = requestBody.getContent();

			// 校验参数
			parmVerify(userInfo, "UserRegister"); 

			// 注册用户
			accountInterfaceService.userRegister(userInfo);

		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			Map<String, String> mapRegister = new HashMap<String, String>();
			responseEntity.setContent(mapRegister);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "UserRegister", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 获取手机验证码
	 *
	 * @param request
	 * <br />
	 * @param response
	 * <br />
	 */
	@RequestMapping(value = "/sendSMS")
	public @ResponseBody ResponseEntity getPhoneVerificationCode(HttpServletRequest request,
			@RequestBody RequestEntityEx<UserInfoBean> requestBody) {
		// Cookie[] cookies = request.getCookies();
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		String verifyCode = null;
		SMSSendCodeResult sMSSendCodeResult = new SMSSendCodeResult(-1, "", "");
		try {
			// 获取手机验证信息
			UserInfoBean userInfo = requestBody.getContent();

			// 校验输入参数
			parmVerify(userInfo, "GetPhoneVerificationCode");

			// 获取手机验证码
			verifyCode = accountInterfaceService.getVerifyCode(userInfo); // 仅仅做校验用

			RongCloud rongCloud = RongCloud.getInstance(PropertiesUtil.getMessageValueByKey("RCAPPKEY"), PropertiesUtil.getMessageValueByKey("RCAppSecret"));

			// 发送短信验证码方法。 
			sMSSendCodeResult = rongCloud.sms.sendCode(userInfo.getPhoneNumber(), PropertiesUtil.getMessageValueByKey("RCTemplateId"), "86", null, null);
			System.out.println("sendCode:" + sMSSendCodeResult.toString());

			//   			// 验证码验证方法 
			//   			SMSVerifyCodeResult sMSVerifyCodeResult = rongCloud.sms.verifyCode("2312312", "2312312");
			//   			System.out.println("verifyCode:  " + sMSVerifyCodeResult.toString());

		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);

			// 构造包体，该接口包体为空
			Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
			mapGetVerifyCode.put("sessionId", sMSSendCodeResult.getSessionId());
			responseEntity.setContent(mapGetVerifyCode);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "GetPhoneVerificationCode", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 登陆
	 * @param requestBody
	 * @return
	 */
	@RequestMapping (value ="/login")
	public @ResponseBody ResponseEntity login(@RequestBody RequestEntityEx<UserInfoBean>requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		// 获取手机验证信息
		UserInfoBean userInfo = requestBody.getContent();

		try {
			// 校验输入参数
			parmVerify(userInfo, "Login");
			//登陆
			userInfo = accountInterfaceService.userLogin(userInfo);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			//			Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
			// mapGetVerifyCode.put("verifyCode", verifyCode);
			if (returnCode.getCode().equals("1")) {
				responseEntity.setContent(userInfo);
			}
			else {
				responseEntity.setContent(new HashMap<>());
			}
			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "Login", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}
	
	@RequestMapping (value ="/thirdLogin")
	public @ResponseBody ResponseEntity threePartyLogin(@RequestBody RequestEntityEx<UserInfoBean>requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		// 获取手机验证信息
		UserInfoBean userInfo = requestBody.getContent();
		try {
			// 校验输入参数
			parmVerify(userInfo, "thirdLogin");
			//登陆
			userInfo = accountInterfaceService.userThirdLogin(userInfo);
			
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			//			Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
			// mapGetVerifyCode.put("verifyCode", verifyCode);
			if (returnCode.getCode().equals("1")) {
				responseEntity.setContent(userInfo);
			}
			else {
				responseEntity.setContent(new HashMap<>());
			}
			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "Login", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}
	
	/**
	 * 检测手机号码是否已经存在并三方注册信息是否存在
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/checkCountForThird")
	public @ResponseBody ResponseEntity checkCountForThird(@RequestBody RequestEntityEx<UserInfoBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取注册信息
			UserInfoBean publicUserBean = requestBody.getContent();

			UserInfoBean bean = publicUserMapper.checkAccount(publicUserBean);
			if (bean == null) {
				//该用户不存在，需要填写密码注册信息
				throw new ServiceException(ReturnCode.USER_MSISDN_NO);
			}
			else {
				String openId = "";
				//如果openID没有值 则可直接绑定 只绑定openid
				switch (publicUserBean.getLoginType()) {
				case 2:
				{
					openId = bean.getOpenId();
				}
				break;
				case 3:
				{
					openId = bean.getQqopenId();
				}
				break;
				case 4:
				{
					openId = bean.getAiliAuthId();
				}
				break;

				default:
					break;
				}
				if (openId != null && !openId.isEmpty()) {
					throw new ServiceException(ReturnCode.USER_MSISDN_EXIST);
				}
				else {
					throw new ServiceException(ReturnCode.USER_MSISDN_YES);
				}
			}

		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			Map<String, String> mapRegister = new HashMap<String, String>();
			responseEntity.setContent(mapRegister);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "UserRegister", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}
	
	/**
	 * 注册 -- 三方登录失败（首次用三方登录）需要绑定手机号码密码等信息
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/registerForThird")
	public @ResponseBody ResponseEntity userRegisterForThird(@RequestBody RequestEntityEx<UserInfoBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		// 获取注册信息
		UserInfoBean userInfo = requestBody.getContent();
		try {
			// 校验参数
			parmVerify(userInfo, "thirdLogin"); 
			parmVerify(userInfo, "UserRegister"); 

			// 注册用户
			userInfo = accountInterfaceService.userThirdRegister(userInfo);

		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			Map<String, String> mapRegister = new HashMap<String, String>();
			if (responseEntity.getHeader().getResultCode().equals("1")) {
				responseEntity.setContent(userInfo);
			}
			else {
				responseEntity.setContent(mapRegister);
			}

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "UserRegister", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}
	

	@RequestMapping(value="/forgotPwd")
	public @ResponseBody ResponseEntity forgotPsw(@RequestBody RequestEntityEx<UserInfoBean>requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime =System.currentTimeMillis();

		try {
			// 获取注册信息
			UserInfoBean userInfo = requestBody.getContent();


			// 校验参数
			parmVerify(userInfo, "ForgotPwd"); 

			// 注册用户
			accountInterfaceService.forgotPwd(userInfo);

		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			Map<String, String> mapRegister = new HashMap<String, String>();
			responseEntity.setContent(mapRegister);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "ForgotPwd", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}

		return responseEntity;

	}

	@RequestMapping(value="/modifyPwd")
	public @ResponseBody ResponseEntity modifyPwd(@RequestBody RequestEntityEx<UserInfoBean>requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime =System.currentTimeMillis();

		try {
			// 获取注册信息
			UserInfoBean userInfo = requestBody.getContent();

			// 校验参数
			parmVerify(userInfo, "ModifyPwd"); 

			// 注册用户
			accountInterfaceService.modifyPwd(userInfo);

		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体，该接口包体为空
			Map<String, String> mapRegister = new HashMap<String, String>();
			responseEntity.setContent(mapRegister);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "ModifyPwd", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}

		return responseEntity;

	}

	/**
	 * 参数校验
	 *
	 * @param publicUserBean
	 * <br />
	 * @param type
	 * <br />
	 */
	private void parmVerify(UserInfoBean publicUserBean, String type) throws ServiceException {
		// 用户注册校验：用户名，手机号码，密码，验证码
		if ("UserRegister".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getPassword()))
				throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			// if (StringUtils.isEmpty(publicUserBean.getUserID()))
			// throw new ServiceException(ReturnCode.USER_USERID_INVALID);
			// if (StringUtils.isEmpty(publicUserBean.getUserRealName()))
			// throw new ServiceException(ReturnCode.USER_REALNAME_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getPhoneNumber()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getVerifyCode()))
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_NULL);
			if (publicUserBean.getPassword().length() < 6){
				throw new ServiceException(ReturnCode.USER_PASSWORDLEGHTERROR);
			}
			if(StringUtils.isEmpty(publicUserBean.getSessionId())){
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}
			if(publicUserBean.getUserType() != 1 && publicUserBean.getUserType() != 2 && publicUserBean.getUserType() != 3){
				throw new ServiceException(ReturnCode.USER_ACCOUNTUSERTYPE);
			}
		}
		//登陆验证
		if ("Login".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getPassword()))
				throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getPhoneNumber()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getClient()))
				throw new ServiceException(ReturnCode.USER_CLIENTTYPE_INVALID);

		}
		//登陆验证
		if ("thirdLogin".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getOpenId()))
				throw new ServiceException(ReturnCode.USER_AUTHORIZEFAIL);
			if (StringUtils.isEmpty(publicUserBean.getUserName()))
				throw new ServiceException(ReturnCode.USER_AUTHORIZEFAIL_NAME);
			if (StringUtils.isEmpty(publicUserBean.getHeadPic()))
				throw new ServiceException(ReturnCode.USER_AUTHORIZEFAIL_HEEADPIC);
			if (StringUtils.isEmpty(publicUserBean.getClient()))
				throw new ServiceException(ReturnCode.USER_CLIENTTYPE_INVALID);
			if (String.valueOf(publicUserBean.getLoginType()).isEmpty() || (publicUserBean.getLoginType() != 2 && 
					publicUserBean.getLoginType() != 3 && 
					publicUserBean.getLoginType() != 4))
				throw new ServiceException(ReturnCode.USER_LOGINTYPEFAIL);

		}
		//忘记密码
		if ("ForgotPwd".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getPassword()))
				throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getPhoneNumber()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getVerifyCode()))
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_NULL);
		}
		//修改密码
		if ("ModifyPwd".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getPassword()))
				throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getNewPassword()))
				throw new ServiceException(ReturnCode.USER_NEWPWD_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getPhoneNumber()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
		}
		if ("GetPhoneVerificationCode".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getPhoneNumber()))
				throw new ServiceException(ReturnCode.USER_MSISDN_isEmpty);
		}
	}

}

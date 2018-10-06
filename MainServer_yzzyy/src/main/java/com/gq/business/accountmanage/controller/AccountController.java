package com.gq.business.accountmanage.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.base.BaseController;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.service.IPublicUserService;
import com.gq.business.integral.mappers.IntegrationRuleMapper;
import com.gq.business.integral.service.IIntegrationRuleService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.request.RequestEntityEx;
import com.gq.common.response.ResponseEntity;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.SmsClient;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;
import com.mchange.v2.async.StrandedTaskReporting;

/**
 * 用户操作接口层
 *
 * @author
 * @ClassName: AccountController
 * @Description: 实现用户操作的接口功能
 * @date:
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {
	@Autowired
	private IPublicUserService userInterfaceService;
	
	/**
	 * DEBUG日志
	 */
	// private Logger log = MHLogManager.getDebugLog();

	/*
	 * @RequestMapping("/testUser") public @ResponseBody ResponseEntity
	 * testUser(@RequestBody CommonRequestBean<CDTOTestUser> testBody) {
	 * CDTOTestUser oBody = testBody.getContent();
	 * 
	 * ResponseEntity oResponse = new ResponseEntity();
	 * oResponse.getHeader().setResultCode("10000");
	 * oResponse.getHeader().setResultMsg("success");
	 * 
	 * Map<String, String> mapUserInfo = new HashMap<String, String>();
	 * mapUserInfo.put("id", "100"); mapUserInfo.put("name", "zhangxiang");
	 * mapUserInfo.put("status", "true");
	 * //oResponse.getContent().add(mapUserInfo); return oResponse; }
	 */

	/**
	 * 注册接口
	 */
	// 输入参数样例：{"content" : {"phoneNumber" : "13851683232", "pN":"18066666666","password" :
	// "E10ADC3949BA59ABBE56E057F20F883E", "verificationCode" : "123456"}}
	@RequestMapping(value = "/register")
	public @ResponseBody ResponseEntity userRegister(@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取注册信息
			PublicUserBean inputPublicUser = requestBody.getContent();

			// 由于输入的参数是phoneNumber，因此还需要将此参数赋值给MSISDN
			inputPublicUser.setMsisdn(inputPublicUser.getPhoneNumber());

			// 由于输入的参数是userName，因此将此参数赋值给userRealName作为初始值
			inputPublicUser.setUserName(inputPublicUser.getMsisdn());
			
			//存储推广人手机号码
			inputPublicUser.setShareUser(inputPublicUser.getShareUser()==null||inputPublicUser.getShareUser()==""?"":inputPublicUser.getShareUser());
			
			//分享用户的标示
			inputPublicUser.setShareUserId(inputPublicUser.getShareUserId()==null||inputPublicUser.getShareUserId()==""?"":inputPublicUser.getShareUserId());

			// 校验参数
			parmVerify(inputPublicUser, "UserRegister");

			// 注册用户
			userInterfaceService.userRegister(inputPublicUser);

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
	 * 登录接口
	 *
	 * @param request
	 * <br />
	 * @param response
	 * <br />
	 */
	// 入参样例：{"content" : {"phoneNumber" : "13851683232", "password" :
	// "E10ADC3949BA59ABBE56E057F20F883E"}}
	@RequestMapping(value = "/login")
	public @ResponseBody ResponseEntity loginAccount(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		PublicUserBean loggedUser = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取登录用户信息
			String phoneNumber = requestBody.getContent().get("phoneNumber").toString();
			String password = requestBody.getContent().get("password").toString();
			String channel = requestBody.getContent().get("channel").toString();
			String sign = requestBody.getContent().get("sign").toString();
			Map<String, Object> params = new HashMap<String, Object>();
			// 由于输入的参数是以phoneNumber作为用户名，因此还需要将此参数赋值给userName
			params.put("phoneNumber",phoneNumber);
			params.put("channel",channel);
			params.put("password",password);
			params.put("sign", sign);

			// 校验参数
			PublicUserBean inputPublicUser = new PublicUserBean();
			inputPublicUser.setUserName(phoneNumber);
			inputPublicUser.setPassword(password);
			// 校验参数
			parmVerify(inputPublicUser, "LoginAccount");
			String sr = UUID.randomUUID().toString().replace("-", "");
			System.out.println("唯一编码"+sr);
			// 登录
			loggedUser = userInterfaceService.loginAccount(params);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);

			// 构造包体
			Map<String, String> mapLoginInfo = new HashMap<String, String>();
			// 如果登录成功，设置sessionID
			if (loggedUser != null) {
				mapLoginInfo.put("sessionID", loggedUser.getSessionID());
				mapLoginInfo.put("userID", loggedUser.getUserID());
				mapLoginInfo.put("userName", loggedUser.getUserName());
				//设置session
				request.getSession().setAttribute("loginuser", loggedUser);
			} 
			// 如果登录失败，设置sessionID为-1
			else
				mapLoginInfo.put("sessionID", "-1");
			responseEntity.setContent(mapLoginInfo);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "LoginAccount", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 获取个人信息
	 *
	 * @param request
	 * <br />
	 * @param response
	 * <br />
	 */
	@RequestMapping(value = "/baseInfo")
	public @ResponseBody ResponseEntity baseInfo(@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		PublicUserBean userBean = null;
		long startTime = System.currentTimeMillis();

		try {
			// 获取登录用户信息
			PublicUserBean inputPublicUser = requestBody.getContent();
			// 校验参数
			parmVerify(inputPublicUser, "BaseInfo");
			// 登录
			userBean = userInterfaceService.baseInfo(inputPublicUser);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("headImg", null);
			resultMap.put("msisdn", userBean.getMsisdn());
			resultMap.put("userName", userBean.getUserName());
//			resultMap.put("patientCard",userBean.getPatientCard() );
			resultMap.put("gender", userBean.getGender());
			resultMap.put("age", userBean.getAge());
			responseEntity.setContent(resultMap);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "BaseInfo", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 修改个人信息
	 *
	 * @param request
	 * <br />
	 * @param response
	 * <br />
	 */
	@RequestMapping(value = "/updateBaseInfo")
	public @ResponseBody ResponseEntity updateBaseInfo(@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		PublicUserBean userBean = null;
		long startTime = System.currentTimeMillis();

		try {
			// 获取登录用户信息
			PublicUserBean inputPublicUser = requestBody.getContent();
			// 校验参数
			parmVerify(inputPublicUser, "UpdateBaseInfo");
			
//			inputPublicUser.setUserName(inputPublicUser.getUserName());
			// 登录
			userBean = userInterfaceService.updateBaseInfo(inputPublicUser);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			// 构造包体
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("headImg", null);
			resultMap.put("msisdn", userBean.getMsisdn());
			resultMap.put("userName", userBean.getUserName());
			resultMap.put("gender", userBean.getGender());
			resultMap.put("age", userBean.getAge());
			responseEntity.setContent(resultMap);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "UpdateBaseInfo", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 退出系统
	 */
	@RequestMapping(value = "/logout")
	public @ResponseBody void logoutAccount(@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		try {
			// 获取输入的参数
			PublicUserBean inputPublicUser = requestBody.getContent();

			// 校验参数
			//parmVerify(inputPublicUser, "LogoutAccount");

			// 退出系统
			userInterfaceService.logoutAccount(inputPublicUser);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
		} finally {
			// 构造应答对象
			ResponseEntity responseEntity = new ResponseEntity(returnCode);

			// 构造包体
			Map<String, String> mapLogoutInfo = new HashMap<String, String>();
			responseEntity.setContent(mapLogoutInfo);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "UserRegister", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/changePassword")
	public @ResponseBody ResponseEntity changePassword(@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取输入的修改密码参数
			PublicUserBean inputPublicUser = requestBody.getContent();

			// 由于输入的参数是phoneNumber，因此还需要将此参数赋值给MSISDN
			inputPublicUser.setMsisdn(inputPublicUser.getPhoneNumber());
			
//			parmVerify(inputPublicUser, "BaseInfo");
			
//			inputPublicUser.setUserName(inputPublicUser.getUserName());
//			inputPublicUser.setUserRealName(inputPublicUser.getUserRealName());
//			inputPublicUser.setUserID(inputPublicUser.getUserID());
			// 校验参数e10adc3949ba59abbe56e057f20f883e  18362093343
			parmVerify(inputPublicUser, "ChangePassword");

			// 修改参数
			userInterfaceService.changePassword(inputPublicUser);
		}
		// 服务异常
		catch (ServiceException se) {
			returnCode = se.getScode();
		}
		// 系统异常
		catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造应答对象
			responseEntity = new ResponseEntity(returnCode);

			// 构造包体，该接口包体为空
			Map<String, String> mapChangePWD = new HashMap<String, String>();
			responseEntity.setContent(mapChangePWD);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "changePassword", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 找回密码
	 *
	 * @param request
	 * <br />
	 * @param response
	 * <br />
	 */
	@RequestMapping(value = "/retrievePassword")
	public @ResponseBody ResponseEntity retrievePassword(@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取输入的找回密码参数
			PublicUserBean inputPublicUser = requestBody.getContent();

			// 由于输入的参数是phoneNumber，因此还需要将此参数赋值给MSISDN
			inputPublicUser.setMsisdn(inputPublicUser.getPhoneNumber());

			// 校验参数
			parmVerify(inputPublicUser, "RetrievePassword");

			// 找回密码
			userInterfaceService.forgotPassword(inputPublicUser);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造应答对象
			responseEntity = new ResponseEntity(returnCode);

			// 构造包体，该接口包体为空
			Map<String, String> mapRetrieveInfo = new HashMap<String, String>();
			responseEntity.setContent(mapRetrieveInfo);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "retrievePassword", requestBody.toString(),
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
			@RequestBody RequestEntityEx<PublicUserBean> requestBody) {
		// Cookie[] cookies = request.getCookies();
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		String verifyCode = null;
		try {
			// 获取手机验证信息
			PublicUserBean inputPublicUser = requestBody.getContent();

			// 由于输入的参数是phoneNumber，因此还需要将此参数赋值给MSISDN
			inputPublicUser.setMsisdn(inputPublicUser.getPhoneNumber());
			inputPublicUser.setUserName(inputPublicUser.getPhoneNumber());

			// 校验输入参数
			parmVerify(inputPublicUser, "GetPhoneVerificationCode");

			// 获取手机验证码
			verifyCode = userInterfaceService.getVerifyCode(inputPublicUser);
			// 发送验证码
			SmsClient.sendSms(inputPublicUser.getMsisdn(), verifyCode);
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
			// mapGetVerifyCode.put("verifyCode", verifyCode);
			responseEntity.setContent(mapGetVerifyCode);

			// 记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "GetPhoneVerificationCode", requestBody.toString(),
					responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	/**
	 * 绑定就诊卡
	 *
	 * @param request
	 * <br />
	 * @param response
	 * <br />
	 */
	/*
	 * @RequestMapping(value = "/BindPatientCard") public void
	 * bindPatientCard(HttpServletRequest request, HttpServletResponse response)
	 * { long startTime = System.currentTimeMillis(); String reqBody =
	 * HttpUtil.getPostBody(request); String returnCode = ""; int level =
	 * Level.INFO; try { Digester digester = new Digester();
	 * digester.setValidating(false);
	 * digester.addObjectCreate("BindPatientCard", PublicUserBean.class);
	 * digester.addCallMethod("BindPatientCard/SessionID", "setSessionID", 0);
	 * digester.addCallMethod("BindPatientCard/UserName", "setUserName", 0);
	 * digester.addCallMethod("BindPatientCard/PatientCardID",
	 * "setPatientCardID", 0); PublicUserBean publicUserBean = (PublicUserBean)
	 * digester.parse(new StringReader(reqBody)); parmVerify(publicUserBean,
	 * "BindPatientCard"); // 绑定就诊卡号
	 * userInterfaceService.bindPatientCard(publicUserBean);
	 * 
	 * response.setContentType("text/xml;charset=utf8");
	 * response.setHeader("Content-Encoding", "None"); returnCode =
	 * ReturnCode.SUCCESS; } catch (SBHospitalException e) { level =
	 * Level.ERROR; returnCode = e.getReturnCode();
	 * log.error(ErrorDesciptionUtil.getMessage(returnCode)); } catch
	 * (SAXException e) { level = Level.ERROR;
	 * log.error("XML{} Struct is error.", reqBody); log.error("{}", e);
	 * returnCode = ReturnCode.XMLSTRUCT_INCORRECT; } catch (Exception e) {
	 * level = Level.ERROR; log.error("Exception:{}", e); returnCode =
	 * ReturnCode.SYSTEM_ERROR; } finally { String rspBody = ""; try {
	 * UserResultBean userResultBean = new UserResultBean();
	 * userResultBean.setResultCode(returnCode); if
	 * (!ReturnCode.SUCCESS.equals(returnCode)) { CException cexception = new
	 * CException(); cexception.setErrorCode(returnCode);
	 * cexception.setErrorDesc(ErrorDesciptionUtil.getMessage(returnCode));
	 * userResultBean.setCException(cexception); } rspBody =
	 * createRspBody(userResultBean); response.getWriter().write(rspBody); }
	 * catch (IOException e) { log.error("IOException:{}", e); } // 接口日志
	 * LogManager.interfaceLog(level, "APK", "MHSERVER", "BindPatientCard",
	 * reqBody, rspBody, System.currentTimeMillis() - startTime); }
	 * 
	 * }
	 */

	/**
	 * 参数校验
	 *
	 * @param publicUserBean
	 * <br />
	 * @param type
	 * <br />
	 */
	private void parmVerify(PublicUserBean publicUserBean, String type) throws ServiceException {
		// 用户注册校验：用户名，手机号码，密码，验证码
		if ("UserRegister".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getPassword()))
				throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			// if (StringUtils.isEmpty(publicUserBean.getUserID()))
			// throw new ServiceException(ReturnCode.USER_USERID_INVALID);
			// if (StringUtils.isEmpty(publicUserBean.getUserRealName()))
			// throw new ServiceException(ReturnCode.USER_REALNAME_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getMsisdn()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getVerificationCode()))
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
		}

		// 登录过程需要校验：用户名、密码
		if ("LoginAccount".equals(type)) {
			// if (StringUtils.isEmpty(publicUserBean.getClientType()))
			// throw new ServiceException(ReturnCode.USER_CLIENTTYPE_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getUserName()))
				throw new ServiceException(ReturnCode.USER_USERNAME_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getPassword()))
				throw new ServiceException(ReturnCode.USER_PWD_INVALID);
		}

		if ("GetPhoneVerificationCode".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getMsisdn()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getVerifyType()))
				throw new ServiceException(ReturnCode.USER_VERIFYTYPE_INVALID);
		}

		if ("ChangePassword".equals(type)) {
//			 if (StringUtils.isEmpty(publicUserBean.getUserName()))
//			 throw new ServiceException(ReturnCode.USER_USERNAME_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getMsisdn()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			//if (StringUtils.isEmpty(publicUserBean.getPassword()))
			//	throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			// if (StringUtils.isEmpty(publicUserBean.getSessionID()))
			// throw new ServiceException(ReturnCode.USER_SESSIONID_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getNewPassword()))
				throw new ServiceException(ReturnCode.USER_NEWPASSWORD_INVALID);
			if ("1".equals(publicUserBean.getChangeType())) {
				if (StringUtils.isEmpty(publicUserBean.getPassword()))
					throw new ServiceException(ReturnCode.USER_PWD_INVALID);
			}else if ("2".equals(publicUserBean.getChangeType())) {
				if (StringUtils.isEmpty(publicUserBean.getNewPassword()))
					throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}
		}

		if ("RetrievePassword".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getMsisdn()))
				throw new ServiceException(ReturnCode.USER_MSISDN_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getNewPassword()))
				throw new ServiceException(ReturnCode.USER_NEWPASSWORD_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getVerificationCode()))
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
		}

		if ("BindPatientCard".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getUserName()))
				throw new ServiceException(ReturnCode.USER_USERNAME_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getSessionID()))
				throw new ServiceException(ReturnCode.USER_SESSIONID_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getPatientCardID()))
				throw new ServiceException(ReturnCode.USER_SESSIONID_INVALID);
		}

		if ("LogoutAccount".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getUserName()))
				throw new ServiceException(ReturnCode.USER_USERNAME_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getSessionID()))
				throw new ServiceException(ReturnCode.USER_SESSIONID_INVALID);
		}

		if ("BaseInfo".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getUserID()))
				throw new ServiceException(ReturnCode.USER_USERID_INVALID);
		}

		if ("UpdateBaseInfo".equals(type)) {
			if (StringUtils.isEmpty(publicUserBean.getUserID()))
				throw new ServiceException(ReturnCode.USER_USERID_INVALID);
			if (StringUtils.isEmpty(publicUserBean.getUserName()))
				throw new ServiceException(ReturnCode.USER_USERNAME_INVALID);
		}
	}
}

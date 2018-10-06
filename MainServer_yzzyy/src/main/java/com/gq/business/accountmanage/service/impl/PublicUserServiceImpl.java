package com.gq.business.accountmanage.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.gq.business.accountmanage.mappers.PublicUserMapper;
import com.gq.business.accountmanage.mappers.TransactionMapper;
import com.gq.business.accountmanage.mappers.VerifyCodeMapper;
import com.gq.business.accountmanage.model.LoginInfoBean;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.model.TransactionBean;
import com.gq.business.accountmanage.model.VeifyCodeBean;
import com.gq.business.accountmanage.service.IPublicUserService;
import com.gq.business.integral.service.IIntegrationRuleService;
import com.gq.common.exception.ServiceException;
import com.gq.common.utils.MathUtils;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;

/**
 * 用户操作Service层
 *
 * @ClassName: IUserInterfaceService
 * @Description: 提供大众用户操作Service服务
 */
@Service
public class PublicUserServiceImpl implements IPublicUserService {

	@Autowired
	private PublicUserMapper publicUserMapper;
	@Autowired
	private VerifyCodeMapper verifyCodeMapper;
	@Autowired
	private TransactionMapper transactionMapper;
	
	@Autowired
	private IIntegrationRuleService integralService;


	/**
	 * 忘记密码时的短信验证码类型
	 */
	private static final String GETVERIFYCODEBYFORGETPWD = "1";
	/**
	 * 修改密码时的短信验证码类型
	 */
	private static final String GETVERIFYCODEBYCHANGEPWD = "4";

	/**
	 * DEBUG日志
	 */
	// private Logger log = MHLogManager.getDebugLog();

	/**
	 * 登录接口
	 *
	 * @param param
	 * <br />
	 * @return <br />
	 * @throws ServiceException
	 */
	public PublicUserBean loginAccount(Map<String, Object> params) throws ServiceException {
		PublicUserBean param = new PublicUserBean();
		String phoneNumber = params.get("phoneNumber").toString();
		String password = params.get("password").toString();
		String channel = params.get("channel").toString();
		String sign = params.get("sign").toString();
		
		param.setUserName(phoneNumber);
		param.setPassword(password);
		param.setMsisdn(phoneNumber);

		// 先按照用户名校验密码
		PublicUserBean pubUserBean = publicUserMapper.checkAccount(param);

		// 如果校验失败，再按照手机号码校验密码
		if (null == pubUserBean) {
			// 设置手机号码
			param.setMsisdn(phoneNumber);

			// 按照手机号码校验密码
			pubUserBean = publicUserMapper.checkAccountByMsisdn(param);

			// 如果仍然失败，抛出异常
			if (null == pubUserBean) {
				// need record log
				throw new ServiceException(ReturnCode.USER_PWD_ERROR);
			}
		}

		// 如果登录成功，创建sessionID
		String transID = UUID.randomUUID().toString().replace("-", "");
		// 构造sessionBean
		TransactionBean transactionBean = new TransactionBean();
		transactionBean.setUserName(pubUserBean.getUserName());
		transactionBean.setTransID(transID);
		transactionBean.setApproach(channel);
		transactionBean.setSign(sign);
		try {
			// 将session保存到数据库
			transactionMapper.insert(transactionBean);
		} catch (DuplicateKeyException e) {
			transactionMapper.deleteByUsr(transactionBean);
			transactionMapper.insert(transactionBean);
		}
		// 设置sessionID
		pubUserBean.setSessionID(transID);

		//用户登录信息入库
		long time = System.currentTimeMillis();
		LoginInfoBean logininfo = new LoginInfoBean();
		logininfo.setUserid(pubUserBean.getUserID());
		LoginInfoBean	loginBean = publicUserMapper.selectId(logininfo);
		if(loginBean!=null){
			if(loginBean.getEndtime().equals("9999-09-09 00:00:00.0")){
				int id = loginBean.getId();
				logininfo.setId(id);
				publicUserMapper.editloginInfo(logininfo);
			}

		}
		logininfo.setChannel(channel);
		logininfo.setEndtime("9999-09-09 00:00:00");
		publicUserMapper.addloginInfo(logininfo);

		// 返回user信息
		return pubUserBean;
	}

	/**
	 * 获取手机验证码
	 *
	 * @param publicUserBean
	 * <br />
	 */
	public String getVerifyCode(PublicUserBean publicUserBean) throws ServiceException {
		String userName = "";

		// 如果是因为忘记密码而获取验证码，需要检查该手机号是否注册
		if (GETVERIFYCODEBYFORGETPWD.equals(publicUserBean.getVerifyType())) {
			// 检查手机号码是否注册
			userName = publicUserMapper.getUserNameByMsisdn(publicUserBean.getMsisdn());

			// 如果手机号码未注册，不产生验证码，并返回异常
			if (StringUtils.isEmpty(userName)) {
				// need record log
				throw new ServiceException(ReturnCode.USER_PHONENUM_NOT_REGISTER);
			}
		}else if (GETVERIFYCODEBYCHANGEPWD.equals(publicUserBean.getVerifyType())) {
			// 检查手机号码是否注册
			userName = publicUserMapper.getUserNameByMsisdn(publicUserBean.getMsisdn());

			// 如果手机号码未注册，不产生验证码，并返回异常
			if (StringUtils.isEmpty(userName)) {
				// need record log
				throw new ServiceException(ReturnCode.USER_PHONENUM_NOT_REGISTER);
			}
		} else {
			// 判断手机号码是否注册
			if (publicUserMapper.userNoByMsisdn(publicUserBean) != 0) {
				// need record log
				throw new ServiceException(ReturnCode.USER_MSISDN_REGISTERED);
			}
		}

		// 生成手机验证码
		String verifyCode = MathUtils.getVerifyCode();

		// 手机验证码入库
		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		veifyCodeBean.setMsisdn(publicUserBean.getMsisdn());
		veifyCodeBean.setVerifyCode(verifyCode);
		try {
			verifyCodeMapper.insert(veifyCodeBean);
		} catch (DuplicateKeyException e) {
			verifyCodeMapper.update(veifyCodeBean);
		}
		return verifyCode;
		/*
		 * //发送手机验证码 if (StringUtils.isEmpty(userName)) {
		 * SmsClient.sendSms(publicUserBean.getMsisdn(), verifyCode); } else {
		 * SmsClient.sendSms(publicUserBean.getMsisdn(), verifyCode, userName);
		 * }
		 */
	}

	/**
	 * 用户注册
	 *
	 * @param publicUserBean
	 * <br />
	 */
	public void userRegister(PublicUserBean publicUserBean) throws ServiceException {
		// 校验手机验证码
		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		veifyCodeBean.setMsisdn(publicUserBean.getMsisdn());
		veifyCodeBean.setVerifyCode(publicUserBean.getVerificationCode());

		// 如果校验失败，抛出异常
		List<VeifyCodeBean> dbCode = verifyCodeMapper.checkVerifyCode(veifyCodeBean);
		if (null == dbCode || dbCode.size() <= 0) {
			// need record log
			throw new ServiceException(ReturnCode.USER_CHECK_VERIFYCODE_FAIL);
		} else {
			Date now = new Date();
			Date dbTime = dbCode.get(0).getTimestamp();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dbTime);
			// 验证码时间+5分钟
			cal.add(Calendar.MINUTE, 5);
			Date dbTimeAfterFiveMinutes = cal.getTime();
			if (dbTimeAfterFiveMinutes.before(now)) {
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}
			// 销毁验证码
			verifyCodeMapper.destory(veifyCodeBean);
		}

		// 判断手机号码是否注册
		if (publicUserMapper.userNoByMsisdn(publicUserBean) != 0) {
			// need record log
			throw new ServiceException(ReturnCode.USER_MSISDN_REGISTERED);
		}

		// 增加用户
		try {
			String shareUser = publicUserBean.getShareUserId()==null?"":publicUserBean.getShareUserId();
			if(!shareUser.equals("") && publicUserBean.getShareUser()==null || publicUserBean.getShareUser().equals("")){
				publicUserBean.setShareUser(shareUser);
			}
			publicUserMapper.addUser(publicUserBean);
			Map<String, String> map = new HashMap<>();

			/**
			 * 给注册用户添加积分
			 */
			if(null != publicUserBean.getUserName() && !"".equals(publicUserBean.getUserName())){
				map.put("type", "1");
				map.put("username", publicUserBean.getUserName());
				map.put("paymoney", "");
				integralService.IntegrationRule(map);
			}
			/**
			 * 给分享用户添加积分
			 */
			if(null != publicUserBean.getShareUserId() && !"".equals(publicUserBean.getShareUserId())){
				map.put("type", "4");
				map.put("username", publicUserBean.getShareUserId());
				map.put("paymoney", "");
				integralService.IntegrationRule(map);
			}
		} catch (DuplicateKeyException e) {
			// need record log
			throw new ServiceException(ReturnCode.USER_USERNAME_EXISTS);
		}
	}

	/**
	 * 修改密码
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	public void changePassword(PublicUserBean publicUserBean) throws ServiceException {
		if ("1".equals(publicUserBean.getChangeType())) {
			PublicUserBean pubUserBean = publicUserMapper.checkAccount(publicUserBean);
			if (null == pubUserBean) {
				// need record log
				throw new ServiceException(ReturnCode.USER_PWD_ERROR_CHANGE);
			}
		}else{
		// 校验手机验证码
		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		veifyCodeBean.setMsisdn(publicUserBean.getMsisdn());
		veifyCodeBean.setVerifyCode(publicUserBean.getVerificationCode());
		
		// 如果校验失败，抛出异常
		List<VeifyCodeBean> dbCode = verifyCodeMapper.checkVerifyCode(veifyCodeBean);
		if (null == dbCode || dbCode.size() <= 0) {
			// need record log
			throw new ServiceException(ReturnCode.USER_CHECK_VERIFYCODE_FAIL);
		} else {
			Date now = new Date();
			Date dbTime = dbCode.get(0).getTimestamp();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dbTime);
			// 验证码时间+5分钟
			cal.add(Calendar.MINUTE, 5);
			Date dbTimeAfterFiveMinutes = cal.getTime();
			if (dbTimeAfterFiveMinutes.before(now)) {
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}
			// 销毁验证码
			verifyCodeMapper.destory(veifyCodeBean);
		}
		}
		// 检查该用户是否已经登录
//		 TransactionBean transactionBean = new TransactionBean();
//		 transactionBean.setTransID(publicUserBean.getSessionID());
//		 transactionBean.setUserName(publicUserBean.getUserName());
//		 authentication(transactionBean);
		

		// 验证原密码，如果验证原密码失败，抛出密码错异常
		/*PublicUserBean pubUserBean = publicUserMapper.checkAccount(publicUserBean);
		if (null == pubUserBean) {
			// need record log
			throw new ServiceException(ReturnCode.USER_PWD_ERROR_CHANGE);
		}*/

		// 更改密码
		publicUserMapper.changePwd(publicUserBean);
	}

	/**
	 * 找回密码
	 *
	 * @param publicUserBean
	 * <br />
	 * @throws ServiceException
	 */
	public void forgotPassword(PublicUserBean publicUserBean) throws ServiceException {
		// 校验手机验证码
		VeifyCodeBean veifyCodeBean = new VeifyCodeBean();
		veifyCodeBean.setMsisdn(publicUserBean.getMsisdn());
		veifyCodeBean.setVerifyCode(publicUserBean.getVerificationCode());

		// 如果校验失败，抛出异常
		List<VeifyCodeBean> dbCode = verifyCodeMapper.checkVerifyCode(veifyCodeBean);
		if (null == dbCode || dbCode.size() <= 0) {
			// need record log
			throw new ServiceException(ReturnCode.USER_CHECK_VERIFYCODE_FAIL);
		} else {
			Date now = new Date();
			Date dbTime = dbCode.get(0).getTimestamp();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dbTime);
			// 验证码时间+5分钟
			cal.add(Calendar.MINUTE, 5);
			Date dbTimeAfterFiveMinutes = cal.getTime();
			if (dbTimeAfterFiveMinutes.before(now)) {
				throw new ServiceException(ReturnCode.USER_VERIFYCODE_INVALID);
			}
			// 销毁验证码
			verifyCodeMapper.destory(veifyCodeBean);
		}

		// 更改密码
		publicUserMapper.changePwd(publicUserBean);
	}

	/**
	 * 绑定就诊卡
	 *
	 * @param publicUserBean
	 * <br />
	 */
	/*
	 * public void bindPatientCard(PublicUserBean publicUserBean) {
	 * TransactionBean transactionBean = new TransactionBean();
	 * transactionBean.setTransID(publicUserBean.getSessionID());
	 * transactionBean.setUserName(publicUserBean.getUserName()); // 用户鉴权
	 * authentication(transactionBean); PublicUserBean publicUserBeanbySQL =
	 * publicUserMapper.queryPatientCard(publicUserBean); if (null ==
	 * publicUserBeanbySQL) {
	 * publicUserBean.setUserName(publicUserBean.getUserName().substring(2));
	 * publicUserBeanbySQL = publicUserMapper.queryPatientCard(publicUserBean);
	 * } // 查看此用户是否已绑定就医卡 if
	 * (!StringUtils.isEmpty(publicUserBeanbySQL.getPatientCardID())) {
	 * log.error("patientcard{} been binded when phoneNum is {}", publicUserBean
	 * .getPatientCardID(), publicUserBean.getMsisdn()); throw new
	 * SBHospitalException(ReturnCode.PATIENTCARD_BINDED,
	 * "patientcard is bean binded"); } // 查his系统 Map<String, Object> map = new
	 * HashMap<String, Object>(); map.put("INPATIENTID",
	 * publicUserBean.getPatientCardID()); // 构造请求数据 String patientidCheckXML =
	 * registerAndFeeService
	 * .checkPatientidRegisteredForMobile(XmlUtil.createRequestXml(map));
	 * 
	 * PatientidCheckBean patientidCheckRsp = parseXML(patientidCheckXML); if
	 * (!CHECK_PATIENT_ID_REGISTERED_SUCCESS
	 * .equals(patientidCheckRsp.getAppCode())) {
	 * log.error("get info error by PatientCardID{}",
	 * publicUserBean.getPatientCardID()); throw new
	 * SBHospitalException(ReturnCode.GET_INPATIENTID_FAILED,
	 * "get info error by PatientCardID"); } if
	 * (!patientidCheckRsp.getMsisdn().equals(publicUserBeanbySQL.getMsisdn()))
	 * { log.error("phoneNum:{} is not match the userPhoneNum:{} in his",
	 * publicUserBean, patientidCheckRsp.getMsisdn()); throw new
	 * SBHospitalException(ReturnCode.MSISDN_NOTMATCH_HIS,
	 * "msisdn is not match the his' msisdn"); } //就诊卡绑定
	 * publicUserMapper.bindPatientCard(publicUserBean);
	 * 
	 * // 绑定就诊卡成功后需要保存就诊卡信息
	 * patientidCheckRsp.setInPatientID(publicUserBean.getPatientCardID()); if
	 * (publicUserMapper.checkPatientCard(patientidCheckRsp) == 0) {
	 * publicUserMapper.insertPatientCard(patientidCheckRsp); }
	 * 
	 * 
	 * }
	 */

	/**
	 * 退出
	 *
	 * @param publicUserBean
	 * <br />
	 */
	public void logoutAccount(PublicUserBean publicUserBean) throws ServiceException {
		TransactionBean transactionBean = new TransactionBean();
		String transID = UUID.randomUUID().toString().replace("-", "");
		transactionBean.setTransID(transID);
		transactionBean.setUserName(publicUserBean.getUserName());
		transactionBean.setSign("0");
		transactionBean.setApproach(publicUserBean.getApproach());
		// 用户鉴权
		//authentication(transactionBean);

		// 销毁sessionID
		//transactionMapper.destory(transactionBean);
		transactionMapper.insert(transactionBean);
	}

	/**
	 * 用户鉴权
	 *
	 * @param transactionBean
	 * <br />
	 */
	public void authentication(TransactionBean transactionBean) throws ServiceException {
		if (transactionMapper.checkTransaction(transactionBean) == 0) {
			// need record log
			throw new ServiceException(ReturnCode.USER_SESSIONID_INVALID);
		}
	}

	/**
	 * 用户鉴权
	 *
	 * @param transactionBean
	 * <br />
	 * @return <br />
	 */
	public int authenticationForJ(TransactionBean transactionBean) {
		return transactionMapper.checkTransaction(transactionBean);
	}

	/**
	 * 获取个人信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	@Override
	public PublicUserBean baseInfo(PublicUserBean publicUserBean) throws ServiceException {
		return publicUserMapper.getByID(publicUserBean);
	}

	/**
	 * 修改个人信息
	 *
	 * @param publicUserBean
	 * <br />
	 * @return <br />
	 */
	@Override
	public PublicUserBean updateBaseInfo(PublicUserBean publicUserBean) throws ServiceException {
		int result = publicUserMapper.updateUser(publicUserBean);
		if (result != 0) {
			return publicUserBean;
		}
		throw new ServiceException(ReturnCode.USER_USERID_INVALID);
	}
	
	//根据订单ID获取对应的创建者用户
	@Override
	public PublicUserBean getPublicUserByOrderID(String orderId)
	{
		return publicUserMapper.getPublicUserByOrderID(orderId);
	}

	/**
	 * 获取就诊卡信息
	 *
	 * @param xml
	 * <br />
	 * @return <br />
	 */
	/*
	 * private PatientidCheckBean parseXML(String xml) { try { Digester digester
	 * = new Digester(); digester.setValidating(false);
	 * digester.addObjectCreate("ROOT", PatientidCheckBean.class);
	 * digester.addSetProperties("ROOT/LEAF", "APPCODE", "appCode");
	 * digester.addSetProperties("ROOT/LEAF", "HOME_TEL", "msisdn");
	 * digester.addSetProperties("ROOT/LEAF", "OUTISVALID", "outIsValid");
	 * digester.addSetProperties("ROOT/LEAF", "NAME", "name");
	 * digester.addSetProperties("ROOT/LEAF", "SEX_CODE", "sexCode");
	 * digester.addSetProperties("ROOT/LEAF", "SEX_NAME", "sexName");
	 * digester.addSetProperties("ROOT/LEAF", "BIRTHDAY", "birthday");
	 * digester.addSetProperties("ROOT/LEAF", "IDENNO", "idEnNo");
	 * digester.addSetProperties("ROOT/LEAF", "HOME", "home");
	 * digester.addSetProperties("ROOT/LEAF", "PERSONNELTYPE", "personnelType");
	 * digester.addSetProperties("ROOT/LEAF", "PERSONNELTYPENAME",
	 * "personnelTypeName"); digester.addSetProperties("ROOT/LEAF",
	 * "SPECIALKINDDESC", "specialKindDesc");
	 * digester.addSetProperties("ROOT/LEAF", "SICARD_NO", "siCardNo");
	 * digester.addSetProperties("ROOT/LEAF", "PIINSID", "piInSid");
	 * digester.addSetProperties("ROOT/LEAF", "PSKIND", "pskInd"); return
	 * (PatientidCheckBean) digester.parse(new StringReader(xml)); } catch
	 * (Exception e) { log.error("XML{} Struct is error.", xml); log.error("{}",
	 * e); throw new SBHospitalException(ReturnCode.HIS_FAILED); } }
	 */

	/*
	 * //根据身份证查询健康卡等信息 private PublicUserBean
	 * getPatientInfoByIDCard(PublicUserBean publicUserBean) { PublicUserBean
	 * pBean = null; String IDNumber = publicUserBean.getUserID();
	 * 
	 * if(!StringUtils.isEmpty(IDNumber)) { HealthCardByIDCardBean
	 * healthCardByIDCardBean =
	 * HisReqStringUtil.getPatientInfoByIdentity(IDNumber);
	 * if(!org.springframework.util.StringUtils.isEmpty(healthCardByIDCardBean))
	 * { String phoneNum = healthCardByIDCardBean.getPhoneNumber();
	 * //如果用户注册填写的手机号跟his返回的预留手机号一致的话，绑定
	 * if(publicUserBean.getMsisdn().equals(phoneNum)) { pBean = new
	 * PublicUserBean();
	 * pBean.setPatientHealthCardID(healthCardByIDCardBean.getPatientHealthCardID
	 * ()); pBean.setPatientCardID(healthCardByIDCardBean.getPatientCardID());
	 * pBean.setPatientID(healthCardByIDCardBean.getPatientID());
	 * pBean.setPhoneNumber(phoneNum); } } } return pBean; }
	 */
}

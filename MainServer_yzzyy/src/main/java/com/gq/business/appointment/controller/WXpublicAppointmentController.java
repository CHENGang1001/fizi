package com.gq.business.appointment.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gq.base.BaseController;
import com.gq.business.accountmanage.mappers.PublicUserMapper;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.appointment.service.IAppointmentService;
import com.gq.business.payment.service.IPaymentService;
import com.gq.business.payment.service.WXIPaymentService;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.SmsClient;
import com.gq.config.ReturnCode;

@Controller
@RequestMapping(value = "/wxpuappointment")
public class WXpublicAppointmentController extends BaseController {
	// 日志测试文件
	private static Logger log = Logger.getLogger(WXpublicAppointmentController.class);
	@Autowired
	private IAppointmentService appointmentService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private WXIPaymentService wxPaymentService;
	@Autowired 
	private PublicUserMapper publicUserMapper;

	/**
	 * 
	 * 取消预约（未支付的）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/cancelRegister")
	@ResponseBody
	public ResponseEntity unaddSubscription(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		Map<String, Object> param = requestBody.getContent();
		log.info("-----取消预约接口cancelRegister------" + param.toString());
		ResponseEntity resEntity = appointmentService.unaddSubscription(param);
		if (resEntity != null) {
			if(resEntity.getHeader().getResultCode() == null){
				String billNo = param.get("billNo").toString();
				Map<String, Object> map = paymentService.queryDate(billNo);
				map.remove("cancelregister");
				map.put("cancelregister","5");
				paymentService.updateSubscriptionCancelregister(map);
				PublicUserBean nowBean = new PublicUserBean();
				nowBean.setUserID(param.get("userID").toString()); // 患者的userID
				nowBean = publicUserMapper.getByID(nowBean);
				// PublicUserBean loggedUser = (PublicUserBean)request.getSession().getAttribute("loginuser");
				if(nowBean != null){
					//获得用户ID
					String userId = nowBean .getUserID();
					//手机号码
					String msisdn = nowBean.getMsisdn();
					//用户姓名
					String userName = nowBean.getUserName();
					//医生姓名
					String doctorName =param.get("doctorName").toString();
					//String billNo = param.get("billNo").toString();
					//获取就诊日期
					String bespeakDate = param.get("bespeakDate").toString();
					//就诊人卡号
					String cardNo = param.get("cardNo").toString();
					//就诊人姓名
					String patientName = paymentService.queryPatientname(cardNo,userId);
					SmsClient.sendUnregisterPaySms(msisdn,bespeakDate,userName, doctorName,patientName);
				}
			}
		}
		return resEntity;
	}

	/**
	 * 
	 * 取消预约（已支付的）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/unregisterPay")
	@ResponseBody
	public ResponseEntity unregisterPay(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		ResponseEntity res = null;
		boolean isOk = false;
		String payNumber = null;
		int flag = 0;
		Map<String, Object> param = requestBody.getContent();
		log.info("公众号----取消预约接口unregisterPay------" + param.toString());
		try {
			if (null != requestBody) {
				if (null != param && !param.isEmpty()) {
					// 根据单据号去数据库查询订单号
					payNumber = getPayNumber(param);
					param.put("payNumber", payNumber);
					param.remove("pay_for");
					String payType = param.get("payType").toString();
					
					//HIS退号退费
					log.info("订单号："+payNumber+" 公众号---开始通知HIS执行退号退费操作：——————>");
					res = new ResponseEntity();
					int i=0;
					for (i = 0; i < 3; i++) {
						//判断是否通知过HIS，若通知过HIS，第三方未成功退费那也跳出循环，不在重复通知HIS
						if(flag == 1){
							break;
						}
						log.info("订单号："+payNumber+" 公众号---第"+i+"次通知医院his退号退费=====》");
						// 通知his退号退费
						res = appointmentService.unregisterPay(param);
						log.info("订单号："+payNumber+" 公众号----取消预约接口unregisterPay------通知his退号退费" + param.toString());
						log.info("订单号："+payNumber+" 公众号----取消预约接口unregisterPa-------返回参数是：" + res.getContent().toString());
						log.info("订单号："+payNumber+" 公众号----取消预约接口unregisterPa-------HIS接口返回码是：" + res.getHeader().getResultCode());
						log.info("订单号："+payNumber+" 公众号----取消预约接口unregisterPa-------HIS接口返回码是：" + res.getHeader().getResultMsg());
						log.info("订单号："+payNumber+" 公众号---his支付挂号退费ResultCode==》" + res.getHeader().getResultCode()+"HisResultMsg==》"+res.getHeader().getResultCode()); 
						ResponseHeader header = res.getHeader();
						if (header.getResultCode().equals("1") && header.getResultCode() == null) {
							//已成功通知HIS，且HIS已成功退号
							log.info("订单号："+payNumber+" 已成功通知HIS，且HIS已成功退号，将FLAG设为1。");
							flag = 1;
							//通知银行退款
							// 根据订单号查询支付金额
							String amount = getPayAmountByOrderId(payNumber);
							log.info("订单号："+payNumber+" 根据订单号查询支付金额=====》"+amount);
							param.put("amount", amount);
							// 第三方退款(判断payType-2-app退号|payType-4-微信公众号退款)
                            if("4".equals(payType)){
                            	log.info("订单号："+payNumber+" 公众号退公众号预约的号");
                            	isOk = wxPaymentService.wxPublicRefundOrder(param);
    							log.info("订单号："+payNumber+" 公众号退公众号预约号---第三方退款返回信息=====》" + isOk);
								
							}else{
								if(payNumber.contains("p")){
									log.info("订单号："+payNumber+" 公众号退公众号预约的号");
	                            	isOk = wxPaymentService.wxPublicRefundOrder(param);
	    							log.info("订单号："+payNumber+" 公众号退公众号预约号---第三方退款返回信息=====》" + isOk);
								}else{
									log.info("订单号："+payNumber+" 公众号退app预约的号");
									isOk = paymentService.refundOrder(param);
									log.info("订单号："+payNumber+"公众号退app预约的号 第三方退款返回信息=====》" + isOk);
								}
							}
														
							if(isOk){
								//更新本地订单
								paymentService.updateCancelStatus(payNumber, "2");
								log.info("订单号："+payNumber+" 公众号---更新本地数据订单表退费状态=====》为2");
								returnCode = ReturnCode.OK;
								break;
							}else {
								log.info("订单号："+payNumber+" 公众号---第三方退款返回失败信息=====》" + isOk);
								returnCode = ReturnCode.REFUND_FAIL;
								
							}
							// 退号失败 暂停1S后继续通知医院退号
							Thread.sleep(1000);
						}
					}
					log.info("订单号："+payNumber+" 公众号——————>通知HIS执行退号退费操作结束！共执行了："+i+"次");
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			if (isOk) {
				// 第三方退费成功，返回医院通知结果
				header = res.getHeader();
				//退款成功后，发送短信通知
				PublicUserBean nowBean = new PublicUserBean();
				nowBean.setUserID(param.get("userID").toString()); // 患者的userID
				nowBean = publicUserMapper.getByID(nowBean);
				if(nowBean != null){
					//获得用户ID
					String userId = nowBean.getUserID();
					//手机号码
					String msisdn = nowBean.getMsisdn();
					//用户姓名
					String userName = nowBean.getUserName();
					//医生姓名
					String doctorName =param.get("doctorName").toString();
					String billNo = param.get("billNo").toString();
					//获取就诊日期
					Map<String, Object> map = paymentService.queryDate(billNo);
					String date = map.get("bespeakDate").toString();
					String bespeakDate = date.substring(0, 10);
					//就诊人卡号
					String cardNo = param.get("cardNo").toString();
					//就诊人姓名
					String patientName = paymentService.queryPatientname(cardNo,userId);
					SmsClient.sendUnregisterPaySms(msisdn, bespeakDate,userName, doctorName,patientName);
					log.info("公众号---第三方退款成功后，发送短信通知！");
				}
			} else {
				// 第三方退费失败，返回退费失败信息
				header.setResultCode(returnCode.getCode());
				header.setResultMsg(returnCode.getMsg());
			}
			responseEntity.setHeader(header);
			Map<String, String> map = new HashMap<String, String>();
			responseEntity.setContent(map);
		}
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "unregisterPay", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 根据支付用途和单据号查询支付流水号
	 * 
	 * @param param
	 * @return 查询到的流水号
	 */
	private String getPayNumber(Map<String, Object> param) {
		String ret = "";

		// 用途：1：预约支付、2：当日挂号支付、3：预约挂号支付、4：门诊缴费、5：住院预交
		String payFor = param.get("pay_for").toString();
		// 单据号
		String billNo = param.get("billNo").toString();
		if (payFor.equals("2")) {
			// 当日挂号支付
			ret = paymentService.getPayNumberInRegisterPay(billNo);
		} else if (payFor.equals("3")) {
			// 预约挂号支付
			ret = paymentService.getPayNumberInSubscriptionPay(billNo);
		}
		return ret;
	}

	/**
	 * 根据订单号查询订单金额
	 * 
	 * @param orderId
	 * @return
	 */
	private String getPayAmountByOrderId(String orderId) {
		return paymentService.getPayAmountByOrderId(orderId);
	}
}

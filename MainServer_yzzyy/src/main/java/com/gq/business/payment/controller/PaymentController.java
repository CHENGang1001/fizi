package com.gq.business.payment.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abc.pay.client.TrxException;
import com.abc.pay.client.ebus.PaymentResult;
import com.gq.base.BaseController;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.service.IPublicUserService;
import com.gq.business.appointment.service.IAppointmentService;
import com.gq.business.appointment.service.impl.AppointmentServiceImpl;
import com.gq.business.payment.model.LogBean;
import com.gq.business.payment.model.TradingRecordBean;
import com.gq.business.payment.service.IPaymentService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.request.RequestEntityEx;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.AdapterUtils;
import com.gq.common.utils.HttpUtils;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.SmsClient;
import com.gq.common.utils.payment.CCB.CCBPayment;
import com.gq.common.utils.payment.wx.WxConstants;
import com.gq.common.utils.payment.wx.WxPayment;
import com.gq.common.utils.payment.wx.XmlUtils;
import com.gq.config.ReturnCode;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController {
	// 日志测试文件
	private static Logger log = Logger.getLogger(PaymentController.class);
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IAppointmentService appointmentService;
	@Autowired
	private IPublicUserService userInterfaceService;

	/**
	 * 建行支付支付url
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getCcbUrl")
	@ResponseBody
	public ResponseEntity getCcbUrl(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		String result = null;
		LogBean logbean =new LogBean();
		logbean.setInto("建行支付");
		logbean.setPayType("2");
		logbean.setStartTime(startTime);
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				
				if (null != param && !param.isEmpty()) {
					// 生成订单
					TradingRecordBean trade = getOrder(param);
					
					// 支付订单号
					String orderId = CCBPayment.getOrderID();
					
					trade.setOrderId(orderId);
					param.put("orderId", orderId);
					String gateWay = param.get("gateway").toString();
					param.remove("gateway");
					// 获取登录者用户名
					//PublicUserBean user = (PublicUserBean) request.getSession().getAttribute("loginuser");
					trade.setCreaterId(param.get("userId").toString());
					result = paymentService.createOrder(trade, gateWay);
					logbean.setAmount(trade.getAmount());
					logbean.setOrderId(orderId);
					logbean.setRemark(trade.getPayFor());
					// 生成业务逻辑订单
					paymentService.createHisOrder(param);
				}
			}
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			if (StringUtils.isNullOrEmpty(result)) {
				header.setResultCode(ReturnCode.SYSTEM_ERROR.getCode());
				header.setResultMsg(ReturnCode.SYSTEM_ERROR.getMsg());
				logbean.setResultCode(ReturnCode.SYSTEM_ERROR.getCode());
				logbean.setResultMsg(ReturnCode.SYSTEM_ERROR.getCode());
			} else {
				header.setResultCode(ReturnCode.OK.getCode());
				header.setResultMsg(ReturnCode.OK.getMsg());
				logbean.setResultCode(ReturnCode.OK.getCode());
				logbean.setResultMsg(ReturnCode.OK.getMsg());
			}
			paymentService.insertlog(logbean);
			responseEntity.setHeader(header);
			Map<String, String> map = new HashMap<String, String>();
			map.put("ccbUrl", result);
			responseEntity.setContent(map);
			log.info("建行支付url=======>" + map);
		}
		
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getCcbUrl", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		
		paymentService.insertlog(logbean);
		return responseEntity;
	}

	/**
	 * 建行支付异步回调
	 * 
	 * @param requestBody
	 * @return
	 */
	@SuppressWarnings({ "null", "unchecked" })
	@RequestMapping(value = "/ccbNotify")
	public void ccbNotify(HttpServletRequest request, HttpServletResponse response) {
		log.info("进入建行回调======》");
		LogBean logbean = new LogBean();
		logbean.setInto("建行回调");
		// String result = null;
		// 0、设定商户结果显示页面
		// String isSuccess = "true";
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		System.out.println("request---->" + request.getParameter("SUCCESS"));
		logbean.setOrderId(request.getParameter("ORDERID"));
		try {
			if (null != request) {
				// String path = request.getContextPath();
				// String basePath =
				// request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
				// String posId = request.getParameter("POSTID");//商户柜台代码
				// String branchId = request.getParameter("BRANCHID");//分行代码
				String orderId = request.getParameter("ORDERID");// 订单号
				System.out.println("订单号orderId---->" + orderId);
				String payMent = request.getParameter("PAYMENT");// 支付金额
				System.out.println("支付金额payMent---->" + payMent);
				// String curCode = request.getParameter("CURCODE");//币种
				String accType = request.getParameter("ACC_TYPE");// 账户类型
				System.out.println("账户类型accType---->" + accType);
				String success = request.getParameter("SUCCESS");// 成功标志
				System.out.println("成功标志success---->" + success);
				String type = request.getParameter("TYPE");// 接口类型
				System.out.println("接口类型type---->" + type);
				// String referer = request.getParameter("REFERER");
				// String clientIp = request.getParameter("CLIENTIP");
				// String accDate = request.getParameter("ACCDATE");//系统记账日期
				//
				// String sign = request.getParameter("SIGN");//数字签名
				if (success == "Y" || success.equals("Y")) {
					// 处理多次回调多次调用业务代码问题
					List<TradingRecordBean> list = new ArrayList<TradingRecordBean>();
					list = paymentService.getOrderList(orderId);
					log.info("订单信息===》" + list);
					// 如果未支付并且未退费，将该订单状态设置为已支付，若调用HIS失败，则再回置为已退费
					if ("1".equals(list.get(0).getStatus()) && "1".equals(list.get(0).getCancelregister())) {
						// 将订单状态改为已支付
						paymentService.ccbPaySuccess(orderId);

						// 循环调用3次，调用HIS将该业务设置为已支付，并且返回值保存在业务表
						boolean isSuccess = false;
						for (int i = 0; i < 3; i++) {
							isSuccess = callHisPaySuccess(orderId);
							if (isSuccess)
								break;
							Thread.sleep(1 * 1000);
						}
						if(isSuccess){
							logbean.setCallHisPay("1");
						}else{
							logbean.setCallHisPay("0");
						}
						log.info("通知his====》" + isSuccess);
						// 根据订单号获取对应的操作者信息
						PublicUserBean userInfo = userInterfaceService.getPublicUserByOrderID(orderId);
						Map<String, Object> orderDetail = null;
						// 如果调用成功，发送短信
						if (isSuccess) {
							// his处理成功
							returnCode = ReturnCode.OK;
							// 获得支付用途
							String payFor = paymentService.selectPayFor(orderId);
							String payfor = "";

							if (userInfo != null) {
								//用户Id
								String userId = userInfo.getUserID();
								// 用户手机号
								String msisdn = userInfo.getMsisdn();
								// 用户姓名
								String userRealName = userInfo.getUserName();

								if ("3".equals(payFor)) {
									// 根据订单号获取对应订单详情
									orderDetail = paymentService.query(orderId);
									String date = orderDetail.get("bespeakDate").toString();
									String beginTime = orderDetail.get("beginTime").toString();
									String bespeakDate = date.substring(0, 10) + " " + beginTime;
									String time = date.substring(0, 10);
									// 医生姓名
									String doctorName = orderDetail.get("doctorName").toString();
									// 号别
									String billNo = orderDetail.get("billNo").toString();
									// 序号
									String orderNumber = orderDetail.get("orderNumber").toString();
									//就诊人卡号
									String cardNo = orderDetail.get("cardNo").toString();
									//就诊人姓名
									String patientName = paymentService.queryPatientname(cardNo,userId);
									
									//获取门诊号
									ResponseEntity responseEntity = new ResponseEntity();
									//调用his接口
									Map<String, Object> param1 = new HashMap<>();
									param1.put("cardType",orderDetail.get("cardType"));
									param1.put("cardNo",orderDetail.get("cardNo"));
									param1.put("password","");
									param1.put("iDCard",orderDetail.get("idCard"));
									param1.put("name",orderDetail.get("name"));
									param1.put("sex", "");
									responseEntity = appointmentService.checkPatientCard(param1);
									log.info("订单号："+orderNumber+"---"+responseEntity.getContent().toString());
									Object content = null;
									content = responseEntity.getContent();
									Map<String, String> result = null; 
									result = (Map<String, String>) content;
									log.info("短信通知result:"+result);
									String outPatient = "";
									if(result.containsKey("outPatient")){
										outPatient = result.get("outPatient").toString();
										log.info("订单号："+orderNumber+"门诊号："+" :"+outPatient);
									}													
									SmsClient.sendAppointmentSms(msisdn, userRealName, doctorName, orderNumber, outPatient,
											bespeakDate,time,patientName);
								} else if ("2".equals(payFor)) {
									// 根据订单号获取对应当日挂号订单详情
									orderDetail = paymentService.queryTodayList(orderId);
									String bespeakDate = orderDetail.get("beginTime").toString();
									// 医生姓名
									String doctorName = orderDetail.get("doctorName").toString();
									// 号别
									String billNo = orderDetail.get("billNo").toString();
									// 序号
									String orderNumber = orderDetail.get("orderNumber").toString();
									//就诊人卡号
									String cardNo = orderDetail.get("cardNo").toString();
									//就诊人姓名
									String patientName = paymentService.queryPatientname(cardNo,userId);
									
									//获取门诊号
									ResponseEntity responseEntity = new ResponseEntity();
									//调用his接口
									Map<String, Object> param1 = new HashMap<>();
									param1.put("cardType",orderDetail.get("cardType"));
									param1.put("cardNo",orderDetail.get("cardNo"));
									param1.put("password","");
									param1.put("iDCard",orderDetail.get("idCard"));
									param1.put("name",orderDetail.get("name"));
									param1.put("sex", "");
									responseEntity = appointmentService.checkPatientCard(param1);
									log.info("订单号："+orderNumber+"---"+responseEntity.getContent().toString());
									Object content = null;
									content = responseEntity.getContent();
									Map<String, String> result = null; 
									result = (Map<String, String>) content;
									log.info("短信通知result:"+result);
									String outPatient = "";
									if(result.containsKey("outPatient")){
										outPatient = result.get("outPatient").toString();
										log.info("订单号："+orderNumber+"门诊号："+" :"+outPatient);
									}
									SmsClient.sendTodayAppointmentSms(msisdn, userRealName, doctorName, orderNumber,
											outPatient, bespeakDate,patientName);
								} else if ("4".equals(payFor)) {
									payfor = "门诊";
									Map<String, Object> map = paymentService.getToPay(orderId);
									String billCost = map.get("billCost").toString();
									//取药窗口号
									String drugWinNo = "";
									String WinNo = map.get("drugWinNo").toString();
									String NO = WinNo.substring(0, WinNo.length() - 1);
									if (":".equals(NO.substring(NO.length()-1, NO.length()))) {
										NO = NO.substring(0, NO.length()-1);
									}
									int q = NO.indexOf("|");
									if (q >= 0) {
										drugWinNo = NO.replace("|", "，");
									} else {
										drugWinNo = NO;
									}
									//单据号
									String billNo = map.get("billNo").toString();
									String outPatient = "";
									//获取门诊号
									Map<String, Object> map2 = new HashMap<>();
									map2.put("billNo", billNo);
									ResponseEntity responseEntity = new ResponseEntity();
									responseEntity = appointmentService.getOutPatient(map2);
									log.info("his返回："+responseEntity.toString());
									if("1".equals(responseEntity.getHeader().getResultCode())){
										log.info("短信通知--单据号："+billNo+"返回结果："+"---"+responseEntity.getContent().toString());
										List<Map<String, String>> result = (List<Map<String, String>>) responseEntity.getContent();
										if(result.get(0).containsKey("outPatient")){
											outPatient = result.get(0).get("outPatient").toString();
											log.info("短信通知--单据号："+billNo+" 门诊号："+" :"+outPatient);
										}	
									}
									SmsClient.sendToSms(msisdn, userRealName, payfor, billCost,drugWinNo,outPatient);
								} else if ("5".equals(payFor)) {
									payfor = "住院预交";
									Map<String, Object> map = paymentService.queryOrder(orderId);
									String billCost = map.get("amount").toString();
									SmsClient.sendToPaySms(msisdn, userRealName, payfor, billCost);
								}
							}
						}
						// his处理失败，调用退费接口，退还支付金额
						else {
							Map<String, Object> refundParam = new HashMap<String, Object>();
							// 订单号
							refundParam.put("payNumber", orderId);
							// 订单金额
							refundParam.put("amount", payMent);
							// 调用退费接口
							boolean isOK = paymentService.refundOrder(refundParam);
							if(isOK){
								logbean.setRefundhisResultMsg("成功");
							}else{
								logbean.setRefundhisResultMsg("失败");
							}
							// 更改订单状态为已退费
							if (isOK) {
								Map<String, Object> order = paymentService.queryOrder(orderId);
								if (userInfo != null) {
									// 用户手机号
									String msisdn = userInfo.getMsisdn();
									// 用户姓名
									String userName = userInfo.getUserName();
									// 获得支付用途
									String payFor = "";
									String payfor = order.get("pay_for").toString();
									if ("3".equals(payfor)) {
										order.remove("pay_for");
										payFor = "预约挂号";
									} else if ("4".equals(payfor)) {
										order.remove("pay_for");
										payFor = "门诊";
									} else if ("5".equals(payfor)) {
										order.remove("pay_for");
										payFor = "住院预交";
									}
									SmsClient.sendRefundSms(msisdn, userName, payFor);
									// 冲正成功
									paymentService.updateCancelStatus(orderId, "3");
								}
							} else {
								// 冲正失败
								paymentService.updateCancelStatus(orderId, "4");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("建行支付异常信息=======》" + e.getMessage());
			e.printStackTrace();
		} finally {
			// 给银行返回状态
			logbean.setResultCode(returnCode.getCode());
			logbean.setResultMsg(returnCode.getMsg());
			paymentService.updatelog(logbean);
		}
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "ccbNotify", request.toString(), null,
				System.currentTimeMillis() - startTime);
		/*
		 * 
		 * ModelAndView mav = new ModelAndView("/return_url_show");
		 * mav.addObject("isSuccess", isSuccess); return mav;
		 */
	}

	/**
	 * 获取待支付列表
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getPaymentList")
	@ResponseBody
	public ResponseEntity getPaymentList(@RequestBody RequestEntity requestBody) {
		long startTime = System.currentTimeMillis();
		Map<String, Object> param = requestBody.getContent();
		ResponseEntity responseEntity = paymentService.getPaymentList(param);
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "GetPaymentList", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 获取待支付详情
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getPaymentDetail")
	@ResponseBody
	public ResponseEntity getPaymentDetail(@RequestBody RequestEntity requestBody) {
		long startTime = System.currentTimeMillis();
		Map<String, Object> param = requestBody.getContent();
		ResponseEntity responseEntity = paymentService.getPaymentDetail(param);
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "GetPaymentDetail", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 支付订单,未完成
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/pay")
	public @ResponseBody ResponseEntity pay(@RequestBody RequestEntityEx<Map<String, Object>> requestBody) {
		long startTime = System.currentTimeMillis();
		Map<String, Object> param = requestBody.getContent();
		ResponseEntity responseEntity = paymentService.pay(param);
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "Pay", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 获取支付记录列表
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getPaymentRecordList")
	@ResponseBody
	public ResponseEntity getPaymentRecordList(@RequestBody RequestEntity requestBody) {
		long startTime = System.currentTimeMillis();
		Map<String, Object> param = requestBody.getContent();
		ResponseEntity responseEntity = paymentService.getPaymentList(param);
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "GetPaymentRecordList", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 获取支付记录详情
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getPaymentRecordDetail")
	@ResponseBody
	public ResponseEntity getPaymentRecordDetail(@RequestBody RequestEntity requestBody) {
		long startTime = System.currentTimeMillis();
		Map<String, Object> param = requestBody.getContent();
		ResponseEntity responseEntity = paymentService.getPaymentDetail(param);
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "GetPaymentRecordDetail", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 支付订单（手机网页支付）
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/payWithWeb")
	@ResponseBody
	public ResponseEntity payWithWeb(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		String result = null;
		try {
			Map<String, Object> param = requestBody.getContent();
			// 校验参数
			this.paramVerify(param, "PayWithWeb");
			//
			result = paymentService.payWithWeb(param);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			if (StringUtils.isNullOrEmpty(result)) {
				ResponseHeader header = new ResponseHeader();
				header.setResultCode(ReturnCode.SYSTEM_ERROR.getCode());
				header.setResultMsg(ReturnCode.SYSTEM_ERROR.getMsg());
				responseEntity.setHeader(header);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("paymentLink", result);
			responseEntity.setContent(map);
		}
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "PayWithWeb", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	@RequestMapping(value = "/merchantResult")
	@ResponseBody
	public void merchantResult(HttpServletRequest request, HttpServletResponse response) {
		// 1、取得MSG参数，并利用此参数值生成支付结果对象
		String msg = request.getParameter("MSG");
		PaymentResult tResult = null;
		try {
			tResult = new PaymentResult(msg);
		} catch (TrxException e) {
			e.printStackTrace();
		}
		// 2、判断支付结果状态，进行后续操作
		if (tResult.isSuccess()) {
			// 3、支付成功并且验签、解析成功
			System.out.println("TrxType         = [" + tResult.getValue("TrxType") + "]<br>");
			System.out.println("OrderNo         = [" + tResult.getValue("OrderNo") + "]<br>");
			System.out.println("Amount          = [" + tResult.getValue("Amount") + "]<br>");
			System.out.println("BatchNo         = [" + tResult.getValue("BatchNo") + "]<br>");
			System.out.println("VoucherNo       = [" + tResult.getValue("VoucherNo") + "]<br>");
			System.out.println("HostDate        = [" + tResult.getValue("HostDate") + "]<br>");
			System.out.println("HostTime        = [" + tResult.getValue("HostTime") + "]<br>");
			System.out.println("MerchantRemarks = [" + tResult.getValue("MerchantRemarks") + "]<br>");
			System.out.println("PayType         = [" + tResult.getValue("PayType") + "]<br>");
			System.out.println("NotifyType      = [" + tResult.getValue("NotifyType") + "]<br>");
			System.out.println("TrnxNo          = [" + tResult.getValue("iRspRef") + "]<br>");
		} else {
			// 4、支付成功但是由于验签或者解析报文等操作失败
			System.out.println("ReturnCode   = [" + tResult.getReturnCode() + "]<br>");
			System.out.println("ErrorMessage = [" + tResult.getErrorMessage() + "]<br>");
		}
	}

	/**
	 * 微信支付
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/wxPay")
	@ResponseBody
	public ResponseEntity wxPay(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		LogBean logbean = new LogBean();
		
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		logbean.setInto("微信支付");
		logbean.setPayType("1");
		logbean.setStartTime(startTime);
		ResponseEntity responseEntity = null;
		Map<String, String> returnParam = new HashMap<String, String>();
		boolean isOK = false;
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				if (null != param && !param.isEmpty()) {
					// 生成订单
					TradingRecordBean bean = getOrder(param);
					// 支付订单号
					String orderId = WxPayment.getOrderID();
					log.info("订单号："+orderId);
					bean.setOrderId(orderId);
					param.put("orderId", orderId);
					// 获取登录者用户名
					//PublicUserBean user = (PublicUserBean) request.getSession().getAttribute("loginuser");
					bean.setCreaterId(param.get("userId").toString());
					paymentService.createOrder(bean, "");
					// 生成业务逻辑订单
					paymentService.createHisOrder(param);
					log.info("订单号："+orderId+" 已生成业务逻辑订单--参数-"+param.toString());
					Map<String, String> wxParam = new HashMap<String, String>();
					// 配置微信支付验签所需参数
					wxParam = WxPayment.getWxParam(param);
					// 生成签名
					String sign = WxPayment.createWeiSign(wxParam,1);
					wxParam.put("sign", sign);
					// 将map类型数据转换为xml格式
					String entity = WxPayment.toXml(wxParam);
					// 向微信发送请求
					String content = HttpUtils.sendPost(WxConstants.API_URL + WxConstants.UNIFIEDORDER_URL, entity);
					// 将返回的xml结果转换为map格式
					wxParam = XmlUtils.readStringXmlOut(content);
					log.info("订单号："+orderId+"向微信发送请求返回结果return_code："+wxParam.get("return_code"));
					log.info("订单号："+orderId+"向微信发送请求返回结果result_code："+wxParam.get("result_code"));
					// return_code和result_code都为SUCCESS的时候返回prepay_id
					logbean.setAmount(param.get("amount").toString());
					logbean.setRemark(param.get("payFor").toString());
					logbean.setOrderId(orderId);
					if (WxConstants.SUCCESS.equals(wxParam.get("return_code"))
							&& WxConstants.SUCCESS.equals(wxParam.get("result_code"))) {		
						returnParam.put("appid", WxConstants.APPID);
						returnParam.put("partnerid", WxConstants.MCH_ID);
						returnParam.put("prepayid", wxParam.get("prepay_id"));
						returnParam.put("package", WxConstants.PACKAGE);
						returnParam.put("noncestr", wxParam.get("nonce_str"));
						returnParam.put("timestamp", WxPayment.getTimeStamp());
						sign = WxPayment.createWeiSign(returnParam,1);
						returnParam.put("sign", sign);
						returnParam.put("orderId", orderId);
						returnCode = ReturnCode.OK;
						isOK = true;
						log.info("订单号："+orderId+" isOk:"+isOK);
					} else {
						// 获取prepay_id(预支付交易会话标识)失败
						returnCode = ReturnCode.PREPAY_ID_FAIL;
						isOK = false;
						log.info("订单号："+orderId+" isOk:"+isOK);
					}
				}
			}

		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			logbean.setResultCode(returnCode.getCode());
			logbean.setResultMsg(returnCode.getMsg());
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			if (isOK) {
				responseEntity.setContent(returnParam);
			} else {
				responseEntity.setContent("");
			}
		}
		paymentService.insertlog(logbean);
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "PayWithWeb", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 微信支付异步通知
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@SuppressWarnings({ "null", "unchecked" })
	@RequestMapping(value = "/wxNotify")
	public void weiNotify(HttpServletRequest req, HttpServletResponse resp) {
		log.info("进入微信回调=======》");
		LogBean logbean = new LogBean();
		logbean.setInto("微信支付回调");
		try {
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setHeader("Content-type", "text/html;charset=UTF-8");
			String resString = XmlUtils.parseRequst(req);
			System.out.println("通知内容：" + resString);
			log.info("微信回调通知内容：" + resString);
			String respString = "fail";
			if (resString != null && !"".equals(resString)) {
				Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
				logbean.setOrderId(map.get("out_trade_no"));
				String return_code = map.get("return_code");
				if (return_code != null && WxConstants.SUCCESS.equals(return_code)) {
					String result_code = map.get("result_code");
					String appid = map.get("appid");
					String mch_id = map.get("mch_id");
					if (WxConstants.SUCCESS.equals(result_code) && WxConstants.APPID.equals(appid)
							&& WxConstants.MCH_ID.equals(mch_id)) {
						// 此处可以在添加相关处理业务，校验通知参数中的商户订单号out_trade_no和金额total_fee是否和商户业务系统的单号和金额是否一致，一致后方可更新数据库表中的记录。

						// 更新支付状态
						String out_trade_no = map.get("out_trade_no");
						String trade_no = map.get("transaction_id");
						String amount = map.get("total_fee");
						log.info("订单号："+out_trade_no+"支付成功，进入回调");
						// 处理多次回调多次调用业务代码问题
						List<TradingRecordBean> list = new ArrayList<TradingRecordBean>();
						list = paymentService.getOrderList(out_trade_no);
						log.info("订单信息" + list);
						if ("1".equals(list.get(0).getStatus()) && "1".equals(list.get(0).getCancelregister())) {
							// 只处理未支付且未退费的数据
							TradingRecordBean tradeOrder = new TradingRecordBean();
							tradeOrder.setOrderId(out_trade_no);
							tradeOrder.setTransactionId(trade_no);
							paymentService.wxPaySuccess(tradeOrder);
							// 循环调用3次，调用HIS将该业务设置为已支付，并且返回值保存在业务表
							boolean isSuccess = false;
							for (int i = 0; i < 3; i++) {
								isSuccess = callHisPaySuccess(out_trade_no);
							if (isSuccess)
									break;
								Thread.sleep(1 * 1000);
							}
							if(isSuccess){
								logbean.setCallHisPay("1");
							}else{
								logbean.setCallHisPay("0");
							}
							log.info("通知his" + isSuccess);
							// 根据订单号获取对应的操作者信息
							PublicUserBean userInfo = userInterfaceService.getPublicUserByOrderID(out_trade_no);
							log.info("订单号:"+out_trade_no+" 操作员信息："+userInfo.toString());
							Map<String, Object> orderDetail = null;
							if (isSuccess) {
								log.info("订单号:"+out_trade_no+"通知his成功,发送短信");
								String payFor = paymentService.selectPayFor(out_trade_no);
								respString = "success";
								log.info("订单号:"+out_trade_no+" 查询业务类型："+payFor);

								if (userInfo != null) {
									log.info("订单号:"+out_trade_no+" userInfo != null");
									//用户ID
									String userId = userInfo.getUserID();
									// 用户手机号
									String msisdn = userInfo.getMsisdn();
									// 用户姓名
									String userRealName = userInfo.getUserName();

									String payfor = "";
									if ("3".equals(payFor)) {
										// 根据订单号获取对应预约挂号订单详情
										orderDetail = paymentService.query(out_trade_no);
										// 就诊日期
										String date = orderDetail.get("bespeakDate").toString();
										String beginTime = orderDetail.get("beginTime").toString();
										String bespeakDate = date.substring(0, 10) + " " + beginTime;
										String time = date.substring(0, 10);
										// 医生姓名
										String doctorName = orderDetail.get("doctorName").toString();
										// 序号
										String orderNumber = orderDetail.get("orderNumber").toString();
										// 号别
										String billNo = orderDetail.get("billNo").toString();
										//就诊人卡号
										String cardNo = orderDetail.get("cardNo").toString();
										//就诊人姓名
										String patientName = paymentService.queryPatientname(cardNo,userId);
										
										
										//获取门诊号
										ResponseEntity responseEntity = new ResponseEntity();
										//调用his接口
										Map<String, Object> param1 = new HashMap<>();
										param1.put("cardType",orderDetail.get("cardType"));
										param1.put("cardNo",orderDetail.get("cardNo"));
										param1.put("password","");
										param1.put("iDCard",orderDetail.get("idCard"));
										param1.put("name",orderDetail.get("name"));
										param1.put("sex", "");
										responseEntity = appointmentService.checkPatientCard(param1);
										log.info("订单号："+out_trade_no+"---"+responseEntity.getContent().toString());
										Object content = null;
										content = responseEntity.getContent();
										Map<String, String> result = null; 
										result = (Map<String, String>) content;
										log.info("短信通知result:"+result);
										String outPatient = "";
										if(result.containsKey("outPatient")){
											outPatient = result.get("outPatient").toString();
											log.info("订单号："+out_trade_no+"门诊号："+" :"+outPatient);
										}
										
										SmsClient.sendAppointmentSms(msisdn, userRealName, doctorName, orderNumber,
												outPatient, bespeakDate,time,patientName);
										
									} else if ("2".equals(payFor)) {
										// 根据订单号获取对应当日挂号订单详情
										orderDetail = paymentService.queryTodayList(out_trade_no);
										String bespeakDate = orderDetail.get("beginTime").toString();
										// 医生姓名
										String doctorName = orderDetail.get("doctorName").toString();
										// 号别
										String billNo = orderDetail.get("billNo").toString();
										// 序号
										String orderNumber = orderDetail.get("orderNumber").toString();
										//就诊人卡号
										String cardNo = orderDetail.get("cardNo").toString();
										//就诊人姓名
										String patientName = paymentService.queryPatientname(cardNo,userId);
										
										
										//获取门诊号
										ResponseEntity responseEntity = new ResponseEntity();
										//调用his接口
										Map<String, Object> param1 = new HashMap<>();
										param1.put("cardType",orderDetail.get("cardType"));
										param1.put("cardNo",orderDetail.get("cardNo"));
										param1.put("password","");
										param1.put("iDCard",orderDetail.get("idCard"));
										param1.put("name",orderDetail.get("name"));
										param1.put("sex", "");
										responseEntity = appointmentService.checkPatientCard(param1);
										logbean.setResultCode(responseEntity.getHeader().getResultCode());
										logbean.setResultMsg(responseEntity.getHeader().getResultMsg());
										log.info("订单号："+out_trade_no+"---"+responseEntity.getContent().toString());
										Object content = null;
										content = responseEntity.getContent();
										Map<String, String> result = null; 
										result = (Map<String, String>) content;
										log.info("短信通知result:"+result);
										String outPatient = "";
										if(result.containsKey("outPatient")){
											outPatient = result.get("outPatient").toString();
											log.info("订单号："+out_trade_no+"门诊号："+" :"+outPatient);
										}
										
										SmsClient.sendTodayAppointmentSms(msisdn, userRealName, doctorName,
												orderNumber, outPatient, bespeakDate,patientName);
									} else if ("4".equals(payFor)) {
										log.info("订单号:"+out_trade_no+" 进入门诊处理");
										payfor = "门诊";
										Map<String, Object> param = paymentService.getToPay(out_trade_no);
										log.info("订单号:"+out_trade_no+"进入门诊--"+param.toString());
										String billCost = param.get("billCost").toString();
										//取药窗口号
										String drugWinNo = "";
										String WinNo = param.get("drugWinNo").toString();
										String NO = WinNo.substring(0, WinNo.length() - 1);
										if (":".equals(NO.substring(NO.length()-1, NO.length()))) {
											NO = NO.substring(0, NO.length()-1);
										}
										int q = NO.indexOf("|");
										if (q >= 0) {
											drugWinNo = NO.replace("|", "，");
										} else {
											drugWinNo = NO;
										}
										//单据号
										String billNo = param.get("billNo").toString();
										String outPatient = "";
										log.info("订单号:"+out_trade_no+" 单据号billNo:"+billNo);
										//获取门诊号
										Map<String, Object> map2 = new HashMap<>();
										map2.put("billNo", billNo);
										ResponseEntity responseEntity = new ResponseEntity();
										responseEntity = appointmentService.getOutPatient(map2);
										log.info("门诊号："+responseEntity.toString());	
										if("1".equals(responseEntity.getHeader().getResultCode())){
											log.info("短信通知--单据号："+billNo+"返回结果："+"---"+responseEntity.getContent().toString());
											List<Map<String, String>> result = (List<Map<String, String>>) responseEntity.getContent();
											if(result.get(0).containsKey("outPatient")){
											outPatient = result.get(0).get("outPatient").toString();
											log.info("短信通知--单据号："+billNo+" 门诊号："+" :"+outPatient);
												}					
										}																
										SmsClient.sendToSms(msisdn, userRealName, payfor, billCost,drugWinNo,outPatient);
									} else if ("5".equals(payFor)) {
										payfor = "住院预交";
										Map<String, Object> param = paymentService.queryOrder(out_trade_no);
										String billCost = param.get("amount").toString();
										SmsClient.sendToPaySms(msisdn, userRealName, payfor, billCost);
									}
								}
							} else {
								// his处理失败，调用退费接口，退还支付金额
								Map<String, Object> refundParam = new HashMap<String, Object>();
								// 订单号
								refundParam.put("payNumber", out_trade_no);
								//订单金额,微信支付回调收到的单位为分，应转化为元再退费，不然会超出退费金额--金靖 2016.11.25
								String refundAmount = String.valueOf((Float.valueOf(amount) / 100.0));
								refundParam.put("amount", refundAmount);
								// 退费接口
								boolean isOK = paymentService.refundOrder(refundParam);
								if(isOK){
									logbean.setRefundhisResultMsg("成功");
								}else{
									logbean.setRefundhisResultMsg("失败");
								}
								
								// 更改订单状态
								if (isOK) {
									Map<String, Object> order = paymentService.queryOrder(out_trade_no);
									if (userInfo != null) {
										// 用户手机号
										String msisdn = userInfo.getMsisdn();
										// 用户姓名
										String userName = userInfo.getUserName();
										// 获得支付用途
										String payFor = "";
										String payfor = order.get("pay_for").toString();
										if ("3".equals(payfor)) {
											order.remove("pay_for");
											payFor = "预约挂号";
										} else if ("4".equals(payfor)) {
											order.remove("pay_for");
											payFor = "门诊";
										} else if ("5".equals(payfor)) {
											order.remove("pay_for");
											payFor = "住院预交";
										}
										SmsClient.sendRefundSms(msisdn, userName, payFor);
										// 冲正成功
										paymentService.updateCancelStatus(out_trade_no, "3");
									}
								} else {
									// 冲正失败
									paymentService.updateCancelStatus(out_trade_no, "4");
								}
							}
						}
					} else {
						// 支付未成功或信息不匹配
						respString = "fail";
					}
				}
			}
			resp.getWriter().write(respString);
		} catch (Exception e) {
			log.info("微信支付异常信息=======》" + e.getMessage());
			logbean.setResultMsg(e.getMessage());
			e.printStackTrace();
		}finally{
			paymentService.updatelog(logbean);
		}
		
	}

	/**
	 * 参数校验
	 *
	 * @param param
	 * <br />
	 * @param type
	 * <br />
	 */
	private void paramVerify(Map<String, Object> param, String type) throws ServiceException {
		if ("PayWithWeb".equals(type)) {
			if (null == param.get("orderId"))
				throw new ServiceException(ReturnCode.ORDER_INVALID);
			if (null == param.get("type"))
				throw new ServiceException(ReturnCode.ORDER_INVALID);
			if (null == param.get("returnUrl"))
				throw new ServiceException(ReturnCode.ORDER_INVALID);
			if (null == param.get("billTime"))
				throw new ServiceException(ReturnCode.ORDER_INVALID);
			if (null == param.get("money"))
				throw new ServiceException(ReturnCode.ORDER_INVALID);
		}
	}

	private TradingRecordBean getOrder(Map<String, Object> param) {
		TradingRecordBean tradeOrder = new TradingRecordBean();
		if (param.containsKey("amount")) {
			log.info("订单金额" + param.get("amount").toString());
			BigDecimal chanpayAcount=new BigDecimal(param.get("amount").toString());
			BigDecimal bssAcount=  chanpayAcount.divide(new BigDecimal(1));
			String amount = String.valueOf(bssAcount.multiply(new BigDecimal(1)));
			log.info("处理后订单金额" + amount);
			tradeOrder.setAmount(amount);
		}
		if (param.containsKey("payFor")) {
			tradeOrder.setPayFor(param.get("payFor").toString());
		}
		if (param.containsKey("payType")) {
			tradeOrder.setPayType(param.get("payType").toString());
		}
		if(param.containsKey("approach")){
			tradeOrder.setApproach(param.get("approach").toString());
		}

		return tradeOrder;
	}

	@RequestMapping(value = "/paymentCCBReceiveTest")
	@ResponseBody
	public ModelAndView paymentABCReceiveTest(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/return_url_show");
		mav.addObject("isSuccess", request.getParameter("isSuccess"));
		return mav;
	}

	private boolean callHisPaySuccess(String orderId) {
		// 业务结果
		boolean isSuccess = false;
		List<TradingRecordBean> dbOrder = paymentService.getOrderList(orderId);
		// 获取his响应结果
		ResponseHeader header = new ResponseHeader();
		if (null != dbOrder && dbOrder.size() > 0) {
			TradingRecordBean order = dbOrder.get(0);
			Map<String, Object> param = new HashMap<String, Object>();
			if ("1".equals(order.getPayFor())) {
				// 预约支付(被取消)，暂不实现
			} else if ("2".equals(order.getPayFor())) {
				// 当日挂号支付
				System.out.println("当日挂号支付---->enter！");
				param = paymentService.getRegisterPay(orderId);

				// 替换从数据库中获取的key的名称
				if (param.get("passWord") != null)
					param.put("password", param.get("passWord").toString());
				else
					param.put("password", "");
				param.remove("passWord");
				if (param.get("idCard") != null)
					param.put("iDCard", param.get("idCard").toString());
				else
					param.put("iDCard", "");
				param.remove("idCard");
				param.remove("orderId");

				System.out.println("当日挂号支付---->getRegisterPay out！" + param.toString());
				// 支付流水号
				param.put("payNumber", orderId);
				// 支付方式 1.现金 2.支付宝 3.微信 4.银行卡
				String method = null;
				if (order.getPayType().equals("1")) {
					method = "4";
				} else if (order.getPayType().equals("2") || order.getPayType().equals("4")) {
					method = "3";
				}
				param.put("payMethod", method);
				// 支付金额
				param.put("payAmount", order.getAmount());
				// 支付文字描述信息
				param.put("payExtend", "");
				ResponseEntity res = new ResponseEntity();
				System.out.println("当日挂号支付---->通知enter！" + param.toString());
				log.info("通知his当日挂号参数=======》" + param.toString());
				res = appointmentService.registerPay(param);
				System.out.println("当日挂号支付---->通知out！" + res.getContent().toString());
				log.info("当日挂号his返回值content=======》" + res.getContent().toString());
				log.info("当日挂号his返回值code=======》" + res.getHeader().getResultCode());
				log.info("当日挂号his返回值msg=======》" + res.getHeader().getResultMsg());
				header = res.getHeader();
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updateRegisterPay(orderId, (Map<String, Object>) res.getContent());
				}
			} else if ("3".equals(order.getPayFor())) {
				System.out.println("HIS 入库---->");
				// 预约挂号支付
				param = paymentService.getSubscriptionToPay(orderId);
				String bespeakDate = param.get("bespeakDate").toString();

				param.put("bespeakDate", bespeakDate.substring(0, 10));
				// 支付流水号
				param.put("payNumber", orderId);
				// 支付方式 1.现金 2.支付宝 3.微信 4.银行卡
				String method = null;
				if (order.getPayType().equals("1")) {
					log.info("订单号："+orderId+" 支付方式："+"银行卡");
					method = "4";
				} else if (order.getPayType().equals("2") || order.getPayType().equals("4")) {
					log.info("订单号："+orderId+" 支付方式："+"微信");
					method = "3";
				}
				param.put("payMethod", method);
				// 替换从数据库中获取的key的名称
				if (param.get("passWord") != null)
					param.put("password", param.get("passWord").toString());
				else
					param.put("password", "");
				param.remove("passWord");
				if (param.get("idCard") != null)
					param.put("iDCard", param.get("idCard").toString());
				else
					param.put("iDCard", "");
				param.remove("idCard");
				param.remove("orderId");
				// 支付金额
				param.put("payAmount", order.getAmount());
				// 支付文字描述信息
				param.put("payExtend", "");
				ResponseEntity res = new ResponseEntity();
				System.out.println("param----->" + param.toString());
				log.info("订单号："+orderId+" 通知his预约挂号参数=======》" + param.toString());
				res = appointmentService.addSubscriptionToPay(param);
				System.out.println("res----->" + res.getContent().toString());
				log.info("订单号："+orderId+" 预约挂号his返回值content=======》" + res.getContent().toString());
				log.info("订单号："+orderId+" 预约挂号his返回值code=======》" + res.getHeader().getResultCode());
				log.info("订单号："+orderId+" 预约挂号his返回值msg=======》" + res.getHeader().getResultMsg());
				header = res.getHeader();
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updateSubscriptionToPay(orderId, (Map<String, Object>) res.getContent());
				    log.info("订单号："+orderId+" HIS入库---->成功");
				}

				System.out.println("HIS入库---->成功！");
			} else if ("4".equals(order.getPayFor())) {
				// 门诊缴费支付
				param = paymentService.getToPay(orderId);
				// 支付流水号
				param.put("payNumber", orderId);
				// 支付方式 1.现金 2.支付宝 3.微信 4.银行卡
				String method = null;
				if (order.getPayType().equals("1")) {
					method = "4";
				} else if (order.getPayType().equals("2") || order.getPayType().equals("4")) {
					method = "3";
				}
				param.put("payMethod", method);
				// 支付金额
				param.put("payAmount", order.getAmount());
				// 支付文字描述信息
				param.put("payExtend", "");

				// 替换从数据库中获取的key的名称
				if (param.get("passWord") == null)
					param.put("password", "");
				else
					param.put("password", param.get("passWord").toString());
				param.remove("passWord");
				param.remove("orderId");
				log.info("通知his门诊缴费参数=======》" + param.toString());
				ResponseEntity res = new ResponseEntity();
				res = appointmentService.toPay(param);
				log.info("门诊缴费his返回值content=======》" + res.getContent().toString());
				log.info("门诊缴费his返回值code=======》" + res.getHeader().getResultCode());
				log.info("门诊缴费his返回值msg=======》" + res.getHeader().getResultMsg());
				header = res.getHeader();
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updateToPay(orderId, (Map<String, Object>) res.getContent());
					
					/*//插入门诊明细数据
					Map<String, Object> param1 = new HashMap<String, Object>();
					param1 = (Map<String, Object>) res.getContent();
					String billNo = param1.get("billNo").toString();
					param1.put("billNo", billNo);
					param1.put("billType","1");
					// 根据billNo获取划价单详情
					ResponseEntity responseEntity = appointmentService.queryBillDetail(param1);
					
					paymentService.insertToPayDetail((Map<String, Object>) responseEntity.getContent(),billNo);*/
				}
			} else if ("5".equals(order.getPayFor())) {
				// 住院预交支付
				param = paymentService.getPrePayExpense(orderId);
				// 支付流水号
				param.put("payNumber", orderId);
				// 支付方式 1.现金 2.支付宝 3.微信 4.银行卡
				String method = null;
				if (order.getPayType().equals("1")) {
					method = "4";
				} else if (order.getPayType().equals("2") || order.getPayType().equals("4")) {
					method = "3";
				}
				param.put("payMethod", method);
				// 支付金额
				param.put("payAmount", order.getAmount());
				// 支付文字描述信息
				param.put("payExtend", "");

				// 替换从数据库中获取的key的名称
				if (param.get("idCard") != null)
					param.put("iDCard", param.get("idCard").toString());
				else
					param.put("iDCard", "");
				param.remove("idCard");
				param.remove("orderId");
				log.info("通知his住院预交参数=======》" + param.toString());
				ResponseEntity res = new ResponseEntity();
				res = appointmentService.prePayExpense(param);
				log.info("住院预交his返回值content=======》" + res.getContent().toString());
				log.info("住院预交his返回值code=======》" + res.getHeader().getResultCode());
				log.info("住院预交his返回值msg=======》" + res.getHeader().getResultMsg());
				header = res.getHeader();
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updatePrePayExpense(orderId, (Map<String, Object>) res.getContent());
				}
			}
		}
		return isSuccess;
	}
}

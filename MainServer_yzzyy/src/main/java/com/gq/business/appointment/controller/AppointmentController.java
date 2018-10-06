package com.gq.business.appointment.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.base.BaseController;
import com.gq.business.appointment.model.StopScheduleBean;
import com.gq.business.appointment.service.IAppointmentService;
import com.gq.business.payment.mappers.PaymentMapper;
import com.gq.business.payment.model.TradingRecordBean;
import com.gq.business.payment.service.IPaymentService;
import com.gq.business.payment.service.WXIPaymentService;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.SmsClient;
import com.gq.common.utils.payment.CCB.CCBPayment;
import com.gq.config.ReturnCode;

@Controller
@RequestMapping(value = "/appointment")
@Component("appointmentController")
public class AppointmentController extends BaseController {
	// 日志测试文件
	private static Logger log = Logger.getLogger(AppointmentController.class);
	@Autowired
	private IAppointmentService appointmentService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private PaymentMapper paymentMapper;
	@Autowired
	private WXIPaymentService wxPaymentService;
	/**
	 * 查询大小科室列表，其中仅小科室的出参中存在挂号类型信息 1
	 * 
	 * @param requestBody
	 * @return
	 */
	// @RequestMapping(value = "/getDepartmentList")
	// @ResponseBody
	// public ResponseEntity getDepartmentList(@RequestBody RequestEntity
	// requestBody) {
	// Map<String, Object> param = requestBody.getContent();
	// return appointmentService.getDepartmentList(param);
	// }

	/**
	 * 获取排版信息(日期)
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getAppointmentList")
	@ResponseBody
	public ResponseEntity queryScheduleDate(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryScheduleDate(param);
	}

	/**
	 * 
	 * 获取排班信息(号类)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/queryScheduleClass")
	@ResponseBody
	public ResponseEntity queryScheduleClass(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryScheduleClass(param);
	}

	/**
	 * 
	 * 获取排班信息全部
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/queyDeptList")
	@ResponseBody
	public ResponseEntity queryScheduleAll(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryScheduleAll(param);
	}

	/**
	 * 
	 * 查询门诊科室信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getDepartmentList")
	@ResponseBody
	public ResponseEntity findAllDepartments(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findAllDepartments(param);
	}

	/**
	 * 
	 * 查询所有医生信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findAllDoctors")
	@ResponseBody
	public ResponseEntity findAllDoctors(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findAllDoctors(param);
	}
	
	/**
	 * 
	 * 查询我的医生信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findPatientDoctors")
	@ResponseBody
	public ResponseEntity findPatientDoctors(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findPatientDoctors(param);
	}

	/**
	 * 
	 * 查询(指定)科室医生信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getDoctorDepartmentList")
	@ResponseBody
	public ResponseEntity findDepartmentsDoctors(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findDepartmentsDoctors(param);
	}

	/**
	 * 
	 * 号源序号/时段查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getAppointmentNumberList")
	@ResponseBody
	public ResponseEntity getRegIndex(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.getRegIndex(param);
	}

	/**
	 * 
	 * 锁号交易(可选)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/lockRegister")
	@ResponseBody
	public ResponseEntity lockRegisterNo(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.lockRegisterNo(param);
	}

	/**
	 * 
	 * 取消锁号(可选)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/unlockRegister")
	@ResponseBody
	public ResponseEntity unlockRegisterNo(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.unlockRegisterNo(param);
	}

	/**
	 * 
	 * 预约挂号(窗口支付)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@SuppressWarnings({ "unchecked", "null" })
	@RequestMapping(value = "/addSubscription")
	@ResponseBody
	public ResponseEntity addSubscription(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		log.info("进入预约（不支付）接口");
		
		//初始化返回值
		ReturnCode returnCode = ReturnCode.OK;
		
		String result = null;		
		boolean bSubscpFlag = false;
		ResponseEntity rEntity = new ResponseEntity();
		
		//获取传入参数
		Map<String, Object> param = requestBody.getContent();
		log.info("前端参数===========》"+param);
		
		try {
			//调用HIS预约不支付接口
			ResponseEntity resEntityHIS = appointmentService.addSubscription(param);
			log.info("预约不支付his返回信息=====》" + resEntityHIS.getHeader().getResultCode());
			
			//如果调用成功
			if ("1".equals(resEntityHIS.getHeader().getResultCode()) && resEntityHIS.getHeader().getResultCode() == null) {
				//获取his返回的参数
				Map<String, String> paramReturn =(Map<String, String>) resEntityHIS.getContent();
				//his返回的号别
				String billNo = paramReturn.get("billNo");
				log.info("预约不支付his返回参数paramReturn=====》" + paramReturn);
				
				//订单详情入库
				if (null != param && !param.isEmpty()) {
					// 生成订单
					TradingRecordBean trade = getOrder(param);
					// 支付订单号
					String orderId = CCBPayment.getOrderID();
					trade.setOrderId(orderId);
					param.put("orderId", orderId);
					param.put("billNo", billNo);
					// 获取登录者用户ID
					trade.setCreaterId(param.get("userId").toString());
					log.info("金额" + trade.getAmount());
					
					//把支付信息入库
					result = paymentService.createOrder(trade, "");
					
					//把订单详情入库
					paymentService.createHisOrder(param);
					
					//构造短信参数
					String userId = param.get("userId").toString();
					String msisdn = param.get("msisdn").toString();
					String userName = "";
					if (null != param.get("userName")) {
						//用户姓名
						userName = param.get("userName").toString();
					}
					String doctorName = param.get("doctorName").toString();
					String date = param.get("bespeakDate").toString();
					String beginTime = param.get("beginTime").toString();
					String bespeakDate = date.substring(0, 10)+" "+beginTime;
					String time = date.substring(0, 10);
					String orderNumber = param.get("orderNumber").toString();
					String cardNo = param.get("cardNo").toString();
					String patientName = paymentService.queryPatientname(cardNo,userId);
					
					//获取门诊号
					ResponseEntity responseEntity = new ResponseEntity();
					//调用his接口
					responseEntity = appointmentService.checkPatientCard(param);
					Object content = null;
					content = responseEntity.getContent();
					Map<String, String> list = null; 
					list = (Map<String, String>) content;
					String outPatient = list.get("outPatient").toString();					
					//发短信通知
					SmsClient.sendAppointmentNopaySms(msisdn, userName, doctorName,orderNumber , outPatient, bespeakDate,time,patientName);
					bSubscpFlag = true;
				}
			}
			//如果调用HIS预约不支付接口失败
			else {
				//查看是否预约成功，没成功解锁号源
				rEntity = unlovk(param);
				}
		} catch (Exception e) {
			//查看是否预约成功，没成功解锁号源
			rEntity = unlovk(param);
			e.printStackTrace();
		}finally{
			ResponseHeader header = new ResponseHeader();
			if (bSubscpFlag) {
				header.setResultCode(returnCode.getCode());
				header.setResultMsg(returnCode.getMsg());
			} else{
				header.setResultCode(rEntity.getHeader().getResultCode());
				header.setResultMsg(rEntity.getHeader().getResultMsg());
			}
			rEntity.setHeader(header);
			rEntity.setContent("");
		}
		return rEntity;
	}
	
	/**
	 * 查看是否预约成功，没成功解锁号源
	 * @param param
	 * @return
	 */
	private ResponseEntity unlovk(Map<String, Object> param) {
		// 初始化返回值
		ReturnCode returnCode = ReturnCode.OK;

		boolean bSubscpFlag = false;
		ResponseEntity rEntity = new ResponseEntity();

		try {
			// 调用HIS预约列表接口，确认用户是否预约成功
			Map<String, Object> map = new HashMap<>();
			map.put("cardNo", param.get("cardNo"));
			map.put("cardType", param.get("cardType"));
			map.put("password", param.get("password"));
			log.info("调用HIS预约列表接口参数(异常)===========》" + map);

			ResponseEntity resEntity = appointmentService.querySubscription(map);
			log.info("调用HIS预约列表接口返回值code(异常)===========》" + resEntity.getHeader().getResultCode());
			log.info("调用HIS预约列表接口返回值msg(异常)===========》" + resEntity.getHeader().getResultMsg());
			log.info("调用HIS预约列表接口返回值Hismsg(异常)===========》" + resEntity.getHeader().getResultMsg());

			// 如果调用预约列表成功
			if ("1".equals(resEntity.getHeader().getResultCode()) && resEntity.getHeader().getResultCode() == null) {
				List<Map<String, String>> subscriptionReturnList = (List<Map<String, String>>) resEntity.getContent();
				if (null != subscriptionReturnList) {
					bSubscpFlag = false;
					// 遍历该用户预约挂号列表，判断是否已经挂上号了
					for (Map<String, String> subscriptionReturn : subscriptionReturnList) {
						log.info("调用HIS预约列表接口返回值===========》" + subscriptionReturn);
						// 按照医生姓名、时间、号序确定是否预约成功
						if (param.get("doctorName").toString().equals(subscriptionReturn.get("doctorName").toString())
								&& param.get("orderNumber").toString().equals(subscriptionReturn.get("appointmentNumber").toString())
								&& param.get("bespeakDate").toString().equals(subscriptionReturn.get("date").toString())) {
							bSubscpFlag = true;
							break;
						}
					}
					// 如果预约失败，解锁号源
					if (false == bSubscpFlag) {
						// 调用三次解锁接口
						map.put("serialNumber", param.get("serialNumber"));
						log.info("调用HIS取消锁号接口参数===========》" + map);

						for (int i = 0; i < 3; i++) {
							resEntity = null;
							resEntity = appointmentService.unlockRegisterNo(map);
							if ("1".equals(resEntity.getHeader().getResultCode())
									&& resEntity.getHeader().getResultCode() == null) {
								returnCode = ReturnCode.UNLOCK_SUCCESS;
								break;
							} else {
								returnCode = ReturnCode.UNLOCK_FAIL;
							}
							Thread.sleep(1 * 1000);
						}
					}
				}
			}
			// 如果调用预约列表查询接口失败，直接解锁号源
			else {
				map.put("serialNumber", param.get("serialNumber"));
				log.info("预约列表为空=========》"+map);
				for (int i = 0; i < 3; i++) {
					resEntity = null;
					resEntity = appointmentService.unlockRegisterNo(map);
					log.info("预约列表为空=========》"+resEntity.getHeader().getResultCode());
					if ("1".equals(resEntity.getHeader().getResultCode())
							&& resEntity.getHeader().getResultCode() == null) {
						log.info("解锁成功=========》");
						returnCode = ReturnCode.UNLOCK_SUCCESS;
						break;
					} else {
						log.info("解锁失败=========》");
						returnCode = ReturnCode.UNLOCK_FAIL;
					}
					Thread.sleep(1 * 1000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			rEntity.setHeader(header);
			rEntity.setContent("");
		}
		return rEntity;
	}
	
	private TradingRecordBean getOrder(Map<String, Object> param) {
		TradingRecordBean tradeOrder = new TradingRecordBean();
		if (param.containsKey("amount")) {
			tradeOrder.setAmount(param.get("amount").toString());
		}
		if (param.containsKey("payFor")) {
			tradeOrder.setPayFor(param.get("payFor").toString());
		}
		if (param.containsKey("payType")) {
			tradeOrder.setPayType(param.get("payType").toString());
		}
		/*if(param.containsKey("approach")){
			tradeOrder.setApproach(param.get("approach").toString());
		}*/
		return tradeOrder;
	}

	/**
	 * 
	 * 查询预约信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getAppointmentRecordList")
	@ResponseBody
	public ResponseEntity querySubscription(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		ResponseEntity res = appointmentService.querySubscription(param);
		String outPatient = "";
		if("1".equals(param.get("type").toString())){
			//获取门诊号
			ResponseEntity reoutPatient = new ResponseEntity();
			//调用his接口
			reoutPatient = appointmentService.checkPatientCard(param);
			Object content = null;
			content = reoutPatient.getContent();
			Map<String, String> list = null; 
			list = (Map<String, String>) content;
			outPatient = list.get("outPatient").toString();	
		}
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = new ResponseEntity();
		ResponseHeader header = res.getHeader();
		ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
		try {
			if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
				array = (ArrayList<Map<String, Object>>) res.getContent();
				String type = "";
				String billNo = "";
				String ret = "";
				for(int i=0;i<array.size();i++){
					//将门诊号返回给前端
					array.get(i).put("outPatient", outPatient);
					log.info("循环："+array.get(i).toString()+" outPatient:"+outPatient);
				}
				for (Map<String, Object> tmpMap : array) {	
					type = tmpMap.get("subscriptionFlag").toString();
					billNo = tmpMap.get("billNo").toString();
					// 0 是当日挂号,1 是预约挂号
					if ("0".equals(type)) {
						// 根据billNo查询当日挂号表的订单号，存在返回String类型结果，不存在返回null
						ret = paymentService.getPayNumberInRegisterPay(billNo);
					} else if ("1".equals(type)) {
						// 根据billNo查询预约挂号表的订单号，存在返回String类型结果，不存在返回null
						ret = paymentService.getPayNumberInSubscriptionPay(billNo);
						log.info("订单号：ret==="+ret+"-->ret.tostring="+ret.toString());
						String payType = paymentMapper.loadRecord(ret);
						tmpMap.put("payType", payType);	
						log.info("订单号："+ret+" 支付方式为2微信4微信公众号："+payType);
						log.info("订单信息："+tmpMap);
					}
					if (ret == null || "".equals(ret)) {
						// 数据库查询不到订单号，说明是线下挂号
						tmpMap.put("isOnline", "0");
					} else {
						// 数据库查询到订单号，说明是线上挂号
						tmpMap.put("isOnline", "1");
					}
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			responseEntity.setHeader(header);
			responseEntity.setContent(array);
		}
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "querySubscription", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 
	 * 预约支付(暂时关闭)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/addSubscriptionPay")
	@ResponseBody
	public ResponseEntity addSubscriptionPay(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.addSubscriptionPay(param);
	}
	
	/**
	 * 获取订单支付方式
	 */
	private String getPayType(String orderId){
		ReturnCode returnCode = ReturnCode.OK;
		String payType = "";
		//orderId = "20170809102908wx0834";
		log.info("进入获取getPayType====>");
		try {
			// 获取医生注册信息
			if(orderId != null && orderId !=""){
				log.info("判断orderId不为空====>");
			    payType = paymentService.getPayType(orderId);
			    log.info("payType---"+payType);
			}	
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			// 构造返回应答对象
		}
		return payType;
	}
	
	/**
	 * 
	 * 当日挂号支付
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/registerPay")
	@ResponseBody
	public ResponseEntity registerPay(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.registerPay(param);
	}

	/**
	 * 
	 * 支付挂号退号
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
		try {
			if (null != requestBody) {
				if (null != param && !param.isEmpty()) {
					// 根据单据号去数据库查询订单号
					payNumber = getPayNumber(param);
					param.put("payNumber", payNumber);
					param.remove("pay_for");
					String payType = param.get("payType").toString();
					
					//HIS退号退费
					log.info("订单号："+payNumber+" 开始通知HIS执行退号退费操作——————>");
					res = new ResponseEntity();
					int i=0;
					for (i = 0; i < 3; i++) {
						//判断是否通知过HIS，若通知过HIS，第三方未成功退费那也跳出循环，不在重复通知HIS
						if(flag == 1){
							break;
						}
						log.info("订单号："+payNumber+" 第"+i+"次通知医院his退号退费=====》");
						// 通知his退号退费
						res = appointmentService.unregisterPay(param);
						log.info("订单号："+payNumber+" his支付挂号退费ResultCode==》" + res.getHeader().getResultCode()+" HisResultMsg==》"+res.getHeader().getResultCode()); 
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
							// 第三方退款(2app退款4公众号退款)
							if("4".equals(payType)){
								log.info("订单号："+payNumber+"app退公众号预约的号");
							    isOk = wxPaymentService.wxPublicRefundOrder(param);
							    log.info("订单号："+payNumber+" app退公众号预约的号---第三方退款返回信息=====》" + isOk);							
							}else{
								if(payNumber.contains("p")){
									log.info("订单号："+payNumber+"app退公众号预约的号");
								    isOk = wxPaymentService.wxPublicRefundOrder(param);
								    log.info("订单号："+payNumber+" app退公众号预约的号---第三方退款返回信息=====》" + isOk);			
								}else{
								log.info("订单号："+payNumber+"app退app预约的号");
								isOk = paymentService.refundOrder(param);
								log.info("订单号："+payNumber+"app 退app预约号 第三方退款返回信息=====》" + isOk);
								}
							}
														
							if(isOk){
								//更新本地订单
								paymentService.updateCancelStatus(payNumber, "2");
								log.info("订单号："+payNumber+" 更新本地数据订单表退费状态=====》");
								returnCode = ReturnCode.OK;
								break;
							}else {
								log.info("订单号："+payNumber+" 第三方退款返回失败信息=====》" + isOk);
								returnCode = ReturnCode.REFUND_FAIL;
								
							}
							// 退号失败 暂停1S后继续通知医院退号
							Thread.sleep(1000);
						}
					/*// 根据订单号查询支付金额
					String amount = getPayAmountByOrderId(payNumber);
					log.info("根据订单号查询支付金额=====》"+amount);
					param.put("amount", amount);
					// 第三方退款
					isOk = paymentService.refundOrder(param);
					log.info("第三方退款返回信息=====》" + isOk);*/
					/*if (isOk) {
						res = new ResponseEntity();
						for (int i = 0; i < 3; i++) {
							log.info("第"+i+"次通知医院his退号退费=====》");
							// 通知his退号退费
							res = appointmentService.unregisterPay(param);
							log.info("支付挂号退费his返回信息=====》" + res.getHeader().getResultCode());
							ResponseHeader header = res.getHeader();
							if (header.getResultCode().equals("1") && header.getHisResultCode() == null) {
								paymentService.updateCancelStatus(payNumber, "2");
								log.info("更新本地数据订单表退费状态=====》");
								returnCode = ReturnCode.OK;
								break;
							}
							// 退号失败 暂停1S后继续通知医院退号
							Thread.sleep(1000);
						}
					} else {
						log.info("第三方退款返回失败信息=====》" + isOk);
						returnCode = ReturnCode.REFUND_FAIL;
					}*/
					}
					log.info("订单号："+payNumber+" ——————>通知HIS执行退号退费操作结束！共执行了："+i+"次");
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
				//PublicUserBean loggedUser = (PublicUserBean)request.getSession().getAttribute("loginuser");
				//Map<String, Object> param = paymentMapper.selectAll(payNumber);
					//获得用户ID
					String userId = param.get("userId").toString();
					//手机号码
					String msisdn = param.get("msisdn").toString();
					//用户姓名
					String userName = "";
					if (null != param.get("userName")) {
						//用户姓名
						userName = param.get("userName").toString();
					}
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
				    log.info("订单号："+payNumber+" 第三方退款成功后，发送短信通知！");
			} else {
				// 第三方退费失败，返回退费失败信息
				
				header.setResultCode(returnCode.getCode());
				header.setResultMsg(returnCode.getMsg());
				log.info("订单号："+payNumber+" 第三方退款失败，返回退费失败信息=====》"+returnCode.getCode()+returnCode.getMsg());
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
		ResponseEntity resEntity = appointmentService.unaddSubscription(param);
		if (resEntity != null) {
			if(resEntity.getHeader().getResultCode() == null){
				String billNo = param.get("billNo").toString();
				Map<String, Object> map = paymentService.queryDate(billNo);
				//map.remove("cancelregister");
				map.put("cancelregister","5");
				paymentService.updateSubscriptionCancelregister(map);
				//PublicUserBean loggedUser = (PublicUserBean)request.getSession().getAttribute("loginuser");
					//获得用户ID
					String userId = param.get("userId").toString();
					//手机号码
					String msisdn = param.get("msisdn").toString();
					//用户姓名
					String userName = "";
					if (null != param.get("userName")) {
						//用户姓名
						userName = param.get("userName").toString();
					}
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
		return resEntity;
	}

	/**
	 * 
	 * 预约挂号（支付）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/addSubscriptionToPay")
	@ResponseBody
	public ResponseEntity addSubscriptionToPay(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.addSubscriptionToPay(param);
	}

	/**
	 * 
	 * 门诊待缴费单据
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findBill")
	@ResponseBody
	public ResponseEntity findBill(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findBill(param);
	}

	/**
	 * 
	 * 4.4生成待缴费单据(仅测试库)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/CreateBill")
	@ResponseBody
	public ResponseEntity CreateBill(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.CreateBill(param);
	}

	/**
	 * 
	 * 获取单据明细
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findBillItem")
	@ResponseBody
	public ResponseEntity findBillItem(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findBillItem(param);
	}

	/**
	 * 
	 * 门诊缴费
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/toPay")
	@ResponseBody
	public ResponseEntity toPay(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.toPay(param);
	}

	/**
	 * 
	 * 卡信息校验
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getPatientList")
	@ResponseBody
	public ResponseEntity checkPatientCard(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.checkPatientCard(param);
	}

	/**
	 * 
	 * 新增虚拟信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/addPatient")
	@ResponseBody
	public ResponseEntity addPatientCard(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.addPatientCard(param);
	}

	/**
	 * 
	 * 6.1住院费用查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getInhospitalDetail")
	@ResponseBody
	public ResponseEntity queryInhosFee(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryInhosFee(param);
	}

	/**
	 * 
	 * 6.2费用清单查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getInhospitalList")
	@ResponseBody
	public ResponseEntity queryFeeList(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryFeeList(param);
	}

	/**
	 * 
	 * 6.3住院预交
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/prePayExpense")
	@ResponseBody
	public ResponseEntity prePayExpense(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.prePayExpense(param);
	}

	/**
	 * 
	 * 6.4 分类费用查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/queryFeeListClass")
	@ResponseBody
	public ResponseEntity queryFeeListClass(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryFeeListClass(param);
	}
	
	
	/**
	 * 
	 * 6.5  住院预交金支付记录
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/queryPayExpenseList")
	@ResponseBody
	public ResponseEntity queryPayExpenseList(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryPayExpenseList(param);
	}

	/**
	 * 
	 * 7.1检验检查报告列表
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findReport")
	@ResponseBody
	public ResponseEntity findReport(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findReport(param);
	}

	/**
	 * 
	 * 7.2检验检查报告
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findReportResult")
	@ResponseBody
	public ResponseEntity findReportResult(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findReportResult(param);
	}

	/**
	 * 
	 * 8.1获取体检记录
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/PeisTask")
	@ResponseBody
	public ResponseEntity PeisTask(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.PeisTask(param);
	}

	/**
	 * 
	 * 8.2.1体检分科项目报告(方式一)
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisDepartmentReport ")
	@ResponseBody
	public ResponseEntity peisDepartmentReport(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisDepartmentReport(param);
	}

	/**
	 * 
	 * 8.2.2.1体检分科项目
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisReportItem")
	@ResponseBody
	public ResponseEntity peisReportItem(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisReportItem(param);
	}

	/**
	 * 
	 * 8.2.2.2获取项目报告
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisReportItemResult")
	@ResponseBody
	public ResponseEntity peisReportItemResult(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisReportItemResult(param);
	}

	/**
	 * 
	 * 8.3总检结论及建议
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisItemSumReport ")
	@ResponseBody
	public ResponseEntity peisItemSumReport(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisItemSumReport(param);
	}

	/**
	 * 
	 * 9.1总对帐查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getPaysum")
	@ResponseBody
	public ResponseEntity getPaysum(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.getPaysum(param);
	}

	/**
	 * 9.2明细对帐查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getPayDetail")
	@ResponseBody
	public ResponseEntity getPayDetail(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.getPayDetail(param);
	}

	/**
	 * 
	 * 10.1门诊就诊记录查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryClinicList")
	@ResponseBody
	public ResponseEntity queryClinicList(@RequestBody RequestEntity requestBody) {
		//初始化返回值
		ReturnCode returnCode = ReturnCode.OK;
		ResponseHeader header = null;
		ResponseEntity res;
		ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> every = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> param = requestBody.getContent();
			
			//获取门诊号
			ResponseEntity responseEntity = new ResponseEntity();
			//调用his接口
			Map<String, Object> param1 = new HashMap<>();
			param1.put("cardType",param.get("cardType"));
			param1.put("cardNo",param.get("cardNo"));
			param1.put("password","");
			param1.put("iDCard",param.get("iDCard"));
			param1.put("name",param.get("name"));
			param1.put("sex", "");
			responseEntity = appointmentService.checkPatientCard(param1);
			log.info("参数："+param1+" 结果:"+responseEntity.getContent());
			Object content = null;
			content = responseEntity.getContent();
			Map<String, String> result = null; 
			result = (Map<String, String>) content;
			String outPatient = "";
			if(result.containsKey("outPatient")){
				outPatient = result.get("outPatient").toString();
				log.info("门诊号："+outPatient);
			}				
			Map<String, Object> map = new HashMap<String,Object>();	
			res = appointmentService.queryClinicList(param);
			array = (ArrayList<Map<String, Object>>) res.getContent();			
			header = res.getHeader();
            if(array != null){
            	for(int i = 0;i < array.size();i++){
        			every = (ArrayList<Map<String, Object>>) array.get(i).get("result");
        			 if ("1".equals(header.getResultCode()) && header.getResultCode() == null){		
        					for (Map<String, Object> tmpMap : every){
        						tmpMap.put("outPatient", outPatient);
        					}				
        			 }
        			}	
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}finally {
			res = new ResponseEntity(returnCode);
			res.setHeader(header);
			res.setContent(array);	
		}
		return res;
	}

	/**
	 * 
	 * 10.2门诊就诊记录详情查询
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/queryBillDetail")
	@ResponseBody
	public ResponseEntity queryBillDetail(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.queryBillDetail(param);
	}

	/**
	 * 
	 * 11.1查询队列
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/getQueue")
	@ResponseBody
	public ResponseEntity getQueue(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.getQueue(param);
	}

	/**
	 * 通过时间查询可预约不同类型的预约列表
	 * 
	 * @param requestBody
	 * @return
	 */
	// @RequestMapping(value = "/getAppointmentList")
	// @ResponseBody
	// public ResponseEntity getAppointmentList(@RequestBody RequestEntity
	// requestBody) {
	// Map<String, Object> param = requestBody.getContent();
	// return appointmentService.getAppointmentList(param);
	// }

	// /**
	// * 通过时间查询可挂号不同类型的挂号列表。
	// *
	// * @param requestBody
	// * @return
	// */
	// @RequestMapping(value = "/getRegisterList")
	// @ResponseBody
	// public ResponseEntity getRegisterList(@RequestBody RequestEntity
	// requestBody) {
	// Map<String, Object> param = requestBody.getContent();
	// return appointmentService.getAppointmentList(param);
	// }

	/**
	 * 判断当前选择的日期是否是今天，若是则显示挂号，否则显示预约
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/confirmTheDate")
	@ResponseBody
	public ResponseEntity confirmTheDate(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		// 返回结果，0(是今天)/1(不是今天)
		String result = "1";
		long startTime = System.currentTimeMillis();
		Map<String, Object> param = requestBody.getContent();

		if (null != param && null != param.get("date")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				// 获取日期并判断是否是今天
				Date paramDate = sdf.parse(param.get("date").toString());
				String paramYearMonthDay = sdf.format(paramDate);
				Date startDate = sdfs.parse(paramYearMonthDay + " 00:00:00");
				Date endDate = sdfs.parse(paramYearMonthDay + " 23:59:59");
				Date now = new Date();
				if (startDate.before(now) && now.before(endDate)) {
					result = "0";
				}
			} catch (Exception e) {
				returnCode = ReturnCode.SYSTEM_ERROR;
				returnCode.setMsg(e.getMessage());
			} finally {
				// 构造返回对象
				responseEntity = new ResponseEntity(returnCode);
				// 构造包体
				Map<String, String> mapResult = new HashMap<String, String>();
				mapResult.put("flag", result);
				responseEntity.setContent(mapResult);
				// 记录接口日志
				MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "ConfirmTheDate", requestBody.toString(),
						responseEntity.toString(), System.currentTimeMillis() - startTime);
			}
		} else {
			returnCode = ReturnCode.SYSTEM_ERROR;
			responseEntity = new ResponseEntity(returnCode);
			Map<String, String> mapResult = new HashMap<String, String>();
			responseEntity.setContent(mapResult);
		}
		return responseEntity;
	}

	// /**
	// * 通过已选的科室和挂号类型等信息，获取对应的号序列表
	// *
	// * @param requestBody
	// * @return
	// */
	// @RequestMapping(value = "/getAppointmentNumberList")
	// @ResponseBody
	// public ResponseEntity getAppointmentNumberList(@RequestBody RequestEntity
	// requestBody) {
	// Map<String, Object> param = requestBody.getContent();
	// return appointmentService.getAppointmentNumberList(param);
	// }

	/**
	 * 通过已选的科室和挂号类型等信息，获取对应的挂号号序列表。
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getRegisterNumberList")
	@ResponseBody
	public ResponseEntity getRegisterNumberList(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.getRegisterNumberList(param);
	}

	/**
	 * 保存预约信息，生成订单，供后续支付
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/appoint")
	@ResponseBody
	public ResponseEntity appoint(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.appoint(param);
	}

	/**
	 * 保存挂号信息，生成订单，供后续支付。
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/register")
	@ResponseBody
	public ResponseEntity register(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.register(param);
	}

	/**
	 * 分页查询预约记录
	 * 
	 * @param requestBody
	 * @return
	 */
	// @RequestMapping(value = "/getAppointmentRecordList")
	// @ResponseBody
	// public ResponseEntity getAppointmentRecordList(@RequestBody RequestEntity
	// requestBody) {
	// Map<String, Object> param = requestBody.getContent();
	// return appointmentService.getAppointmentRecordList(param);
	// }

	/**
	 * 获取指定预约的详情信息
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getAppointmentRecordDetail")
	@ResponseBody
	public ResponseEntity getAppointmentRecordDetail(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.getAppointmentRecordDetail(param);
	}

	/**
	 * 根据订单号获取业务数据
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getOrderInfo")
	@ResponseBody
	public ResponseEntity getOrderInfo(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		Map<String, String> returnParam = new HashMap<String, String>();
		try {
			if(null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				if(null != param && !param.isEmpty()) {
					TradingRecordBean bean = new TradingRecordBean();
					//设置订单号
					String orderId = param.get("orderId").toString();
					bean.setOrderId(orderId);
					//待查询订单为已支付订单
					bean.setStatus("2");
					//根据订单号查询列表数据
					List<TradingRecordBean> dbOrder = paymentMapper.getOrdreList(bean);
					if (null != dbOrder && dbOrder.size() > 0) {
						bean = dbOrder.get(0);
						String payFor = bean.getPayFor();
						Map<String, Object> tmpParam = new HashMap<String, Object>();
						if("2".equals(payFor)) {
							//当日挂号支付
							tmpParam = paymentMapper.getRegisterPay(orderId);
							//号类(1专家号、2普通号)
							returnParam.put("appointmentType", tmpParam.get("appointmentType").toString());
							//医生姓名
							returnParam.put("doctorName", tmpParam.get("doctorName").toString());
							//医生职称
							returnParam.put("doctorPost", tmpParam.get("doctorPost").toString());
							//就诊科室
							returnParam.put("departmentName", tmpParam.get("departmentName").toString());
							//患者姓名
							returnParam.put("patientName", tmpParam.get("name").toString());
							//就诊日期
							returnParam.put("appointmentDate", tmpParam.get("serialNumber").toString().split("_")[0]);
							//号序
							returnParam.put("orderNumber", tmpParam.get("orderNumber").toString());
							//开始时间
							returnParam.put("beginTime", tmpParam.get("beginTime").toString());
							//午别
							returnParam.put("noonType", tmpParam.get("noonType").toString());
							//单据金额
							returnParam.put("billCost", bean.getAmount());
							//业务类型
							returnParam.put("type", payFor);
						} else if("3".equals(payFor)) {
							//预约挂号支付
							tmpParam = paymentMapper.getSubscriptionToPay(orderId);
							//号类(1专家号、2普通号)
							returnParam.put("appointmentType", tmpParam.get("appointmentType").toString());
							//医生姓名
							returnParam.put("doctorName", tmpParam.get("doctorName").toString());
							//医生职称
							returnParam.put("doctorPost", tmpParam.get("doctorPost").toString());
							//就诊科室
							returnParam.put("departmentName", tmpParam.get("departmentName").toString());
							//患者姓名
							returnParam.put("patientName", tmpParam.get("name").toString());
							//就诊日期
							returnParam.put("appointmentDate", tmpParam.get("bespeakDate").toString().split(" ")[0]);
							//号序
							returnParam.put("orderNumber", tmpParam.get("orderNumber").toString());
							//开始时间
							returnParam.put("beginTime", tmpParam.get("beginTime").toString());
							//午别
							returnParam.put("noonType", tmpParam.get("noonType").toString());
							//单据金额
							returnParam.put("billCost", bean.getAmount());
							//业务类型
							returnParam.put("type", payFor);
						} else if("4".equals(payFor)) {
							//门诊缴费
							tmpParam = paymentMapper.getToPay(orderId);
							//开方科室Id
							returnParam.put("billingDepartmentId", tmpParam.get("billingDepartmentId").toString());
							//开方科室
							returnParam.put("billingDepartment", tmpParam.get("billingDepartment").toString());
							//登记时间
							returnParam.put("checkInTime", tmpParam.get("checkInTime").toString());
							//开方医生
							returnParam.put("billingDoctor", tmpParam.get("billingDoctor").toString());
							//单据金额
							returnParam.put("billCost", tmpParam.get("billCost").toString());
							//业务类型
							returnParam.put("type", payFor);
							//取药串口号
							returnParam.put("drugWinNo", tmpParam.get("drugWinNo").toString());
						} else if("5".equals(payFor)) {
							//住院预交
							tmpParam = paymentMapper.getPrePayExpense(orderId);
							//卡号
							returnParam.put("cardNo", tmpParam.get("cardNo").toString());
							//患者姓名
							returnParam.put("patientName", tmpParam.get("name").toString());
							//患者身份证号
							returnParam.put("idCard", tmpParam.get("idCard").toString());
							//单据金额
							returnParam.put("billCost", bean.getAmount());
							//业务类型
							returnParam.put("type", payFor);
						}
						returnCode = ReturnCode.OK;
					}
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(returnParam);
		}
		
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getOrderInfo",
			requestBody.toString(), responseEntity.toString(),
			System.currentTimeMillis() - startTime);
		
		return responseEntity;
	}
	
	/**
	 * 取消预约。
	 * 
	 * @param requestBody
	 * @return
	 */
	// @RequestMapping(value = "/cancelRegister")
	// @ResponseBody
	// public ResponseEntity cancelRegister(@RequestBody RequestEntity
	// requestBody) {
	// Map<String, Object> param = requestBody.getContent();
	// return appointmentService.cancelRegister(param);
	// }

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

	private String getPayAmountByOrderId(String orderId) {
		return paymentService.getPayAmountByOrderId(orderId);
	}

	
	/**
	 * 
	 * 3.2.10 停诊通知
	 * 
	 * 方法说明：
	 * 中医院停诊接口只提供了停诊的安排信息，不包括被停诊的患者信息。
	 * 从接口中获得停诊信息，在数据库中查出t_payment_order.createrId（操作账号的id，即是t_publicuser中的userid）
	 * 从t_publicuser中查出操作账号的手机号码，发送短信通知。
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void StopSchedule(){
		Map<String, Object> param = new HashMap<String, Object>();
			//获取当前的 年月日 时间
		Date date = new Date();  
		SimpleDateFormat nowTime = new SimpleDateFormat("yyyy-MM-dd");  
		String Date = nowTime.format(date);  
			//添加至 Map<String, Object> param
		param.put("date",Date);
		ResponseEntity responseEntity = new ResponseEntity();
		log.info("进入停诊，调用HIS获取数据======》" + responseEntity.getContent().toString());
		try{
			//从接口中获取停诊信息
			responseEntity =  appointmentService.querystopScheduleDate(param);
			log.info("停诊信息=======》" + responseEntity.getContent().toString());
			if (responseEntity.getContent() != null) {
					//把接口中多获取的数据强制转换成List
				List<Map<String, Object>> docParamReturn =(List<Map<String, Object>>) responseEntity.getContent();
					//遍历List中的停诊信息
				for (int i = 0; i < docParamReturn.size(); i++) {
					//获得订单库中的标识码Code
					String proCode = docParamReturn.get(i).get("proCode").toString();
					//获取停诊的开始时间（年月日时分秒）
					String beginTime = docParamReturn.get(i).get("beginTime").toString();
					//获取停诊的结束时间（年月日时分秒）
					String endinTime = docParamReturn.get(i).get("endinTime").toString();
					//拆分出 例-08:30（时：分）的字符串，到数据库中作为停诊开始时段判断的条件
					String begin = beginTime.substring(11,beginTime.length()-3);
					//拆分出 例-23:59（时：分）的字符串，到数据库中作为停诊结束时段判断的条件
					String endin = endinTime.substring(11,endinTime.length()-3);
					
					//创建一个新的对象类型，并把遍历的数据放入
					StopScheduleBean info = new StopScheduleBean();
					info.setProCode(proCode);
					info.setBeginTime(beginTime);
					info.setEndinTime(endinTime);
					info.setBegin(begin);
					info.setEndin(endin);
					log.info("user-info=======》"+info);
					/**
					 * 下面的方法中包含了：
					 *1根据停诊信息从数据库中查出userid，
					 *2再用userid查出账户操作者的手机号码，并发送停诊通知
					*/
					//查询已支付的订单
					appointmentService.selectStopSchedule(info);
					log.info("查询已支付订单并发短信=======》OK");
					//查询窗口支付的订单
					appointmentService.queryStopSchedule(info);
					log.info("查询窗口支付订单并发短信=======》OK");
				}
				
			}
		} catch (Exception e) {
			log.info("停诊通知异常=======》" + e.getMessage());
			e.printStackTrace();
		}

	}
}

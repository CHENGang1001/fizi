package com.gq.business.payment.controller;

import java.io.IOException;
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
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.accountmanage.service.IPublicUserService;
import com.gq.business.appointment.service.IAppointmentService;
import com.gq.business.payment.model.LogBean;
import com.gq.business.payment.model.TradingRecordBean;
import com.gq.business.payment.service.IPaymentService;
import com.gq.business.payment.service.WXIPaymentService;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.HttpUtils;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.SmsClient;
import com.gq.common.utils.payment.wx.WXPublicConstants;
import com.gq.common.utils.payment.wx.WXPublicPayment;
import com.gq.common.utils.payment.wx.XmlUtils;
import com.gq.config.ReturnCode;

@Controller
@RequestMapping("/payment")
public class WXPublicPaymentController {

	// 日志测试文件
	private static Logger log = Logger.getLogger(WXPublicPaymentController.class);

	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IAppointmentService appointmentService;
	@Autowired
	private IPublicUserService userInterfaceService;
	@Autowired
	private WXIPaymentService wxPaymentService;

	/**
	 * 微信公众号支付
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/wxPublicPay")
	@ResponseBody
	public ResponseEntity wxPublicPay(@RequestBody RequestEntity requestBody, HttpServletRequest request) {
		System.out.println("用户端ip地址为：" + request.getRemoteAddr());
		
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		Map<String, String> returnParam = new HashMap<String, String>();
		boolean isOK = false;
		LogBean logbean =new LogBean();
		logbean.setInto("微信公众号支付");
		logbean.setPayType("3");
		logbean.setStartTime(startTime);
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				log.info("接口---wxPublicPay---入参是：" + param.toString());
				String openid = param.get("openid").toString();
				
				if (null != param && !param.isEmpty()) {
					log.info("接口---wxPublicPay---00000000");
					// 生成订单
					TradingRecordBean bean = getOrder(param);
					// 支付订单号
					String orderId = WXPublicPayment.getOrderID();
					logbean.setOrderId(orderId);
					log.info("接口---wxPublicPay---此次订单号是：" + orderId);
					bean.setOrderId(orderId);
					param.put("orderId", orderId);
					// 获取登录者用户名
					// PublicUserBean user = (PublicUserBean) request.getSession().getAttribute("loginuser");
					log.info("订单号："+orderId+" 接口---wxPublicPay---入参2是：" + param.toString());
					String userID = param.get("userID").toString();
					log.info("订单号："+orderId+" 接口---wxPublicPay---此次登录者userID是否为空：" + userID);
					bean.setCreaterId(userID);
					paymentService.createOrder(bean, "");
					// 生成业务逻辑订单
					paymentService.createHisOrder(param);
					Map<String, String> wxParam = new HashMap<String, String>();
					param.put("spbill_create_ip", request.getRemoteAddr()); // 用户端ip
					// 配置微信公众号支付验签所需参数
					wxParam = WXPublicPayment.getWXPublicParam(param);
					// 生成签名
					String sign = WXPublicPayment.createWeiSign(wxParam);
					wxParam.put("sign", sign);
					// 将map类型数据转换为xml格式
					String entity = WXPublicPayment.toXml(wxParam);
					// 向微信发送请求
					String content = HttpUtils.sendPost(WXPublicConstants.WX_PUBLIC_API_URL + WXPublicConstants.WX_PUBLIC_UNIFIEDORDER_URL, entity);
					log.info("订单号："+orderId+" 接口---wxPublicPay---微信返回的请求信息是：" + content);
					// 将返回的xml结果转换为map格式
					wxParam = XmlUtils.readStringXmlOut(content);
					log.info("订单号："+orderId+" 接口---wxPublicPay---将返回的xml结果转换为map格式是：" + wxParam.toString());
					// return_code和result_code都为SUCCESS的时候返回prepay_id
					if (WXPublicConstants.WX_PUBLIC_SUCCESS.equals(wxParam.get("return_code"))
							&& WXPublicConstants.WX_PUBLIC_SUCCESS.equals(wxParam.get("result_code"))) { // 前端H5调起支付API
						String paySign = "";
						returnParam.put("appId", WXPublicConstants.WX_PUBLIC_APPID); // 公众号id
						returnParam.put("timeStamp", WXPublicPayment.getTimeStamp()); // 时间戳
						returnParam.put("nonceStr", WXPublicPayment.genNonceStr()); // 随机字符串
						// 统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
						returnParam.put("package", "prepay_id=" + wxParam.get("prepay_id"));
						returnParam.put("signType", "MD5");

						paySign = WXPublicPayment.createWeiSign(returnParam);
						returnParam.put("paySign", paySign); // 签名

						returnParam.put("wx_package", returnParam.get("package")); // prepay_id=129387600
						returnParam.put("orderId", orderId); // 临时新增---传给前端用

						// 以下参数是给微信发送模板消息时使用, 没有则传空
						returnParam.put("wx_orderid", orderId); // 订单号
						returnParam.put("openid", openid); // 患者openid
						returnParam.put("wx_name", param.containsKey("name") ? param.get("name").toString() : ""); // 患者姓名
						returnParam.put("wx_money", param.containsKey("amount") ? param.get("amount").toString() : ""); // 订单金额
						returnParam.put("wx_department", param.containsKey("departmentName") ? param.get("departmentName").toString() : ""); // 科室名称
						returnParam.put("wx_doctor", param.containsKey("doctorName") ? param.get("doctorName").toString() : ""); // 医生名称
						returnParam.put("wx_card", param.containsKey("cardNo") ? param.get("cardNo").toString() : ""); // 患者就诊卡号
						returnParam.put("payFor", param.containsKey("payFor") ? param.get("payFor").toString() : ""); // 支付类别
						logbean.setRemark(returnParam.get("payFor"));
						logbean.setAmount(returnParam.get("wx_money"));
						log.info("订单号："+orderId+" 接口---wxPublicPay---后台支付接口返回的信息是：" + returnParam.toString());
						isOK = true;
					} else {
						// 获取prepay_id(预支付交易会话标识)失败
						returnCode = ReturnCode.PREPAY_ID_FAIL;
						isOK = false;
					}
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			logbean.setResultCode(returnCode.getCode());
			logbean.setResultMsg(returnCode.getMsg());
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			paymentService.insertlog(logbean);
			if (isOK) {
				responseEntity.setContent(returnParam);
			} else {
				responseEntity.setContent("");
			}
		}
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "wxPublicPay", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 微信公众号支付异步通知
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/wxPublicNotify")
	public void wxPublicNotify(HttpServletRequest req, HttpServletResponse resp) {
		LogBean logbean = new LogBean();
		try {
			log.info("接口---wxPublicNotify---收到微信的回调信息");
			
			logbean.setInto("微信公众号支付回调");
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			resp.setHeader("Content-type", "text/html;charset=UTF-8");
			// 接收微信回调参数  - XML格式字符串
			String resString = XmlUtils.parseRequst(req);
			log.info("接口---wxPublicNotify---微信的回调信息是：" + resString);
			System.out.println("通知内容：" + resString);

			String respString = "FAIL";
			if (resString != null && !"".equals(resString)) {
				// 将XML格式字符串转换为 map
				Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
				logbean.setOrderId(map.get("out_trade_no"));
				log.info("接口---wxPublicNotify---微信的回调信息map格式是：" + map.toString());

				String return_code = map.get("return_code");
				if (return_code != null && WXPublicConstants.WX_PUBLIC_SUCCESS.equals(return_code)) {
					String result_code = map.get("result_code");
					String appid = map.get("appid");
					String mch_id = map.get("mch_id");
					if (WXPublicConstants.WX_PUBLIC_SUCCESS.equals(result_code) && WXPublicConstants.WX_PUBLIC_APPID.equals(appid)
							&& WXPublicConstants.WX_PUBLIC_MCH_ID.equals(mch_id)) {
						// 此处可以在添加相关处理业务，校验通知参数中的商户订单号out_trade_no和金额total_fee是否和商户业务系统的单号和金额是否一致，一致后方可更新数据库表中的记录。
						log.info("订单号："+map.get("out_trade_no")+" 接口---wxPublicNotify---开始进入回调的业务逻辑处理");
						// 更新支付状态
						String out_trade_no = map.get("out_trade_no");
						String trade_no = map.get("transaction_id");
						String amount = map.get("total_fee");
						// 处理多次回调多次调用业务代码问题
						List<TradingRecordBean> list = new ArrayList<TradingRecordBean>();
						list = paymentService.getOrderList(out_trade_no);
						log.info("订单号："+out_trade_no+" 接口---wxPublicNotify---list的大小是：" + list.size());
						log.info("订单号："+out_trade_no+"接口---wxPublicNotify---list的具体信息是：" + list.toString());
						log.info("订单号："+out_trade_no+"list.get(0).getStatus()是：" + list.get(0).getStatus());
						log.info("订单号："+out_trade_no+"list.get(0).getCancelregister()是：" + list.get(0).getCancelregister());
						if ("1".equals(list.get(0).getStatus()) && "1".equals(list.get(0).getCancelregister())) {
							// 只处理未支付且未退费的数据
							TradingRecordBean tradeOrder = new TradingRecordBean();
							tradeOrder.setOrderId(out_trade_no);
							tradeOrder.setTransactionId(trade_no);
							paymentService.wxPaySuccess(tradeOrder); // 修改订单状态
							// 循环调用3次，调用HIS将该业务设置为已支付，并且返回值保存在业务表
							boolean isSuccess = false;
							log.info("订单号："+out_trade_no+"----------开始进入callHisPaySuccess方法-------------");
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
							log.info("订单号："+out_trade_no+"------------执行callHisPaySuccess方法结束------------");
							// 根据订单号获取对应的操作者信息
							PublicUserBean userInfo = userInterfaceService.getPublicUserByOrderID(out_trade_no);
							Map<String, Object> orderDetail = null;
							if (isSuccess) {
								String payFor = paymentService.selectPayFor(out_trade_no);
								respString = "success";

								if (userInfo != null) {
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
										payfor = "门诊";
										Map<String, Object> param = paymentService.getToPay(out_trade_no);
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
								// 微信公众号退费接口
								boolean isOK = wxPaymentService.wxPublicRefundOrder(refundParam);
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
						respString = "FAIL";
					}
				}
			}
			log.info("接口---wxPublicNotify---后台回调接口返回的信息是：" + respString);
			resp.getWriter().write(respString);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			paymentService.updatelog(logbean);
		}
		
	}

	/**
	 * 生成订单
	 * @param param
	 * @return
	 */
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

		return tradeOrder;
	}

	/**
	 * 回调时, 业务逻辑处理
	 * 
	 * @param orderId
	 * @return
	 */
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
				log.info("订单号："+orderId+"--------当日挂号支付业务逻辑处理-------");
				// 当日挂号支付
				System.out.println("当日挂号支付---->enter！");
				param = paymentService.getRegisterPay(orderId);
				log.info("订单号："+orderId+"--------当日挂号支付业务逻辑处理-------返回参数是：" + param.toString());
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
				log.info("订单号："+orderId+"--------当日挂号支付调用HIS接口-------返回参数是：" + param.toString());
				res = appointmentService.registerPay(param);
				log.info("订单号："+orderId+"--------当日挂号支付调用HIS接口-------返回参数是：" + res.getContent().toString());
				System.out.println("当日挂号支付---->通知out！" + res.getContent().toString());
				header = res.getHeader();
				log.info("订单号："+orderId+"--------当日挂号支付调用HIS接口-------HIS接口返回码是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------当日挂号支付调用HIS接口-------HIS接口返回码是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------当日挂号调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				log.info("订单号："+orderId+"--------当日挂号调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updateRegisterPay(orderId, (Map<String, Object>) res.getContent());
				}
			} else if ("3".equals(order.getPayFor())) {
				log.info("订单号："+orderId+"--------预约挂号支付业务逻辑处理-------");
				System.out.println("HIS 入库---->");
				// 预约挂号支付
				param = paymentService.getSubscriptionToPay(orderId);
				log.info("订单号："+orderId+"--------预约挂号支付业务逻辑处理-------返回参数是：" + param.toString());
				String bespeakDate = param.get("bespeakDate").toString();

				param.put("bespeakDate", bespeakDate.substring(0, 10));
				// 支付流水号
				param.put("payNumber", orderId);
				// 支付方式 1.现金 2.支付宝 3.微信 4.银行卡
				String method = null;
				if (order.getPayType().equals("1") ) {
					method = "4";
				} else if (order.getPayType().equals("2") || order.getPayType().equals("4")) {
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
				log.info("订单号："+orderId+"--------预约挂号调用HIS接口-------入参是：" +  param.toString());
				res = appointmentService.addSubscriptionToPay(param);
				log.info("订单号："+orderId+"--------预约挂号调用HIS接口-------返回参数是：" + res.getContent().toString());
				System.out.println("res----->" + res.getContent().toString());
				header = res.getHeader();
				log.info("订单号："+orderId+"--------预约挂号调用HIS接口-------HIS接口返回码是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------预约挂号调用HIS接口-------HIS接口返回码是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------预约挂号调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				log.info("订单号："+orderId+"--------预约挂号调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updateSubscriptionToPay(orderId, (Map<String, Object>) res.getContent());
					log.info("订单号："+orderId+"-------预约挂号-----调用HIS接口成功");
				}
				log.info("订单号："+orderId+"HIS入库成功！");
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
				log.info("订单号："+orderId+"--------门诊缴费支付调用HIS接口-------返回参数是：" + param.toString());
				ResponseEntity res = new ResponseEntity();
				res = appointmentService.toPay(param);
				log.info("订单号："+orderId+"--------门诊缴费支付调用HIS接口-------返回参数是：" + res.getContent().toString());
				header = res.getHeader();
				log.info("订单号："+orderId+"--------门诊缴费支付调用HIS接口-------返回头部信息是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------门诊缴费支付调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updateToPay(orderId, (Map<String, Object>) res.getContent());
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
				log.info("订单号："+orderId+"--------住院预交支付调用HIS接口-------返回参数是：" + param.toString());
				ResponseEntity res = new ResponseEntity();
				res = appointmentService.prePayExpense(param);
				log.info("订单号："+orderId+"--------住院预交支付调用HIS接口-------返回参数是：" + res.getContent().toString());
				header = res.getHeader();
				log.info("订单号："+orderId+"--------住院预交支付调用HIS接口-------HIS接口返回码是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------住院预交支付调用HIS接口-------HIS接口返回码是：" + header.getResultCode());
				log.info("订单号："+orderId+"--------住院预交支付调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				log.info("订单号："+orderId+"--------住院预交支付调用HIS接口-------HIS接口返回码是：" + header.getResultMsg());
				if ("1".equals(header.getResultCode()) && header.getResultCode() == null) {
					// his处理业务成功
					isSuccess = true;
					paymentService.updatePrePayExpense(orderId, (Map<String, Object>) res.getContent());
					log.info("订单号："+orderId+"--------住院预交支付调用HIS接口成功-------");
				}
			}
		}
		return isSuccess;
	}

	/**
	 * 微信公众号退费接口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = { "/wxPublicRefund" })
	@ResponseBody
	public void refund(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int result = 0;
		String resultJson = "";
		String amount = request.getParameter("amount"); // 退款金额 (0.1元)
		String orderId = request.getParameter("orderId"); // 退款订单号

		// String amount = "0.01";
		// String orderId = "20170223101033wx1253";

		// 微信退费接口
		result = WXPublicPayment.wxRefundOrder(amount, orderId);
		if (result == 1) {
			System.out.println("退款成功！");
			resultJson = "{\"content\":{ " + "\"msg\": " + "\"" + "SUCCESS" + "\"" + "," + "\"code\": " + "\"" + "10000"
					+ "\"" + "}" + "}";
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().write(resultJson);
		} else {
			System.out.println("退款失败！");
			resultJson = "{\"content\":{ " + "\"msg\": " + "\"" + "FAIL" + "\"" + "," + "\"code\": " + "\"" + "99999"
					+ "\"" + "}" + "}";
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().write(resultJson);
		}
	}
}

package com.gq.business.payment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.TabbedPaneUI;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.appointment.controller.AppointmentController;
import com.gq.business.appointment.service.impl.AppointmentServiceImpl;
import com.gq.business.payment.mappers.PaymentMapper;
import com.gq.business.payment.model.LogBean;
import com.gq.business.payment.model.TradingRecordBean;
import com.gq.business.payment.service.IPaymentService;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;
import com.gq.common.utils.AdapterUtils;
import com.gq.common.utils.payment.ABCPayment;
import com.gq.common.utils.payment.CCB.CCBPayment;
import com.gq.common.utils.payment.wx.WxConstants;
import com.gq.common.utils.payment.wx.WxPayment;
import com.gq.config.ReturnCode;

@Service
public class PaymentServiceImpl implements IPaymentService {
	// 日志测试文件
	private static Logger log = Logger.getLogger(PaymentServiceImpl.class);
	private static final String GET_BILL_LIST = "getBillList";
	private static final String GET_BILL_DETAIL = "getBillDetail";
	private static final String CONFIRM_BILL = "confirmBill";
	@Autowired
	private PaymentMapper paymentMapper;
	
	@Override
	public ResponseEntity getPaymentList(Map<String, Object> param) {
		return AdapterUtils.modelSend(PaymentServiceImpl.GET_BILL_LIST, param);
	}

	@Override
	public ResponseEntity getPaymentDetail(Map<String, Object> param) {
		return AdapterUtils.modelSend(PaymentServiceImpl.GET_BILL_DETAIL, param);
	}

	@Override
	public ResponseEntity pay(Map<String, Object> param) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String payWithWeb(Map<String, Object> param) throws ServiceException {
		// 根据订单号获取订单详情
		ResponseEntity responseEntity = AdapterUtils.modelSend(PaymentServiceImpl.GET_BILL_DETAIL, param);
		List<Map<String, String>> items = null;
		if (!responseEntity.getHeader().getResultCode().equals(ReturnCode.NO_DATA.getCode())) {
			items = (List<Map<String, String>>) responseEntity.getContent();
		}
		// 参数设置
		param.put("OrderAmount", param.get("money"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date orderTime = null;
		try {
			orderTime = sdf.parse(param.get("billTime").toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf = new SimpleDateFormat("yyyy/MM/dd");
		param.put("OrderDate", sdf.format(orderTime));
		sdf = new SimpleDateFormat("HH:mm:ss");
		param.put("OrderTime", sdf.format(orderTime));
		param.put("OrderNo", param.get("orderId"));
		param.put("ResultNotifyURL", param.get("returnUrl"));
		// 进行支付
		ABCPayment abcPay = new ABCPayment();
		return abcPay.pay(param, items);
	}

	@Override
	public ResponseEntity confirmBill(Map<String, Object> param) {
		return AdapterUtils.modelSend(PaymentServiceImpl.CONFIRM_BILL, param);
	}

	@Override
	public String createOrder(TradingRecordBean trb, String gateway) throws ServiceException {
		String resp = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//订单创建时间
			trb.setCreateTime(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
			return null;
		}
		
		// 支付状态：1、待支付；2、已支付；
		trb.setStatus("1");
		//支付类型：1：建行支付 2:微信支付
		if("1".equals(trb.getPayType())) {
			//生成订单url
			resp = CCBPayment.generateOrder(trb, gateway);
			trb.setPaymentUrl(resp);
		}
		// 取消预约状态：1：没有取消 2：已经取消
		trb.setCancelregister("1");
		this.paymentMapper.insertRecord(trb);
		
		return resp;
	}
	
	@Override
	public String getOrder(TradingRecordBean trb) throws ServiceException {
		String resp = "";
		//获取订单
		resp = CCBPayment.getOrder(trb);
		trb.setPaymentUrl(resp);
		
		return resp;
	}
	
	@Override
	public void ccbPaySuccess(String orderId) {
		TradingRecordBean orderSearch = new TradingRecordBean();
		orderSearch.setOrderId(orderId);
		List<TradingRecordBean> abcOrderlist = paymentMapper.getOrdreList(orderSearch);
		if (null != abcOrderlist && !abcOrderlist.isEmpty()) {
			TradingRecordBean order = abcOrderlist.get(0);
			order.setStatus("2");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				order.setFinishDate(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			paymentMapper.updateCcbOrder(order);
		}
	}
	
	@Override
	public void wxPaySuccess(TradingRecordBean order) {
		//第三方订单号
		String transaction_id = order.getTransactionId();
		List<TradingRecordBean> wxOrderlist = paymentMapper.getOrdreList(order);
		if (null != wxOrderlist && !wxOrderlist.isEmpty()) {
			order = wxOrderlist.get(0);
			order.setTransactionId(transaction_id);
			order.setStatus("2");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				order.setFinishDate(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			paymentMapper.updateWxOrder(order);
			log.info("订单号："+transaction_id+" 已设置为已支付状态");
		}
	}
	
	@Override
	public List<TradingRecordBean> getOrderList(String orderId) {
		TradingRecordBean orderSearch = new TradingRecordBean();
		orderSearch.setOrderId(orderId);
		List<TradingRecordBean> list = paymentMapper.getOrdreList(orderSearch);
		return list;
	}
	
	@Override
	public void createHisOrder(Map<String, Object> param) {
		// 当前系统时间
		long createTime = System.currentTimeMillis();
		
		if(param.get("payFor").equals("1")) {
			//预约支付（窗口支付）
			param.put("createTime", createTime);
			param.put("lockStatus", "3");
			paymentMapper.insertSubscription(param);
		} else if (param.get("payFor").equals("2")) {
			//当日挂号支付
			
			//创建业务数据时系统时间
			param.put("createTime", createTime);
			//号源状态为已锁号
			param.put("lockStatus", "0");
			paymentMapper.insertRegisterPay(param);	
		} else if (param.get("payFor").equals("3")) {
			//预约挂号（支付)
			
			//创建业务数据时系统时间
			param.put("createTime", createTime);
			//号源状态为已锁号
			param.put("lockStatus", "0");
			paymentMapper.insertSubscriptionToPay(param);
		} else if (param.get("payFor").equals("4")) {
			//门诊缴费
			paymentMapper.insertToPay(param);
		} else if (param.get("payFor").equals("5")) {
			//住院预交
			paymentMapper.insertPrePayExpense(param);
		}
	}
	
	@Override
	public Map<String, Object> getRegisterPay(String orderId) {
		return paymentMapper.getRegisterPay(orderId);
	}
	
	@Override
	public Map<String, Object> getSubscriptionToPay(String orderId) {
		return paymentMapper.getSubscriptionToPay(orderId);
	}
	
	@Override
	public Map<String, Object> getToPay(String orderId) {
		return paymentMapper.getToPay(orderId);
	}
	
	@Override
	public Map<String, Object> getPrePayExpense(String orderId) {
		return paymentMapper.getPrePayExpense(orderId);
	}

	@Override
	public void updateSubscriptionToPay(String orderId, Map<String, Object> res) {
		res.put("orderId", orderId);
		paymentMapper.updateSubscriptionToPay(res);
		
	}

	@Override
	public void updatePrePayExpense(String orderId, Map<String, Object> content) {
		content.put("orderId", orderId);
		paymentMapper.updatePrePayExpense(content);
	}

	@Override
	public void updateToPay(String orderId, Map<String, Object> content) {
		content.put("orderId", orderId);
		paymentMapper.updateToPay(content);
	}

	@Override
	public void updateRegisterPay(String orderId, Map<String, Object> content) {
		content.put("orderId", orderId);
		paymentMapper.updateRegisterPay(content);
	}

	@Override
	public String getPayNumberInRegisterPay(String billNo) {
		return paymentMapper.getPayNumberInRegisterPay(billNo);
	}

	@Override
	public String getPayNumberInSubscriptionPay(String billNo) {
		return paymentMapper.getPayNumberInSubscriptionPay(billNo);
	}

	/**
	 * 更新订单表退费状态
	 * @param payNumber 订单号
	 * @param cancelType 1：支付 2：退费 3：冲正成功 4：冲正失败
	 */
	@Override
	public void updateCancelStatus(String payNumber, String cancelType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderId", payNumber);
		//1：支付 2：退费 3：冲正成功 4：冲正失败
		if("2".equals(cancelType)) {
			param.put("cancelregister", "2");
		} else if("3".equals(cancelType)) {
			param.put("cancelregister", "3");
		} else if("4".equals(cancelType)) {
			param.put("cancelregister", "4");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//预约取消时间
			param.put("cancelregisterdate" ,sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		paymentMapper.updateCancelStatus(param);
	}

	@Override
	public String getPayAmountByOrderId(String orderId) {
		return paymentMapper.getPayAmountByOrderId(orderId);
	}

	@Override
	public Map<String, Object> query(String orderId) {
		return paymentMapper.selectAll(orderId);
	}
	
	@Override
	public String selectPayFor(String orderId) {
		return paymentMapper.selectPayFor(orderId);
	}
	
	@Override
	public Map<String, Object> queryTodayList(String orderId) {
		return paymentMapper.selectTodayList(orderId);
	}
	
	@Override
	public Map<String, Object> queryOrder(String orderId) {
		return paymentMapper.selectOrder(orderId);
	}
	
	/**
	 * 退款，根据支付类型选择退款方式
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public boolean refundOrder(Map<String, Object> param) {
		int result;
		boolean isOk = false;
		
		//订单号
		String payNumber = param.get("payNumber").toString();
		//订单金额
		String amount = param.get("amount").toString();
		//支付类型：1.建行支付 2.微信支付
		String payType = paymentMapper.loadRecord(payNumber);
		log.info("订单号："+payNumber+"查询支付类型(1建行2微信)=====》"+payType);
		if(null != payType && !payType.equals("")) {
			if ("1".equals(payType)) {
				log.info("订单号："+payNumber+" 建行退费=====》");
				isOk = CCBPayment.sendToCCB(amount, payNumber);
				log.info("订单号："+payNumber+" 建行退费返回结果=====》isOk="+isOk);
			} else if ("2".equals(payType)) {
				log.info("订单号："+payNumber+" 微信退费使用未结算资金退费=====》");
				result = WxPayment.wxRefundOrder(amount, payNumber,WxConstants.REFUND_ACCOUNT_UNSETTLED);
				if(result == 2){
			    log.info("订单号："+payNumber+" 微信退费未结算资金不足,使用可用余额资金退费=====》");
				result = WxPayment.wxRefundOrder(amount, payNumber,WxConstants.REFUND_ACCOUNT_RECHARGE);
				}
				if (result == 1) {
					isOk = true;
				}
			}
		}
		return isOk;
	}

	/**
	 * 获取当日挂号已锁号列表
	 */
	@Override
	public List<Map<String, Object>> getRegisterPayList() {
		return paymentMapper.getRegisterPayList();
	}

	/**
	 * 获取预约挂号已锁号列表
	 */
	@Override
	public List<Map<String, Object>> getSubscriptionToPayList() {
		return paymentMapper.getSubscriptionToPayList();
	}
	
	/**
	 * 更改当日挂号已锁号状态
	 */
	@Override
	public void updateRegisterPayLockStatus(Map<String, Object> param) {
		paymentMapper.updateRegisterPayLockStatus(param);
	}
	
	/**
	 * 更改预约挂号已锁号状态
	 */
	@Override
	public void updateSubscriptionToPayLockStatus(Map<String, Object> param) {
		paymentMapper.updateSubscriptionToPayLockStatus(param);
	}
	/**
	 * 根据billNo查询预约日期
	 */
	@Override
	public Map<String, Object> queryDate(String billNo) {
		
		return paymentMapper.selectDate(billNo);
	}

	@Override
	public String queryPatientname(String cardNo,String userId) {
		return paymentMapper.selectPatientname(cardNo,userId);
	}

	@Override
	public void updateSubscriptionCancelregister(Map<String, Object> map) {
		paymentMapper.updateSubscriptionCancelregister(map);
		
	}

	/**
	 * 记录门诊明细
	 */
	@Override
	public void insertToPayDetail(Map<String, Object> content, String billNo) {
		//插入明细数据
		content.put("billNo",billNo);
		paymentMapper.insertToPayDetail(content);
	}

	@Override
	public String getPayType(String orderId) {
		String payType = paymentMapper.getPayType(orderId);
		return payType;
	}

	@Override
	public void insertlog(LogBean logbean) {
		// TODO Auto-generated method stub
		 paymentMapper.insertlog(logbean);
	}

	@Override
	public void updatelog(LogBean logbean) {
		// TODO Auto-generated method stub
		paymentMapper.updatelog(logbean);
	}
}

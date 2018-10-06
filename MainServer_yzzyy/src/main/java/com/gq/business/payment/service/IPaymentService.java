package com.gq.business.payment.service;

import java.util.List;
import java.util.Map;

import com.gq.business.payment.model.LogBean;
import com.gq.business.payment.model.TradingRecordBean;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;

public interface IPaymentService {

	ResponseEntity getPaymentList(Map<String, Object> param);

	ResponseEntity getPaymentDetail(Map<String, Object> param);

	ResponseEntity pay(Map<String, Object> param);

	String payWithWeb(Map<String, Object> param) throws ServiceException;

	ResponseEntity confirmBill(Map<String, Object> param);
	
	String createOrder(TradingRecordBean trade, String gateway) throws ServiceException;

	List<TradingRecordBean> getOrderList(String orderId);

	void ccbPaySuccess(String orderId);

	void createHisOrder(Map<String, Object> param);

	Map<String, Object> getRegisterPay(String order);

	Map<String, Object> getSubscriptionToPay(String order);

	Map<String, Object> getToPay(String order);

	Map<String, Object> getPrePayExpense(String order);

	String getOrder(TradingRecordBean trb) throws ServiceException;

	void updateSubscriptionToPay( String orderId, Map<String, Object> object);

	void updatePrePayExpense(String orderId, Map<String, Object> content);

	void updateToPay(String orderId, Map<String, Object> content);

	void updateRegisterPay(String orderId, Map<String, Object> content);

	String getPayNumberInRegisterPay(String billNo);

	String getPayNumberInSubscriptionPay(String billNo);

	void updateCancelStatus(String payNumber, String cancelType);

	String getPayAmountByOrderId(String orderId);

	void wxPaySuccess(TradingRecordBean tradeOrder);
	
	Map<String, Object> query(String orderId);
	
	String selectPayFor(String orderId);
	
	Map<String, Object> queryTodayList(String orderId);
	
	Map<String, Object> queryOrder(String orderId);
	
	boolean refundOrder(Map<String, Object> param);
	
	//获取当日挂号已锁号列表
	List<Map<String, Object>> getRegisterPayList();
	
	//获取预约挂号已锁号列表
	List<Map<String, Object>> getSubscriptionToPayList();

	/**
	 * 更改当日挂号已锁号状态
	 */
    void updateRegisterPayLockStatus(Map<String, Object> param);
    
    /**
	 * 更改预约挂号已锁号状态
	 */
    void updateSubscriptionToPayLockStatus(Map<String, Object> param);
    /**
	 * 根据billNo查询预约日期
	 */
    Map<String, Object> queryDate(String billNo);
    /**
	 * 根据cardNo查询就诊人姓名
	 */
    String queryPatientname(String cardNo,String userId);
    /**
	 * 更改预约挂号状态
	 */
    void updateSubscriptionCancelregister(Map<String, Object> map);
    /**
	 * 记录门诊明细
     * @param billNo 
	 */
	void insertToPayDetail(Map<String, Object> content, String billNo);

	String getPayType(String orderId);

	/**
	 * 把日志入库
	 * @param logbean
	 */
	void insertlog(LogBean logbean);

	/**
	 * 加回调数据
	 * @param logbean
	 */
	void updatelog(LogBean logbean);
}

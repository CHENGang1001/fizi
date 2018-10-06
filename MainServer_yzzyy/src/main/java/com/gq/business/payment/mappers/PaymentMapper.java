package com.gq.business.payment.mappers;

import java.util.List;
import java.util.Map;

import com.gq.business.payment.model.PriceListDetailBean;
import com.gq.business.payment.model.PriceListBean;
import com.gq.business.payment.model.LogBean;
import com.gq.business.payment.model.OrderBean;
import com.gq.business.payment.model.ReservationBean;
import com.gq.business.payment.model.TradingRecordBean;

public interface PaymentMapper {

	/**
	 * 获取支付列表
	 *
	 * @param param
	 * <br />
	 * @return <br />
	 */
	List<OrderBean> queryOrderListByCardNum(OrderBean order);

	/**
	 * 获取支付列表,分页
	 *
	 * @param param
	 * <br />
	 * @return <br />
	 */
	List<OrderBean> queryOrderListByCardNumAndPage(OrderBean order);

	/**
	 * 根据订单ID获取订单
	 *
	 * @param param
	 * <br />
	 * @return <br />
	 */
	OrderBean queryOrderByID(OrderBean order);

	/**
	 * 获取划价单详情列表
	 *
	 * @param param
	 * <br />
	 * @return <br />
	 */
	List<PriceListDetailBean> queryPriceListDetail(OrderBean order);

	/**
	 * 获取预约信息
	 *
	 * @param param
	 * <br />
	 * @return <br />
	 */
	ReservationBean queryReservationInfo(OrderBean order);

	/**
	 * 保存订单
	 *
	 * @param priceList
	 * <br />
	 * @return <br />
	 */
	int inserOrder(OrderBean order);

	/**
	 * 保存划价单
	 *
	 * @param preNos
	 * <br />
	 * @return <br />
	 */
	int insertPriceList(PriceListBean priceList);

	/**
	 * 检查划价单号在数据库中是否已经存在
	 *
	 * @param preNos
	 * <br />
	 * @return <br />
	 */
	int checkPreNoByPreNo(PriceListBean priceList);
	
	/**
	 * 插入建行支付数据
	 *
	 * @param order
	 * <br />
	 * @return TradingRecordBean
	 */
	int insertRecord(TradingRecordBean order);

	/**
	 * 查询建行支付数据
	 *
	 * @param orderSearch
	 * <br />
	 * @return TradingRecordBean
	 */
	List<TradingRecordBean> getOrdreList(TradingRecordBean orderSearch);

	/**
	 * 更新建行支付数据
	 *
	 * @param order
	 * <br />
	 * @return void
	 */
	void updateCcbOrder(TradingRecordBean order);

	/**
	 * 插入当日挂号支付数据
	 * 
	 * @param param
	 */
	void insertRegisterPay(Map<String, Object> param);
	
	/**
	 * 插入预约挂号支付数据
	 * 
	 * @param param
	 */
	void insertSubscriptionToPay(Map<String, Object> param);
	
	/**
	 * 插入门诊支付数据
	 * 
	 * @param param
	 */
	void insertToPay(Map<String, Object> param);
	
	/**
	 * 插入住院预交支付数据
	 * 
	 * @param param
	 */
	void insertPrePayExpense(Map<String, Object> param);
	
	/**
	 * 获取当日挂号支付数据
	 * 
	 * @param param Map<String, Object>
	 */
	Map<String, Object> getRegisterPay(String orderId);
	
	/**
	 * 获取预约挂号支付数据
	 * 
	 * @param param Map<String, Object>
	 */
	Map<String, Object> getSubscriptionToPay(String orderId);
	
	/**
	 * 获取门诊支付数据
	 * 
	 * @param param Map<String, Object>
	 */
	Map<String, Object> getToPay(String orderId);
	
	/**
	 * 获取住院预交支付数据
	 * 
	 * @param param Map<String, Object>
	 */
	Map<String, Object> getPrePayExpense(String orderId);

	void updateSubscriptionToPay(Map<String, Object> res);

	void updatePrePayExpense(Map<String, Object> content);

	void updateToPay(Map<String, Object> content);

	void updateRegisterPay(Map<String, Object> content);

	String getPayNumberInRegisterPay(String billNo);

	String getPayNumberInSubscriptionPay(String billNo);

	void updateCancelStatus(Map<String, Object> param);

	String getPayAmountByOrderId(String orderId);
	
    //根据oderId查询支付方式
    String loadRecord(String orderId);
    
    //根据订单号查询预约挂号订单情况
    Map<String, Object> selectAll(String orderId);
    
    String selectPayFor(String orderId);
    //根据订单号查询当日挂号订单情况
    Map<String, Object> selectTodayList(String orderId);
    
    //根据订单查询支付详情
    Map<String, Object> selectOrder(String orderId);

	/**
	 * 更新微信支付订单状态
	 * 
	 * @param order
	 */
	void updateWxOrder(TradingRecordBean order);
	
	 //根据oderId查询支付状态
    String getPaymentStatus(String orderId);
    
    /**
	 * 获取当日挂号已锁号列表
	 */
    List<Map<String, Object>> getRegisterPayList();
    
    /**
	 * 获取预约挂号已锁号列表
	 */
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
     * @param billNo
     * @return
     */
    Map<String, Object> selectDate(String billNo);
    /**
     * 根据cardNo查询就诊人姓名
     * @param cardNo
     * @return
     */
    String selectPatientname(String cardNo,String userId);
    /**
	 * 更改预约挂号状态
	 */
    void updateSubscriptionCancelregister(Map<String, Object> map);
    /**
	 * 插入预约挂号窗口支付数据
	 * 
	 * @param param
	 */
	void insertSubscription(Map<String, Object> param);

	/**
	 * 记录门诊明细
	 */
	void insertToPayDetail(Map<String, Object> content);

	String getPayType(String orderId);

	/**
	 * log日志存库
	 * @param logbean
	 */
	void insertlog(LogBean logbean);

	void updatelog(LogBean logbean);
}

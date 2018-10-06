package com.gq.business.appointment.service;

import java.util.Map;



import com.gq.business.appointment.model.StopScheduleBean;
import com.gq.common.response.ResponseEntity;

public interface IAppointmentService {
	
	public ResponseEntity getQueue(Map<String, Object> param);
	
	public ResponseEntity queryBillDetail(Map<String, Object> param);
	
	public ResponseEntity queryClinicList(Map<String, Object> param);
	
	public ResponseEntity queryFeeListClass(Map<String, Object> param);
	
	public ResponseEntity getPayDetail(Map<String, Object> param);
	
	public ResponseEntity getPaysum(Map<String, Object> param);
	
	public ResponseEntity peisItemSumReport(Map<String, Object> param);
	
	public ResponseEntity peisReportItemResult(Map<String, Object> param);
	
	public ResponseEntity peisReportItem(Map<String, Object> param);
	
	public ResponseEntity peisDepartmentReport(Map<String, Object> param);
	
	public ResponseEntity PeisTask(Map<String, Object> param);
	
	public ResponseEntity findReportResult(Map<String, Object> param);
	
	public ResponseEntity findReport(Map<String, Object> param);
	
	public ResponseEntity prePayExpense(Map<String, Object> param);
	
	public ResponseEntity queryFeeList(Map<String, Object> param);
	
	public ResponseEntity queryInhosFee(Map<String, Object> param);
	
	public ResponseEntity addPatientCard(Map<String, Object> param);
	
	public ResponseEntity addSubscriptionToPay(Map<String, Object> param);

	public ResponseEntity unaddSubscription(Map<String, Object> param);
	
	public ResponseEntity checkPatientCard(Map<String, Object> param);
	
	public ResponseEntity toPay(Map<String, Object> param);
	
	public ResponseEntity findBillItem(Map<String, Object> param);
	
	public ResponseEntity findBill(Map<String, Object> param);
	
	public ResponseEntity CreateBill(Map<String, Object> param);
	
	public ResponseEntity unregisterPay(Map<String, Object> param);
	
	public ResponseEntity registerPay(Map<String, Object> param);
	
	public ResponseEntity addSubscriptionPay(Map<String, Object> param);
	
	public ResponseEntity querySubscription(Map<String, Object> param);
	
	public ResponseEntity addSubscription(Map<String, Object> param);
	
	public ResponseEntity unlockRegisterNo(Map<String, Object> param);
	
	public ResponseEntity lockRegisterNo(Map<String, Object> param);
	
	public ResponseEntity getRegIndex(Map<String, Object> param);
	
	public ResponseEntity findDepartmentsDoctors(Map<String, Object> param);
	
	public ResponseEntity findAllDoctors(Map<String, Object> param);
	
	public ResponseEntity findAllDepartments(Map<String, Object> param);
	
	public ResponseEntity queryScheduleAll(Map<String, Object> param);
	
	public ResponseEntity queryScheduleDate(Map<String, Object> param);
	
	public ResponseEntity queryScheduleClass(Map<String, Object> param);

//	public ResponseEntity getDepartmentList(Map<String, Object> param);

	public ResponseEntity getAppointmentList(Map<String, Object> param);

	public ResponseEntity getRegisterList(Map<String, Object> param);

//	public ResponseEntity getRegIndex(Map<String, Object> param);

	public ResponseEntity getRegisterNumberList(Map<String, Object> param);

	public ResponseEntity appoint(Map<String, Object> param);

	public ResponseEntity register(Map<String, Object> param);

	public ResponseEntity getAppointmentRecordList(Map<String, Object> param);

	public ResponseEntity getAppointmentRecordDetail(Map<String, Object> param);

	public ResponseEntity cancelRegister(Map<String, Object> param);
	
	//停诊通知：获取his的接口的停诊信息
	public ResponseEntity querystopScheduleDate(Map<String, Object> param);

	//停诊通知：通过his接口获取的数据对数据库进行查询操作，并发短信
	public void selectStopSchedule(StopScheduleBean info);

	//停诊通知：查询窗口支付的预约患者，并发短信
	public void queryStopSchedule(StopScheduleBean info);

	//住院预交金支付记录
	public ResponseEntity queryPayExpenseList(Map<String, Object> param);

	
	//获取门诊号
	public ResponseEntity getOutPatient(Map<String, Object> orderDetail);


	/**
	 * 
	 * 查询我的医生信息
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	public ResponseEntity findPatientDoctors(Map<String, Object> param);
}

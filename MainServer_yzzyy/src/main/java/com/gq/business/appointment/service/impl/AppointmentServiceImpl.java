package com.gq.business.appointment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.appointment.controller.AppointmentController;
import com.gq.business.appointment.mappers.AppointmentMapper;
import com.gq.business.appointment.model.StopScheduleBean;
import com.gq.business.appointment.service.IAppointmentService;
import com.gq.common.response.ResponseEntity;
import com.gq.common.utils.AdapterUtils;
import com.gq.common.utils.SmsClient;
import com.gq.config.ReturnCode;

@Service
public class AppointmentServiceImpl implements IAppointmentService {
	
	@Autowired
	private AppointmentMapper appointmentMapper;

	// 大科室
	// private static final String DEPARTMENT_TYPE_BIG = "0";
	// 小科室
	// private static final String DEPARTMENT_TYPE_SMALL = "1";
//	private static final String GET_DEPARTMENT_LIST = "queryDeptList";
	private static final String GET_APPOINTMENT_LIST = "getAppointmentList";
	private static final String GET_REGISTER_LIST = "getRegisterList";
//	private static final String GET_APPOINTMENT_NUMBER_LIST = "getAppointmentNumberList";
	private static final String GET_REGISTER_NUMBER_LIST = "getRegisterNumberList";
	private static final String APPOINT = "appoint";
	private static final String REGISTER = "register";
	private static final String GET_APPOINTMENT_RECORD_LIST = "getAppointmentRecordList";
	private static final String GET_APPOINTMENT_RECORD_DETAIL = "getAppointmentRecordDetail";
	private static final String CANCEL_REGISTER = "cancelRegister";
	private static final String QueryScheduleClass = "queryScheduleClass";
	private static final String QueryScheduleDate = "queryScheduleDate";
	private static final String QueryScheduleAll = "queryScheduleAll";
	private static final String FindAllDepartments = "findAllDepartments";
	private static final String FindAllDoctors = "findAllDoctors";
	private static final String FindDepartmentsDoctors = "findDepartmentsDoctors";
	private static final String GetRegIndex = "getRegIndex";
	private static final String Lock_Register_No = "lockRegisterNo";
	private static final String ToPay = "toPay";
	private static final String UnlockRegisterNo = "unlockRegisterNo";
	private static final String AddSubscription = "addSubscription";
	private static final String QuerySubscription = "querySubscription";
	private static final String AddSubscriptionPay = "addSubscriptionPay";
	private static final String RegisterPay = "registerPay";
	private static final String UnregisterPay = "unregisterPay";
	private static final String FindBill = "findBill";
	private static final String CreateBill = "CreateBill";
	private static final String FindBillItem = "findBillItem";
	private static final String CheckPatientCard = "checkPatientCard";
	private static final String UnaddSubscription = "unaddSubscription";
	private static final String AddSubscriptionToPay = "addSubscriptionToPay";
	private static final String AddPatientCard = "addPatientCard";
	private static final String PrePayExpense = "prePayExpense";
	private static final String QueryFeeList = "queryFeeList";
	private static final String QueryInhosFee = "queryInhosFee";
	private static final String FindReport = "findReport";
	private static final String FindReportResult = "findReportResult";
	private static final String PeisTask = "PeisTask";
	private static final String PeisDepartmentReport = "peisDepartmentReport";
	private static final String PeisReportItem = "peisReportItem";
	private static final String PeisReportItemResult = "peisReportItemResult";
	private static final String PeisItemSumReport = "peisItemSumReport";
	private static final String GetPaysum  = "getPaysum";
	private static final String GetPayDetail = "getPayDetail";
	private static final String QueryFeeListClass = "queryFeeListClass";
	private static final String QueryClinicList = "queryClinicList";
	private static final String QueryBillDetail = "queryBillDetail";
	private static final String GetQueue = "getQueue";
	private static final String QuerystopScheduleDate = "querystopScheduleDate";
	private static final String QueryPayExpenseList = "queryPayExpenseList";


	private static final String GetOutPatient = "getOutPatient";

	private static final String findPatientDoctors = "findPatientDoctors";
	// 日志测试文件
	private static Logger log = Logger.getLogger(AppointmentController.class);
	
	
//	@Override
//	public ResponseEntity getDepartmentList(Map<String, Object> param) {
//		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_DEPARTMENT_LIST, param);
//	}
//	
	

	@Override
	public ResponseEntity getAppointmentList(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_APPOINTMENT_LIST, param);
	}

	@Override
	public ResponseEntity getRegisterList(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_REGISTER_LIST, param);
	}

//	@Override
//	public ResponseEntity getAppointmentNumberList(Map<String, Object> param) {
//		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_APPOINTMENT_NUMBER_LIST, param);
//	}

	@Override
	public ResponseEntity getRegisterNumberList(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_REGISTER_NUMBER_LIST, param);
	}

	@Override
	public ResponseEntity appoint(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.APPOINT, param);
	}

	@Override
	public ResponseEntity register(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.REGISTER, param);
	}

	@Override
	public ResponseEntity getAppointmentRecordList(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_APPOINTMENT_RECORD_LIST, param);
	}

	@Override
	public ResponseEntity queryClinicList(Map<String, Object> param) {	
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryClinicList, param);
	}

	@Override
	public ResponseEntity getAppointmentRecordDetail(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GET_APPOINTMENT_RECORD_DETAIL, param);
	}

	@Override
	public ResponseEntity cancelRegister(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.CANCEL_REGISTER, param);
	}
	
	@Override
	public ResponseEntity queryScheduleClass(Map<String, Object> param){
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryScheduleClass, param);
	}

	@Override
	public ResponseEntity queryScheduleDate(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryScheduleDate, param);
	}



	@Override
	public ResponseEntity findAllDepartments(Map<String, Object> param) {
		param.put("cooperationUnit", "01");
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindAllDepartments, param);
	}



	@Override
	public ResponseEntity findDepartmentsDoctors(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindDepartmentsDoctors, param);
	}



	@Override
	public ResponseEntity findAllDoctors(Map<String, Object> param) {
		param.put("cooperationUnit", "01");
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindAllDoctors, param);
	}



	@Override
	public ResponseEntity queryScheduleAll(Map<String, Object> param) {
		param.put("cooperationUnit", "01");
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryScheduleAll, param);
	}



	@Override
	public ResponseEntity lockRegisterNo(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.Lock_Register_No, param);
	}



	@Override
	public ResponseEntity getRegIndex(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GetRegIndex, param);
	}



	@Override
	public ResponseEntity checkPatientCard(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.CheckPatientCard, param);
	}



	@Override
	public ResponseEntity findBillItem(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindBillItem, param);
	}



	@Override
	public ResponseEntity findBill(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindBill, param);
	}



	@Override
	public ResponseEntity unregisterPay(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.UnregisterPay, param);
	}



	@Override
	public ResponseEntity registerPay(Map<String, Object> param) {
		
		String PNumber=UUID.randomUUID().toString();
		param.put("PayNumber", PNumber);
		
		return AdapterUtils.modelSend(AppointmentServiceImpl.RegisterPay, param);
	}



	@Override
	public ResponseEntity addSubscriptionPay(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.AddSubscriptionPay, param);
	}



	@Override
	public ResponseEntity querySubscription(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QuerySubscription, param);
	}



	@Override
	public ResponseEntity addSubscription(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.AddSubscription, param);
	}



	@Override
	public ResponseEntity unlockRegisterNo(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.UnlockRegisterNo, param);
	}



	@Override
	public ResponseEntity toPay(Map<String, Object> param) {		
		return AdapterUtils.modelSend(AppointmentServiceImpl.ToPay, param);
	}

	@Override
	public ResponseEntity unaddSubscription(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.UnaddSubscription, param);
	}

	@Override
	public ResponseEntity addSubscriptionToPay(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.AddSubscriptionToPay, param);
	}

	@Override
	public ResponseEntity getPayDetail(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GetPayDetail, param);
	}

	@Override
	public ResponseEntity getPaysum(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GetPaysum, param);
	}

	@Override
	public ResponseEntity peisItemSumReport(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.PeisItemSumReport, param);
	}

	@Override
	public ResponseEntity peisReportItemResult(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.PeisReportItemResult, param);
	}

	@Override
	public ResponseEntity peisReportItem(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.PeisReportItem, param);
	}

	@Override
	public ResponseEntity peisDepartmentReport(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.PeisDepartmentReport, param);
	}

	@Override
	public ResponseEntity PeisTask(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.PeisTask, param);
	}

	@Override
	public ResponseEntity findReportResult(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindReportResult, param);
	}

	@Override
	public ResponseEntity findReport(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.FindReport, param);
	}

	@Override
	public ResponseEntity prePayExpense(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.PrePayExpense, param);
	}

	@Override
	public ResponseEntity queryFeeList(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryFeeList, param);
	}

	@Override
	public ResponseEntity queryInhosFee(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryInhosFee, param);
	}

	@Override
	public ResponseEntity addPatientCard(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.AddPatientCard, param);
	}

	@Override
	public ResponseEntity CreateBill(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.CreateBill, param);
	}

	@Override
	public ResponseEntity queryFeeListClass(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryFeeListClass, param);
	}

	@Override
	public ResponseEntity queryBillDetail(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryBillDetail, param);
	}
	
	@Override
	public ResponseEntity querystopScheduleDate(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QuerystopScheduleDate, param);
	}
	
	@Override
	public ResponseEntity getQueue(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GetQueue, param);
	}
	
	/**
	 * 停诊通知
	 * 
	 * 查询已支付的数据，并发送通知短信
	 * */
	@Override
	public void selectStopSchedule(StopScheduleBean info) {
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<StopScheduleBean> list = new ArrayList<StopScheduleBean>() ;
				//根据停诊信息，查出被停诊患者的相关数据。主要查出数据库中t_payment_order.createrId（即等于是userid）
		list = appointmentMapper.selectStopSchedule(info);
		log.info("(已支付)需要通知的患者list=====》:"+ list.toString() );
				//遍历被停诊的患者信息
		for(int i=0;i<list.size();i++){
				//获取createrId（即等于是userid）信息
			String createrId = list.get(i).getCreaterId();
				//创建新的对象
			StopScheduleBean in = new StopScheduleBean();
				//把createrId放入，作为查询条件
			in.setCreaterId(createrId);
			List<StopScheduleBean> num = new ArrayList<StopScheduleBean>();
				//查询出账户操作者的手机号码
			num = appointmentMapper.selectNum(in);
				//判断号码是否为空
			if(num.size()>0){
				//获取num中的手机号码
				String phonenum = num.get(0).getMsisdn();
				//获取list中的患者名字
				String patientName = list.get(i).getName();
				//获取list中的医生名字
				String doctorName = list.get(i).getDoctorName();
				//获取原本预约的时间（年月日时分秒）
				String bespeakDate = list.get(i).getBespeakDate();
				//把字符串中的年月日截取出来
				String date = bespeakDate.substring(0,10);
				//发送停诊通知短信
				SmsClient.sendStopScheduleSms(phonenum,patientName,date, doctorName);
				log.info("发送停诊通知短信=====》phonenum:" +phonenum+",patientName:"+patientName+",date:"+date+",doctorName:"+doctorName );
			}
		}
	}

	/**
	 * 停诊通知
	 * 
	 * 查询窗口支付的数据，并发送通知短信
	 * */
	@Override
	public void queryStopSchedule(StopScheduleBean info) {
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<StopScheduleBean> list = new ArrayList<StopScheduleBean>();
				//根据停诊信息，查出被停诊患者的相关数据。主要查出数据库中t_payment_order.createrId（即等于是userid）
		list = appointmentMapper.queryStopSchedule(info);
		log.info("(窗口支付)需要通知的患者list=====》:"+ list.toString() );
				//遍历被停诊的患者信息
		for(int i=0;i<list.size();i++){
				//获取createrId（即等于是userid）信息
			String createrId = list.get(i).getCreaterId();
				//创建新的对象
			StopScheduleBean in = new StopScheduleBean();
				//把createrId放入，作为查询条件
			in.setCreaterId(createrId);
			List<StopScheduleBean> num = new ArrayList<StopScheduleBean>();
				//查询出账户操作者的手机号码
			num = appointmentMapper.selectNum(in);
				//判断号码是否为空
			if(num.size()>0){
				//获取num中的手机号码
				String phonenum = num.get(0).getMsisdn();
				//获取list中的患者名字
				String patientName = list.get(i).getName();
				//获取list中的医生名字
				String doctorName = list.get(i).getDoctorName();
				//获取原本预约的时间（年月日时分秒）
				String bespeakDate = list.get(i).getBespeakDate();
				//把字符串中的年月日截取出来
				String date = bespeakDate.substring(0,10);
				//发送停诊通知短信
				SmsClient.sendStopScheduleSms2(phonenum,patientName,date, doctorName);
				log.info("发送停诊通知短信=====》phonenum:" +phonenum+",patientName:"+patientName+",date:"+date+",doctorName:"+doctorName );
			}
		}
		
	}
	/**
	 * 住院预交金支付记录
	 * */
	@Override
	public ResponseEntity queryPayExpenseList(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.QueryPayExpenseList, param);
	}

	/**
	 * 根据单据号获取门诊号
	 */
	@Override
	public ResponseEntity getOutPatient(Map<String, Object> orderDetail) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.GetOutPatient, orderDetail);
	}


	@Override
	public ResponseEntity findPatientDoctors(Map<String, Object> param) {
		return AdapterUtils.modelSend(AppointmentServiceImpl.findPatientDoctors,param);
	}
	

	
}

package com.gq.business.appointment.controller;

import java.nio.channels.ScatteringByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.impl.client.TunnelRefusedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.base.BaseController;
import com.gq.business.accountmanage.model.PublicUserBean;
import com.gq.business.appointment.model.StopScheduleBean;
import com.gq.business.appointment.service.IAppointmentService;
import com.gq.business.payment.controller.PaymentController;
import com.gq.business.payment.mappers.PaymentMapper;
import com.gq.business.payment.model.TradingRecordBean;
import com.gq.business.payment.service.IPaymentService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.SmsClient;
import com.gq.common.utils.payment.CCB.CCBPayment;
import com.gq.config.ReturnCode;

@Controller
@RequestMapping(value = "/doctorAppointment")
@Component("doctorAppointmentController")
public class DoctorAppointmentController extends BaseController {
	// 日志测试文件
	private static Logger log = Logger.getLogger(DoctorAppointmentController.class);
	@Autowired
	private IAppointmentService appointmentService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private PaymentMapper paymentMapper;
	
	
	/**
	 * 
	 * 获取体检记录列表（测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/PeisTaskNew")
	@ResponseBody
	public ResponseEntity PeisTaskNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.PeisTask(param);
	}
	
	/**
	 * 
	 *  获取体检结论（测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisItemSumReportNew")
	@ResponseBody
	public ResponseEntity peisItemSumReportNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisItemSumReport(param);
	}
	
	
	/**
	 * 
	 *  总检结论及建议（测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisReportItemNew")
	@ResponseBody
	public ResponseEntity peisReportItemNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisReportItem(param);
	}
	
	/**
	 * 
	 *   获取项目报告（测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/peisReportItemResultNew")
	@ResponseBody
	public ResponseEntity peisReportItemResultNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.peisReportItemResult(param);
	}

	
	/**
	 * 
	 *  检验检查报告列表（测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findReportNew")
	@ResponseBody
	public ResponseEntity findReportNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findReport(param);
	}
	
	/**
	 * 
	 * 检验检查报告 （测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/findReportResultNew")
	@ResponseBody
	public ResponseEntity findReportResultNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.findReportResult(param);
	}
	
	/**
	 * 
	 * 卡信息校验（测试）
	 * 
	 * @param requestBody
	 * @return
	 * 
	 */
	@RequestMapping(value = "/checkPatientCardNew")
	@ResponseBody
	public ResponseEntity checkPatientCardNew(@RequestBody RequestEntity requestBody) {
		Map<String, Object> param = requestBody.getContent();
		return appointmentService.checkPatientCard(param);
	}
	
	

	
	
}

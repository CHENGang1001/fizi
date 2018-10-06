package com.gq.business.appointment.timerTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gq.business.appointment.service.IAppointmentService;
import com.gq.business.payment.service.IPaymentService;
import com.gq.common.log.LogManager;
import com.gq.common.response.ResponseEntity;

/**
 * 每3分钟运行一次,所有号源会在5-6分钟内释放
 */
@Component("UnlockRegisterNo")
public class UnlockRegisterNo {

	private static Logger logger = LogManager.getDebugLog();
	//当日挂号类型
	private static final String RegisterPay = "1";
	//预约挂号类型
	private static final String SubscriptionToPay = "2";
	
	@Autowired
	private IAppointmentService appointmentService;
	@Autowired
	private IPaymentService paymentService;

	public void execute() {
		// 当前系统时间
		long currentTime = System.currentTimeMillis();
		// 释放当日挂号已锁号源
		unlockRegisterPayList(currentTime);
		// 释放预约挂号已锁号源
		unlockSubscriptionToPayList(currentTime);
	}

	private void unlockRegisterPayList(long currentTime) {
		// 获取当日挂号已锁号列表
		List<Map<String, Object>> list = paymentService.getRegisterPayList();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				//判断当前挂号信息是否过时
				if(checkIsOutDate(map, currentTime, RegisterPay)) {
					continue;
				}
				if (map.get("createTime") == null) {
					continue;
				}
				// 创建业务订单系统时间
				String createTime = map.get("createTime").toString();
				// 如果订单超过五分钟未支付
				if ((currentTime - Long.valueOf(createTime)) >= (5 * 60 * 1000)) {
					// 通知his释放号源
					ResponseEntity res = new ResponseEntity();
					res = callHisUnlockRegisterNo(map);
					// 如果释放号源成功，则更新数据库状态
					Map<String, Object> tmp = new HashMap<String, Object>();
					if ("1".equals(res.getHeader().getResultCode()) 
							&& res.getHeader().getResultCode() == null) {
						//已释放
						tmp.put("lockStatus", "1");
					} else {
						logger.debug("UnlockRegisterNo :", "code-" + res.getHeader().getResultCode() + "msg-" + res.getHeader().getResultMsg());
						//释放失败
						tmp.put("lockStatus", "4");
					}
					//订单号
					tmp.put("orderId", map.get("orderId"));
					paymentService.updateRegisterPayLockStatus(tmp);
				}
			}
		}
	}

	private void unlockSubscriptionToPayList(long currentTime) {
		// 获取预约挂号已锁号列表
		List<Map<String, Object>> list = paymentService.getSubscriptionToPayList();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				//判断当前挂号信息是否过时
				if(checkIsOutDate(map, currentTime, SubscriptionToPay)) {
					continue;
				}
				if (map.get("createTime") == null) {
					continue;
				}
				// 创建业务订单系统时间
				String createTime = map.get("createTime").toString();
				// 如果订单超过五分钟未支付
				if ((currentTime - Long.valueOf(createTime)) >= (5 * 60 * 1000)) {
					// 通知his释放号源
					ResponseEntity res = new ResponseEntity();
					res = callHisUnlockRegisterNo(map);
					// 如果释放号源成功，则更新数据库状态
					Map<String, Object> tmp = new HashMap<String, Object>();
					if ("1".equals(res.getHeader().getResultCode()) 
							&& res.getHeader().getResultCode() == null) {
						//号源状态设置为已释放
						tmp.put("lockStatus", "1");
					} else {
						//号源状态设置为释放失败
						tmp.put("lockStatus", "4");
						logger.debug("UnlockRegisterNo :", "code-" + res.getHeader().getResultCode() + "msg-" + res.getHeader().getResultMsg());
					}
					//订单号
					tmp.put("orderId", map.get("orderId"));
					paymentService.updateSubscriptionToPayLockStatus(tmp);
				}
			}
		}
	}

	/**
	 * 通知his释放号源
	 * 
	 * @param param
	 */
	private ResponseEntity callHisUnlockRegisterNo(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cardType", param.get("cardType"));
		map.put("cardNo", param.get("cardNo"));
		if (param.get("passWord") != null)
			map.put("password", param.get("passWord"));
		else
			map.put("password", "");
		map.put("serialNumber", param.get("serialNumber"));

		return appointmentService.unlockRegisterNo(map);
	}
	
	/**
	 * 判断当前系统时间是否过时，如果过时，更改数据库状态
	 * @param map
	 * @param currentTime
	 * @param type
	 * @return 当前挂号信息是否过时 true-过时，false-不过时
	 */
	private boolean checkIsOutDate(Map<String, Object> map, long currentTime, String type) {
		boolean isOutDate = false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String nowTime = sdf.format(new Date());
		//取当前系统日期
		int date = Integer.valueOf(nowTime.substring(0, 8));
		//取当前系统时分
		int time = Integer.valueOf(nowTime.substring(8));
		
		String serialNum = map.get("serialNumber").toString();
		String beginTime = map.get("beginTime").toString();
		//系统记录预约日期
		int regDate = Integer.valueOf(serialNum.substring(0, serialNum.indexOf("_")).replace("-", ""));
		//系统记录预约时间
		int regTime = Integer.valueOf(beginTime.replace(":", ""));
		if(date > regDate || (date == regDate && time > regTime)) {
			isOutDate = true;
			Map<String, Object> tmp = new HashMap<String, Object>();
			//号源过时
			tmp.put("lockStatOus", "2");
			//订单号
			tmp.put("orderId", map.get("orderId"));
			
			if(type.equals(RegisterPay)) {
				paymentService.updateRegisterPayLockStatus(tmp);
			} else if (type.equals(SubscriptionToPay)) {
				paymentService.updateSubscriptionToPayLockStatus(tmp);
			}
		}
		
		return isOutDate;
	}
}

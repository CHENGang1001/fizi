package com.gq.business.appointment.mappers;

import java.util.List;

import com.gq.business.appointment.model.StopScheduleBean;

public interface AppointmentMapper {

	List<StopScheduleBean> selectStopSchedule(StopScheduleBean info);

	List<StopScheduleBean> selectNum(StopScheduleBean in);

	List<StopScheduleBean> queryStopSchedule(StopScheduleBean info);


}

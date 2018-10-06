package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetHospitalNewsList;
import com.gq.business.inforesources.model.HealthCareBean;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;

public interface ManageHealthCareService {

	public void addHealthCare(HealthCareBean careInfo) throws ServiceException;

	//获取养身保健的所有信息列表
	public ResponseEntity selectHealthCare() throws ServiceException;

	//根据id查询单条详情
	public ResponseEntity selectByIdHealthCare(String careId);

	//根据id修改
	public void editorHealthCare(HealthCareBean careInfo);

	//删除
	public void deleteHealthCare(String careId);

}

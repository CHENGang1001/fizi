package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.HealthCareBean;
import com.gq.business.inforesources.model.HospitalNewsBean;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;

public interface ManageHospitalNewsService {
	public void addHospitalNews(HospitalNewsBean newsInfo) throws ServiceException;

	public void deleteHospitalNews(String newsId) throws ServiceException;

	public void editorHospitalNews(HospitalNewsBean newsInfo) throws ServiceException;

	//查询所有新闻资讯放到
	public ResponseEntity selectHospitalNews() throws ServiceException;

	//根据id查询单条新闻资讯放到list
	public ResponseEntity selectByIdHospitalNews(String newsId) throws ServiceException;

}

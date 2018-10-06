package com.gq.business.inforesources.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.ManageHospitalNewsMapper;
import com.gq.business.inforesources.model.GetHospitalNewsList;
import com.gq.business.inforesources.model.HealthCareBean;
import com.gq.business.inforesources.model.HospitalNewsBean;
import com.gq.business.inforesources.model.NewsListBean;
import com.gq.business.inforesources.service.ManageHospitalNewsService;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;
import com.gq.config.ReturnCode;

@Service
public class ManageHospitalNewsServiceImpl implements ManageHospitalNewsService {

	@Autowired
	private ManageHospitalNewsMapper manageHospitalNewsMapper;
	
	@Override
	public void addHospitalNews(HospitalNewsBean newsInfo) throws ServiceException {
		manageHospitalNewsMapper.addHospitalNews(newsInfo);
		
	}

	@Override
	public void deleteHospitalNews(String newsId) throws ServiceException {
		manageHospitalNewsMapper.deleteHospitalNews(newsId);
	}

	@Override
	public void editorHospitalNews(HospitalNewsBean newsInfo) throws ServiceException {
		manageHospitalNewsMapper.editorHospitalNews(newsInfo);
	}

	@Override
	public ResponseEntity selectHospitalNews() throws ServiceException {
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<HospitalNewsBean> list = null;
		
		try{
			list = manageHospitalNewsMapper.selectHospitalNews();
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity selectByIdHospitalNews(String newsId) throws ServiceException{
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<HospitalNewsBean> list = null;
		
		try{
			list = manageHospitalNewsMapper.selectByIdHospitalNews(newsId);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

}

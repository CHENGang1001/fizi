package com.gq.business.inforesources.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.ManageHealthCareMapper;
import com.gq.business.inforesources.model.GetHospitalNewsList;
import com.gq.business.inforesources.model.HealthCareBean;
import com.gq.business.inforesources.model.HospitalNewsBean;
import com.gq.business.inforesources.service.ManageHealthCareService;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;
import com.gq.config.ReturnCode;

@Service
public class ManageHealthCareServiceImpl implements ManageHealthCareService{

	@Autowired
	private ManageHealthCareMapper manageHealthCareMapper;
	//新增
	@Override
	public void addHealthCare(HealthCareBean careInfo) throws ServiceException {
		manageHealthCareMapper.addHealthCare(careInfo);
	}
	
	//修改
	@Override
	public void editorHealthCare(HealthCareBean careInfo) {
		manageHealthCareMapper.editorHealthCare(careInfo);
		
	}
	//删除
	@Override
	public void deleteHealthCare(String careId) {
		manageHealthCareMapper.deleteHealthCare(careId);
		
	}

	//获取养生保健的数据信息
	@Override
	public ResponseEntity selectHealthCare() throws ServiceException {
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<HealthCareBean> list = null;
		
		try{
			list = manageHealthCareMapper.selectHealthCare();
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}
	
	public ResponseEntity selectByIdHealthCare(String careId) {
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<HealthCareBean> list = null;
		
		try{
			list = manageHealthCareMapper.selectByIdHealthCare(careId);
		}
		catch(EmptyResultDataAccessException e){
			try {
				throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		res.setContent(list);
		return res;
	}

}

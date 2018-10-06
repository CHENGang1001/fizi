package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetEncyDiseaseListMapper;
import com.gq.business.inforesources.model.GetEncyDiseaseDetail;
import com.gq.business.inforesources.model.GetEncyDiseaseList;
import com.gq.business.inforesources.service.GetEncyDiseaseListService;

@Service
public class GetEncyDiseaseListServiceImpl implements GetEncyDiseaseListService{

	@Autowired
	private GetEncyDiseaseListMapper encyDiseaseListMapper;
	
	@Override
	public List<GetEncyDiseaseList> getEncyDiseaseList(String diseasecategoryid) {
		// TODO Auto-generated method stub
		return this.encyDiseaseListMapper.getEncyDiseaseList(diseasecategoryid);
	}

	@Override
	public List<GetEncyDiseaseDetail> getEncyDiseaseDetail(String diseaseid) {
		// TODO Auto-generated method stub
		return this.encyDiseaseListMapper.getEncyDiseaseDetail(diseaseid);
	}
}

package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetDiseaseListMapper;
import com.gq.business.inforesources.model.GetDiseaseList;
import com.gq.business.inforesources.model.GetOrganDiseaseDetail;
import com.gq.business.inforesources.service.GetDiseaseListService;

@Service
public class GetDiseaseListServiceImpl implements GetDiseaseListService{
	@Autowired
	private GetDiseaseListMapper diseaseListMapper;

	@Override
	public List<GetDiseaseList> getDiseaseList(String organid) {
		// TODO Auto-generated method stub
		return this.diseaseListMapper.getDiseaseList(organid);
	}

	@Override
	public List<GetOrganDiseaseDetail> getDiseaseDetail(String diseaseid) {
		// TODO Auto-generated method stub
		return this.diseaseListMapper.getDiseaseDetail(diseaseid);
	}
	
}

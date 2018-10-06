package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetDiseaseDetailMapper;
import com.gq.business.inforesources.model.GetOrganDiseaseDetail;
import com.gq.business.inforesources.service.GetDiseaseDetailService;

@Service
public class GetDiseaseDetailServiceImpl implements GetDiseaseDetailService{
	
	@Autowired
	private GetDiseaseDetailMapper diseaseDetailMapper;

	@Override
	public List<GetOrganDiseaseDetail> getDiseaseDetail(String diseaseid) {
		// TODO Auto-generated method stub
		return this.diseaseDetailMapper.getDiseaseDetail(diseaseid);
	}
	
}

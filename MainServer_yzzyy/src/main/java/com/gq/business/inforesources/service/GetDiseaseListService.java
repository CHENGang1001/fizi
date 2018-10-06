package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetDiseaseList;
import com.gq.business.inforesources.model.GetOrganDiseaseDetail;

public interface GetDiseaseListService {
	List<GetDiseaseList> getDiseaseList(String organid);
	
	List<GetOrganDiseaseDetail> getDiseaseDetail(String diseaseid);
}

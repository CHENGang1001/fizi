package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetEncyDiseaseDetail;
import com.gq.business.inforesources.model.GetEncyDiseaseList;

public interface GetEncyDiseaseListService {
	List<GetEncyDiseaseList> getEncyDiseaseList(String diseasecategoryid);
	
	List<GetEncyDiseaseDetail> getEncyDiseaseDetail(String diseaseid);
}

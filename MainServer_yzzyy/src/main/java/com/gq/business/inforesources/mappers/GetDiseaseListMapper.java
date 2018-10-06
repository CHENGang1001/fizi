package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetDiseaseList;
import com.gq.business.inforesources.model.GetOrganDiseaseDetail;

public interface GetDiseaseListMapper {
	List<GetDiseaseList> getDiseaseList(String organid);
	
	List<GetOrganDiseaseDetail> getDiseaseDetail(String diseaseid);
}

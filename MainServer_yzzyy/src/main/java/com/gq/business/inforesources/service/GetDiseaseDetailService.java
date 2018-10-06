package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetOrganDiseaseDetail;

public interface GetDiseaseDetailService {
	List<GetOrganDiseaseDetail> getDiseaseDetail(String diseaseid);
}

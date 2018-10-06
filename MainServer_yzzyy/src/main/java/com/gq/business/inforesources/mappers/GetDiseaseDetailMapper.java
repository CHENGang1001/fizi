package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetOrganDiseaseDetail;

public interface GetDiseaseDetailMapper {
	List<GetOrganDiseaseDetail> getDiseaseDetail(String diseaseid);
}

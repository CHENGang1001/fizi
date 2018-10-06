package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetHospitalNewsList;
import com.gq.business.inforesources.model.HealthCareBean;

public interface ManageHealthCareMapper {

	public void addHealthCare(HealthCareBean careInfo);

	public List<HealthCareBean> selectHealthCare();

	public List<HealthCareBean> selectByIdHealthCare(String careId);

	public void editorHealthCare(HealthCareBean careInfo);

	public void deleteHealthCare(String careId);

	public List<GetHospitalNewsList> getHealthCareDetail(String careId);


}

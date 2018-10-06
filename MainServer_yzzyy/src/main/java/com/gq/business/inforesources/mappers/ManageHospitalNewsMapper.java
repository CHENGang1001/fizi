package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetHospitalNewsList;
import com.gq.business.inforesources.model.HospitalNewsBean;
import com.gq.common.response.ResponseEntity;

public interface ManageHospitalNewsMapper {

	public void addHospitalNews(HospitalNewsBean newsInfo);

	public void deleteHospitalNews(String newsId);

	public void editorHospitalNews(HospitalNewsBean newsInfo);

	//查询所有新闻资讯
	public List<HospitalNewsBean> selectHospitalNews();

	//根据id查询单条新闻
	public List<HospitalNewsBean> selectByIdHospitalNews(String newsId);

}

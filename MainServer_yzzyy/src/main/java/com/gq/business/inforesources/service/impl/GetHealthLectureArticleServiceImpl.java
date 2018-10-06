package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetHealthLectureArticleMapper;
import com.gq.business.inforesources.model.GetHealthLectureArticleDetail;
import com.gq.business.inforesources.model.GetHealthLectureArticleList;
import com.gq.business.inforesources.service.GetHealthLectureArticleService;

@Service
public class GetHealthLectureArticleServiceImpl implements GetHealthLectureArticleService{
	
	@Autowired
	private GetHealthLectureArticleMapper getHealthLectureArticleMapper;

	@Override
	public List<GetHealthLectureArticleList> getHealthLectureArticleList(
			String categoryId) {
		// TODO Auto-generated method stub
		return this.getHealthLectureArticleMapper.getHealthLectureArticleList(categoryId);
	}

	@Override
	public List<GetHealthLectureArticleDetail> getHealthLectureArticleDetail(
			String articleId) {
		// TODO Auto-generated method stub
		return this.getHealthLectureArticleMapper.getHealthLectureArticleDetail(articleId);
	}
	

}

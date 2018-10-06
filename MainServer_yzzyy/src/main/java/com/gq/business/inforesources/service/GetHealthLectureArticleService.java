package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetHealthLectureArticleDetail;
import com.gq.business.inforesources.model.GetHealthLectureArticleList;

public interface GetHealthLectureArticleService {
	List<GetHealthLectureArticleList> getHealthLectureArticleList(String categoryId);
	
	List<GetHealthLectureArticleDetail> getHealthLectureArticleDetail(String articleId);
}

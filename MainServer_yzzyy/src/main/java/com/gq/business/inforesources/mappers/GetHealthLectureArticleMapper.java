package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetHealthLectureArticleDetail;
import com.gq.business.inforesources.model.GetHealthLectureArticleList;

public interface GetHealthLectureArticleMapper {
	List<GetHealthLectureArticleList> getHealthLectureArticleList(String categoryId);
	
	List<GetHealthLectureArticleDetail> getHealthLectureArticleDetail(String articleId);
}

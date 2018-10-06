package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetHealthLectureCategoryListMapper;
import com.gq.business.inforesources.model.GetHealthLectureCategoryList;
import com.gq.business.inforesources.service.GetHealthLectureCategoryListService;

@Service
public class GetHealthLectureCategoryListServiceImpl implements GetHealthLectureCategoryListService{
	@Autowired
	private GetHealthLectureCategoryListMapper getHealthLectureCategoryListMapper;

	@Override
	public List<GetHealthLectureCategoryList> getHealthLectureCategoryList() {
		// TODO Auto-generated method stub
		return this.getHealthLectureCategoryListMapper.getHealthLectureCategoryList();
	}
}

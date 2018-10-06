package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetDiseaseCatagoryListMapper;
import com.gq.business.inforesources.model.GetDiseaseCatagoryList;
import com.gq.business.inforesources.service.GetDiseaseCategoryListService;

@Service
public class GetDiseaseCatagoryListServiceImpl implements GetDiseaseCategoryListService{
	
	@Autowired
	private GetDiseaseCatagoryListMapper diseaseCatagoryList;
	
	@Override
	public List<GetDiseaseCatagoryList> getDiseaseCategoryList() {
		// TODO Auto-generated method stub
		return this.diseaseCatagoryList.getDiseaseCatagoryList();
	}
	
}

package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetMedicineCategoryMapper;
import com.gq.business.inforesources.model.GetMedicineCategory;
import com.gq.business.inforesources.service.GetMedicineCategoryService;

@Service
public class GetMedicineCategoryServiceImpl implements GetMedicineCategoryService{
	@Autowired
	private GetMedicineCategoryMapper categoryMapper;
	
	@Override
	public List<GetMedicineCategory> getMedicineCategory() {
		// TODO Auto-generated method stub
		return this.categoryMapper.getMedicineCategory();
	}

}

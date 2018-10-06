package com.gq.business.inforesources.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetMedicineListMapper;
import com.gq.business.inforesources.model.GetMedicineDetail;
import com.gq.business.inforesources.model.GetMedicineList;
import com.gq.business.inforesources.service.GetMedicineListService;

@Service
public class GetMedicineListServiceImpl implements GetMedicineListService{
	@Autowired
	private GetMedicineListMapper medicineListMapper;
	
	@Override
	public List<GetMedicineList> getMedicineList(String medicinecategoryid) {
		// TODO Auto-generated method stub
		return this.medicineListMapper.getMedicineList(medicinecategoryid);
	}

	@Override
	public List<GetMedicineDetail> getMedicineDetail(String medicineid) {
		// TODO Auto-generated method stub
		return this.medicineListMapper.getMedicineDetail(medicineid);
	}

}

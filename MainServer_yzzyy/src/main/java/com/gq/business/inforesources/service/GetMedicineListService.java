package com.gq.business.inforesources.service;

import java.util.List;

import com.gq.business.inforesources.model.GetMedicineDetail;
import com.gq.business.inforesources.model.GetMedicineList;

public interface GetMedicineListService {
	List<GetMedicineList> getMedicineList(String medicinecategoryid);
	
	List<GetMedicineDetail> getMedicineDetail(String medicineid);
}

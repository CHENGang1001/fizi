package com.gq.business.inforesources.mappers;

import java.util.List;

import com.gq.business.inforesources.model.GetMedicineDetail;
import com.gq.business.inforesources.model.GetMedicineList;

public interface GetMedicineListMapper {
	List<GetMedicineList> getMedicineList(String medicinecategoryid);
	
	List<GetMedicineDetail> getMedicineDetail(String medicineid);
}

package com.gq.business.inforesources.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.mappers.GetEncyDiseaseListMapper;
import com.gq.business.inforesources.mappers.GetHealthLectureArticleMapper;
import com.gq.business.inforesources.mappers.GetHealthLectureCategoryListMapper;
import com.gq.business.inforesources.mappers.GetMedicineCategoryMapper;
import com.gq.business.inforesources.mappers.GetMedicineListMapper;
import com.gq.business.inforesources.model.GetDiseaseCatagoryList;
import com.gq.business.inforesources.model.GetDiseaseList;
import com.gq.business.inforesources.model.GetEncyDiseaseDetail;
import com.gq.business.inforesources.model.GetEncyDiseaseList;
import com.gq.business.inforesources.model.GetHealthLectureArticleDetail;
import com.gq.business.inforesources.model.GetHealthLectureArticleList;
import com.gq.business.inforesources.model.GetHealthLectureCategoryList;
import com.gq.business.inforesources.model.GetMedicineCategory;
import com.gq.business.inforesources.model.GetMedicineDetail;
import com.gq.business.inforesources.model.GetMedicineList;
import com.gq.business.inforesources.model.GetOrganDiseaseDetail;
import com.gq.business.inforesources.model.GetOrganList;
import com.gq.business.inforesources.model.GetPartList;
import com.gq.business.inforesources.service.impl.GetDiseaseCatagoryListServiceImpl;
import com.gq.business.inforesources.service.impl.GetDiseaseListServiceImpl;
import com.gq.business.inforesources.service.impl.GetOrganListServiceImpl;
import com.gq.business.inforesources.service.impl.GetPartListServiceImpl;
import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;
import com.gq.config.ReturnCode;

@Service
public class IOrganDiseaseManageService implements IIOrganDiseaseManageService{
	//智能导诊
	@Autowired
	private GetPartListServiceImpl partListService;
	
	@Autowired
	private GetOrganListServiceImpl organListService;
	
	@Autowired
	private GetDiseaseListServiceImpl diseaseListServiceImpl;
	
	@Override
	public ResponseEntity getBodyPartList() throws ServiceException{
		ResponseEntity result = new ResponseEntity(ReturnCode.OK);
		result.setContent(new HashMap<String, Object>());
		List<GetPartList> list = null;
		try{
			list = partListService.getPartList();
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		result.setContent(list);
		return result;
	}
	
	@Override
	public ResponseEntity getOrganList(String partId) throws ServiceException{
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		List<GetOrganList> list = null;
		if(null == partId){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = organListService.getOrganList(partId);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity getDiseaseList(String organid) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetDiseaseList> list = null;
		
		if(null == organid){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = diseaseListServiceImpl.getDiseaseList(organid);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity getDiseaseDetail(String diseaseid) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetOrganDiseaseDetail> list = null;
		
		if(null == diseaseid){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = diseaseListServiceImpl.getDiseaseDetail(diseaseid);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	//健康百科
	
	@Autowired
	private GetDiseaseCatagoryListServiceImpl diseaseCataGoryListServiceImpl;
	
	@Autowired
	
	private GetEncyDiseaseListMapper encyDiseaseListMapper;
	
	@Autowired
	private GetMedicineCategoryMapper medicineCategoryMapper;
	
	@Autowired
	private GetMedicineListMapper medicineListMapper;
		
	@Override
	public ResponseEntity getDiseaseCategoryList()  throws ServiceException{
		// TODO Auto-generated method stub
		ResponseEntity result = new ResponseEntity(ReturnCode.OK);
		result.setContent(new HashMap<String, Object>());
		List<GetDiseaseCatagoryList> list = null;
		try{
			list = diseaseCataGoryListServiceImpl.getDiseaseCategoryList();
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		result.setContent(list);
		return result;
	}

	@Override
	public ResponseEntity getEncyDiseaseList(String diseasecatagoryid) throws ServiceException {
		// TODO Auto-generated method stub
		
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetEncyDiseaseList> list = null;
		
		if(null == diseasecatagoryid){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = encyDiseaseListMapper.getEncyDiseaseList(diseasecatagoryid);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity getEncyDiseaseDetail(String diseaseid) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetEncyDiseaseDetail> list = null;
		
		if(null == diseaseid){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = encyDiseaseListMapper.getEncyDiseaseDetail(diseaseid);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity getMedicineCategoryList() throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity result = new ResponseEntity(ReturnCode.OK);
		result.setContent(new HashMap<String, Object>());
		List<GetMedicineCategory> list = null;
		try{
			list = medicineCategoryMapper.getMedicineCategory();
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		result.setContent(list);
		return result;
	}

	@Override
	public ResponseEntity getMedicineList(String medicinecategoryid) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetMedicineList> list = null;
		
		if(null == medicinecategoryid){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = medicineListMapper.getMedicineList(medicinecategoryid);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity getMedicineDetail(String medicineid) throws ServiceException {
		// TODO Auto-generated method stub
		
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetMedicineDetail> list = null;
		
		if(null == medicineid){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = medicineListMapper.getMedicineDetail(medicineid);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	//健康讲座
	@Autowired
	private GetHealthLectureCategoryListMapper getHealthLectureCategoryListMapper;
	
	@Autowired
	private GetHealthLectureArticleMapper getHealthLectureArticleMapper;
	
	@Override
	public ResponseEntity getHealthLectureCategoryList() throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity result = new ResponseEntity(ReturnCode.OK);
		result.setContent(new HashMap<String, Object>());
		List<GetHealthLectureCategoryList> list = null;
		try{
			list = getHealthLectureCategoryListMapper.getHealthLectureCategoryList();
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		result.setContent(list);
		return result;
	}

	@Override
	public ResponseEntity getHealthLectureArticleList(String categoryId) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetHealthLectureArticleList> list = null;
		
		if(null == categoryId){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = getHealthLectureArticleMapper.getHealthLectureArticleList(categoryId);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}

	@Override
	public ResponseEntity getHealthLectureArticleDetail(String articleId) throws ServiceException {
		// TODO Auto-generated method stub
		ResponseEntity res = new ResponseEntity(ReturnCode.OK);
		res.setContent(new HashMap<String, Object>());
		List<GetHealthLectureArticleDetail> list = null;
		
		if(null == articleId){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
		try{
			list = getHealthLectureArticleMapper.getHealthLectureArticleDetail(articleId);
		}
		catch(EmptyResultDataAccessException e){
			throw new ServiceException(ReturnCode.INFO_PARTIDDB_ERROR);
		}
		res.setContent(list);
		return res;
	}
	
}

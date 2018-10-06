package com.gq.business.inforesources.service;

import com.gq.common.exception.ServiceException;
import com.gq.common.response.ResponseEntity;

public interface IIOrganDiseaseManageService {
	public ResponseEntity getBodyPartList() throws ServiceException;
	
	public ResponseEntity getOrganList(String partId) throws ServiceException;
	
	public ResponseEntity getDiseaseList(String organid) throws ServiceException;
	
	public ResponseEntity getDiseaseDetail(String diseaseid) throws ServiceException;
	
	public ResponseEntity getDiseaseCategoryList() throws ServiceException;
	
	public ResponseEntity getEncyDiseaseList(String diseasecatagoryid) throws ServiceException;
	
	public ResponseEntity getEncyDiseaseDetail(String diseaseid) throws ServiceException;
	
	public ResponseEntity getMedicineCategoryList() throws ServiceException;
	
	public ResponseEntity getMedicineList(String medicinecategoryid) throws ServiceException;
	
	public ResponseEntity getMedicineDetail(String medicineid) throws ServiceException;
	
	public ResponseEntity getHealthLectureCategoryList() throws ServiceException;
	
	public ResponseEntity getHealthLectureArticleList(String categoryId) throws ServiceException;
	
	public ResponseEntity getHealthLectureArticleDetail(String articleId) throws ServiceException;

}

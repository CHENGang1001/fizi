package com.gq.business.inforesources.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.business.inforesources.service.IOrganDiseaseManageService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.utils.Level;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;

@Controller
@RequestMapping("/encyclopedia")
public class EncyclopediaController {

	@Autowired
	private IOrganDiseaseManageService iOrganDiseaseManageService;

	@RequestMapping(value = "/getDiseaseCategoryList")
	public @ResponseBody ResponseEntity getOrganList(@RequestBody RequestEntity requestBody){
		//	    	return iOrganDiseaseManageService.getDiseaseCategoryList();

		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		try {
			responseEntity = iOrganDiseaseManageService.getDiseaseCategoryList();
		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象
			if(responseEntity == null){

				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
				responseEntity.setContent(mapGetVerifyCode);
			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getDiseaseCategoryList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	private void parmVerify(String diseaseId) throws ServiceException {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(diseaseId)){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
	}

	@RequestMapping(value = "/getDiseaseList")
	public @ResponseBody ResponseEntity getEncyDiseaseList(@RequestBody RequestEntity requestBody){
		//	    	return iOrganDiseaseManageService.getEncyDiseaseList(requestBody.getContent().get("diseaseCategoryId").toString());
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String diseaseCategoryId;
		try {
			diseaseCategoryId = requestBody.getContent().get("diseaseCategoryId").toString();

			parmVerify(diseaseCategoryId);

			responseEntity = iOrganDiseaseManageService.getEncyDiseaseList(diseaseCategoryId);

		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象
			if(responseEntity == null){

				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
				responseEntity.setContent(mapGetVerifyCode);
			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getEncyDiseaseList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getDiseaseDetail")
	public @ResponseBody ResponseEntity getEncyDiseaseDetail(@RequestBody RequestEntity requestBody){
		//	    	return iOrganDiseaseManageService.getEncyDiseaseDetail(requestBody.getContent().get("diseaseId").toString());
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String diseaseId;
		try {
			diseaseId = requestBody.getContent().get("diseaseId").toString();

			parmVerify(diseaseId);

			responseEntity = iOrganDiseaseManageService.getEncyDiseaseDetail(diseaseId);

		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象
			if(responseEntity == null){

				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
				responseEntity.setContent(mapGetVerifyCode);
			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getEncyDiseaseDetail", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getMedicineCategoryList")
	public @ResponseBody ResponseEntity getMedicineCategoryList(@RequestBody RequestEntity requestBody){
		//		return iOrganDiseaseManageService.getMedicineCategoryList();
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		try {       		
			responseEntity = iOrganDiseaseManageService.getMedicineCategoryList();

		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象
			if(responseEntity == null){

				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
				responseEntity.setContent(mapGetVerifyCode);
			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getMedicineCategoryList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getMedicineList")
	public @ResponseBody ResponseEntity getMedicineList(@RequestBody RequestEntity requestBody){
		//		return iOrganDiseaseManageService.getMedicineList(requestBody.getContent().get("medicineCategoryId").toString());
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();

		String medicineCategoryId;
		try {
			medicineCategoryId = requestBody.getContent().get("medicineCategoryId").toString();

			parmVerify(medicineCategoryId);

			responseEntity = iOrganDiseaseManageService.getMedicineList(medicineCategoryId);

		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;

			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象
			if(responseEntity == null){

				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();

				responseEntity.setContent(mapGetVerifyCode);
			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getMedicineList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getMedicineDetail")
	public @ResponseBody ResponseEntity getMedicineDetail(@RequestBody RequestEntity requestBody){
		//		return iOrganDiseaseManageService.getMedicineDetail(requestBody.getContent().get("medicineId").toString());
		ReturnCode      returnCode    = ReturnCode.OK;

		ResponseEntity responseEntity = null;

		long startTime = System.currentTimeMillis();

		String medicineId;
		try {
			medicineId = requestBody.getContent().get("medicineId").toString();

			parmVerify(medicineId);

			responseEntity = iOrganDiseaseManageService.getMedicineDetail(medicineId);

		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;

			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象
			if(responseEntity == null){

				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();

				responseEntity.setContent(mapGetVerifyCode);
			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getMedicineDetail", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}
}

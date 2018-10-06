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
@RequestMapping("/leading")
public class OrganDiseaseManageController {

	@Autowired
	private IOrganDiseaseManageService iOrganDiseaseManageService;

	@RequestMapping(value = "/getBodyPartList")
	public @ResponseBody ResponseEntity getBodyPartList(@RequestBody RequestEntity requestBody){
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		try {
			responseEntity = iOrganDiseaseManageService.getBodyPartList();
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
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getBodyPartList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getOrganList")
	public @ResponseBody ResponseEntity getOrganList(@RequestBody RequestEntity requestBody){
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String partId;
		try {
			partId = requestBody.getContent().get("partId").toString();

			parmVerify(partId);

			responseEntity = iOrganDiseaseManageService.getOrganList(partId);

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
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getOrganList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getOrganDiseaseList")
	public @ResponseBody ResponseEntity getDiseaseList(@RequestBody RequestEntity requestBody){
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String organId;
		try {
			organId = requestBody.getContent().get("organId").toString();

			parmVerify(organId);

			responseEntity = iOrganDiseaseManageService.getDiseaseList(organId);

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
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getDiseaseList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
		//    	return iOrganDiseaseManageService.getDiseaseList(requestBody.getContent().get("organId").toString());
	}

	@RequestMapping(value = "/getOrganDiseaseDetail")
	public @ResponseBody ResponseEntity getDiseaseDetail(@RequestBody RequestEntity requestBody){

		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String diseaseId;
		try {
			diseaseId = requestBody.getContent().get("diseaseId").toString();

			parmVerify(diseaseId);

			responseEntity = iOrganDiseaseManageService.getDiseaseDetail(diseaseId);

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
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getOrganDiseaseDetail", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;

		//    	return iOrganDiseaseManageService.getDiseaseDetail(requestBody.getContent().get("diseaseId").toString());
	}

	private void parmVerify(String type) throws ServiceException{
		if(StringUtils.isEmpty(type)){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
	}

}

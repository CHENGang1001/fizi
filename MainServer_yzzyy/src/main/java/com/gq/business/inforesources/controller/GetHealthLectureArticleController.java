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
@RequestMapping("/lecture")
public class GetHealthLectureArticleController {
	@Autowired
	private IOrganDiseaseManageService iOrganDiseaseManageService;

	@RequestMapping(value = "/getHealthLectureCategoryList")
	public @ResponseBody ResponseEntity getHealthLectureCategoryList(@RequestBody RequestEntity requestBody){

		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		try {
			responseEntity = iOrganDiseaseManageService.getHealthLectureCategoryList();

		} catch (ServiceException se) {
			// TODO: handle exception
			returnCode = se.getScode();
		}
		catch(Exception e){
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}
		finally{
			//构造返回应答对象\
			if(responseEntity == null){
				responseEntity = new ResponseEntity(returnCode);

				//构造包体，该接口包体为空
				Map<String, String> mapGetVerifyCode = new HashMap<String, String>();
				responseEntity.setContent(mapGetVerifyCode);

			}
			//记录接口日志
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getHealthLectureCategoryList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	private void parmVerify(String diseaseId) throws ServiceException {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(diseaseId)){
			throw new ServiceException(ReturnCode.INFO_PARTID_ERROR);
		}
	}

	@RequestMapping(value = "/getHealthLectureArticleList")
	public @ResponseBody ResponseEntity getHealthLectureArticleList(@RequestBody RequestEntity requestBody){
		//	    	return iOrganDiseaseManageService.getHealthLectureArticleList(requestBody.getContent().get("categoryId").toString());
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String categoryId;
		try {
			categoryId = requestBody.getContent().get("categoryId").toString();

			parmVerify(categoryId);

			responseEntity = iOrganDiseaseManageService.getHealthLectureArticleList(categoryId);

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
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getHealthLectureArticleList", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getHealthLectureArticleDetail")
	public @ResponseBody ResponseEntity getHealthLectureArticleDetail(@RequestBody RequestEntity requestBody){
		//	    	return iOrganDiseaseManageService.getHealthLectureArticleDetail(requestBody.getContent().get("articleId").toString());
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long           startTime      = System.currentTimeMillis();    	

		String articleId;
		try {
			articleId = requestBody.getContent().get("articleId").toString();

			parmVerify(articleId);

			responseEntity = iOrganDiseaseManageService.getHealthLectureArticleDetail(articleId);

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
			MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getHealthLectureArticleDetail", requestBody.toString(), responseEntity.toString(), System.currentTimeMillis() - startTime);
		}
		return responseEntity;
	}
}

package com.gq.business.inforesources.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.base.BaseController;
import com.gq.business.inforesources.model.HealthCareBean;
import com.gq.business.inforesources.model.HospitalNewsBean;
import com.gq.business.inforesources.service.ManageHealthCareService;
import com.gq.business.inforesources.service.ManageHospitalNewsService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.request.RequestEntityEx;
import com.gq.common.response.ResponseEntity;
import com.gq.config.ReturnCode;


@Controller
@RequestMapping("/healthcare")
public class ManageHealthCareController extends BaseController{
		
	@Autowired
	private ManageHealthCareService manageHealthCareService;	
	
	/**
	 * 新增
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/addHealthCare")
	public @ResponseBody ResponseEntity addHealthCare(@RequestBody RequestEntityEx<HealthCareBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取信息
			HealthCareBean careInfo = requestBody.getContent();
			
			manageHealthCareService.addHealthCare(careInfo);
		} catch (ServiceException se) {
			returnCode = se.getScode();
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			responseEntity.setContent(new HashMap<String, Object>());
		}
		return responseEntity;
	}
	
	/**
	 * 查询所有
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/selectHealthCare")
	public @ResponseBody ResponseEntity selectHealthCare(@RequestBody RequestEntityEx<HealthCareBean> requestBody) throws ServiceException{
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = manageHealthCareService.selectHealthCare();
			//responseEntity.setContent(detailObject);
		} catch(Exception e){
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
		}
		return responseEntity;
	}
	
	/**
	 * 根据id单条查询
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/selectByIdHealthCare")
	public @ResponseBody ResponseEntity selectByIdHealthCare(@RequestBody RequestEntityEx<HealthCareBean> requestBody) throws ServiceException{
		ReturnCode      returnCode    = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		
		String careId;
		try {
			careId = requestBody.getContent().getCareId();
			responseEntity = manageHealthCareService.selectByIdHealthCare(careId);
			//responseEntity.setContent(detailObject);
		} catch(Exception e){
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
		}
		return responseEntity;
	}
	
	/**
	 * 修改
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/editorHealthCare")
	public @ResponseBody ResponseEntity editorHealthCare(@RequestBody RequestEntityEx<HealthCareBean> requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		try {
			// 获取信息
			HealthCareBean careInfo = requestBody.getContent();

			manageHealthCareService.editorHealthCare(careInfo);
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			responseEntity.setContent(new HashMap<String, Object>());
		}
		return responseEntity;
	}
	
	/**
	 * 删除
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/deleteHealthCare")
	public @ResponseBody ResponseEntity deleteHealthCare(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		ResponseEntity responseEntity = null;
		long startTime = System.currentTimeMillis();
		
		String careId;
		try {
			// 获取信息
			careId = requestBody.getContent().get("careId").toString();

			manageHealthCareService.deleteHealthCare(careId);
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity(returnCode);
			responseEntity.setContent(new HashMap<String, Object>());
		}
		return responseEntity;
	}


}

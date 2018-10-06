package com.gq.business.inforesources.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.base.BaseController;
import com.gq.business.inforesources.model.HospitalNewsBean;
import com.gq.business.inforesources.service.ManageHospitalNewsService;
import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.request.RequestEntityEx;
import com.gq.common.response.ResponseEntity;
import com.gq.config.ReturnCode;

@Controller
@RequestMapping("/managehospital")
public class ManageHospitalNewsController extends BaseController {

		@Autowired
		private ManageHospitalNewsService manageHospitalNewsService;

		/**
		 * 新增
		 * 
		 * @param requestBody
		 * @return
		 */
		@RequestMapping(value = "/addHospitalNews")
		public @ResponseBody ResponseEntity addHospitalNews(@RequestBody RequestEntityEx<HospitalNewsBean> requestBody) {
			ReturnCode returnCode = ReturnCode.OK;
			ResponseEntity responseEntity = null;
			long startTime = System.currentTimeMillis();
			try {
				// 获取信息
				HospitalNewsBean newsInfo = requestBody.getContent();
				
				manageHospitalNewsService.addHospitalNews(newsInfo);
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
		 * 删除
		 * 
		 * @param requestBody
		 * @return
		 */
		@RequestMapping(value = "/deleteHospitalNews")
		public @ResponseBody ResponseEntity deleteHospitalNews(@RequestBody RequestEntity requestBody) {
			ReturnCode returnCode = ReturnCode.OK;
			ResponseEntity responseEntity = null;
			long startTime = System.currentTimeMillis();
			
			String newsId;
			try {
				// 获取信息
				newsId = requestBody.getContent().get("newsId").toString();

				manageHospitalNewsService.deleteHospitalNews(newsId);
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
		 * 修改
		 * 
		 * @param requestBody
		 * @return
		 */
		@RequestMapping(value = "/editorHospitalNews")
		public @ResponseBody ResponseEntity editorHospitalNews(@RequestBody RequestEntityEx<HospitalNewsBean> requestBody) {
			ReturnCode returnCode = ReturnCode.OK;
			ResponseEntity responseEntity = null;
			long startTime = System.currentTimeMillis();
			try {
				// 获取信息
				HospitalNewsBean newsInfo = requestBody.getContent();
 
				manageHospitalNewsService.editorHospitalNews(newsInfo);
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
		@RequestMapping(value = "/selectHospitalNews")
		public @ResponseBody ResponseEntity selectHospitalNews(@RequestBody RequestEntityEx<HospitalNewsBean> requestBody) throws ServiceException{
			ReturnCode      returnCode    = ReturnCode.OK;
			ResponseEntity responseEntity = null;
			try {
				responseEntity = manageHospitalNewsService.selectHospitalNews();
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
		@RequestMapping(value = "/selectByIdHospitalNews")
		public @ResponseBody ResponseEntity selectByIdHospitalNews(@RequestBody RequestEntityEx<HospitalNewsBean> requestBody) throws ServiceException{
			ReturnCode      returnCode    = ReturnCode.OK;
			ResponseEntity responseEntity = null;
			String newsId;
			try {
				newsId = requestBody.getContent().getNewsId();
				responseEntity = manageHospitalNewsService.selectByIdHospitalNews(newsId);
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
		
}

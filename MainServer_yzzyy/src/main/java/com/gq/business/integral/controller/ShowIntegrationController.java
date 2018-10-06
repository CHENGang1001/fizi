package com.gq.business.integral.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.business.integral.bean.GoodsBean;
import com.gq.business.integral.bean.OrderGoodsBean;
import com.gq.business.integral.service.IIntegralManagService;
import com.gq.common.log.Level;
import com.gq.common.log.LogManager;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.config.ReturnCode;

@Controller
public class ShowIntegrationController {
	@Autowired
	private IIntegralManagService manageService;
	/**
	 * 管理登录
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/exchangeLogin")
	@ResponseBody
	public ResponseEntity exchangeLogin(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		Map<String, Object> returnParam = new HashMap<String, Object>();
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				String userName = param.get("userName").toString();
				String passWord = param.get("passWord").toString();
				if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(passWord)){

					returnParam=manageService.exchangeLogin(param);

				}

				if(returnParam.isEmpty()) {
					returnCode = ReturnCode.NO_DATA;
				} else {
					String Role = returnParam.get("ROLE").toString();
					String role = param.get("role").toString();
					System.out.println(Role.equals(role));
					if(Role.equals(role)){
						returnCode = ReturnCode.OK;
					}else{
						returnCode = ReturnCode.NO_DATA;
					}
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "exchangeLogin", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	/**
	 * 发布商品
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/publishGoods")
	@ResponseBody
	public ResponseEntity publishGoods(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				GoodsBean goods = new GoodsBean();

				goods.setCurrent_amount(param.get("current_amount").toString());
				goods.setGoods_description(param.get("goods_description").toString());
				goods.setGoods_name(param.get("goods_name").toString());
				goods.setGoods_status(param.get("goods_status").toString());
				goods.setNeeded_integral(param.get("needed_integral").toString());
				goods.setPurchase(param.get("purchase").toString());
				goods.setTotal_amount(param.get("total_amount").toString());
				goods.setUnit_price(param.get("unit_price").toString());
				goods.setClassify(param.get("classify").toString());
				goods.setProject(param.get("project").toString());
				String imageSourse = param.get("imageSource").toString();
				int is = manageService.publishGoods(goods,imageSourse);
				if(is==1){
					returnCode = ReturnCode.OK;
				}else{
					returnCode = ReturnCode.GOODSNAME_ISREPEAT;
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "publishGoods", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 编辑商品
	 */
	@RequestMapping(value = "/editGoods")
	@ResponseBody
	public ResponseEntity editGoods(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if(null != requestBody){
				Map<String, Object> param = requestBody.getContent();
				GoodsBean goods = new GoodsBean();
				
				//编辑库存，获取参数
				goods.setCurrent_amount(param.get("current_amount").toString());
				goods.setGoods_code(param.get("goods_code").toString());
				int is = manageService.editGoods(goods);
				if( is == 1){
					returnCode = ReturnCode.OK;
				}else{
					returnCode = ReturnCode.GOODS_EDIT_FAILE;
				}	
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "editGoods", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	
	/**
	 * 兑换商品
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/exchangeGoods")
	@ResponseBody
	public ResponseEntity exchangeGoods(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				String orderNo = param.get("order_no").toString();
				String is = manageService.exchangeGoods(orderNo);
				if(is.equals("1")){
					returnCode = ReturnCode.OK;
				}else{
					returnCode = ReturnCode.HAS_EXCHANGED;
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "exchangeGoods", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 商品上架/下架
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/uploadGoodsStatus")
	@ResponseBody
	public ResponseEntity uploadGoodsStatus(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				GoodsBean goods = new GoodsBean();
				if(param!=null){
					goods.setGoods_code(param.get("goodsId").toString());
					goods.setGoods_status(param.get("goodsStatus").toString());
					manageService.uploadGoodsStatus(goods);
				}
				returnCode = ReturnCode.OK;
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "uploadGoodsStatus", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 积分商城后台管理获取商品列表
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/integralGoodsList")
	@ResponseBody
	public ResponseEntity integralGoodsList(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		List<Map<String, Object>> list = null;
		try {
			if(null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				list = manageService.integralGoodsList(param);
				if(list.isEmpty()) {
					returnCode = ReturnCode.NO_DATA;
				} else {
					returnCode = ReturnCode.OK;
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(list);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "integralGoodsList", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 查询订单状态
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getOrder")
	@ResponseBody
	public ResponseEntity getOrder(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		OrderGoodsBean returnParam = new OrderGoodsBean();
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				returnParam = manageService.getOrder(param);
				if(returnParam==null) {
					returnCode = ReturnCode.NO_DATA;
				} else {
					returnCode = ReturnCode.OK;
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(returnParam);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getOrder", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	
	/**
	 * 获取图片
	 */
	@RequestMapping(value = "/getPic")
	@ResponseBody
	public ResponseEntity getPic(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		Map<String, Object> returnGoods = new HashMap<String, Object>();
		try {
			if(null != requestBody){
				Map<String, Object> param = requestBody.getContent();				
				//获取图片路径
				String picUrl = manageService.getGoodsPic(param.get("code").toString());				
				if(picUrl == "" || picUrl == null){
					returnCode = ReturnCode.SYSTEM_ERROR;
				}else{
					returnCode = ReturnCode.OK;
					returnGoods.put("picUrl", picUrl);
				}	
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(returnGoods);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getPic", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	
	/**
	 * 编辑商品
	 */
	@RequestMapping(value = "/editGoodsAll")
	@ResponseBody
	public ResponseEntity editGoodsAll(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if(null != requestBody){
				Map<String, Object> param = requestBody.getContent();
				GoodsBean goods = new GoodsBean();
				
				//编辑库存，获取参数
				goods.setCurrent_amount(param.get("current_amount").toString());
				goods.setGoods_description(param.get("goods_description").toString());
				goods.setGoods_name(param.get("goods_name").toString());
				goods.setNeeded_integral(param.get("needed_integral").toString());
				goods.setPurchase(param.get("purchase").toString());
				goods.setTotal_amount(param.get("total_amount").toString());
				goods.setUnit_price(param.get("unit_price").toString());
				goods.setClassify(param.get("classify").toString());
				goods.setProject(param.get("project").toString());
				goods.setGoods_code(param.get("goods_code").toString());
				String imageSourse = param.get("imageSource").toString();
				int is = manageService.editGoodsAll(goods,imageSourse);
				if( is == 1){
					returnCode = ReturnCode.OK;
				}else{
					returnCode = ReturnCode.GOODS_EDIT_FAILE;
				}	
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "editGoodsAll", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	
	@RequestMapping(value = "/deleteGoods")
	@ResponseBody
	public ResponseEntity deleteGoods(@RequestBody RequestEntity requestBody){
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if(requestBody != null){
				Map<String, Object> param = requestBody.getContent();
				String goodsCode = param.get("goodsCode").toString();
				int is = manageService.deleteGoods(goodsCode);
				if(is == 0){
					returnCode = ReturnCode.SYSTEM_ERROR;
				}
			}
			
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}finally{
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(null);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "deleteGoods", requestBody.toString(),
		responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	
	/**
	 * 按照商品代码查找兑换记录
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/findOrderByCode")
	@ResponseBody
	public ResponseEntity findOrderByCode(@RequestBody RequestEntity requestBody){
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		List<Map<String, Object>> list = null;
		try {
			if(requestBody != null){
				Map<String, Object> param = requestBody.getContent();
				String goodsCode = param.get("goodsCode").toString();
				list = manageService.findOrderByCode(goodsCode);
				if(list.isEmpty()){
					returnCode = ReturnCode.NO_DATA;
				}
			}
			
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		}finally{
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(list);
		}
		// 接口日志
				LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "findOrderByCode", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
		
	}
	
	
}

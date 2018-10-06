package com.gq.business.accountmanage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.business.accountmanage.dto.CDTOTestUser;
import com.gq.business.reports.dto.*;
import com.gq.common.request.RequestEntityEx;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;

@Controller
@RequestMapping("/account")
public class UserManageController {
	
    @RequestMapping("/testUser")
    //public @ResponseBody ResponseEntity testUser(@RequestBody CommonRequestBean<TestRequestBody> testBody)
    public @ResponseBody ResponseEntity testUser(@RequestBody RequestEntityEx<CDTOTestUser> testBody)
    {
    	//Map<String, Object> param = requestBody.getContent();
    	//CDTOTestUser oBody = testBody;//.getContent();
    	//CDTOTestUser oBody = testBody.getContent();
    	return null;
    	//String sTemp = sBody + "AAA";
    	//oBody = null;
    	
//    	ResponseEntity oResponse = new ResponseEntity();
//    	oResponse.getHeader().setResultCode("10000");
//    	oResponse.getHeader().setResultMsg("success");
//    	
//    	Map<String, String> mapUserInfo = new HashMap<String, String>();
//    	mapUserInfo.put("id", "100");
//    	mapUserInfo.put("name", "zhangxiang");
//    	mapUserInfo.put("status", "true");
//    	return oResponse;
    }
    
}

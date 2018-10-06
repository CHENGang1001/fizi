package com.gq.base;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BaseController {

	@ModelAttribute
	public void setHeader(HttpServletResponse response) {
		response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
		
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
	}
}

package com.gq.common.response;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.gq.common.utils.GsonUtils;
import com.gq.config.ReturnCode;

public class ResponseEntity {

	private ResponseHeader header = new ResponseHeader();
	private Object content = new Object();

	public ResponseHeader getHeader() {
		return header;
	}

	public void setHeader(ResponseHeader header) {
		this.header = header;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	public ResponseEntity() {
	}
	
	public ResponseEntity(ReturnCode code) {
		this.header.setResultCode(code.getCode());
		this.header.setResultMsg(code.getMsg());
	}
	public static String toJson(String returnCode, Object result) {
		ResponseHeader header = new ResponseHeader();
		header.setResultCode(returnCode);
		header.setResultMsg(ReturnCode.getReturnCodeByCode(returnCode).getMsg());
		ResponseEntity resEntity = new ResponseEntity();
		resEntity.setHeader(header);
		if (null != result) {
			resEntity.setContent(result);
		}
		return GsonUtils.toJson(resEntity);
	}

	public static void setResponse(HttpServletResponse response, String rspBody) throws IOException {
		response.setContentType("text/json;charset=UTF-8");// application/json
		response.getWriter().write(rspBody);
	}
}

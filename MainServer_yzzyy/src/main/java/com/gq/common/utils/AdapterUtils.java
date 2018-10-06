package com.gq.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AbstractDocument.Content;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.druid.support.json.JSONUtils;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.config.ReturnCode;

public class AdapterUtils {
	private static final String CONFIG_FILE = "config.xml";
	private static String url = null;

	/**
	 * 从配置文件中读取adapter地址
	 */
	private static void loadURL() {
		String path = null;
		try {
			path = Thread.currentThread().getContextClassLoader().getResource(CONFIG_FILE).toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println(path);
		FileInputStream is;
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			File file = new File(path);
			is = new FileInputStream(file);
			doc = reader.read(is);
			Element root = doc.getRootElement();
			Element e = root.element("adapter");
			url = e.getText();
			is.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 向adapter发送请求
	 * 
	 * @param data
	 * @return
	 */
	public static String send(String data) {
		if (url == null) {
			loadURL();
		}
		return HttpUtils.sendPost(url, data);
	}

	/**
	 * 向adapter发送请求
	 * 
	 * @param method
	 * @param data
	 * @return
	 */
	public static String send(String method, Map<String, Object> param) {
		if (url == null) {
			loadURL();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("method", method);
		map.put("data", param);
		String json = JSONUtils.toJSONString(map);
		return HttpUtils.sendPost(url, json);
	}

	/**
	 * 向adapter发送请求,返回ResponseEntity数据
	 * 
	 * @param method
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ResponseEntity modelSend(String method, Map<String, Object> param) {
		ResponseEntity response = new ResponseEntity();
		ResponseHeader header = new ResponseHeader();
		String res = AdapterUtils.send(method, param);
		String resultFlag = "FAIL";
		if (!StringUtils.isNullOrEmpty(res)) {
			Map<String, Object> result = (HashMap<String, Object>) JSONUtils.parse(res);
			if (result.get("adapterCode").equals("1")) {
				Map<String, Object> dataMap = (HashMap<String, Object>) result.get("data");
				header.setResultCode(result.get("adapterCode").toString());
				header.setResultMsg(result.get("adapterMessage").toString());
					if(!dataMap.get("resultCode").equals("0")){
					}
				response.setHeader(header);
				response.setContent(dataMap.get("result"));
				resultFlag = "SUCCESS";
			}
		}
		if (resultFlag.equals("FAIL")) {
			header.setResultCode(ReturnCode.NO_DATA.getCode());
			header.setResultMsg(ReturnCode.NO_DATA.getMsg());
			response.setHeader(header);
			response.setContent(new HashMap<String, Object>());
		}
		return response;
	}
}

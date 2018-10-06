package com.gq.business.inforesources.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.base.BaseController;
import com.gq.business.inforesources.bean.UpgradeAPKBean;
import com.gq.business.inforesources.service.UpgradeAPKService;
import com.gq.common.log.Level;
import com.gq.common.log.LogManager;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.JsonUtils;
import com.gq.common.utils.MHLogManager;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;

/**
 * 手机端检测接口
 *
 * @Author: xiuyajia
 * @Date: 12/13/13 2:30 PM
 */
@Controller
public class UpgradeAPKController extends BaseController {

	/**
	 * 手机升级服务层自动注入
	 */
	@Autowired
	private UpgradeAPKService UpgradeAPKService;

	/**
	 * DEBUG日志
	 */
	private Logger log = LogManager.getDebugLog();

	/**
	 * 手机端检测接口
	 *
	 * @param request
	 *            <br/>
	 * @param response
	 *            <br/>
	 */
	@RequestMapping(value = "/CheckUpgrade")
	@ResponseBody
	public ResponseEntity checkUpgrade(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.OK;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		String result = "";
		UpgradeAPKBean bean = null;
		try {
			if(null != requestBody) {
				// 获得当前客户端的版本号和版本类型
				Map<String, Object> versionInfo = requestBody.getContent();
				// 获得当前版本序号
				String currentVersion = versionInfo.get("currentVersion").toString();
				// 获得客户端类型
				String apkType = versionInfo.get("apkType").toString();

				// 判断参数是否为空
				if (StringUtils.isEmpty(currentVersion, apkType)) {
					returnCode = ReturnCode.SYSTEM_ERROR;
				} else {
					// 获得最新的版本信息
					bean = UpgradeAPKService.getLatestUpgradeAPKBean(apkType);
					if (bean == null) {
						// 如果数据库中没有数据，则返回无需升级的信息
						bean = new UpgradeAPKBean();
						bean.setVersion("");
						bean.setVersionUrl("");
					} else {
						if (null != bean.getUpdateDescription()) {
							bean.setStrUpdateDescription(bean.getUpdateDescription());
						}
						currentVersion = currentVersion.replace(".", "");
						String version = bean.getVersion().replace(".", "");
						if ("4".equals(bean.getApkType())) {
							if (Integer.valueOf(currentVersion) >= Integer.parseInt(version)) {
								bean.setIsUpdateNeeded("0");
							} else {
								bean.setIsUpdateNeeded(bean.getVersionType());
							}
						} else {
							// 比较当前版本号和最新的版本号的大小
							if (Integer.valueOf(currentVersion) >= Integer.parseInt(bean.getVersionIndex())) {
								bean.setIsUpdateNeeded("0");
							} else {
								bean.setIsUpdateNeeded(bean.getVersionType());
							}
						}
					}
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
			if(bean != null) {
				result = JsonUtils.toJson(bean);
			}
			responseEntity.setHeader(header);
			responseEntity.setContent(result);
		}
		// 记录接口日志
		MHLogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "CheckUpgrade",
				requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}
	
}

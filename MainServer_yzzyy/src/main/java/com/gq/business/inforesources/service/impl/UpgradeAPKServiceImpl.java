package com.gq.business.inforesources.service.impl;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.inforesources.bean.UpgradeAPKBean;
import com.gq.business.inforesources.mappers.UpgradeAPKMapper;
import com.gq.business.inforesources.service.UpgradeAPKService;
import com.gq.common.log.LogManager;
import com.gq.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机升级接口Service
 *
 * @Author: xiuyajia
 * @Date: 12/13/13 3:01 PM
 */
@Service
public class UpgradeAPKServiceImpl implements UpgradeAPKService {

    /**
     * 自动注入
     */
    @Autowired
    private UpgradeAPKMapper upgradeAPKMapper;
    
    /**
     * DEBUG日志
     */
    private Logger log = LogManager.getDebugLog();

    @Override
    public UpgradeAPKBean getLatestUpgradeAPKBean(String apkType) {
        return upgradeAPKMapper.getLatestUpgradeAPKBean(apkType);
    }

    @Override
    public Map<String, String> getCurrentVersionByXml(String xml) {
        Map<String, String> returnMap = new HashMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = document.getRootElement();
            // 获得LEAF子节点
            Element eCurrentVersion = rootElt.element("CurrentVersion");
            Element eApkType = rootElt.element("ApkType");
            // 获得LEAF子节点的所有属性
            if (eCurrentVersion != null) {
                returnMap.put("CurrentVersion", eCurrentVersion.getTextTrim());
            }
            if (eApkType != null) {
                returnMap.put("ApkType", eApkType.getTextTrim());
            }
        } catch (DocumentException e) {
        	log.error("", e);
        }

        return returnMap;
    }

    @Override
    public String getResponseXml(UpgradeAPKBean upgradeAPKBean, String isUpdateNeeded) {
        StringBuffer responseXml = new StringBuffer();
        responseXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        responseXml.append("<result>");
        responseXml.append("<ResultCode>0</ResultCode>");
        responseXml.append("<CheckUpgradeResult>");
        responseXml.append("<IsUpdateNeeded>").append(isUpdateNeeded).append("</IsUpdateNeeded>");
        responseXml.append("<UpdateDescription>")
                .append(upgradeAPKBean.getUpdateDescription())
                .append("</UpdateDescription>");
        responseXml.append("<LatestVersion>").append(upgradeAPKBean.getVersion()).append("</LatestVersion>");
        responseXml.append("<LatestVersionUrl>").append(upgradeAPKBean.getVersionUrl()).append("</LatestVersionUrl>");
        responseXml.append("</CheckUpgradeResult>");
        responseXml.append("</result>");
        return responseXml.toString();
    }
}

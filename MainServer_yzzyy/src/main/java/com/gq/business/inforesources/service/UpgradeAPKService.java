package com.gq.business.inforesources.service;


import java.util.Map;

import com.gq.business.inforesources.bean.UpgradeAPKBean;

/**
 * 升级APK信息接口
 *
 * @Author: xiuyajia
 * @Date: 12/13/13 3:00 PM
 */
public interface UpgradeAPKService {

    /**
     * 获得最新的更新信息
     *
     * @param apkType <br/>
     * @return <br/>
     */
     UpgradeAPKBean getLatestUpgradeAPKBean(String apkType);

    /**
     * 根据Xml获得当前版本号
     *
     * @param xml <br/>
     * @return <br/>
     */
     Map<String, String> getCurrentVersionByXml(String xml);

    /**
     * 获得返回的XML信息 <br/>
     *
     * @param upgradeAPKBean <br/>
     * @param isUpdateNeeded <br/>
     * @return <br/>
     */
     String getResponseXml(UpgradeAPKBean upgradeAPKBean, String isUpdateNeeded);
}

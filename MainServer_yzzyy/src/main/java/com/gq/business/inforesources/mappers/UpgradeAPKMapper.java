package com.gq.business.inforesources.mappers;

import org.apache.ibatis.annotations.Param;

import com.gq.business.inforesources.bean.UpgradeAPKBean;

import java.util.List;

/**
 * 手机升级接口Service
 *
 * @Author: xiuyajia
 * @Date: 12/13/13 2:49 PM
 */
public interface UpgradeAPKMapper {

    /**
     * 获得最新的更新信息
     *
     * @param apkType <br />
     * @return <br />
     */
    UpgradeAPKBean getLatestUpgradeAPKBean(@Param("apkType") String apkType);

    /**
     * 根据ID号查询APK升级信息
     *
     * @param upgradeID <br />
     * @return <br />
     */
    UpgradeAPKBean queryUpgradeApkBeanByID(@Param("upgradeID") String upgradeID);

    /**
     * 获得升级APK信息List总数
     *
     * @param version <br />
     * @param apkType <br />
     * @return <br />
     */
    int queryUpgradeApkBeanListCount(@Param("version") String version,
                                     @Param("apkType") String apkType);

    /**
     * 获得升级APK信息List
     *
     * @param version   <br />
     * @param apkType   <br />
     * @param pageStart <br />
     * @param pageEnd   <br />
     * @return <br />
     */
    List<UpgradeAPKBean> queryUpgradeApkBeanList(
            @Param("version") String version, @Param("apkType") String apkType,
            @Param("pageStart") int pageStart, @Param("pageEnd") int pageEnd);

    /**
     * 检查upgrade version 是否重复
     *
     * @param upgradeAPKBean <br />
     * @return <br />
     */
    int checkUpgradeVersion(UpgradeAPKBean upgradeAPKBean);

    /**
     * 更新APK升级信息
     *
     * @param upgradeAPKBean <br />
     * @return <br />
     */
    int updateUpgrade(UpgradeAPKBean upgradeAPKBean);

    /**
     * 获得UpgradeID序列
     *
     * @return <br />
     */
    String queryUpgradeIDSequence();

    /**
     * 新增APK更新信息
     *
     * @param upgradeAPKBean <br />
     * @return <br />
     */
    int insertUpgrade(UpgradeAPKBean upgradeAPKBean);


    /**
     * 删除APK升级信息
     *
     * @param upgradeIDList <br />
     * @return <br />
     */
    int deleteUpgrade(List<String> upgradeIDList);


}

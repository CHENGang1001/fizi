package com.gq.business.inforesources.bean;

/**
 * 手机升级接口
 *
 * @Author: xiuyajia
 * @Date: 12/13/13 2:50 PM
 */
public class UpgradeAPKBean {

	/**
	 * ID
	 */
	private String upgradeID;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 版本URL
	 */
	private String versionUrl;
	/**
	 * 版本类型
	 */
	private String versionType;
	/**
	 * 版本序号，判断版本新旧
	 */
	private String versionIndex;

	/**
	 * 1：大众版 2：医生版
	 */
	private String apkType;
	/**
	 * 升级说明
	 */
	private String updateDescription;
	private String strUpdateDescription;
	/**
	 * 保存在服务器中的真实路径
	 */
	private String versionRealPath;

	private String isUpdateNeeded;

	public String getUpgradeID() {
		return upgradeID;
	}

	public void setUpgradeID(String upgradeID) {
		this.upgradeID = upgradeID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionUrl() {
		return versionUrl;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getVersionIndex() {
		return versionIndex;
	}

	public void setVersionIndex(String versionIndex) {
		this.versionIndex = versionIndex;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public String getApkType() {
		return apkType;
	}

	public void setApkType(String apkType) {
		this.apkType = apkType;
	}

	public String getStrUpdateDescription() {
		return strUpdateDescription;
	}

	public void setStrUpdateDescription(String strUpdateDescription) {
		this.strUpdateDescription = strUpdateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getVersionRealPath() {
		return versionRealPath;
	}

	public void setVersionRealPath(String versionRealPath) {
		this.versionRealPath = versionRealPath;
	}

	public String getIsUpdateNeeded() {
		return isUpdateNeeded;
	}

	public void setIsUpdateNeeded(String isUpdateNeeded) {
		this.isUpdateNeeded = isUpdateNeeded;
	}

}

package com.gq.business.integral.bean;

import java.io.Serializable;
/**
 * 图片Bean
 *
 * @author liangyuli
 * @ClassName: ImageBean
 * @Description: 图片Bean
 * @date: Feb 22, 10:29:27 PM
 */
public class ImageBean implements Serializable {
	private static final long serialVersionUID = -5124382864816179168L;

	/** 主键编号 */
	private String id;
	
	/** 资源文件名称 */
	private String sourceName;
	
	/** 资源文件路径 */
	private String sourcePath;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

}

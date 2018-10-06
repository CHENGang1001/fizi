package com.gq.business.inforesources.bean;

/**
 * 公共数据定义
 * <p/>
 * User: xyjia
 * Date: 11/19/13
 * Time: 10:55 AM
 */
public class GlobalData {

    /**
     * 私有构造函数
     */
    private GlobalData() {

    }

    /**
     * 数据库BLOB字段编码
     */
    public static final String BLOB_CODE = "GB2312";

    /**
     * 客户类型
     */
    public static final String PUBLIC_CLIENT_TYPE = "0";

    /**
     * 上传文件最大大小 20MB
     */
    public static final long MAX_APK_SIZE = 20971520L;

    /**
     * 上传图片的最大大小为2MB
     */
    public static final long MAX_IMAGE_SIZE = 2097152L;
    
    /**
     * 根据就诊卡号查询病人信息成功
     */
    public static final String CHECK_PATIENT_ID_REGISTERED_SUCCESS = "1";
    
    /**
     * 上传文件保存根目录
     */
    public static final String UPLOAD_SAVED_BASE_DIR = "baseDir";
    
    /**
     * 上传图片的根目录
     */
    public static final String IMG_SAVED_BASE_DIR = "baseDirOfImages";
    
    /**
     * 上传图片的临时保存目录
     */
    public static final String IMG_SAVED_TEMP_DIR = "tempDirOfUploadImages";
    
    /**
     * 上传图片保存目录
     */
    public static final String IMG_SAVED_DIR = "dirOfUploadImages";
    
    /**
     * 缩略图保存目录
     */
    public static final String IMG_THU_SAVED_DIR = "dirOfThumbnails";
    
    /**
     * 上传文件的根目录
     */
    public static final String FILE_SAVED_BASE_DIR = "baseDirOfFiles";
    
    /**
     * 临时图片目录(图片目录类型)
     */
    public static final int TEMP_IMG_DIR = 0;
    
    /**
     * 原图保存目录(图片目录类型)
     */
    public static final int ORI_IMG_DIR = 1;
    
    /**
     * 缩略图保存目录(图片目录类型)
     */
    public static final int THU_IMG_DIR = 2;
    
    /**
     * 文件保存子目录
     */
    public static final int FILE_DIR = 3;
    
    /**
     * 缩略图尺寸
     */
    public static final int THUMBNAIL_DEFAULT_SIZE = 50;
    
    /**
     * 缩略图质量
     */
    public static final String THUMBNAIL_DEFAULT_QUALITITY = "100";
    
    /**
     * 缩略图默认缩放质量
     */
    public static final float THUMBNAIL_DEFAULT_FLOAT_QUALITITY = 0.9f;
    
    /**
     * 多个图片URL分隔符
     */
    public static final String PIC_SEPARATOR = ";";
    
    /**
     * 上传的文件保存的绝对路径（根目录）
     */
    public static final String FILE_ABSOLUTE_PATH = "filePath";
    
    /**
     * 服务器IP及Port
     */
    public static final String SERVER_IP_AND_PORT = "ipAndPort";
    
    /**
     * 项目部署到Tomcat时目录名称
     */
    public static final String WEB_DIR_NAME = "webapps";
    
}

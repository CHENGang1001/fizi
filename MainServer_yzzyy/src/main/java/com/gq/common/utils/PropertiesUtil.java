package com.gq.common.utils;

//import org.slf4j.Logger;
//import com.cienet.common.log.LogManager;

import org.springframework.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: xyjia
 * Date: 11/13/13
 * Time: 3:18 PM
 */
public class PropertiesUtil {
	
	/**
     * DEBUG日志
     */
    //private static Logger log = LogManager.getDebugLog();

    /**
     * 私有的构造函数
     */
    private PropertiesUtil() {

    }


    /**
     * 获得到资源文件中的内容
     *
     * @param keyName key名称
     * @return <br>
     */
    public static String getMessageValueByKey(String keyName) {
        return getMessageValueByKey(keyName, null);
    }

    /**
     * 获得到资源文件中的内容
     *
     * @param keyName  key名称
     * @param basePath 文件路径前后都要有斜杠
     * @return <br>
     */
    public static String getMessageValueByKey(String keyName, String basePath) {
        return getMessageValueByKey(keyName, basePath, null);
    }

    /**
     * 获得到配置文件中的内容
     *
     * @param keyName      key名称
     * @param basePath     文件路径前后都要有斜杠
     * @param baseFileName 文件名
     * @return <br>
     */
    public static String getMessageValueByKey(String keyName, String basePath, String baseFileName) {

        if (StringUtils.isEmpty(basePath)) {
            basePath = "/";
        }
        if (StringUtils.isEmpty(baseFileName)) {
            baseFileName = "global.properties";
        }

        InputStream inputStream = PropertiesUtil.class.getResourceAsStream(basePath + baseFileName);
        Properties properties = new Properties();
        String messageValue = null;
        try {
            properties.load(inputStream);

            messageValue = properties.getProperty(keyName);
        } catch (IOException e) {
        	;//log.error("", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                	;//log.error("", e);
                }
            }
        }
        return messageValue;
    }
}

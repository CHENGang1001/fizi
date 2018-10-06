package com.gq.common.utils;

import java.io.UnsupportedEncodingException;

//import org.slf4j.Logger;
//import com.cienet.common.log.LogManager;

/**
 * String工具类
 *
 * @author gqdai
 * @Date: 20131107
 */
public class StringUtils {
	
	/**
     * DEBUG日志
     */
    //private static Logger log = LogManager.getDebugLog();

    /**
     * 私有的构造函数
     */
    private StringUtils() {

    }

    /**
     * null转空字符串
     *
     * @param args <br />
     * @return <br />
     */
    public static String nullToEmpty(String args) {
        return args == null ? "" : args;
    }

    /**
     * 字节数组转字符串，指定字符集
     *
     * @param srcObj     <br />
     * @param charEncode <br />
     * @return <br />
     */
    public static String byteToString(byte[] srcObj, String charEncode) {
        if (null == srcObj) {
            return "";
        }
        String destObj = null;
        try {
            destObj = new String(srcObj, charEncode);
        } catch (UnsupportedEncodingException e) {
        	;//log.error("", e);
        }
        return destObj;
    }

    /**
     * 判断字符串是否为空，有一个为空返回true
     *
     * @param strings <br />
     * @return <br />
     */
    public static boolean isEmpty(String... strings) {
    	 if (strings == null || strings.length == 0) {
             return true;
         }
        for (String args : strings) {
            if (null == args || "".equals(args)) {
                return true;
            }
        }
        return false;
        
    }
    
    /**
     * 检测字符串对象是否为null或空格
     * 
     * @param args <br />
     * @return <br />
     */
    public static boolean isNullOrEmpty(String args) {
        if (null == args) {
            return true;
        }
        
        return args.matches("^\\s*$");
    }
}


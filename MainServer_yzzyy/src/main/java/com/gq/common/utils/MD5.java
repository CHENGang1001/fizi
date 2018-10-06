package com.gq.common.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密MD5工具类
 *
 * @Author: xyjia
 * @Date: 2013年12月20日15:16:25
 */
public class MD5 {

    /**
     * 私有的构造函数
     */
    private MD5() {

    }

    /**
     * 全局数组
     */
    private static final String[] STR_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static final int MARK_INVERSION256 = 256;
    private static final int MARK_INVERSION16 = 16;

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte <br />
     * @return <br />
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += MARK_INVERSION256;
        }
        int iD1 = iRet / MARK_INVERSION16;
        int iD2 = iRet % MARK_INVERSION16;
        return STR_DIGITS[iD1] + STR_DIGITS[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte <br/>
     * @return <br/>
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 获得MD5加密字符串
     *
     * @param strObj <br />
     * @return <br />
     */
    public static String getMD5Code(String strObj) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            strObj = byteToString(md.digest(strObj.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();//获得MD5码异常
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();//获得MD5码异常
        }
        return strObj;
    }
}

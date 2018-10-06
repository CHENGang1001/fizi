package com.gq.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 数字工具类
 *
 * @ClassName: MathUtils
 * @Description: 数字工具类
 */
public class MathUtils {

    /**
     * 私有的构造函数
     */
    private MathUtils() {

    }

    private static final int SIX = 6;
    private static final int TEEN = 10;

    /**
     * 生成随机6位验证码
     *
     * @return <br />
     */
    public static String getVerifyCode() {
        StringBuffer verifyCode = new StringBuffer(SIX);
        Random random = new Random();
        for (int i = 0; i < SIX; i++) {
            verifyCode.append(random.nextInt(TEEN));
        }
        return verifyCode.toString();
    }

    /**
     * 生成随机6位验证码
     *
     * @return <br />
     */
    public static String getTransID() {
        return UUID.randomUUID().toString().replaceAll("-", "0");
    }
}

package com.gq.business.inforesources.bean;

import java.io.*;

/**
 * 文件工具类
 *
 * @Author: xiuyajia
 * @Date: 2/21/14 4:09 PM
 */
public class FileUtil {

    /**
     * 私有的构造函数
     */
    private FileUtil() {

    }

    private static final int BUFFER_SIZE = 1024;
    
    

    /**
     * 复制文件
     *
     * @param sourceFilePath <br />
     * @param targetFilePath <br />
     * @throws IOException <br />
     */
    public static void copyFile(String sourceFilePath, String targetFilePath) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[BUFFER_SIZE];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
}

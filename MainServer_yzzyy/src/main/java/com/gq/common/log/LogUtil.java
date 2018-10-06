package com.gq.common.log;

import net.sf.json.JSONObject;

import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;

/**
 * 日志工具类
 * @ClassName: LogUtil 
 * @Description: 日志工具类
 * @author zhixinchen
 * @date Apr 29, 2014 10:44:05 AM
 */
public final class LogUtil
{
	/**
	 * his返回结果是否成功
	 * @param body
	 * @return
	 */
	public static boolean isSuccessForHis(String body)
	{
		if (!StringUtils.isEmpty(body)
				&& (body.indexOf("APPCODE=\"1\"") != -1 || body.indexOf("APPCODE=\"2\"") != -1))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 排队叫号返回结果是否成功
	 * @param body
	 * @return
	 */
	public static boolean isSuccessForQueuing(String body)
	{
		if (!StringUtils.isEmpty(body)
				&& body.indexOf("<APPCODE>0</APPCODE>") != -1)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * OA返回结果是否成功
	 * @param body
	 * @return
	 */
	public static boolean isSuccessForOA(String body)
	{
		if (!StringUtils.isEmpty(body))
		{
			JSONObject jsonObject = JSONObject.fromObject(body);
			if (!jsonObject.isEmpty() && !jsonObject.isNullObject()
					&& ReturnCode.OK.equals(jsonObject.getString("FLAG")))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * jsd返回结果是否成功
	 * @param body
	 * @return
	 */
	public static boolean isSuccessForJSD(String body)
	{
		if (!StringUtils.isEmpty(body)
				&& (body.indexOf("<RSPCODE>1</RSPCODE>") != -1))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * jsd账户余额返回成功
	 * @param body
	 * @return
	 */
	public static boolean isSuccessForJsdOfBalance(String body)
	{
		if (!StringUtils.isEmpty(body)
				&& (body.indexOf("<RSPCODE>0000</RSPCODE>") != -1))
		{
			return true;
		}
		return false;
	}
	
	 /**
     * 信息日志打印
     * 
     * @param prefixName 前缀名称
     * @param params  参数
     */
    public static void log(String prefixName, String msgContent) {
        System.out.println(prefixName + " : " + msgContent);
    }
}

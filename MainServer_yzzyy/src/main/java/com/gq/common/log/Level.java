package com.gq.common.log;

/**
 * 日志等级标示类
 * @ClassName: Level 
 * @Description: 日志等级标示类
 * @author zhixinchen
 * @date Apr 29, 2014 10:15:45 AM
 */
public interface Level
{
	/**
	 * debug日志级别
	 */
	public static int DEBUG = 0;
	
	/**
	 * info日志级别
	 */
	public static int INFO = 1;
	
	/**
	 * warn日志级别
	 */
	public static int WARN = 2;
	
	/**
	 * error日志级别
	 */
	public static int ERROR = 3;
}

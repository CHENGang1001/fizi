package com.gq.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志管理类
 */
public class MHLogManager {

    /**
     * 私有的构造函数
     */
    private MHLogManager() {

    }

    /**
     * 获取interface日志记录器
     */
    private static Logger interfaceLog = LogManager.getLogger();
   //private static Logger interfaceLog = LoggerFactory.getLogger("INTERFACELog");

    /**
     * 获取debug日志记录器
     */
    private static Logger debugLog = LogManager.getLogger("DEBUGLog");

    /**
     * 接口日志实现类
     *
     * @param source        <br />
     * @param target        <br />
     * @param requestParm   <br />
     * @param responseParm  <br />
     * @param costTime      <br />
     * @param interfaceName <br />
     */
    public static void interfaceLog(int level, String source, String target,
			String interfaceName, String requestParm, String responseParm, long costTime)
	{
    	switch (level)
    	{
    	case Level.DEBUG : interfaceLog.debug("Level:[{}]|Source:[{}]|Target:[{}]INTERFACE:[{}]|REQPARM:[{}]|RSPPARM:[{}]|COSTTIME:[{}]",
    			                            "DEBUG",
							                source,
							                target,
							                interfaceName,
							                StringUtils.isEmpty(requestParm) ? "" : requestParm.replaceAll("\\s{2,}|\t|\r|\n", ""),
							                StringUtils.isEmpty(responseParm) ? "" : responseParm.replaceAll("\\s{2,}|\t|\r|\n", ""),
							                costTime); break;
    	case Level.INFO : interfaceLog.info("Level:[{}]|Source:[{}]|Target:[{}]INTERFACE:[{}]|REQPARM:[{}]|RSPPARM:[{}]|COSTTIME:[{}]",
    										"INFO",
							                source,
							                target,
							                interfaceName,
							                StringUtils.isEmpty(requestParm) ? "" : requestParm.replaceAll("\\s{2,}|\t|\r|\n", ""),
							                StringUtils.isEmpty(responseParm) ? "" : responseParm.replaceAll("\\s{2,}|\t|\r|\n", ""),
							                costTime); break;
    	default : interfaceLog.error("Level:[{}]|Source:[{}]|Target:[{}]INTERFACE:[{}]|REQPARM:[{}]|RSPPARM:[{}]|COSTTIME:[{}]",
    								"ERROR",
					                source,
					                target,
					                interfaceName,
					                StringUtils.isEmpty(requestParm) ? "" : requestParm.replaceAll("\\s{2,}|\t|\r|\n", ""),
					                StringUtils.isEmpty(responseParm) ? "" : responseParm.replaceAll("\\s{2,}|\t|\r|\n", ""),
					                costTime);
    	}
    }
    
    /**
     * 银联日志记录专用接口
     *
     * @param target <br />
     * @param mehtod <br />
     * @param req    <br />
     * @param resp   <br />
     */
    public static void interfaceLog(String target, String mehtod, String req, String resp) {
        interfaceLog.info("Target:[{}]|Mehtod:[{}]|REQ:[{}]|RESQ:[{}]", target, mehtod, req, resp);
    }

    /**
     * @return the debugLog
     */
    public static Logger getDebugLog() {
        return debugLog;
    }
}

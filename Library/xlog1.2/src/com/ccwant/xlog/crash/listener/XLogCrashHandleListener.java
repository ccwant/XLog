package com.ccwant.xlog.crash.listener;

/**
 * 程序崩溃监听事件
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public interface XLogCrashHandleListener {
	
	/**
	 *  程序崩溃时的处理
	 * @param logPath 日志的路径
	 */
	public void crashHandle(String logPath);
	

}

package com.ccwant.xlog.crash;

import com.ccwant.xlog.crash.listener.XLogCrashHandleListener;

/**
 * 应用程序的崩溃处理基础接口
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public interface CrashHandler {

	public void crashHandler(Thread thread, Throwable ex);
	public XLogCrashHandleListener getCrashHandleListener();
	public void setCrashHandleListener(XLogCrashHandleListener crashHandleListener);
}

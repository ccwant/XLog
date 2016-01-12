package com.ccwant.xlog.crash;

import com.ccwant.xlog.crash.listener.XLogCrashHandleListener;

/**
 * 崩溃处理程序的抽象类
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public abstract class AbstractCrashHandler implements CrashHandler {
	private static final Thread.UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    private XLogCrashHandleListener mCrashHandleListener;
    @Override
    public void crashHandler(Thread thread, Throwable ex){
        sDefaultHandler.uncaughtException(thread, ex);

    }
    @Override
	public XLogCrashHandleListener getCrashHandleListener() {
		return mCrashHandleListener;
	}

	@Override
	public void setCrashHandleListener(XLogCrashHandleListener crashHandleListener) {
		this.mCrashHandleListener = crashHandleListener;
	}
    
    
    
}

package com.ccwant.xlog.crash;

import java.lang.Thread.UncaughtExceptionHandler;

import android.os.Looper;

/**
 * 崩溃捕捉器
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public class CrashCatcher implements UncaughtExceptionHandler{

	private CrashHandler mCrashHandler;
	public CrashCatcher(CrashHandler crashHandler){
		mCrashHandler=crashHandler;
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		  new Thread(new CrashHandlerRunnable(thread, ex)).start();
	}
	class CrashHandlerRunnable implements Runnable{
		Thread thread;
		Throwable ex;
		public CrashHandlerRunnable(Thread thread, Throwable ex){
			this.thread=thread;
			this.ex=ex;
		}
		@Override
		public void run() {
			 Looper.prepare();
             try {
             	mCrashHandler.crashHandler(thread, ex);
             }catch (Exception e) {
                 e.printStackTrace();
             }
             Looper.loop();
		}
	}
}

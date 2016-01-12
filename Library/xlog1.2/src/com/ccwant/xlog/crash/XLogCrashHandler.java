package com.ccwant.xlog.crash;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.ccwant.xlog.XLogConfiguration;
import com.ccwant.xlog.cache.SaveLogHandler;
import com.ccwant.xlog.crash.listener.XLogCrashHandleListener;
import com.ccwant.xlog.utils.XLogUtils;

/**
 * 应用程序的崩溃处理
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public class XLogCrashHandler extends AbstractCrashHandler{

	public static final String TAG = "XLogCrashHandler"; 
    /**
     * XLog的配置信息
     */
    private XLogConfiguration mConfiguration;

    /**
     * 用来处理异常发生时的动作
     */
    private XLogCrashHandleListener mCrashHandleListener;
    /**
     * 缓存Log日志的任务执行器
     */
  	private Executor taskExecutorForCachedLogs;
  	/**
  	 * 获取设备信息的任务执行器
  	 */
  	private ExecutorService mSingleExecutor = Executors.newSingleThreadExecutor();
  	/**
  	 * 获取设备信息的同步任务
  	 */
  	private FutureTask<String> mFuture;
  	/**
  	 * 设置获取设备信息的超时时间
  	 */
    private int TIMEOUT = 10000;
    /**
     * 保证只有一个CrashHandler实例 
     * @param executor 缓存Log日志的任务执行器
     */
    public XLogCrashHandler(Executor executor) {  
    	taskExecutorForCachedLogs=executor;
    }  
    /**
     * 初始化 
     * @param configuration XLog的配置信息
     * @param listener 用来处理异常发生时的动作(可以为空)
     */
    public void init(XLogConfiguration configuration,XLogCrashHandleListener listener) {  
      //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(new CrashCatcher(this)); 
        if (configuration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}
		if (this.mConfiguration == null) {
			this.mConfiguration = configuration;
			
		} else {
			Log.w(TAG,"Try to initialize XLogConfiguration which had already been initialized before. " + "To re-init XLogConfiguration with new configuration call XLog.destroy() at first.");
		}
		mCrashHandleListener=listener;
    }  
	@Override
	public void crashHandler(Thread thread, Throwable ex) {
		
		if(mFuture != null && !mFuture.isDone()){
            mFuture.cancel(false);
        }
		mFuture=new FutureTask<String>(new CrashInfoCallable(thread,ex));   
		mSingleExecutor.execute(mFuture);   
    	try {
            String content=mFuture.get(TIMEOUT, TimeUnit.MILLISECONDS);
            String filePath=save(content);
            if (mCrashHandleListener != null) {
    			mCrashHandleListener.crashHandle(filePath);
    		}
        } catch (Exception e) {
            Log.e(TAG, "Get crash info time out !",e);
        }
		//super.crashHandler(thread, ex);
	}
	/**
	 * 将崩溃日志写入缓存,返回文件路径
	 * @param content
	 */
    private String save(String content){
    	final String dirName= mConfiguration.getXLogParames().mCacheDir;
    	final String fileName=XLogUtils.date();
    	final String textContent=content;
    	SaveLogHandler handler=new SaveLogHandler(taskExecutorForCachedLogs);
    	return handler.save(dirName, fileName, textContent);
    }
}

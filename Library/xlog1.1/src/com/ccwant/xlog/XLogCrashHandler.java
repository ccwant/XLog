package com.ccwant.xlog;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.ccwant.xlog.XLog.saveCache;
import com.ccwant.xlog.cache.XLogDiskCache;
import com.ccwant.xlog.listener.XLogCrashHandleListener;
import com.ccwant.xlog.utils.XLogUtils;

import android.R.integer;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 用于处理接收应用程序的崩溃信息
 * @author Administrator
 *
 */
public class XLogCrashHandler  implements UncaughtExceptionHandler{

	public static final String TAG = "XLogCrashHandler"; 
	/**
	 * 系统默认的UncaughtException处理类   
	 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
    /**
     * XLog的配置信息
     */
    private XLogConfiguration mConfiguration;
    /**
     * 用来存储设备信息和异常信息  
     */
    private Map<String, String> infos = new HashMap<String, String>();  
    /**
     * 用来处理异常发生时的动作
     */
    private XLogCrashHandleListener mCrashHandleListener;
    /**
     * 缓存Log日志的任务执行器
     */
  	private Executor taskExecutorForCachedLogs;
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
     
        //获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this); 
        
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
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}
	 /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息返回true;否则返回false. 
     */  
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (mCrashHandleListener != null) {
					mCrashHandleListener.crashHandle();
				}
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo();
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}  
    
    /** 
     * 收集设备参数信息 
     * @param ctx 
     */  
    public void collectDeviceInfo() {  
        
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                Log.e(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
                Log.e(TAG, "an error occured when collect crash info", e);  
            }  
        }  
    } 
    /** 
     * 保存错误信息到文件中 
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(String.format("[%s:CRASH]%s\r\n", TAG, key + "=" + value));
			Log.e(TAG, key + "=" + value);
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(String.format("[%s:CRASH]%s\r\n", TAG, result));
		
		write(sb.toString());
		Log.e(TAG, "-------------------");
		Log.e(TAG, result);
		return null;
	} 
	/**
	 * 将崩溃日志写入缓存
	 * @param content
	 */
    private void write(String content){

    	final String dirName= mConfiguration.getCacheDir();
    	final String fileName=XLogUtils.date();
    	final String textContent=String.format("[%s:CRASH]%s\r\n", TAG,content);
    	taskExecutorForCachedLogs.execute(new saveCache(dirName, fileName, textContent));
    }
    class saveCache implements Runnable{
    	String mDirName;
    	String mFileName;
    	String mTextContent;
    	public saveCache(String dirName,String fileName,String content){
    		mDirName=dirName;
    		mFileName=fileName;
    		mTextContent=content;
    	}
		@Override
		public void run() {
			XLogDiskCache.save(mDirName,mFileName,mTextContent);
		}
    }
}

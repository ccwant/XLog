package com.ccwant.xlog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;




import com.ccwant.xlog.cache.XLogDiskCache;
import com.ccwant.xlog.listener.XLogCrashHandleListener;
import com.ccwant.xlog.utils.XLogUtils;

import android.os.Environment;
import android.util.Log;


/**
 * Log日志
 * @author Ccwant
 * September 2st
 */
public class XLog {

	/**
	 * Log日志级别
	 * 调试颜色为黑色的，任何消息都会输出
	 * 这里的v代表verbose啰嗦的意思
	 */
	public static final int VERBOSE = 2;
	/**
	 * Log日志级别
	 * 输出为蓝色的，仅输出debug调试的意思
	 */
	public static final int DEBUG = 3;
	/**
	 * Log日志级别
	 * 输出为绿色，一般提示性的消息information
	 */
	public static final int INFO = 4;
	/**
	 * Log日志级别
	 * 输出为橙色，可以看作为warning警告
	 */
	public static final int WARN = 5;
	/**
	 * Log日志级别
	 * 输出为红色，可以想到error错误
	 */
	public static final int ERROR = 6;
	/**
	 * Log日志级别
	 * Android 4.0 的API，此处暂不适用
	 */
	public static final int ASSERT = 7;
	/**
	 * 当前的Log日志级别
	 */
	public int mLogLevel;
	/**
	 * Log的TAG
	 */
	private String mTag;
	/**
	 * 默认线程池大小
	 */
	public static final int DEFAULT_THREAD_POOL_SIZE = 3;

	/**
	 * 默认线程优先级
	 */
	public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;
	/**
	 * 线程池大小
	 */
	private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
	/**
	 * 线程优先级
	 */
	private int threadPriority = DEFAULT_THREAD_PRIORITY;
	
	
	/**
	 * Log的单例模式
	 */
	private volatile static XLog instance;
	
	/**
	 * 配置信息
	 */
	private XLogConfiguration mConfiguration;
	/**
	 * 崩溃处理
	 */
	private XLogCrashHandler mCrashHandler;
	/**
	 * 缓存Log日志的任务执行器
	 */
	private Executor taskExecutorForCachedLogs;
	
	public XLog(){ 
		taskExecutorForCachedLogs=XLogDefaultConfigurationFactory.createExecutor(threadPoolSize, threadPriority);
	}	
	public static XLog makeLog(){
		return makeLog("XLog",VERBOSE);
	}
	public static XLog makeLog(String tag){
		return makeLog(tag,VERBOSE);
	}
	public static XLog makeLog(int level){
		return makeLog("XLog",level);
	}
	public static XLog makeLog(String tag,int level){
		
		if (instance == null) {
			synchronized (XLog.class) {
				if (instance == null) {
					instance=new XLog();
				}
			}
		}
		instance.setTag(tag);
		instance.setLogLevel(level);
		return instance;
	}
	public synchronized void init(XLogConfiguration configuration,XLogCrashHandleListener listener){
		if (configuration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}
		if (this.mConfiguration == null) {
			this.mConfiguration = configuration;
		} else {
			Log.w(mTag,"Try to initialize XLogConfiguration which had already been initialized before. " + "To re-init XLogConfiguration with new configuration call XLog.destroy() at first.");
		}
		if(configuration.getCrash()){
			if(listener==null){
				throw new IllegalArgumentException("XLog listener can not be initialized with null");
			}
			if (this.mCrashHandler == null) {
				mCrashHandler=new XLogCrashHandler(taskExecutorForCachedLogs);
				mCrashHandler.init(configuration,listener);
			}
		}
		
		
		
		
	}
	
	public void i(Object str) {
		if (mConfiguration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}else if (mConfiguration.getDebug()) {
			info(str);
		}
	}
	public void v(Object str) {
		if (mConfiguration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}else if (mConfiguration.getDebug()) {
			verbose(str);
		}
    }
	public void w(Object str) {
		if (mConfiguration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}else if (mConfiguration.getDebug()) {
			warn(str);
		}
    }
    public void e(Object str) {
    	if (mConfiguration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}else if (mConfiguration.getDebug()) {
    		error(str);
    	}
    }
    public void e(Exception ex) {
    	if (mConfiguration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}else if (mConfiguration.getDebug()) {
    		error(ex);
    	}
    }
	public void d(Object str) {
		if (mConfiguration == null) {
			throw new IllegalArgumentException("XLog configuration can not be initialized with null");
		}else if (mConfiguration.getDebug()) {
			debug(str);
		}
    }
	
	public XLogConfiguration getConfiguration() {
		return mConfiguration;
	}
	public void setConfiguration(XLogConfiguration mConfiguration) {
		this.mConfiguration = mConfiguration;
	}
	private String getTag() {
		return mTag;
	}
	private void setTag(String tag) {
		this.mTag = tag;
	}
	public int getLogLevel() {
		return mLogLevel;
	}
	public void setLogLevel(int logLevel) {
		this.mLogLevel = logLevel;
	}
	private void info(Object str) {
	    if (mLogLevel <= INFO) {	        
	        String name = getFunctionName();
	        String ls=(name==null?str.toString():(name+" - "+str));
	        Log.i(mTag, ls);
	        if(mConfiguration.getCache()){
	        	write(INFO,ls);
            }
	    }
	}
	private void verbose(Object str) {
        if (mLogLevel <= VERBOSE) {
            String name = getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.v(mTag, ls);    
            if(mConfiguration.getCache()){
            	write(VERBOSE,ls);
            }
        }
	}
	private void warn(Object str) {
	    if (mLogLevel <= WARN) {
            String name = getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.w(mTag, ls);
            if(mConfiguration.getCache()){
            	write(WARN,ls);
            }
	    }
	}
	private void error(Object str) {
        if (mLogLevel <=ERROR) {            
            String name = getFunctionName();
            String ls=(name==null?str.toString():(name+" - "+str));
            Log.e(mTag, ls);
            if(mConfiguration.getCache()){
            	write(ERROR,ls);
            }
        }
	}
	private void error(Exception ex) {
	    if (mLogLevel <= ERROR) {
	        StringBuffer sb = new StringBuffer();
	        String name = getFunctionName();
	        StackTraceElement[] sts = ex.getStackTrace();

	        if (name != null) {
                sb.append(name+" - "+ex+"\r\n");
            } else {
                sb.append(ex+"\r\n");
            }
	        
	        if (sts != null && sts.length > 0) {
	            for (StackTraceElement st:sts) {
	                if (st != null) {
	                    sb.append("[ "+st.getFileName()+":"+st.getLineNumber()+" ]\r\n");
	                }
	            }
	        }
	        Log.e(mTag, sb.toString());
	        if(mConfiguration.getCache()){
	        	write(ERROR,sb.toString());
            }
	    }
	}
	 private void debug(Object str) {
	        if (mLogLevel <= DEBUG) {
	            String name = getFunctionName();
	            String ls = (name == null?str.toString():(name+" - "+str));
	            Log.d(mTag, ls);
	            if(mConfiguration.getCache()){
	            	write(DEBUG,ls);
	            }
	        }
	}
	/**
	 * 获取函数名称
	 * @return
	 */
	private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st:sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "["+Thread.currentThread().getId()+": "+st.getFileName()+": "+st.getMethodName()+":"+st.getLineNumber()+"]"+XLogUtils.time();
        }
        
        return null;
	}
	
    private String getLevelTag(int logLevel){
    	String levelTag=null;
    	switch (logLevel) {
		case VERBOSE:
			levelTag="V";
			break;
		case DEBUG:
			levelTag="D";
			break;
		case INFO:
			levelTag="I";
			break;
		case WARN:
			levelTag="W";
			break;
		case ERROR:
			levelTag="E";
			break;
		case ASSERT:
			levelTag="A";
			break;
		default:
			break;
		}
    	return levelTag;
    }
    private void write(int logLevel,String content){

    	final String dirName= mConfiguration.getCacheDir();
    	final String fileName=XLogUtils.date();
    	final String textContent=String.format("[%s:%s]%s\r\n", mTag,getLevelTag(logLevel),content);
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
package com.ccwant.xlog.crash;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import android.os.Build;
import android.util.Log;

import com.ccwant.xlog.XLogConfiguration;
import com.ccwant.xlog.cache.SaveLogHandler;
import com.ccwant.xlog.utils.XLogUtils;

public class CrashInfoCallable implements Callable<String>{

    /**
     * 用来存储设备信息和异常信息  
     */
    private Map<String, String> infos = new HashMap<String, String>();  
    
	Thread thread;
	Throwable ex;

	private String TAG;
	public CrashInfoCallable(Thread thread, Throwable ex){
		this.thread=thread;
		this.ex=ex;
	}
	@Override
	public String call() throws Exception {
		// 收集设备参数信息
    	collectDeviceInfo();
    	// 保存日志文件
		return saveCrashInfo2File(ex);
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
		sb.append(String.format("=========================%s=======================",XLogUtils.time()));
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
		sb.append("=========================<END>=======================");
		
		Log.e(TAG, "-------------------");
		Log.e(TAG, result);
		
		return sb.toString();
		
	} 
	
}

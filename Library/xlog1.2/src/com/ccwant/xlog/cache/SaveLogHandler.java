package com.ccwant.xlog.cache;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import android.util.Log;

/**
 * 保存日志到SD卡的处理程序
 * Save log to SDCard handler
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public class SaveLogHandler {
	
	Executor mExecutor;
	FutureTask<String> mFuture;
	private String TAG;
	private long TIMEOUT=3000;
	
	public SaveLogHandler(Executor executor){
		mExecutor=executor;
	}
	public String save(String dirName,String fileName,String content){
		if(mFuture != null && !mFuture.isDone()){
            mFuture.cancel(false);
        }
		mFuture=new FutureTask<String>(new SaveLogCallable(dirName, fileName, content));
		mExecutor.execute(mFuture);   
		try {
            return mFuture.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Save log info time out !",e);
        }
		return null;
	}
}

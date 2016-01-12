package com.ccwant.xlog.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 保存Log日志的线程执行类
 * @author CCwant
 * https://github.com/CCwant/XLog
 */
public class SaveLogCallable implements Callable<String>{

	String mDirName;
	String mFileName;
	String mTextContent;
	public SaveLogCallable(String dirName,String fileName,String content){
		mDirName=dirName;
		mFileName=fileName;
		mTextContent=content;
	}
	@Override
	public String call() throws Exception {
		return DiskCache.save(mDirName, mFileName, mTextContent);
	}
	
}

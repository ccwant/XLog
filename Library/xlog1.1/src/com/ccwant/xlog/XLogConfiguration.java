package com.ccwant.xlog;

import android.content.Context;
import android.os.Handler;

public class XLogConfiguration {

	

	/**
	 * 是否允许调试
	 */
	private boolean mDebug ;
	
	/**
	 * 是否允许收集崩溃日志
	 */
	private boolean mCrash;
	/**
	 * 是否缓存Log日志文件
	 */
	private boolean mCache;

	/**
	 * 缓存文件夹名称
	 */
	private String mCacheDir;

	
	public XLogConfiguration(){
		
	}
	
	public boolean getDebug() {
		return mDebug;
	}

	public void setDebug(boolean debug) {
		mDebug = debug;
	}

	
	public boolean getCrash() {
		return mCrash;
	}

	public void setCrash(boolean mCrash) {
		this.mCrash = mCrash;
	}

	public boolean getCache() {
		return mCache;
	}

	public void setCache(boolean cache) {
		mCache = cache;
	}

	public String getCacheDir() {
		return mCacheDir;
	}

	public void setCacheDir(String cacheDir) {
		mCacheDir = cacheDir;
	}

	
	
	
}

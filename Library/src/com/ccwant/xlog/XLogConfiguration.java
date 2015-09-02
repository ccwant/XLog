package com.ccwant.xlog;

public class XLogConfiguration {

	/**
	 * 是否允许调试
	 */
	private boolean mDebug ;
	/**
	 * 是否缓存Log日志文件
	 */
	private boolean mCache;
	
	/**
	 * 缓存文件夹名称
	 */
	private String mCacheDir;

	
	public boolean getDebug() {
		return mDebug;
	}

	public void setDebug(boolean debug) {
		mDebug = debug;
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

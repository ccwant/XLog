package com.ccwant.xlog;

import android.content.Context;

/**
 * https://github.com/CCwant/XLog
 * @author CCwant
 *
 */
public class XLogConfiguration {

	
	private Context mContext;
	private XLogConfiguration.XLogParames mXLogParames;
	private XLogConfiguration.MailParames mMailParames;
	
	private XLogConfiguration(Context context){
		mContext=context;
	}
	
	
	public XLogConfiguration.XLogParames getXLogParames() {
		return mXLogParames;
	}
	public void setXLogParames(XLogConfiguration.XLogParames parames) {
		this.mXLogParames = parames;
	}
	
	public XLogConfiguration.MailParames getMailParames() {
		return mMailParames;
	}

	public void setMailParames(XLogConfiguration.MailParames mMailParames) {
		this.mMailParames = mMailParames;
	}



	public static class XLogParames{
		/**
		 * 是否允许调试
		 */
		public boolean mDebug ;
		
		/**
		 * 是否允许收集崩溃日志
		 */
		public boolean mCrash;
		/**
		 * 是否缓存Log日志文件
		 */
		public boolean mCache;

		/**
		 * 缓存文件夹名称
		 */
		public String mCacheDir;

		
		
		
	}
    public static class MailParames{

		public String mSmtp ;
		public String mUser;
		public String mPassword;
		public boolean mDebug;
		public String mFrom;
		public String mTo;
		public String mCopyto;
		
	}
	
	

	
	
	public static class Builder{
		private final XLogConfiguration.XLogParames xlogParmes;
		private final XLogConfiguration.MailParames MailParmes;
		private final Context  c;
		
		public Builder(Context context){
			c=context;
			xlogParmes=new XLogConfiguration.XLogParames();
			MailParmes=new XLogConfiguration.MailParames();
		}
		
		public boolean isDebug() {
			return xlogParmes.mDebug;
		}

		public Builder setDebug(boolean debug) {
			this.xlogParmes.mDebug = debug;
			return this;
		}

		public boolean isCrash() {
			return xlogParmes.mCrash;
		}

		public Builder setCrash(boolean crash) {
			this.xlogParmes.mCrash = crash;
			return this;
		}

		public boolean isCache() {
			return xlogParmes.mCache;
		}

		public Builder setCache(boolean cache) {
			this.xlogParmes.mCache = cache;
			return this;
		}

		public String getCacheDir() {
			return xlogParmes.mCacheDir;
		}

		public Builder setCacheDir(String cacheDir) {
			this.xlogParmes.mCacheDir = cacheDir;
			return this;
		}
		
		public String getMailSmtp() {
			return MailParmes.mSmtp;
		}
		public Builder setMailSmtp(String smtp) {
			this.MailParmes.mSmtp = smtp;
			return this;
		}
		public String getMailUser() {
			return MailParmes.mUser;
		}
		public Builder setMailUser(String user) {
			this.MailParmes.mUser = user;
			return this;
		}
		public String getMailPassword() {
			return MailParmes.mPassword;
		}
		public Builder setMailPassword(String password) {
			this.MailParmes.mPassword = password;
			return this;
		}
		public boolean isMailDebug() {
			return MailParmes.mDebug;
		}
		public Builder setMailDebug(boolean debug) {
			this.MailParmes.mDebug = debug;
			return this;
		}
		public String getMailFrom() {
			return MailParmes.mFrom;
		}
		public Builder setMailFrom(String from) {
			this.MailParmes.mFrom = from;
			return this;
		}
		public String getMailTo() {
			return MailParmes.mTo;
		}
		public Builder setMailTo(String to) {
			this.MailParmes.mTo = to;
			return this;
		}
		public String getMailCopyto() {
			return MailParmes.mCopyto;
		}
		public Builder setMailCopyto(String copyto) {
			this.MailParmes.mCopyto = copyto;
			return this;
		}
		
		
		public XLogConfiguration create(){
			
			XLogConfiguration cinfiguration=new XLogConfiguration(c);
			cinfiguration.setXLogParames(xlogParmes);
			cinfiguration.setMailParames(MailParmes);
			
			return cinfiguration;
		}
	}

	
	
	
}

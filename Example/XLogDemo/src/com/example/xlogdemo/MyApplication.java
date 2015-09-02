package com.example.xlogdemo;

import com.ccwant.xlog.XLog;
import com.ccwant.xlog.XLogConfiguration;

import android.app.Application;

public class MyApplication extends Application{
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		XLogConfiguration configuration=new XLogConfiguration();
		//设置是否缓存
		configuration.setCache(true);
		//设置是否调试，为false时，将取消日志的输出
		configuration.setDebug(true);
		//设置缓存目录名，所有日志都在sd卡目录下
		configuration.setCacheDir("MyLog");
		//初始化Xlog，只需要在程序开始运行时初始化
		XLog.makeLog().init(configuration);
	}

}

package com.example.xlogdemo;

import com.ccwant.xlog.XLog;
import com.ccwant.xlog.XLogConfiguration;
import com.ccwant.xlog.listener.XLogCrashHandleListener;
import android.app.Application;
import android.widget.Toast;

public class MyApplication extends Application implements XLogCrashHandleListener{
	@Override
	public void onCreate() {
		super.onCreate();
		XLogConfiguration configuration=new XLogConfiguration();
		//设置是否缓存
		configuration.setCache(true);
		//设置是否处理崩溃信息
		configuration.setCrash(true);
		//设置是否调试，为false时，将取消日志的输出
		configuration.setDebug(true);
		//设置缓存目录名，所有日志都在sd卡目录下
		configuration.setCacheDir("MyLog");
		//初始化Xlog，只需要在程序开始运行时初始化
		XLog.makeLog().init(configuration,this);
	}
	@Override
	public void crashHandle() {
		//程序崩溃时的处理
		Toast.makeText(getApplicationContext(), "很抱歉,程序出现异常,即将退出...", Toast.LENGTH_LONG).show();  
	}
}

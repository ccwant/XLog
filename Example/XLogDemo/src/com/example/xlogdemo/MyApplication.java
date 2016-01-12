package com.example.xlogdemo;

import java.io.File;
import java.nio.channels.AlreadyConnectedException;

import com.ccwant.xlog.XLog;
import com.ccwant.xlog.XLogConfiguration;
import com.ccwant.xlog.crash.listener.XLogCrashHandleListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application implements
		XLogCrashHandleListener {

	@Override
	public void onCreate() {
		super.onCreate();
		
		XLogConfiguration.Builder builder = new XLogConfiguration.Builder(this);
		// 设置是否缓存
		builder.setCache(true);
		// 设置是否处理崩溃信息
		builder.setCrash(true);
		// 设置是否调试，为false时，将取消日志的输出
		builder.setDebug(true);
		// 设置缓存目录名，所有日志都在sd卡目录下
		builder.setCacheDir("MyLog");
		
		// 设置是否开启调试电子邮件
		builder.setMailDebug(true);
		// 设置电子邮件SMTP 主机
		builder.setMailSmtp("smtp.126.com");
		// 设置电子邮件的用户
		builder.setMailUser("xx@126.com");
		// 设置电子邮件的用户密码
		builder.setMailPassword("xxxxxxx");
		// 设置电子邮件的发件人
		builder.setMailFrom("xx@126.com");
		// 设置电子邮件的收件人
		builder.setMailTo("xx@xxx.com");
		// 设置电子邮件的抄送人
		builder.setMailCopyto("xx@xxx.com");

		// 初始化Xlog，只需要在程序开始运行时初始化
		XLog.makeLog().init(this, builder.create(), this);
	}

	@Override
	public void crashHandle(String filePath) {
		final String path = filePath;
		Activity activity = ActivityManager.getInstance().getCurrentActivity();
		// 程序崩溃时的处理
		new AlertDialog.Builder(activity).setTitle("提示").setCancelable(false)
				.setMessage("亲，程序崩溃了,是否发送报告给我们，以便更好的提升APP的兼容性？")
				.setPositiveButton("发送", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (path != null) {//判断path是否为空
							File file = new File(path);
							if (file.exists() && file.isFile()) {
								boolean sendResult=XLog.makeLog().sendToMail("Android日志","很抱歉,程序出现异常,即将退出...", file);
								if(sendResult){
									Toast.makeText(getApplicationContext(), "发送成功！",Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(getApplicationContext(), "发送失败！",Toast.LENGTH_LONG).show();
								}
							}
						}
						ActivityManager.getInstance().exit();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityManager.getInstance().exit();
					}
				})
				.setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						ActivityManager.getInstance().exit();
					}
				}).create().show();
	}

}

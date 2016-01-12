# XLog

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Android专业版Log日志打印收集项目，他可以使你的开发变得更加高效，BUG信息轻松获取

当程序发生异常崩溃时，轻松发送异常日志到指定邮箱

如果你喜欢这个项目请点一下右上方的Star按钮哦，如果你希望订阅，请点击Wacth。非常感谢你的支持！

## 简介

### Logcat效果

![image](https://github.com/CCwant/XLog/blob/master/doc/boot1.png)

### 缓存效果

![image](https://github.com/CCwant/XLog/blob/master/doc/boot2.png)

### 崩溃日志打印效果

![image](https://github.com/CCwant/XLog/blob/master/doc/boot3.png)

### Android程序崩溃运行效果

![image](https://github.com/CCwant/XLog/blob/master/doc/boot4.png)

### 使用前，你需要添加以下权限
``` java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 除此之外，你还需要导入以下Jar包(提供邮件服务)
``` java
activation.jar
additionnal.jar
mail.jar
```

### 示例
``` java

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
		
		//初始化Xlog，只需要在程序开始运行时初始化
		XLog.makeLog().init(configuration,this);
	}
	@Override
	public void crashHandle(String filePath) {
		final String path = filePath;
		//程序崩溃时的处理
		Toast.makeText(getApplicationContext(), "很抱歉,程序出现异常,即将退出...", Toast.LENGTH_LONG).show();  
		//一般Xlog只在程序发生异常崩溃时才发送邮件信息
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
	}
}
```

``` java
		mBtnTestLog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打印Log日志
				XLog.makeLog().v("this is verbose");
				XLog.makeLog().d("this is debug");
				XLog.makeLog().e("this is error");
				XLog.makeLog().i("this is info");
				XLog.makeLog().w("this is warn");
				XLog.makeLog(TAG).i("this is info");
			}
		});
		mBtnTestCrash=(Button)findViewById(R.id.btn_test_crash);
		mBtnTestCrash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 主动迫使程序发生崩溃
				int[] h=new int[0];
				h[3]=1;
			}
		});
```








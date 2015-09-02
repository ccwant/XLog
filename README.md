# XLog

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Android专业版Log日志打印收集项目，他可以使你的开发变得更加高效，BUG信息也能轻松获取


## 简介

### Logcat效果

![image](https://github.com/CCwant/XLog/blob/master/doc/boot1.png)

### 缓存效果

![image](https://github.com/CCwant/XLog/blob/master/doc/boot1.png)

### 使用前，你需要添加以下权限
``` java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
### 示例
``` java
		XLogConfiguration configuration=new XLogConfiguration();
		//设置是否缓存
		configuration.setCache(true);
		//设置是否调试，为false时，将取消日志的输出
		configuration.setDebug(true);
		//设置缓存目录名，所有日志都在sd卡目录下
		configuration.setCacheDir("XX");
		//初始化Xlog，只需要在程序开始运行时初始化
		XLog.makeLog().init(configuration);
```

``` java
		XLog.makeLog().v("this is verbose");
		XLog.makeLog().d("this is debug");
		XLog.makeLog().e("this is error");
		XLog.makeLog().i("this is info");
		XLog.makeLog().w("this is warn");

		XLog.makeLog(TAG).i("this is info");
```






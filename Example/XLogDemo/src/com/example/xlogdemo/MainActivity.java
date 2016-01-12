package com.example.xlogdemo;



import com.ccwant.xlog.XLog;
import com.ccwant.xlog.XLogConfiguration;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private String TAG="MainActivity";
	private Button mBtnTestLog;
	private Button mBtnTestCrash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActivityManager.getInstance().addActivity(this);

		mBtnTestLog=(Button)findViewById(R.id.btn_test_log);
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
				int i=1/0;
//				int[] h=new int[0];
//				h[3]=1;
//				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this).setMessage("很抱歉,程序出现异常,即将退出...").setTitle("提示");
//				dialog.create().show();
			
			}
		});
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.getInstance().removeActivity(this);
	}
	
}

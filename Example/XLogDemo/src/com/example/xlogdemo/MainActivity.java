package com.example.xlogdemo;



import com.ccwant.xlog.XLog;
import com.ccwant.xlog.XLogConfiguration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private String TAG="MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		XLog.makeLog().v("this is verbose");
		XLog.makeLog().d("this is debug");
		XLog.makeLog().e("this is error");
		XLog.makeLog().i("this is info");
		XLog.makeLog().w("this is warn");
		
		XLog.makeLog(TAG).i("this is info");
	}

	
}

package ru.yandex.mobile_school;

import android.app.Application;
import android.content.res.Configuration;

public class MyApplication extends Application {

	private static StringBuilder stringBuilder;

	public static StringBuilder getStringBuilder() {
		if (stringBuilder == null) {
			stringBuilder = new StringBuilder();
		}
		return stringBuilder;
	}


	private void addToLog(String text) {
		getStringBuilder().append(text).append("\n");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		addToLog("Application.onCreate");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		addToLog("Application.onTerminate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		addToLog("Application.onConfigurationChanged");
	}
}

package ru.yandex.mobile_school;

import android.app.Application;
import android.content.Context;

public class YMSApplication extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		YMSApplication.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return YMSApplication.context;
	}

}

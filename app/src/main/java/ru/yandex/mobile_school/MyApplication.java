package ru.yandex.mobile_school;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by hash on 07/04/2017.
 */

public class MyApplication extends Application {

	public static String TAG = "MS_APP";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Application.onCreate");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "Application.onTerminate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "Application.onConfigurationChanged");
	}
}

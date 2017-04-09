package ru.yandex.mobile_school;

import android.app.Application;
import android.content.res.Configuration;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by hash on 07/04/2017.
 */

public class MyApplication extends Application {

	public Queue<String> log;

	public void addToLog(String text) {
		log.add(text);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		log = new ArrayDeque<>();
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

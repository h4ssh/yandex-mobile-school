package ru.yandex.mobile_school;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MyActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(MyApplication.TAG, "Activity.onCreate");

		addViewGroup();
	}

	private void addViewGroup() {
		MyViewGroup viewGroup = new MyViewGroup(this);
		MyView view = new MyView(this);
		view.setText("Exit");
		view.setLayoutParams(new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		viewGroup.addView(view);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(Intent.ACTION_MAIN);
				homeIntent.addCategory( Intent.CATEGORY_HOME );
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(homeIntent);
			}
		});
		setContentView(viewGroup);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(MyApplication.TAG, "Activity.onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(MyApplication.TAG, "Activity.onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(MyApplication.TAG, "Activity.onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(MyApplication.TAG, "Activity.onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(MyApplication.TAG, "Activity.onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(MyApplication.TAG, "Activity.onDestroy");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(MyApplication.TAG, "Activity.onConfigurationChanged");
	}
}

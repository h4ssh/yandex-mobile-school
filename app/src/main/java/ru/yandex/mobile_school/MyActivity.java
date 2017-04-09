package ru.yandex.mobile_school;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyActivity extends AppCompatActivity {

	private TextView logView;
	private Button addViewButton;
	private Button removeViewButton;
	private Button exitButton;
	private MyLinearLayout mMyLinearLayout;
	private MyImageView mMyImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addToLog("Activity.onCreate");
		setContentView(R.layout.activity_main);
		customizeViews();
	}

	private void customizeViews() {
		mMyLinearLayout = (MyLinearLayout)findViewById(R.id.my_view_group);
		addViewButton = (Button)findViewById(R.id.add_view_button);
		removeViewButton = (Button)findViewById(R.id.remove_view_button);
		exitButton = (Button)findViewById(R.id.exit_button);
		logView = (TextView)findViewById(R.id.log_view);
		mMyImageView = new MyImageView(this);
		mMyImageView.setImageResource(R.drawable.android_win);
		mMyImageView.setScaleType(ImageView.ScaleType.CENTER);
		mMyImageView.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		removeViewButton.setEnabled(false);

		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(Intent.ACTION_MAIN);
				homeIntent.addCategory( Intent.CATEGORY_HOME );
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(homeIntent);
			}
		});

		addViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMyLinearLayout.addView(mMyImageView);
				removeViewButton.setEnabled(true);
				addViewButton.setEnabled(false);
			}
		});

		removeViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMyLinearLayout.removeView(mMyImageView);
				removeViewButton.setEnabled(false);
				addViewButton.setEnabled(true);
			}
		});
	}
/*
	private void addViews() {
		MyLinearLayout viewGroup = new MyLinearLayout(this);
		viewGroup.setOrientation(LinearLayout.VERTICAL);
		Button exitButton = new Button(this);
		exitButton.setText("Exit");
		exitButton.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f));
		exitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		ScrollView scrollView = new ScrollView(this);
		scrollView.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));


		logView = new TextView(this);
		logView.setLayoutParams(new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		scrollView.addView(logView);

		viewGroup.addView(scrollView);
		viewGroup.addView(exitButton);
		setContentView(viewGroup);
	}
*/
	@Override
	protected void onStart() {
		super.onStart();
		addToLog("Activity.onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		addToLog("Activity.onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		addToLog("Activity.onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		addToLog("Activity.onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		addToLog("Activity.onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		addToLog("Activity.onDestroy");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		addToLog("Activity.onConfigurationChanged");
	}

	public void addToLog(String text) {
		MyApplication myApp = (MyApplication) getApplication();
		if (logView != null) {
			for (String logString : myApp.log) {
				logView.append(logString + "\n");
			}
			myApp.log.clear();
			logView.append(text + "\n");
		} else {
			myApp.addToLog(text);
		}
	}
}
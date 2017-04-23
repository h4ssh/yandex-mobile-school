package ru.yandex.mobile_school;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;

public class ColorPickerActivity extends SingleFragmentActivity {

	static final String EXTRA_COLOR = "color";
	static final String EXTRA_TITLE = "title";
	static final String EXTRA_DESCR = "description";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getExtras() == null)
			setTitle(R.string.color_picker_activity_add_title);
		else
			setTitle(R.string.color_picker_activity_edit_title);

	}

	@Override
	protected Fragment createFragment() {
		@ColorInt int color = Color.WHITE;
		String title = null;
		String descr = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey(EXTRA_COLOR))
				color = extras.getInt(EXTRA_COLOR);
			if (extras.containsKey(EXTRA_TITLE))
				title = extras.getString(EXTRA_TITLE);
			if (extras.containsKey(EXTRA_DESCR))
				descr = extras.getString(EXTRA_DESCR);
		}
		return ColorPickerFragment.newInstance(color, title, descr, new IColorPicker() {
			@Override
			public void onColorPicked(@ColorInt int color, String title, String descr) {
				Intent data = new Intent();
				data.putExtra(EXTRA_COLOR, color);
				data.putExtra(EXTRA_TITLE, title);
				data.putExtra(EXTRA_DESCR, descr);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	static Intent newIntent() {
		return new Intent(YMSApplication.getContext(), ColorPickerActivity.class);
	}

	static Intent newIntent(ColorItem color) {
		Intent intent = new Intent(YMSApplication.getContext(), ColorPickerActivity.class);
		intent.putExtra(EXTRA_COLOR, color.getColor());
		intent.putExtra(EXTRA_TITLE, color.getTitle());
		intent.putExtra(EXTRA_DESCR, color.getDescription());
		return intent;
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
}

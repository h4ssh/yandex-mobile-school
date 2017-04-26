package ru.yandex.mobile_school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ColorPickerActivity extends SingleFragmentActivity {

	static final String EXTRA_COLOR_ITEM = "color_item";
	private ColorPickedListener mColorPickedListener = new ColorPickedListener();

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
		ColorItem item;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(EXTRA_COLOR_ITEM)) {
			item = extras.getParcelable(EXTRA_COLOR_ITEM);
		} else {
			item = new ColorItem();
		}
		return ColorPickerFragment.newInstance(item, mColorPickedListener);
	}

	@Override
	protected void restoreFragmentState(Fragment fragment) {
		if (fragment instanceof ColorPickerFragment) {
			((ColorPickerFragment)fragment).setDelegate(mColorPickedListener);
		}
	}

	static Intent newIntent(Context context) {
		return new Intent(context, ColorPickerActivity.class);
	}

	static Intent newIntent(Context context, ColorItem color) {
		Intent intent = new Intent(context, ColorPickerActivity.class);
		intent.putExtra(EXTRA_COLOR_ITEM, color);
		return intent;
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private class ColorPickedListener implements IColorPicker {
		@Override
		public void onColorPicked(ColorItem colorItem) {
			Intent data = new Intent();
			data.putExtra(EXTRA_COLOR_ITEM, colorItem);
			setResult(RESULT_OK, data);
			finish();
		}
	}
}
